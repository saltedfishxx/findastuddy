package mapp.com.sg.mapp_ca1;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mapp.com.sg.mapp_ca1.Adapter.MainAdapter;


public class MainActivity extends AppCompatActivity {
    //Create Fragments
    private HomeFragment homeFragment;
    private BrowseFragment browseFragment;
    private ProfileFragment profileFragment;

    private BottomNavigationViewEx btmNav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        //Initialize Fragments
        homeFragment = new HomeFragment();
        browseFragment = new BrowseFragment();
        profileFragment = new ProfileFragment();

        btmNav = (BottomNavigationViewEx) findViewById(R.id.btmNav);
        btmNav.setTextSize(15);
        btmNav.setIconSize(30,30);
        btmNav.enableAnimation(true);
        btmNav.enableShiftingMode(true);
        btmNav.enableItemShiftingMode(true);
        btmNav.setCurrentItem(1);



        btmNav.setOnNavigationItemSelectedListener(new BottomNavigationViewEx.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.action_browse:
                        setFragment(browseFragment);
                        break;
                    case R.id.action_home:
                        setFragment(homeFragment);
                        break;
                    case R.id.action_profile:
                        setFragment(profileFragment);
                        getSupportFragmentManager().beginTransaction().detach(profileFragment).commitNowAllowingStateLoss();
                        getSupportFragmentManager().beginTransaction().attach(profileFragment).commitAllowingStateLoss();
                        break;
                }
                return true;
            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportFragmentManager().beginTransaction().detach(profileFragment).commitNowAllowingStateLoss();
        getSupportFragmentManager().beginTransaction().attach(profileFragment).commitAllowingStateLoss();
    }

    //Change screen based on fragment parsed in
    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_screen, fragment);
        fragmentTransaction.commit();
    }


}
