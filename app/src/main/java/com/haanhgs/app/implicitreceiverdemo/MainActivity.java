package com.haanhgs.app.implicitreceiverdemo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.FileNotFoundException;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST = 1979;
    @BindView(R.id.tvMessage)
    TextView tvMessage;
    @BindView(R.id.ivImage)
    ImageView ivImage;

    private Intent intent;
    private String action;
    private String type;
    private Uri uri;

    private void prepareIntent(){
        intent = getIntent();
        action = intent.getAction();
        type = intent.getType();
    }

    //receive Uri
    private void handleUriIntent(){
        if (Intent.ACTION_VIEW.equals(action)){
            Uri uri = intent.getData();
            if (uri != null) tvMessage.setText(uri.toString());
        }
    }

    private void loadImageFromUri(){
        if (uri != null){
            try{
                Bitmap bitmap = Repo.decodeUri(this, uri, 300);
                if (bitmap != null){
                    ivImage.setImageBitmap(bitmap);
                }
            }catch (FileNotFoundException e){
                Log.e("E.Repo", "file not found");
            }
        }
    }

    //remember to add permission to manifest
    private void checkReadPermission(){
        int p = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (p != PackageManager.PERMISSION_GRANTED){
            String[] permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
            ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST);

//            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
//                Toast.makeText(this, "need this to read image on storage",
//                        Toast.LENGTH_SHORT).show();
//            } else {
//                ActivityCompat.requestPermissions(
//                        this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST);
//            }

        } else {
            loadImageFromUri();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                loadImageFromUri();
            } else {
                Toast.makeText(this, "request denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //receive text and image
    private void handleSendIntent(){
        if (Intent.ACTION_SEND.equals(action) && type != null){

            //case text
            if ("text/plain".equals(type)){
                tvMessage.setText(intent.getStringExtra(Intent.EXTRA_TEXT));
            }

            //receive image ok now
            if (type.startsWith("image/")){
                uri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
                checkReadPermission();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        prepareIntent();
        handleUriIntent();
        handleSendIntent();
    }
}
