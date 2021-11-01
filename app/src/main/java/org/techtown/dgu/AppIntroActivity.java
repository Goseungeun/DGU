package org.techtown.dgu;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

public class AppIntroActivity extends AppIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //   setContentView(R.layout.activity_app_intro_activity);
        addSlide(AppIntroFragment.newInstance("First page","This is the First Page Description",R.drawable.gear, ContextCompat.getColor(getApplicationContext(),R.color.deepgreen)));
        addSlide(AppIntroFragment.newInstance("Second page","This is the Second Page Description",R.drawable.gear, ContextCompat.getColor(getApplicationContext(),R.color.deepgreen)));
        addSlide(AppIntroFragment.newInstance("Third page","This is the Third Page Description",R.drawable.gear, ContextCompat.getColor(getApplicationContext(),R.color.deepgreen)));
        setFadeAnimation();


    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();
    }
}

