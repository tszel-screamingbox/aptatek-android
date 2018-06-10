package com.aptatek.aptatek.domain.interactor;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.aptatek.aptatek.injection.qualifier.ApplicationContext;

import javax.inject.Inject;

import timber.log.Timber;

public class VideoThumbnailInteractor {

    private static final int TARGET_THUMBNAIL_SIZE = 512;

    private final Context context;

    @Inject
    public VideoThumbnailInteractor(@ApplicationContext final Context appContext) {
        this.context = appContext;
    }

    public Bitmap createThumbnailForRawVideo(@NonNull final Uri uri) {
        Bitmap bitmap = null;
        final MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(context, uri);
            bitmap = retriever.getFrameAtTime(0);
        } catch (RuntimeException ex) {
            Timber.d(ex, "Failed to create video thumbnail");
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException ex) {
                // Ignore failures while cleaning up.
            }
        }

        if (bitmap == null) return null;

        // Scale down the bitmap if it's too large.
        final int width = bitmap.getWidth();
        final int height = bitmap.getHeight();
        final int max = Math.max(width, height);
        if (max > TARGET_THUMBNAIL_SIZE) {
            final float scale = 512f / max;
            final int w = Math.round(scale * width);
            final int h = Math.round(scale * height);
            bitmap = Bitmap.createScaledBitmap(bitmap, w, h, true);
        }

        return bitmap;
    }

}
