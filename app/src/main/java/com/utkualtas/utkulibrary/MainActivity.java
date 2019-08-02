package com.utkualtas.utkulibrary;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.utkualtas.utkulib.SpotifyAnimator;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    SpotifyAnimator spotifyAnimator;

    @BindView(R.id.testText)
    TextView testText;

    @BindView(R.id.container)
    LinearLayout container;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        spotifyAnimator = new SpotifyAnimator(this, container)
                .setFocusFilter(true)
                .setFilterColor(Color.parseColor("#66121212"))
                .setScaleRatio(0.97f)
                .setOnClickListener(v -> {
                    startActivity(new Intent(this, MainActivity.class));
                })
                .init();

    }
}
