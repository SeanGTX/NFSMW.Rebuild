/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Intent
 */
package com.google.android.gms.auth;

import android.content.Intent;
import com.google.android.gms.auth.GoogleAuthException;

public class UserRecoverableAuthException
extends GoogleAuthException {
    private final Intent mIntent;

    public UserRecoverableAuthException(String string2, Intent intent) {
        super(string2);
        this.mIntent = intent;
    }

    public Intent getIntent() {
        if (this.mIntent != null) return new Intent(this.mIntent);
        return null;
    }
}

