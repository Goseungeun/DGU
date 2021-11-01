package org.techtown.dgu.member;

import android.app.AlertDialog;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.View;
import android.widget.ImageButton;


import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreference;
import androidx.preference.SwitchPreferenceCompat;

import org.techtown.dgu.R;

public class Setting extends PreferenceFragmentCompat {
    //xml 폴더 안에 있는 setting.xml의 event
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.setting, rootKey);


        //setting.xml에서 어플정보 클릭시 실행
        Preference appinfo = findPreference("appinfo"); // click시 Fragment를 전환할 event를 발생시킬 버튼을 정의합니다.

        appinfo.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                builder.setTitle("어플 정보").setMessage("충북대학교 소프트웨어학과 \n2021년 졸업작품\n\n대학생을 위한 시간관리 어플리케이션\n\n" +
                        "작품 ‘Don’t give up’은 학업과 관련된 계획을 관리하고, 목표 날짜에 목표 분량을 이루기 위한 일일 최소 달성량을 제공하여, 효율적으로 목표에 도달할 수 있게 도움을 주는 어플리케이션이다.\n");
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return false;
            }
        });

        //setting.xml에서 개발자 정보 클릭시 실행
        Preference developerinfo = findPreference("developerinfo"); // click시 Fragment를 전환할 event를 발생시킬 버튼을 정의합니다.

        developerinfo.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                builder.setTitle("개발자 정보").setMessage("[01-05]팀\n\n천문우주학과 2018019016 가을\n천문우주학과 2018019009 고승은\n천문우주학과 2018019025 임지현");
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return false;
            }
        });

        //setting.xml에서 개발자 정보 클릭시 실행
        Preference opensourceinfo = findPreference("opensourceinfo"); // click시 Fragment를 전환할 event를 발생시킬 버튼을 정의합니다.

        opensourceinfo.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                builder.setTitle("오픈소스 정보").setMessage("" +
                        "\n[dependencies]\n" +
                        "androidx.appcompat:appcompat:1.2.0\n" +
                        "com.google.android.material:material:1.3.0\n" +
                        "androidx.constraintlayout:constraintlayout:2.0.4\n" +
                        "com.github.PhilJay:MPAndroidChart:v3.1.0'\n" +
                        "androidx.gridlayout:gridlayout:1.0.0\n" +
                        "androidx.viewpager:viewpager:1.0.0\n" +
                        "junit:junit:4.+\n" +
                        "androidx.test.ext:junit:1.1.2\n" +
                        "androidx.test.espresso:espresso-core:3.3.0\n" +
                        "androidx.preference:preference:1.1.0\n" +
                        "com.airbnb.android:lottie:3.0.7\n" +
                        "com.github.apl-devs:appintro:v4.2.3\n" +
                        "\n\n[splash animation]\n" +
                        "https://lottiefiles.com/4054-smoothymon-clap\n" +
                        "\n\n[icon open source]\n" +
                        "https://www.flaticon.com/kr/free-icon/arrow_507257\n" +
                        "https://www.flaticon.com/free-icon/home_1946488\n" +
                        "https://www.flaticon.com/free-icon/line-chart_709861\n" +
                        "https://www.flaticon.com/free-icon/driver-license_3381635\n" +
                        "https://www.flaticon.com/free-icon/pen_1250615\n" +
                        "https://www.flaticon.com/free-icon/check-mark_1442912\n" +
                        "https://www.flaticon.com/free-icon/x-mark_1617543\n" +
                        "https://www.flaticon.com/free-icon/minus_1828906\n" +
                        "https://www.flaticon.com/free-icon/pause_2088562\n" +
                        "https://www.flaticon.com/free-icon/play-button_482059\n" +
                        "https://www.flaticon.com/free-icon/medal_2583344\n" +
                        "https://www.flaticon.com/free-icon/medal_2583319\n" +
                        "https://www.flaticon.com/free-icon/medal_2583434\n" +
                        "https://www.flaticon.com/free-icon/gear_1242392\n" +
                        "https://www.flaticon.com/free-icon/grade_2228715\n" +
                        "https://www.flaticon.com/free-icon/right-arrow_271228\n" +
                        "https://www.flaticon.com/free-icon/left-arrow_271220\n");
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return false;
            }
        });

    }



}
