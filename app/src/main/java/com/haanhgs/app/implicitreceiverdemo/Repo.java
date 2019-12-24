package com.haanhgs.app.implicitreceiverdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class Repo {

    public static Bitmap decodeUri(Context context, Uri uri, int size)throws FileNotFoundException{
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        InputStream input = context.getContentResolver().openInputStream(uri);
        BitmapFactory.decodeStream(input, null, options);
        int widthTemp = options.outWidth;
        int hightTemp = options.outHeight;
        int scale = 1;

        while (widthTemp/2 >= size && hightTemp/2 >= size){
            widthTemp /= 2;
            hightTemp /= 2;
            scale *= 2;
        }

        BitmapFactory.Options newOptions = new BitmapFactory.Options();
        options.inSampleSize = scale;
        InputStream newInput = context.getContentResolver().openInputStream(uri);
        return  BitmapFactory.decodeStream(newInput, null, newOptions);
    }
}
