/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.net.Uri
 *  android.net.UrlQuerySanitizer
 *  android.webkit.WebView
 *  android.webkit.WebViewClient
 */
package com.google.android.gms.internal;

import android.net.Uri;
import android.net.UrlQuerySanitizer;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.google.android.gms.internal.ak;
import com.google.android.gms.internal.al;
import com.google.android.gms.internal.am;
import com.google.android.gms.internal.an;
import com.google.android.gms.internal.bj;
import com.google.android.gms.internal.bk;
import com.google.android.gms.internal.bm;
import com.google.android.gms.internal.bn;
import com.google.android.gms.internal.bq;
import com.google.android.gms.internal.cr;
import com.google.android.gms.internal.cs;
import com.google.android.gms.internal.cv;
import com.google.android.gms.internal.i;
import com.google.android.gms.internal.q;
import java.util.HashMap;

public class cw
extends WebViewClient {
    private al fm;
    private final Object fx;
    protected final cv gu;
    private final HashMap<String, an> iR = new HashMap();
    private q iS;
    private bn iT;
    private a iU;
    private boolean iV = false;
    private boolean iW;
    private bq iX;

    public cw(cv cv2, boolean bl2) {
        this.fx = new Object();
        this.gu = cv2;
        this.iW = bl2;
    }

    private void a(bm bm2) {
        bk.a(this.gu.getContext(), bm2);
    }

    private static boolean b(Uri object) {
        if ("http".equalsIgnoreCase((String)(object = object.getScheme()))) return true;
        if (!"https".equalsIgnoreCase((String)object)) return false;
        return true;
    }

    private void c(Uri object) {
        String string22 = object.getPath();
        an an2 = this.iR.get(string22);
        if (an2 == null) {
            cs.v("No GMSG handler found for GMSG: " + object);
            return;
        }
        HashMap<String, String> hashMap = new HashMap<String, String>();
        UrlQuerySanitizer urlQuerySanitizer2 = new UrlQuerySanitizer();
        urlQuerySanitizer2.setAllowUnregisteredParamaters(true);
        urlQuerySanitizer2.setUnregisteredParameterValueSanitizer(UrlQuerySanitizer.getAllButNulLegal());
        urlQuerySanitizer2.parseUrl(object.toString());
        for (UrlQuerySanitizer urlQuerySanitizer2 : urlQuerySanitizer2.getParameterList()) {
            hashMap.put(urlQuerySanitizer2.mParameter, urlQuerySanitizer2.mValue);
        }
        if (cs.n(2)) {
            cs.u("Received GMSG: " + string22);
            for (String string22 : hashMap.keySet()) {
                cs.u("  " + string22 + ": " + hashMap.get(string22));
            }
        }
        an2.a(this.gu, hashMap);
    }

    public final void Y() {
        Object object = this.fx;
        synchronized (object) {
            this.iV = false;
            this.iW = true;
            final bk bk2 = this.gu.aA();
            if (bk2 == null) return;
            if (!cr.ax()) {
                cr.iE.post(new Runnable(){

                    @Override
                    public void run() {
                        bk2.Y();
                    }
                });
            } else {
                bk2.Y();
            }
            return;
        }
    }

    public final void a(bj bj2) {
        bn bn2 = null;
        boolean bl2 = this.gu.aE();
        q q2 = bl2 && !this.gu.y().eG ? null : this.iS;
        if (!bl2) {
            bn2 = this.iT;
        }
        this.a(new bm(bj2, q2, bn2, this.iX, this.gu.aD()));
    }

    public final void a(a a2) {
        this.iU = a2;
    }

    public void a(q q2, bn bn2, al al2, bq bq2, boolean bl2) {
        this.a("/appEvent", new ak(al2));
        this.a("/canOpenURLs", am.fn);
        this.a("/click", am.fo);
        this.a("/close", am.fp);
        this.a("/customClose", am.fq);
        this.a("/httpTrack", am.fr);
        this.a("/log", am.fs);
        this.a("/open", am.ft);
        this.a("/touch", am.fu);
        this.a("/video", am.fv);
        this.iS = q2;
        this.iT = bn2;
        this.fm = al2;
        this.iX = bq2;
        this.m(bl2);
    }

    public final void a(String string2, an an2) {
        this.iR.put(string2, an2);
    }

    public final void a(boolean bl2, int n2) {
        q q2 = this.gu.aE() && !this.gu.y().eG ? null : this.iS;
        this.a(new bm(q2, this.iT, this.iX, this.gu, bl2, n2, this.gu.aD()));
    }

    public final void a(boolean bl2, int n2, String string2) {
        bn bn2 = null;
        boolean bl3 = this.gu.aE();
        q q2 = bl3 && !this.gu.y().eG ? null : this.iS;
        if (!bl3) {
            bn2 = this.iT;
        }
        this.a(new bm(q2, bn2, this.fm, this.iX, this.gu, bl2, n2, string2, this.gu.aD()));
    }

    public final void a(boolean bl2, int n2, String string2, String string3) {
        bn bn2 = null;
        boolean bl3 = this.gu.aE();
        q q2 = bl3 && !this.gu.y().eG ? null : this.iS;
        if (!bl3) {
            bn2 = this.iT;
        }
        this.a(new bm(q2, bn2, this.fm, this.iX, this.gu, bl2, n2, string2, string3, this.gu.aD()));
    }

    public boolean aI() {
        Object object = this.fx;
        synchronized (object) {
            return this.iW;
        }
    }

    public final void m(boolean bl2) {
        this.iV = bl2;
    }

    public final void onPageFinished(WebView webView, String string2) {
        if (this.iU == null) return;
        this.iU.a(this.gu);
        this.iU = null;
    }

    public final void reset() {
        Object object = this.fx;
        synchronized (object) {
            this.iR.clear();
            this.iS = null;
            this.iT = null;
            this.iU = null;
            this.fm = null;
            this.iV = false;
            this.iW = false;
            this.iX = null;
            return;
        }
    }

    /*
     * Unable to fully structure code
     */
    public final boolean shouldOverrideUrlLoading(WebView var1_1, String var2_3) {
        cs.u("AdWebView shouldOverrideUrlLoading: " + var2_3);
        var3_4 = Uri.parse((String)var2_3);
        if ("gmsg".equalsIgnoreCase(var3_4.getScheme()) && "mobileads.google.com".equalsIgnoreCase(var3_4.getHost())) {
            this.c(var3_4);
            return true;
        }
        if (this.iV && var1_1 == this.gu && cw.b(var3_4)) {
            return super.shouldOverrideUrlLoading(var1_1, var2_3);
        }
        if (this.gu.willNotDraw()) {
            cs.v("AdWebView unable to handle URL: " + var2_3);
            return true;
        }
        try {
            var4_5 = this.gu.aC();
            var1_1 = var3_4;
            if (var4_5 != null) {
                var1_1 = var3_4;
                if (var4_5.a(var3_4)) {
                    var1_1 = var4_5.a(var3_4, this.gu.getContext());
                }
            }
lbl18:
            // 6 sources

            while (true) {
                this.a(new bj("android.intent.action.VIEW", var1_1.toString(), null, null, null, null, null));
                return true;
            }
        }
        catch (i var1_2) {
            cs.v("Unable to append parameter to URL: " + var2_3);
            var1_1 = var3_4;
            ** continue;
        }
    }

    public static interface a {
        public void a(cv var1);
    }
}

