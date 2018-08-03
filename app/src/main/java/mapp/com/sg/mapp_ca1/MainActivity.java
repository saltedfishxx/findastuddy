package mapp.com.sg.mapp_ca1;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import mapp.com.sg.mapp_ca1.Models.Users;


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
        btmNav.setIconSize(30, 30);
        btmNav.enableAnimation(true);
        btmNav.enableShiftingMode(true);
        btmNav.enableItemShiftingMode(true);
        btmNav.setCurrentItem(1);

        boolean isNew = getIntent().getBooleanExtra("isNewItem", false);
        int position = getIntent().getIntExtra("position", 1);
        Users users = (Users) getIntent().getSerializableExtra("user");

        if (isNew) {
            profileFragment.getUser(users);
            btmNav.setCurrentItem(position);
            setFragment(profileFragment);
            getSupportFragmentManager().beginTransaction().detach(profileFragment).commitNowAllowingStateLoss();
            getSupportFragmentManager().beginTransaction().attach(profileFragment).commitAllowingStateLoss();
        }

        btmNav.setOnNavigationItemSelectedListener(new BottomNavigationViewEx.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
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
    public void onBackPressed() {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle("Sign out")
                .setMessage("Do you wish to sign out?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with adding
                        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                        firebaseAuth.signOut();
                        Intent intent = new Intent(getApplicationContext(), Login.class);
                        startActivity(intent);

                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                        dialog.cancel();
                    }
                })
                .show();
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    //Change screen based on fragment parsed in
    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (fragment == profileFragment) {
            //slide left
            fragmentTransaction.setCustomAnimations(R.animator.enter_from_right, R.animator.exit_to_left, R.animator.enter_from_left, R.animator.exit_to_right);

        } else if (fragment == browseFragment) {
            //slide right
            fragmentTransaction.setCustomAnimations(R.animator.enter_from_left, R.animator.exit_to_right, R.animator.enter_from_right, R.animator.exit_to_left);

        } else if (profileFragment.isVisible()) {
            fragmentTransaction.setCustomAnimations(R.animator.enter_from_left, R.animator.exit_to_right, R.animator.enter_from_right, R.animator.exit_to_left);

        } else if (browseFragment.isVisible()) {
            fragmentTransaction.setCustomAnimations(R.animator.enter_from_right, R.animator.exit_to_left, R.animator.enter_from_left, R.animator.exit_to_right);
        }
        fragmentTransaction.replace(R.id.main_screen, fragment);
        fragmentTransaction.commit();
    }


}
