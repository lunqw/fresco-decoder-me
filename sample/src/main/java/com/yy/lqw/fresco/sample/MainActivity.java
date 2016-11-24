package com.yy.lqw.fresco.sample;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.facebook.common.util.UriUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

public class MainActivity extends AppCompatActivity {
    private SimpleDraweeView mShowView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mShowView = (SimpleDraweeView) findViewById(R.id.v_show);
        findViewById(R.id.btn_svga).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Uri uri = new Uri.Builder()
                        .scheme(UriUtil.LOCAL_RESOURCE_SCHEME)
                        .path(String.valueOf(R.raw.posche))
                        .build();
                playAnimation(uri, true);
            }
        });

        findViewById(R.id.btn_rf).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Uri uri = new Uri.Builder()
                        .scheme(UriUtil.LOCAL_RESOURCE_SCHEME)
                        .path(String.valueOf(R.raw.laugh))
                        .build();
                playAnimation(uri, true);
            }
        });

        findViewById(R.id.btn_rf_preview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Uri uri = new Uri.Builder()
                        .scheme(UriUtil.LOCAL_RESOURCE_SCHEME)
                        .path(String.valueOf(R.raw.laugh))
                        .build();
                playAnimation(uri, false);
            }
        });
    }

    void playAnimation(Uri uri, boolean autoPlay) {
        DraweeController c = Fresco.newDraweeControllerBuilder()
                .setUri(uri)
                .setAutoPlayAnimations(autoPlay)
                .build();
        mShowView.setController(c);
    }
}
