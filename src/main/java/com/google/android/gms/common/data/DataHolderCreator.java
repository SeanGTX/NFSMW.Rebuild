/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.database.CursorWindow
 *  android.os.Bundle
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 */
package com.google.android.gms.common.data;

import android.database.CursorWindow;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.data.DataHolder;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;

public class DataHolderCreator
implements Parcelable.Creator<DataHolder> {
    public static final int CONTENT_DESCRIPTION = 0;

    static void a(DataHolder dataHolder, Parcel parcel, int n2) {
        int n3 = b.l(parcel);
        b.a(parcel, 1, dataHolder.bi(), false);
        b.c(parcel, 1000, dataHolder.getVersionCode());
        b.a((Parcel)parcel, (int)2, (Parcelable[])dataHolder.bj(), (int)n2, (boolean)false);
        b.c(parcel, 3, dataHolder.getStatusCode());
        b.a(parcel, 4, dataHolder.getMetadata(), false);
        b.D(parcel, n3);
    }

    public DataHolder createFromParcel(Parcel object) {
        int n2 = 0;
        Bundle bundle = null;
        int n3 = a.k((Parcel)object);
        CursorWindow[] cursorWindowArray = null;
        String[] stringArray = null;
        int n4 = 0;
        block7: while (object.dataPosition() < n3) {
            int n5 = a.j((Parcel)object);
            switch (a.A(n5)) {
                default: {
                    a.b((Parcel)object, n5);
                    continue block7;
                }
                case 1: {
                    stringArray = a.x((Parcel)object, n5);
                    continue block7;
                }
                case 1000: {
                    n4 = a.g((Parcel)object, n5);
                    continue block7;
                }
                case 2: {
                    cursorWindowArray = (CursorWindow[])a.b((Parcel)object, n5, CursorWindow.CREATOR);
                    continue block7;
                }
                case 3: {
                    n2 = a.g((Parcel)object, n5);
                    continue block7;
                }
                case 4: 
            }
            bundle = a.o((Parcel)object, n5);
        }
        if (object.dataPosition() != n3) {
            throw new a.a("Overread allowed size end=" + n3, (Parcel)object);
        }
        object = new DataHolder(n4, stringArray, cursorWindowArray, n2, bundle);
        ((DataHolder)object).validateContents();
        return object;
    }

    public DataHolder[] newArray(int n2) {
        return new DataHolder[n2];
    }
}

