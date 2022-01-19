/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.AlarmManager
 *  android.app.IntentService
 *  android.app.PendingIntent
 *  android.content.Context
 *  android.content.Intent
 *  android.os.PowerManager
 *  android.os.PowerManager$WakeLock
 *  android.util.Log
 */
package com.google.android.c2dm;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;
import com.google.android.c2dm.C2DMessaging;
import java.io.IOException;

public abstract class C2DMBaseReceiver
extends IntentService {
    private static final String C2DM_INTENT = "com.google.android.c2dm.intent.RECEIVE";
    private static final String C2DM_RETRY = "com.google.android.c2dm.intent.RETRY";
    public static final String ERR_ACCOUNT_MISSING = "ACCOUNT_MISSING";
    public static final String ERR_AUTHENTICATION_FAILED = "AUTHENTICATION_FAILED";
    public static final String ERR_INVALID_PARAMETERS = "INVALID_PARAMETERS";
    public static final String ERR_INVALID_SENDER = "INVALID_SENDER";
    public static final String ERR_PHONE_REGISTRATION_ERROR = "PHONE_REGISTRATION_ERROR";
    public static final String ERR_SERVICE_NOT_AVAILABLE = "SERVICE_NOT_AVAILABLE";
    public static final String ERR_TOO_MANY_REGISTRATIONS = "TOO_MANY_REGISTRATIONS";
    public static final String EXTRA_ERROR = "error";
    public static final String EXTRA_REGISTRATION_ID = "registration_id";
    public static final String EXTRA_UNREGISTERED = "unregistered";
    public static final String REGISTRATION_CALLBACK_INTENT = "com.google.android.c2dm.intent.REGISTRATION";
    private static final String TAG = "C2DM";
    private static final String WAKELOCK_KEY = "C2DM_LIB";
    private static PowerManager.WakeLock mWakeLock;
    private final String senderId;

    public C2DMBaseReceiver(String string) {
        super(string);
        this.senderId = string;
    }

    private void handleRegistration(Context context, Intent object) {
        String string = object.getStringExtra(EXTRA_REGISTRATION_ID);
        String string2 = object.getStringExtra(EXTRA_ERROR);
        object = object.getStringExtra(EXTRA_UNREGISTERED);
        if (Log.isLoggable((String)TAG, (int)3)) {
            Log.d((String)TAG, (String)("dmControl: registrationId = " + string + ", error = " + string2 + ", removed = " + (String)object));
        }
        if (object != null) {
            C2DMessaging.clearRegistrationId(context);
            this.onUnregistered(context);
            return;
        }
        if (string2 != null) {
            C2DMessaging.clearRegistrationId(context);
            Log.e((String)TAG, (String)("Registration error " + string2));
            this.onError(context, string2);
            if (!ERR_SERVICE_NOT_AVAILABLE.equals(string2)) return;
            long l2 = C2DMessaging.getBackoff(context);
            Log.d((String)TAG, (String)("Scheduling registration retry, backoff = " + l2));
            object = PendingIntent.getBroadcast((Context)context, (int)0, (Intent)new Intent(C2DM_RETRY), (int)0);
            ((AlarmManager)context.getSystemService("alarm")).set(3, l2, (PendingIntent)object);
            C2DMessaging.setBackoff(context, l2 * 2L);
            return;
        }
        try {
            this.onRegistered(context, string);
            C2DMessaging.setRegistrationId(context, string);
            return;
        }
        catch (IOException iOException) {
            Log.e((String)TAG, (String)("Registration error " + iOException.getMessage()));
            return;
        }
    }

    static void runIntentInService(Context context, Intent intent) {
        if (mWakeLock == null) {
            mWakeLock = ((PowerManager)context.getSystemService("power")).newWakeLock(1, WAKELOCK_KEY);
        }
        mWakeLock.acquire();
        intent.setClassName(context, context.getPackageName() + ".C2DMReceiver");
        context.startService(intent);
    }

    public abstract void onError(Context var1, String var2);

    /*
     * Enabled unnecessary exception pruning
     */
    public final void onHandleIntent(Intent intent) {
        try {
            Context context = this.getApplicationContext();
            if (intent.getAction().equals(REGISTRATION_CALLBACK_INTENT)) {
                this.handleRegistration(context, intent);
                return;
            }
            if (intent.getAction().equals(C2DM_INTENT)) {
                this.onMessage(context, intent);
                return;
            }
            if (!intent.getAction().equals(C2DM_RETRY)) return;
            C2DMessaging.register(context, this.senderId);
            return;
        }
        finally {
            mWakeLock.release();
        }
    }

    protected abstract void onMessage(Context var1, Intent var2);

    public void onRegistered(Context context, String string) throws IOException {
    }

    public void onUnregistered(Context context) {
    }
}

