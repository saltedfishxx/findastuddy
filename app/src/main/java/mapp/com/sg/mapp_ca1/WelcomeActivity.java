package mapp.com.sg.mapp_ca1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class WelcomeActivity extends AppCompatActivity {

    Button loginbtn, signupbtn;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        loginbtn = (Button) findViewById(R.id.loginbtn);
        signupbtn = (Button) findViewById(R.id.signupbtn);

        loginbtn.setOnClickListener(clickaction);
        signupbtn.setOnClickListener(clickaction);

    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    View.OnClickListener clickaction = new View.OnClickListener() {
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.loginbtn:
                    // it was the first button
                    Intent intent = new Intent(getApplicationContext(), Login.class);
                    startActivity(intent);
                    break;
                case R.id.signupbtn:
                    // it was the second button
                    Intent intent2 = new Intent(getApplicationContext(), Signup.class);
                    startActivity(intent2);
                    break;
            }
        }
    };
}
