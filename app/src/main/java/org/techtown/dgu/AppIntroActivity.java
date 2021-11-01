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
        addSlide(AppIntroFragment.newInstance("DGU\nDon't Give Up","DGU를 시작하기 전에!",R.drawable.logo, ContextCompat.getColor(getApplicationContext(),R.color.deepgreen)));
        addSlide(AppIntroFragment.newInstance("과목/자격증 관리","과목/자격증을 추가하고 관리할 수 있습니다.",R.drawable.a, ContextCompat.getColor(getApplicationContext(),R.color.deepgreen)));
        addSlide(AppIntroFragment.newInstance("과목 추가","주차수와 수업시수의 의미를 확인해주세요.",R.drawable.b, ContextCompat.getColor(getApplicationContext(),R.color.deepgreen)));

        addSlide(AppIntroFragment.newInstance("공부시간측정","과목/자격증 항목 옆 재생버튼을 누르면\n시간 측정이 시작됩니다.\n\n① : 측정 시작 이후 현재까지 집중한 시간"
               +"\n② : 당일 공부한 총 시간\n③ : 시간 측정 중인 항목의 당일 총 공부시간",R.drawable.c, ContextCompat.getColor(getApplicationContext(),R.color.deepgreen)));

        addSlide(AppIntroFragment.newInstance("출석체크","과목 이름을 누르면 출석체크를 할 수 있습니다.",R.drawable.d, ContextCompat.getColor(getApplicationContext(),R.color.deepgreen)));

        addSlide(AppIntroFragment.newInstance("홈 화면","월별 달력과 총 공부시간을 확인할 수 있습니다.\n\n날짜를 클릭하면 타임테이블을 볼 수 있습니다.",R.drawable.e, ContextCompat.getColor(getApplicationContext(),R.color.deepgreen)));

        addSlide(AppIntroFragment.newInstance("타임테이블","해당 날짜에 공부한 기록을 볼 수 있습니다.\n\n공부기록을 보고 피드백을 작성할 수 있습니다.",R.drawable.f, ContextCompat.getColor(getApplicationContext(),R.color.deepgreen)));


        addSlide(AppIntroFragment.newInstance("그래프","해당 학기에 성적을 입력하고\n학기별 성적 변화를 볼 수 있습니다.\n\n과목 불러오기로 공부중인 과목의 이름을\n불러올 수 있습니다.",R.drawable.g, ContextCompat.getColor(getApplicationContext(),R.color.deepgreen)));

        addSlide(AppIntroFragment.newInstance("통계","공부시간 측정기록을 이용한\n다양한 월별 통계를 제공합니다.",R.drawable.h, ContextCompat.getColor(getApplicationContext(),R.color.deepgreen)));
        addSlide(AppIntroFragment.newInstance("","이제 DGU를 시작해 볼까요?",R.drawable.logo, ContextCompat.getColor(getApplicationContext(),R.color.deepgreen)));
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

