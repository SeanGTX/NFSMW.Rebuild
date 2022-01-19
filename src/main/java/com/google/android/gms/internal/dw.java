/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources
 *  android.graphics.Typeface
 *  android.widget.Button
 *  com.google.android.gms.R$color
 *  com.google.android.gms.R$drawable
 *  com.google.android.gms.R$string
 */
package com.google.android.gms.internal;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;
import com.google.android.gms.R;
import com.google.android.gms.internal.du;

public final class dw
extends Button {
    public dw(Context context) {
        this(context, null);
    }

    public dw(Context context, AttributeSet attributeSet) {
        super(context, attributeSet, 16842824);
    }

    private int b(int n2, int n3, int n4) {
        switch (n2) {
            default: {
                throw new IllegalStateException("Unknown color scheme: " + n2);
            }
            case 1: {
                return n4;
            }
            case 0: 
        }
        return n3;
    }

    private void b(Resources resources, int n2, int n3) {
        switch (n2) {
            default: {
                throw new IllegalStateException("Unknown button size: " + n2);
            }
            case 0: 
            case 1: {
                n2 = this.b(n3, R.drawable.common_signin_btn_text_dark, R.drawable.common_signin_btn_text_light);
                break;
            }
            case 2: {
                n2 = this.b(n3, R.drawable.common_signin_btn_icon_dark, R.drawable.common_signin_btn_icon_light);
            }
        }
        if (n2 == -1) {
            throw new IllegalStateException("Could not find background resource!");
        }
        this.setBackgroundDrawable(resources.getDrawable(n2));
    }

    private void c(Resources resources) {
        this.setTypeface(Typeface.DEFAULT_BOLD);
        this.setTextSize(14.0f);
        float f2 = resources.getDisplayMetrics().density;
        this.setMinHeight((int)(f2 * 48.0f + 0.5f));
        this.setMinWidth((int)(f2 * 48.0f + 0.5f));
    }

    private void c(Resources resources, int n2, int n3) {
        this.setTextColor(resources.getColorStateList(this.b(n3, R.color.common_signin_btn_text_dark, R.color.common_signin_btn_text_light)));
        switch (n2) {
            default: {
                throw new IllegalStateException("Unknown button size: " + n2);
            }
            case 0: {
                this.setText(resources.getString(R.string.common_signin_button_text));
                return;
            }
            case 1: {
                this.setText(resources.getString(R.string.common_signin_button_text_long));
                return;
            }
            case 2: 
        }
        this.setText(null);
    }

    public void a(Resources resources, int n2, int n3) {
        boolean bl2 = true;
        boolean bl3 = n2 >= 0 && n2 < 3;
        du.a(bl3, "Unknown button size " + n2);
        bl3 = n3 >= 0 && n3 < 2 ? bl2 : false;
        du.a(bl3, "Unknown color scheme " + n3);
        this.c(resources);
        this.b(resources, n2, n3);
        this.c(resources, n2, n3);
    }
}

