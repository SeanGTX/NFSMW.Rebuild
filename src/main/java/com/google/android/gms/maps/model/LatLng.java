/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 */
package com.google.android.gms.maps.model;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.maps.internal.r;
import com.google.android.gms.maps.model.LatLngCreator;
import com.google.android.gms.maps.model.e;

public final class LatLng
implements SafeParcelable {
    public static final LatLngCreator CREATOR = new LatLngCreator();
    private final int kZ;
    public final double latitude;
    public final double longitude;

    public LatLng(double d2, double d3) {
        this(1, d2, d3);
    }

    LatLng(int n2, double d2, double d3) {
        this.kZ = n2;
        this.longitude = -180.0 <= d3 && d3 < 180.0 ? d3 : ((d3 - 180.0) % 360.0 + 360.0) % 360.0 - 180.0;
        this.latitude = Math.max(-90.0, Math.min(90.0, d2));
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof LatLng)) {
            return false;
        }
        object = (LatLng)object;
        if (Double.doubleToLongBits(this.latitude) != Double.doubleToLongBits(((LatLng)object).latitude)) return false;
        if (Double.doubleToLongBits(this.longitude) == Double.doubleToLongBits(((LatLng)object).longitude)) return true;
        return false;
    }

    int getVersionCode() {
        return this.kZ;
    }

    public int hashCode() {
        long l2 = Double.doubleToLongBits(this.latitude);
        int n2 = (int)(l2 ^ l2 >>> 32);
        l2 = Double.doubleToLongBits(this.longitude);
        return (n2 + 31) * 31 + (int)(l2 ^ l2 >>> 32);
    }

    public String toString() {
        return "lat/lng: (" + this.latitude + "," + this.longitude + ")";
    }

    public void writeToParcel(Parcel parcel, int n2) {
        if (r.eh()) {
            e.a(this, parcel, n2);
            return;
        }
        LatLngCreator.a(this, parcel, n2);
    }
}

