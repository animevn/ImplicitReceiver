package com.haanhgs.app.implicitreceiverdemo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
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

    //receive text and image
    private void handleSendIntent(){
        if (Intent.ACTION_SEND.equals(action)){

            //case text
            if ("text/plain".equals(type)){
                Log.d("D.MainActivity", "text");
                tvMessage.setText(intent.getStringExtra(Intent.EXTRA_TEXT));
            }

            //case image - there's problem, will need to return to this in level 2
            if ("image/*".equals(type)){
                Log.d("D.MainActivity", "image");
                Uri uri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
                loadImageFromUri(this, uri, ivImage, 350);
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
