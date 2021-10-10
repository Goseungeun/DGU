package org.techtown.dgu;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.techtown.dgu.graph.GraphFragment;
import org.techtown.dgu.mylicense.MyLicenseFragment;
import org.techtown.dgu.studylicense.LicenseFragment;
import org.techtown.dgu.subject.SubjectFragment;


public class MainActivity extends AppCompatActivity {

    SubjectFragment subject;
    GraphFragment graph;
    org.techtown.dgu.Home home;
    LicenseFragment license;
    MyLicenseFragment my_license;
    StatsFragment statsfragment;

    //Home의 calendar에서 Timetable로 전달할 날짜값을 받아주는 번들
    Bundle dayBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        subject = new SubjectFragment();
        graph = new GraphFragment();
        home = new org.techtown.dgu.Home();
        license = new LicenseFragment();
        my_license = new MyLicenseFragment();
        statsfragment=new StatsFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.container, home).commit();

        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setSelectedItemId(R.id.tab3);
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.tab1:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, subject).commit();

                        return true;
                    case R.id.tab2:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container,license).commit();

                        return true;
                    case R.id.tab3:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, home).commit();
                        return true;

                    case R.id.tab4:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, graph).commit();
                        return true;

                    case R.id.tab5:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, statsfragment).commit();
                        return true;
                }

                return false;
            }
        });



     /*   // 날짜를 출력하는 텍스트뷰에 오늘 날짜 설정.
        TextView tv = findViewById(R.id.editTextDate);
        Calendar cal = Calendar.getInstance();
        tv.setText(cal.get(Calendar.YEAR) +"-"+ (cal.get(Calendar.MONTH)+1) +"-"+ cal.get(Calendar.DATE));*/

    }
/*
//license_input 날짜 입력
    DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int yy, int mm, int dd) {
                    // Date Picker에서 선택한 날짜를 TextView에 설정
                    TextView tv = findViewById(R.id.editTextDate);
                    tv.setText(String.format("%d-%d-%d", yy,mm+1,dd));
                }
            };

    public void mOnClick_DatePick(View view){
        // DATE Picker가 처음 떴을 때, 오늘 날짜가 보이도록 설정.
        Calendar cal = Calendar.getInstance();
        new DatePickerDialog(this, mDateSetListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE)).show();
    }
*/



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
