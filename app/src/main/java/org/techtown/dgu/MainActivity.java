package org.techtown.dgu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.techtown.dgu.graph.GraphFragment;
import org.techtown.dgu.studylicense.LicenseFragment;
import org.techtown.dgu.subject.SubjectFragment;


public class MainActivity extends AppCompatActivity {

    SubjectFragment subject;
    GraphFragment graph;
    org.techtown.dgu.Home home;
    LicenseFragment license;
    StatsFragment statsfragment;
    long  backBtnTime = 0L;

    String category="category";
    //Home의 calendar에서 Timetable로 전달할 날짜값을 받아주는 번들
    Bundle dayBundle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //중간에 화면꺼짐 방지지
       getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        subject = new SubjectFragment();
        graph = new GraphFragment();
        home = new org.techtown.dgu.Home();
        license = new LicenseFragment();
        statsfragment=new StatsFragment();

        //stopwatch에서 화면 돌아올 때를 위한 함수들
        Intent intent = getIntent();
        if((String)intent.getSerializableExtra("category")!=null){
            this.category = (String)intent.getSerializableExtra("category");
        }

        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);
        switch (this.category){
            case "subject":
                bottomNavigation.setSelectedItemId(R.id.tab1);
                getSupportFragmentManager().beginTransaction().replace(R.id.container, subject).commit();
                break;
            case "license":
                bottomNavigation.setSelectedItemId(R.id.tab2);
                getSupportFragmentManager().beginTransaction().replace(R.id.container, license).commit();
                break;
            default:
                bottomNavigation.setSelectedItemId(R.id.tab3);
                getSupportFragmentManager().beginTransaction().replace(R.id.container, home).commit();
                break;
        }

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



    @Override
    public void onBackPressed() {

        StopwatchFragment fragment1 = (StopwatchFragment) getSupportFragmentManager().findFragmentByTag("StopwatchFragment");
        if (fragment1 == null){
            //backbutton을 두번 눌러야 종료되게
            long curTime = System.currentTimeMillis();
            long gapTime = curTime - backBtnTime;

            if(0 <= gapTime && 2000 >= gapTime) {
                super.onBackPressed();
                Log.v("onBackpressedTest","1번");
            }
            else {
                backBtnTime = curTime;
                Toast.makeText(this, "한번 더 누르면 종료됩니다.",Toast.LENGTH_SHORT).show();
                Log.v("onBackpressedTest","2번");
            }
        }
        else if (fragment1 != null && getSupportFragmentManager().getBackStackEntryCount() > 0){
            //stopwatch에서 뒤로가기 버튼 막기
            Toast.makeText(getApplicationContext(),"시간 측정 중에는 뒤로 갈 수 없습니다.", Toast.LENGTH_LONG).show();
            Log.v("onBackpressedTest","3번");
        }

    }


}
