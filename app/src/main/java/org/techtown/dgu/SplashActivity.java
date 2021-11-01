package org.techtown.dgu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;

import com.airbnb.lottie.LottieAnimationView;

public class SplashActivity extends Activity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Intent mainIntent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);

        sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        LottieAnimationView lottie_animation =(LottieAnimationView) findViewById(R.id.animation_view);




        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                //최초접속시에만 intro화면 출력하도록
                CheckFirstRun();

                SplashActivity.this.startActivity(mainIntent);
                SplashActivity.this.finish();
            }
        },1200);//1000=1초
    }

    private void CheckFirstRun() {
        boolean isFirstRun = sharedPreferences.getBoolean("isFirstRun",true);
        if(isFirstRun)
        {
            mainIntent = new Intent(SplashActivity.this,AppIntroActivity.class);

            sharedPreferences.edit().putBoolean("isFirstRun",false).apply();
        }else{
            mainIntent = new Intent(SplashActivity.this,MainActivity.class);
        }
    }
}
