/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.PendingIntent
 *  android.os.Binder
 *  android.os.IBinder
 *  android.os.IInterface
 *  android.os.Parcel
 *  android.os.RemoteException
 */
package com.google.android.gms.internal;

import android.app.PendingIntent;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface fj
extends IInterface {
    public void onAddGeofencesResult(int var1, String[] var2) throws RemoteException;

    public void onRemoveGeofencesByPendingIntentResult(int var1, PendingIntent var2) throws RemoteException;

    public void onRemoveGeofencesByRequestIdsResult(int var1, String[] var2) throws RemoteException;

    public static abstract class com.google.android.gms.internal.fj$a
    extends Binder
    implements fj {
        public com.google.android.gms.internal.fj$a() {
            this.attachInterface(this, "com.google.android.gms.location.internal.IGeofencerCallbacks");
        }

        public static fj H(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface iInterface = iBinder.queryLocalInterface("com.google.android.gms.location.internal.IGeofencerCallbacks");
            if (iInterface == null) return new a(iBinder);
            if (!(iInterface instanceof fj)) return new a(iBinder);
            return (fj)iInterface;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int n2, Parcel object, Parcel parcel, int n3) throws RemoteException {
            switch (n2) {
                default: {
                    return super.onTransact(n2, object, parcel, n3);
                }
                case 1598968902: {
                    parcel.writeString("com.google.android.gms.location.internal.IGeofencerCallbacks");
                    return true;
                }
                case 1: {
                    object.enforceInterface("com.google.android.gms.location.internal.IGeofencerCallbacks");
                    this.onAddGeofencesResult(object.readInt(), object.createStringArray());
                    parcel.writeNoException();
                    return true;
                }
                case 2: {
                    object.enforceInterface("com.google.android.gms.location.internal.IGeofencerCallbacks");
                    this.onRemoveGeofencesByRequestIdsResult(object.readInt(), object.createStringArray());
                    parcel.writeNoException();
                    return true;
                }
                case 3: 
            }
            object.enforceInterface("com.google.android.gms.location.internal.IGeofencerCallbacks");
            n2 = object.readInt();
            object = object.readInt() != 0 ? (PendingIntent)PendingIntent.CREATOR.createFromParcel(object) : null;
            this.onRemoveGeofencesByPendingIntentResult(n2, (PendingIntent)object);
            parcel.writeNoException();
            return true;
        }

        private static class a
        implements fj {
            private IBinder dU;

            a(IBinder iBinder) {
                this.dU = iBinder;
            }

            public IBinder asBinder() {
                return this.dU;
            }

            @Override
            public void onAddGeofencesResult(int n2, String[] stringArray) throws RemoteException {
                Parcel parcel = Parcel.obtain();
                Parcel parcel2 = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken("com.google.android.gms.location.internal.IGeofencerCallbacks");
                    parcel.writeInt(n2);
                    parcel.writeStringArray(stringArray);
                    this.dU.transact(1, parcel, parcel2, 0);
                    parcel2.readException();
                    return;
                }
                finally {
                    parcel2.recycle();
                    parcel.recycle();
                }
            }

            @Override
            public void onRemoveGeofencesByPendingIntentResult(int n2, PendingIntent pendingIntent) throws RemoteException {
                Parcel parcel = Parcel.obtain();
                Parcel parcel2 = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken("com.google.android.gms.location.internal.IGeofencerCallbacks");
                    parcel.writeInt(n2);
                    if (pendingIntent != null) {
                        parcel.writeInt(1);
                        pendingIntent.writeToParcel(parcel, 0);
                    } else {
                        parcel.writeInt(0);
                    }
                    this.dU.transact(3, parcel, parcel2, 0);
                    parcel2.readException();
                    return;
                }
                finally {
                    parcel2.recycle();
                    parcel.recycle();
                }
            }

            @Override
            public void onRemoveGeofencesByRequestIdsResult(int n2, String[] stringArray) throws RemoteException {
                Parcel parcel = Parcel.obtain();
                Parcel parcel2 = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken("com.google.android.gms.location.internal.IGeofencerCallbacks");
                    parcel.writeInt(n2);
                    parcel.writeStringArray(stringArray);
                    this.dU.transact(2, parcel, parcel2, 0);
                    parcel2.readException();
                    return;
                }
                finally {
                    parcel2.recycle();
                    parcel.recycle();
                }
            }
        }
    }
}

