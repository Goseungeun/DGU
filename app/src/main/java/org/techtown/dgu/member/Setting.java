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

                builder.setTitle("어플 정보").setMessage("충북대학교 소프트웨어학과 \n2021년 졸업작품");
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

                builder.setTitle("개발자 정보").setMessage("[01-05]팀\n\n2018019016 가을\n2018019009 고승은\n2018019025 임지현");
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return false;
            }
        });

    }



}
