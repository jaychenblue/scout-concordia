package com.example.scoutconcordia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.VideoView;

public class SplashScreenActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        final VideoView splashvideoView = (VideoView)findViewById(R.id.videoView);
        String path = "android.resource://" + getPackageName() + "/" + R.raw.logo;
        splashvideoView.setVideoURI(Uri.parse(path));
        splashvideoView.start();
        int vtime = splashvideoView.getDuration();
        new Thread() {
            public void run() {
                try
                {
                    sleep (500);
                    while (splashvideoView.isPlaying());
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(intent);
                finish();
            }
        }.start();
    }
}
