/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 *  android.os.Parcelable$Creator
 */
package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;
import com.google.android.gms.internal.ha;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class hb
implements Parcelable.Creator<ha> {
    static void a(ha ha2, Parcel parcel, int n2) {
        int n3 = b.l(parcel);
        Set<Integer> set = ha2.eF();
        if (set.contains(1)) {
            b.c(parcel, 1, ha2.getVersionCode());
        }
        if (set.contains(2)) {
            b.a(parcel, 2, ha2.getAboutMe(), true);
        }
        if (set.contains(3)) {
            b.a(parcel, 3, ha2.fa(), n2, true);
        }
        if (set.contains(4)) {
            b.a(parcel, 4, ha2.getBirthday(), true);
        }
        if (set.contains(5)) {
            b.a(parcel, 5, ha2.getBraggingRights(), true);
        }
        if (set.contains(6)) {
            b.c(parcel, 6, ha2.getCircledByCount());
        }
        if (set.contains(7)) {
            b.a(parcel, 7, ha2.fb(), n2, true);
        }
        if (set.contains(8)) {
            b.a(parcel, 8, ha2.getCurrentLocation(), true);
        }
        if (set.contains(9)) {
            b.a(parcel, 9, ha2.getDisplayName(), true);
        }
        if (set.contains(12)) {
            b.c(parcel, 12, ha2.getGender());
        }
        if (set.contains(14)) {
            b.a(parcel, 14, ha2.getId(), true);
        }
        if (set.contains(15)) {
            b.a(parcel, 15, ha2.fc(), n2, true);
        }
        if (set.contains(16)) {
            b.a(parcel, 16, ha2.isPlusUser());
        }
        if (set.contains(19)) {
            b.a(parcel, 19, ha2.fd(), n2, true);
        }
        if (set.contains(18)) {
            b.a(parcel, 18, ha2.getLanguage(), true);
        }
        if (set.contains(21)) {
            b.c(parcel, 21, ha2.getObjectType());
        }
        if (set.contains(20)) {
            b.a(parcel, 20, ha2.getNickname(), true);
        }
        if (set.contains(23)) {
            b.b(parcel, 23, ha2.ff(), true);
        }
        if (set.contains(22)) {
            b.b(parcel, 22, ha2.fe(), true);
        }
        if (set.contains(25)) {
            b.c(parcel, 25, ha2.getRelationshipStatus());
        }
        if (set.contains(24)) {
            b.c(parcel, 24, ha2.getPlusOneCount());
        }
        if (set.contains(27)) {
            b.a(parcel, 27, ha2.getUrl(), true);
        }
        if (set.contains(26)) {
            b.a(parcel, 26, ha2.getTagline(), true);
        }
        if (set.contains(29)) {
            b.a(parcel, 29, ha2.isVerified());
        }
        if (set.contains(28)) {
            b.b(parcel, 28, ha2.fg(), true);
        }
        b.D(parcel, n3);
    }

    public ha[] aT(int n2) {
        return new ha[n2];
    }

    public ha al(Parcel parcel) {
        int n2 = a.k(parcel);
        HashSet<Integer> hashSet = new HashSet<Integer>();
        int n3 = 0;
        String string2 = null;
        ha.a a2 = null;
        String string3 = null;
        String string4 = null;
        int n4 = 0;
        ha.b b2 = null;
        String string5 = null;
        String string6 = null;
        int n5 = 0;
        String string7 = null;
        ha.c c2 = null;
        boolean bl2 = false;
        String string8 = null;
        ha.d d2 = null;
        String string9 = null;
        int n6 = 0;
        ArrayList<ha.f> arrayList = null;
        ArrayList<ha.g> arrayList2 = null;
        int n7 = 0;
        int n8 = 0;
        String string10 = null;
        String string11 = null;
        ArrayList<ha.h> arrayList3 = null;
        boolean bl3 = false;
        block27: while (true) {
            if (parcel.dataPosition() >= n2) {
                if (parcel.dataPosition() == n2) return new ha(hashSet, n3, string2, a2, string3, string4, n4, b2, string5, string6, n5, string7, c2, bl2, string8, d2, string9, n6, arrayList, arrayList2, n7, n8, string10, string11, arrayList3, bl3);
                throw new a.a("Overread allowed size end=" + n2, parcel);
            }
            int n9 = a.j(parcel);
            switch (a.A(n9)) {
                default: {
                    a.b(parcel, n9);
                    continue block27;
                }
                case 1: {
                    n3 = a.g(parcel, n9);
                    hashSet.add(1);
                    continue block27;
                }
                case 2: {
                    string2 = a.m(parcel, n9);
                    hashSet.add(2);
                    continue block27;
                }
                case 3: {
                    a2 = a.a(parcel, n9, ha.a.CREATOR);
                    hashSet.add(3);
                    continue block27;
                }
                case 4: {
                    string3 = a.m(parcel, n9);
                    hashSet.add(4);
                    continue block27;
                }
                case 5: {
                    string4 = a.m(parcel, n9);
                    hashSet.add(5);
                    continue block27;
                }
                case 6: {
                    n4 = a.g(parcel, n9);
                    hashSet.add(6);
                    continue block27;
                }
                case 7: {
                    b2 = a.a(parcel, n9, ha.b.CREATOR);
                    hashSet.add(7);
                    continue block27;
                }
                case 8: {
                    string5 = a.m(parcel, n9);
                    hashSet.add(8);
                    continue block27;
                }
                case 9: {
                    string6 = a.m(parcel, n9);
                    hashSet.add(9);
                    continue block27;
                }
                case 12: {
                    n5 = a.g(parcel, n9);
                    hashSet.add(12);
                    continue block27;
                }
                case 14: {
                    string7 = a.m(parcel, n9);
                    hashSet.add(14);
                    continue block27;
                }
                case 15: {
                    c2 = a.a(parcel, n9, ha.c.CREATOR);
                    hashSet.add(15);
                    continue block27;
                }
                case 16: {
                    bl2 = a.c(parcel, n9);
                    hashSet.add(16);
                    continue block27;
                }
                case 19: {
                    d2 = a.a(parcel, n9, ha.d.CREATOR);
                    hashSet.add(19);
                    continue block27;
                }
                case 18: {
                    string8 = a.m(parcel, n9);
                    hashSet.add(18);
                    continue block27;
                }
                case 21: {
                    n6 = a.g(parcel, n9);
                    hashSet.add(21);
                    continue block27;
                }
                case 20: {
                    string9 = a.m(parcel, n9);
                    hashSet.add(20);
                    continue block27;
                }
                case 23: {
                    arrayList2 = a.c(parcel, n9, ha.g.CREATOR);
                    hashSet.add(23);
                    continue block27;
                }
                case 22: {
                    arrayList = a.c(parcel, n9, ha.f.CREATOR);
                    hashSet.add(22);
                    continue block27;
                }
                case 25: {
                    n8 = a.g(parcel, n9);
                    hashSet.add(25);
                    continue block27;
                }
                case 24: {
                    n7 = a.g(parcel, n9);
                    hashSet.add(24);
                    continue block27;
                }
                case 27: {
                    string11 = a.m(parcel, n9);
                    hashSet.add(27);
                    continue block27;
                }
                case 26: {
                    string10 = a.m(parcel, n9);
                    hashSet.add(26);
                    continue block27;
                }
                case 29: {
                    bl3 = a.c(parcel, n9);
                    hashSet.add(29);
                    continue block27;
                }
                case 28: 
            }
            arrayList3 = a.c(parcel, n9, ha.h.CREATOR);
            hashSet.add(28);
        }
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return this.al(parcel);
    }

    public /* synthetic */ Object[] newArray(int n2) {
        return this.aT(n2);
    }
}

