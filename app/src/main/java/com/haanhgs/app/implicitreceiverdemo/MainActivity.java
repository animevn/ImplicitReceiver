package com.haanhgs.app.implicitreceiverdemo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileNotFoundException;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import static com.haanhgs.app.implicitreceiverdemo.Repo.loadImageFromUri;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tvMessage)
    TextView tvMessage;
    @BindView(R.id.ivImage)
    ImageView ivImage;

    private Intent intent;
    private String action;
    private String type;


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

    private void loadImageFromUri(Context context, Uri uri){
        if (uri != null){
            try{
                Bitmap bitmap = Repo.decodeUri(context, uri, 300);
                if (bitmap != null){
                    ivImage.setImageBitmap(bitmap);
                }
            }catch (FileNotFoundException e){
                Log.e("E.Repo", "file not found");
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
                Log.d("D.MainActivity", "image");
                Uri uri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
                loadImageFromUri(this, uri);
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
