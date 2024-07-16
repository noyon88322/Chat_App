package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class splash extends AppCompatActivity {

 CircleImageView img_logo;
 TextView tv_appname,tv_made,tv_woner;

 Animation topAnim,bottomAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        img_logo = findViewById(R.id.img_logo);
        tv_appname = findViewById(R.id.tv_appname);
        tv_made = findViewById(R.id.tv_made);
        tv_woner = findViewById(R.id.tv_woner);


         ////Anim
        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);
        img_logo.setAnimation(topAnim);
        tv_appname.setAnimation(bottomAnim);
        tv_made.setAnimation(bottomAnim);
        tv_woner.setAnimation(bottomAnim);



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(splash.this,login.class);
                startActivity(intent);
                finish();
            }
        },4000);
    }
}