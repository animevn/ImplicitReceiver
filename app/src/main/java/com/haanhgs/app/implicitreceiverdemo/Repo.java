package com.haanhgs.app.implicitreceiverdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class Repo {

    private static Bitmap decodeUri(Context context, Uri uri, int size)throws FileNotFoundException{
        InputStream inputStream = context.getContentResolver().openInputStream(uri);
        Log.d("D.Repo", "inputstream ok");
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(inputStream, null, options);
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
        return  BitmapFactory.decodeStream(inputStream, null, newOptions);
    }

    public static void loadImageFromUri(Context context, Uri uri, ImageView imageView, int size){
        if (uri != null){
            try{
                Bitmap bitmap = decodeUri(context, uri, size);
                if (bitmap != null){
                    Log.d("D.Repo", "bitmap ok");
                    imageView.setImageBitmap(bitmap);
                }
            }catch (FileNotFoundException e){
                Log.e("E.Repo", "file not found");
            }
        }
    }
}
