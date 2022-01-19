/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.BroadcastReceiver
 *  android.content.Context
 *  android.content.Intent
 *  android.content.IntentFilter
 *  android.net.ConnectivityManager
 */
package com.ea.nimble;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import com.ea.nimble.ApplicationEnvironment;
import com.ea.nimble.BackgroundNetworkConnection;
import com.ea.nimble.BaseCore;
import com.ea.nimble.Component;
import com.ea.nimble.Error;
import com.ea.nimble.HttpRequest;
import com.ea.nimble.IHttpRequest;
import com.ea.nimble.INetwork;
import com.ea.nimble.IOperationalTelemetryDispatch;
import com.ea.nimble.Log;
import com.ea.nimble.LogSource;
import com.ea.nimble.Network;
import com.ea.nimble.NetworkConnection;
import com.ea.nimble.NetworkConnectionCallback;
import com.ea.nimble.NetworkConnectionHandle;
import com.ea.nimble.Timer;
import com.ea.nimble.Utility;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class NetworkImpl
extends Component
implements INetwork,
LogSource {
    private static final String BACKUP_NETWORK_REACHABILITY_CHECK_URL = "http://www.ea.com";
    private static final int DETECTION_TIMEOUT = 30;
    private static final String MAIN_NETWORK_REACHABILITY_CHECK_URL = "http://cdn.skum.eamobile.com";
    private static final int[] PING_INTERVAL = new int[]{5, 10, 30, 60};
    private static final int QUICK_DETECTION_TIMEOUT = 5;
    private final int MAX_CONCURRENT_THREADS;
    private ExecutorService m_asyncTaskManager;
    private ConnectivityReceiver m_connectivityReceiver;
    private NetworkConnection m_detectionConnection;
    private boolean m_isWifi;
    private DetectionState m_networkDetectionState;
    private int m_pingIndex;
    private List<NetworkConnection> m_queue;
    private Network.Status m_status;
    private Timer m_timer;
    private LinkedList<NetworkConnection> m_waitingToExecuteQueue;

    public NetworkImpl() {
        this.MAX_CONCURRENT_THREADS = 4;
        Log.Helper.LOGV(this, "constructor, start task manager and monitor the connectivity", new Object[0]);
        this.m_connectivityReceiver = null;
        this.m_status = Network.Status.UNKNOWN;
        this.m_detectionConnection = null;
        this.m_networkDetectionState = DetectionState.NONE;
        this.m_pingIndex = 0;
        this.m_queue = new ArrayList<NetworkConnection>();
        this.startWork();
    }

    static /* synthetic */ Timer access$302(NetworkImpl networkImpl, Timer timer) {
        networkImpl.m_timer = timer;
        return timer;
    }

    private void detect(boolean bl2) {
        if (this.m_detectionConnection != null) {
            if (!bl2) {
                return;
            }
            NetworkConnection networkConnection = this.m_detectionConnection;
            this.m_detectionConnection = null;
            networkConnection.cancel();
        }
        this.stopPing();
        if (this.reachabilityCheck()) {
            if (this.m_status != Network.Status.DEAD) {
                this.setStatus(Network.Status.OK);
            }
            this.m_networkDetectionState = DetectionState.VERIFY_REACHABLE_MAIN;
        } else {
            if (this.m_status == Network.Status.UNKNOWN) {
                this.setStatus(Network.Status.NONE);
            }
            this.m_networkDetectionState = DetectionState.VERIFY_UNREACHABLE_MAIN;
        }
        this.verifyReachability(MAIN_NETWORK_REACHABILITY_CHECK_URL, 5.0);
    }

    /*
     * Enabled unnecessary exception pruning
     */
    private void onReachabilityVerification(NetworkConnectionHandle object) {
        synchronized (this) {
            Exception exception = object.getResponse().getError();
            if (exception == null) {
                Log.Helper.LOGD(this, "network verified reachable.", new Object[0]);
                this.setStatus(Network.Status.OK);
                this.m_detectionConnection = null;
            } else {
                if (object != this.m_detectionConnection) return;
                this.m_detectionConnection = null;
                Log.Helper.LOGD(this, "network verified unreachable, ERROR %s for detection state %s", new Object[]{object.getResponse().getError(), this.m_networkDetectionState});
                if (exception instanceof Error && ((Error)(object = (Error)exception)).getDomain().equals("NimbleError") && ((Error)object).isError(Error.Code.NETWORK_OPERATION_CANCELLED)) {
                    Log.Helper.LOGW(this, "Network detection verification connection get cancelled for unknown reason (maybe reasonable for Android)", new Object[0]);
                }
                switch (2.$SwitchMap$com$ea$nimble$NetworkImpl$DetectionState[this.m_networkDetectionState.ordinal()]) {
                    case 1: {
                        this.m_networkDetectionState = DetectionState.VERIFY_REACHABLE_BACKUP;
                        this.verifyReachability(BACKUP_NETWORK_REACHABILITY_CHECK_URL, 30.0);
                        break;
                    }
                    case 2: {
                        this.setStatus(Network.Status.NONE);
                        break;
                    }
                    case 3: {
                        this.m_networkDetectionState = DetectionState.PING;
                        if (this.m_status == Network.Status.DEAD) {
                            this.startPing();
                            break;
                        }
                        this.setStatus(Network.Status.DEAD);
                        this.m_pingIndex = 0;
                        this.startPing();
                        break;
                    }
                    case 4: {
                        ++this.m_pingIndex;
                        this.startPing();
                        break;
                    }
                }
            }
            return;
        }
    }

    private boolean reachabilityCheck() {
        this.m_isWifi = false;
        Context context = ApplicationEnvironment.getComponent().getApplicationContext();
        if (context == null) {
            return false;
        }
        if ((context = (ConnectivityManager)context.getSystemService("connectivity")) == null) return false;
        if ((context = context.getActiveNetworkInfo()) == null) return false;
        if (!context.isConnectedOrConnecting()) return false;
        if (!BaseCore.getInstance().isActive()) {
            Log.Helper.LOGD(this, "BaseCore not active yet. Postpone reachability check.", new Object[0]);
            return false;
        }
        if (context.getType() != 1) {
            if (context.getType() != 9) return true;
        }
        this.m_isWifi = true;
        return true;
    }

    private void registerNetworkListener() {
        if (this.m_connectivityReceiver != null) return;
        Log.Helper.LOGD(this, "Register network reachability listener.", new Object[0]);
        this.m_connectivityReceiver = new ConnectivityReceiver();
        IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        ApplicationEnvironment.getComponent().getApplicationContext().registerReceiver((BroadcastReceiver)this.m_connectivityReceiver, intentFilter);
    }

    private void setStatus(Network.Status status) {
        Log.Helper.LOGI(this, "Status change %s -> %s", new Object[]{this.m_status, status});
        if (status == this.m_status) return;
        this.m_status = status;
        Utility.sendBroadcast("nimble.notification.networkStatusChanged", null);
    }

    private void startPing() {
        if (this.m_pingIndex >= PING_INTERVAL.length) {
            this.m_pingIndex = PING_INTERVAL.length - 1;
        }
        this.m_timer = new Timer(new TimerTask());
        this.m_timer.schedule(PING_INTERVAL[this.m_pingIndex], false);
    }

    /*
     * Enabled unnecessary exception pruning
     */
    private void startWork() {
        synchronized (this) {
            Object object = this.m_asyncTaskManager;
            if (object != null) {
                return;
            }
            this.detect(true);
            this.registerNetworkListener();
            this.m_asyncTaskManager = Executors.newFixedThreadPool(4);
            if (this.m_waitingToExecuteQueue == null) return;
            if (this.m_waitingToExecuteQueue.isEmpty()) return;
            Log.Helper.LOGW(this, "NetworkConnections waiting to execute on new AsyncTaskManager. Executing.", new Object[0]);
            while (!this.m_waitingToExecuteQueue.isEmpty()) {
                object = this.m_waitingToExecuteQueue.poll();
                Log.Helper.LOGW(this, "Executing request URL: " + ((NetworkConnection)object).getRequest().url.toString(), new Object[0]);
                this.m_asyncTaskManager.execute((Runnable)object);
            }
            return;
        }
    }

    private void stopPing() {
        if (this.m_timer == null) return;
        this.m_timer.cancel();
        this.m_timer = null;
    }

    /*
     * Enabled unnecessary exception pruning
     * Converted monitor instructions to comments
     */
    private void stopWork() {
        // MONITORENTER : this
        this.m_detectionConnection = null;
        this.stopPing();
        this.unregisterNetworkListener();
        // MONITOREXIT : this
        if (this.m_asyncTaskManager == null) {
            return;
        }
        try {
            Iterator<Runnable> iterator = this.m_asyncTaskManager.shutdownNow().iterator();
            while (iterator.hasNext()) {
                ((NetworkConnection)iterator.next()).cancelForAppSuspend();
            }
            this.m_asyncTaskManager.awaitTermination(60L, TimeUnit.SECONDS);
        }
        catch (InterruptedException interruptedException) {
            this.m_asyncTaskManager.shutdownNow();
            Thread.currentThread().interrupt();
        }
        this.m_asyncTaskManager = null;
    }

    /*
     * Unable to fully structure code
     */
    private void unregisterNetworkListener() {
        if (this.m_connectivityReceiver == null) return;
        try {
            ApplicationEnvironment.getComponent().getApplicationContext().unregisterReceiver((BroadcastReceiver)this.m_connectivityReceiver);
lbl4:
            // 2 sources

            while (true) {
                this.m_connectivityReceiver = null;
                return;
            }
        }
        catch (IllegalArgumentException var1_1) {
            Log.Helper.LOGE(this, "Unable to unregister network reachability listener even it does exists", new Object[0]);
            ** continue;
        }
    }

    private void verifyReachability(String string2, double d2) {
        block3: {
            try {
                HttpRequest httpRequest = new HttpRequest(new URL(string2));
                httpRequest.timeout = d2;
                httpRequest.method = IHttpRequest.Method.GET;
                this.m_detectionConnection = new NetworkConnection(this, httpRequest);
                this.m_detectionConnection.setCompletionCallback(new NetworkConnectionCallback(){

                    @Override
                    public void callback(NetworkConnectionHandle networkConnectionHandle) {
                        NetworkImpl.this.onReachabilityVerification(networkConnectionHandle);
                    }
                });
                if (this.m_asyncTaskManager != null && !this.m_asyncTaskManager.isShutdown()) break block3;
            }
            catch (MalformedURLException malformedURLException) {
                Log.Helper.LOGE(this, "Invalid url: " + string2, new Object[0]);
                return;
            }
            Log.Helper.LOGW(this, "AsyncTaskManager is not ready. Queueing networkconnection until AsyncTaskManager is started.", new Object[0]);
            if (this.m_waitingToExecuteQueue == null) {
                this.m_waitingToExecuteQueue = new LinkedList();
            }
            this.m_waitingToExecuteQueue.addFirst(this.m_detectionConnection);
            return;
        }
        this.m_asyncTaskManager.execute(this.m_detectionConnection);
    }

    @Override
    public void cleanup() {
        this.stopWork();
        Log.Helper.LOGV(this, "cleanup", new Object[0]);
    }

    @Override
    public void forceRedetectNetworkStatus() {
        synchronized (this) {
            this.detect(true);
            return;
        }
    }

    @Override
    public String getComponentId() {
        return "com.ea.nimble.network";
    }

    @Override
    public String getLogSourceTitle() {
        return "Network";
    }

    @Override
    public Network.Status getStatus() {
        return this.m_status;
    }

    @Override
    public boolean isNetworkWifi() {
        return this.m_isWifi;
    }

    void removeConnection(NetworkConnection networkConnection) {
        synchronized (this) {
            this.m_queue.remove(networkConnection);
            return;
        }
    }

    @Override
    public void resume() {
        Log.Helper.LOGV(this, "resume", new Object[0]);
        synchronized (this) {
            this.detect(true);
            this.registerNetworkListener();
            return;
        }
    }

    @Override
    public NetworkConnectionHandle sendDeleteRequest(URL object, HashMap<String, String> hashMap, NetworkConnectionCallback networkConnectionCallback) {
        object = new HttpRequest((URL)object);
        ((HttpRequest)object).method = IHttpRequest.Method.DELETE;
        ((HttpRequest)object).headers = hashMap;
        return this.sendRequest((HttpRequest)object, networkConnectionCallback);
    }

    @Override
    public NetworkConnectionHandle sendGetRequest(URL object, HashMap<String, String> hashMap, NetworkConnectionCallback networkConnectionCallback) {
        object = new HttpRequest((URL)object);
        ((HttpRequest)object).method = IHttpRequest.Method.GET;
        ((HttpRequest)object).headers = hashMap;
        return this.sendRequest((HttpRequest)object, networkConnectionCallback);
    }

    @Override
    public NetworkConnectionHandle sendPostRequest(URL object, HashMap<String, String> hashMap, byte[] byArray, NetworkConnectionCallback networkConnectionCallback) {
        object = new HttpRequest((URL)object);
        ((HttpRequest)object).method = IHttpRequest.Method.POST;
        ((HttpRequest)object).headers = hashMap;
        try {
            ((HttpRequest)object).data.write(byArray);
            return this.sendRequest((HttpRequest)object, networkConnectionCallback);
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return this.sendRequest((HttpRequest)object, networkConnectionCallback);
        }
    }

    @Override
    public NetworkConnectionHandle sendRequest(HttpRequest httpRequest, NetworkConnectionCallback networkConnectionCallback) {
        return this.sendRequest(httpRequest, networkConnectionCallback, null);
    }

    /*
     * Enabled unnecessary exception pruning
     * Converted monitor instructions to comments
     */
    @Override
    public NetworkConnectionHandle sendRequest(HttpRequest httpRequest, NetworkConnectionCallback networkConnectionCallback, IOperationalTelemetryDispatch object) {
        object = httpRequest.runInBackground ? new BackgroundNetworkConnection(this, httpRequest, (IOperationalTelemetryDispatch)object) : new NetworkConnection(this, httpRequest, (IOperationalTelemetryDispatch)object);
        ((NetworkConnection)object).setCompletionCallback(networkConnectionCallback);
        if (httpRequest.url == null || !Utility.validString(httpRequest.url.toString())) {
            ((NetworkConnection)object).finishWithError(new Error(Error.Code.INVALID_ARGUMENT, "Sending request without valid url"));
            return object;
        }
        if (this.m_status != Network.Status.OK) {
            ((NetworkConnection)object).finishWithError(new Error(Error.Code.NETWORK_NO_CONNECTION, "No network connection, network status " + this.m_status.toString()));
            return object;
        }
        // MONITORENTER : this
        this.m_queue.add((NetworkConnection)object);
        // MONITOREXIT : this
        if (this.m_asyncTaskManager != null && !this.m_asyncTaskManager.isShutdown()) {
            this.m_asyncTaskManager.execute((Runnable)object);
            return object;
        }
        if (this.m_asyncTaskManager != null) {
            Log.Helper.LOGW(this, "AsyncTaskManager shutdown. Queueing networkconnection until AsyncTaskManager is started.", new Object[0]);
        } else {
            Log.Helper.LOGW(this, "AsyncTaskManager is not ready. Queueing networkconnection until AsyncTaskManager is started.", new Object[0]);
        }
        if (this.m_waitingToExecuteQueue == null) {
            this.m_waitingToExecuteQueue = new LinkedList();
        }
        this.m_waitingToExecuteQueue.add((NetworkConnection)object);
        return object;
    }

    @Override
    public void setup() {
        Log.Helper.LOGV(this, "setup", new Object[0]);
        this.startWork();
    }

    /*
     * Enabled unnecessary exception pruning
     */
    @Override
    public void suspend() {
        synchronized (this) {
            this.stopPing();
            this.unregisterNetworkListener();
            synchronized (this) {
                Iterator iterator = new ArrayList<NetworkConnection>(this.m_queue).iterator();
                while (true) {
                    if (!iterator.hasNext()) {
                        // MONITOREXIT @DISABLED, blocks:[4, 6, 7] lbl8 : MonitorExitStatement: MONITOREXIT : this
                        // MONITOREXIT @DISABLED, blocks:[4, 5, 6, 7] lbl9 : MonitorExitStatement: MONITOREXIT : this
                        Log.Helper.LOGV(this, "suspend", new Object[0]);
                        return;
                    }
                    ((NetworkConnection)iterator.next()).cancelForAppSuspend();
                }
            }
        }
    }

    private class ConnectivityReceiver
    extends BroadcastReceiver {
        private ConnectivityReceiver() {
        }

        public void onReceive(Context object, Intent intent) {
            Log.Helper.LOGD((Object)this, "Network reachability changed!", new Object[0]);
            object = NetworkImpl.this;
            synchronized (object) {
                NetworkImpl.this.detect(true);
                return;
            }
        }
    }

    private static enum DetectionState {
        NONE,
        VERIFY_REACHABLE_MAIN,
        VERIFY_UNREACHABLE_MAIN,
        VERIFY_REACHABLE_BACKUP,
        PING;

    }

    private class TimerTask
    implements Runnable {
        private TimerTask() {
        }

        @Override
        public void run() {
            NetworkImpl networkImpl = NetworkImpl.this;
            synchronized (networkImpl) {
                NetworkImpl.access$302(NetworkImpl.this, null);
                NetworkImpl.this.verifyReachability(NetworkImpl.MAIN_NETWORK_REACHABILITY_CHECK_URL, 30.0);
                return;
            }
        }
    }
}

