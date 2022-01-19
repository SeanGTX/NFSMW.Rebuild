/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.IntentSender
 */
package com.google.android.gms.drive;

import android.content.IntentSender;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.drive.Contents;
import com.google.android.gms.drive.CreateFileActivityBuilder;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.MetadataBuffer;
import com.google.android.gms.drive.OpenFileActivityBuilder;
import com.google.android.gms.drive.query.Query;

public interface DriveApi {
    public PendingResult<Status, OnContentsDiscardedCallback> discardContents(GoogleApiClient var1, Contents var2);

    public DriveFile getFile(GoogleApiClient var1, DriveId var2);

    public DriveFolder getFolder(GoogleApiClient var1, DriveId var2);

    public DriveFolder getRootFolder(GoogleApiClient var1);

    public PendingResult<ContentsResult, OnNewContentsCallback> newContents(GoogleApiClient var1);

    public CreateFileActivityBuilder newCreateFileActivityBuilder();

    public OpenFileActivityBuilder newOpenFileActivityBuilder();

    public PendingResult<MetadataBufferResult, DriveFolder.OnChildrenRetrievedCallback> query(GoogleApiClient var1, Query var2);

    public PendingResult<Status, OnSyncFinishCallback> requestSync(GoogleApiClient var1);

    public static interface ContentsResult
    extends Result {
        public Contents getContents();
    }

    public static interface IntentSenderResult
    extends Result {
        public IntentSender getIntentSender();
    }

    public static interface MetadataBufferResult
    extends Result {
        public MetadataBuffer getMetadataBuffer();
    }

    public static interface OnContentsDiscardedCallback {
        public void onContentsDiscarded(Status var1);
    }

    public static interface OnNewContentsCallback {
        public void onNewContents(ContentsResult var1);
    }

    public static interface OnSyncFinishCallback {
        public void onSyncFinish(Status var1);
    }
}

