package org.techtown.dgu;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    org.techtown.dgu.Subject subject;
    org.techtown.dgu.Graph graph;
    org.techtown.dgu.Home home;
    org.techtown.dgu.License license;
    Getlicense my_license;

    //Home의 calendar에서 Timetable로 전달할 날짜값을 받아주는 번들
    Bundle dayBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        subject = new org.techtown.dgu.Subject();
        graph = new org.techtown.dgu.Graph();
        home = new org.techtown.dgu.Home();
        license = new org.techtown.dgu.License();
        my_license = new Getlicense();

        getSupportFragmentManager().beginTransaction().replace(R.id.container, home).commit();

        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setSelectedItemId(R.id.tab3);
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.tab1:
                        Toast.makeText(getApplicationContext(), "첫 번째 탭 선택됨", Toast.LENGTH_LONG).show();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, subject).commit();

                        return true;
                    case R.id.tab2:
                        Toast.makeText(getApplicationContext(), "두 번째 탭 선택됨", Toast.LENGTH_LONG).show();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container,graph).commit();

                        return true;
                    case R.id.tab3:
                        Toast.makeText(getApplicationContext(), "세 번째 탭 선택됨", Toast.LENGTH_LONG).show();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, home).commit();
                        return true;

                    case R.id.tab4:
                        Toast.makeText(getApplicationContext(), "네 번째 탭 선택됨", Toast.LENGTH_LONG).show();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, license).commit();
                        return true;

                    case R.id.tab5:
                        Toast.makeText(getApplicationContext(), "다섯 번째 탭 선택됨", Toast.LENGTH_LONG).show();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, my_license).commit();
                        return true;
                }

                return false;
            }
        });



    }

//    @Nullable
/*    public android.view.View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_subject_input, container, false);
        Button button = (Button) rootView.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToInputSchoolSubject();
            }
        });

        return rootView;
    }

    public void goToInputSchoolSubject() {
        Intent intent = new Intent(getApplicationContext(), InputSchoolSubject.class);
        startActivity(intent);
        overridePendingTransition(R.anim.entry, R.anim.exit);
    }*/
    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment).commit();      // Fragment로 사용할 MainActivity내의 layout공간을 선택합니다.
    }

    // replaceFragment에서 .addToBackStack(null)만 추가함
    public void replaceFragment_addtobackstack(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment).addToBackStack(null).commit();
    }

    public void setDayBundle(Bundle bundle){
        this.dayBundle=bundle;
    }

}
