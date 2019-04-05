package com.video.aashi.campaign.view;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.IOException;

/**
 * Created by dj1 on 10/10/18.
 */

public class ImageFile {
    File mImageFile;

    public ImageFile() {}

    public void createImageFile(Context context, String name) throws IOException {
        createImageFile(context, name, ".jpg");
    }

    public void createImageFile(Context context, String name, String extension) throws IOException {
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        mImageFile = File.createTempFile(
                name,  /* prefix */
                extension,        /* suffix */
                storageDir      /* directory */
        );
    }

    public File getFile() {
        return mImageFile;
    }

    public boolean exists() {
        return null != mImageFile && mImageFile.exists();
    }

    public String getPath() {
        return Uri.fromFile(mImageFile).toString();
    }

    public void delete() {
        if (mImageFile.exists()) {
            mImageFile.delete();
        }
        mImageFile = null;
    }
}

