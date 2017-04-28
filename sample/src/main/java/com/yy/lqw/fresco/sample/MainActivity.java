package com.yy.lqw.fresco.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

public class MainActivity extends AppCompatActivity {
    private SimpleDraweeView mShowView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        mShowView = (SimpleDraweeView) findViewById(R.id.v_show);
        final RecyclerView inputView = (RecyclerView) findViewById(R.id.rv_input);
        InputAdapter adapter = new InputAdapter();
        adapter.setOnItemClickListener(new InputAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, String item, int position) {
                playAnimation(item);
            }
        });
        inputView.setAdapter(adapter);
        inputView.setLayoutManager(new GridLayoutManager(this, 5));
    }

    private void playAnimation(String uriString) {
        DraweeController c = Fresco.newDraweeControllerBuilder()
                .setUri(uriString)
                .setAutoPlayAnimations(true)
                .build();
        mShowView.setController(c);
    }
}
