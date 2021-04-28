package org.techtown.dgu;

import android.os.Bundle;
import android.preference.PreferenceFragment;



import androidx.annotation.Nullable;
import androidx.preference.PreferenceFragmentCompat;

public class Setting extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.setting, rootKey);

        //xml 폴더 안에 있는 setting.xml의 event
    }


}
