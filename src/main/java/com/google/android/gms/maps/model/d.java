/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 */
package com.google.android.gms.maps.model;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.b;
import com.google.android.gms.maps.model.LatLngBounds;

public class d {
    static void a(LatLngBounds latLngBounds, Parcel parcel, int n2) {
        int n3 = b.l(parcel);
        b.c(parcel, 1, latLngBounds.getVersionCode());
        b.a(parcel, 2, latLngBounds.southwest, n2, false);
        b.a(parcel, 3, latLngBounds.northeast, n2, false);
        b.D(parcel, n3);
    }
}

