package mapp.com.sg.mapp_ca1;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class Signup extends AppCompatActivity {
    CardView cv_secondary,cv_jc, cv_secondaryStream, cv_jcStream;
    LinearLayout layout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        layout = (LinearLayout) findViewById(R.id.termsLayout);

        cv_secondary = (CardView) findViewById(R.id.secondaryYearCard);
        cv_jc = (CardView) findViewById(R.id.jcYearCard);

        cv_secondaryStream = (CardView) findViewById(R.id.streamCard);
        cv_jcStream = (CardView) findViewById(R.id.streamJCCard);

        cv_jc.setVisibility(View.GONE);
        cv_jcStream.setVisibility(View.GONE);

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.RGEducation);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected

                if(checkedId == R.id.secondaryRB){
                    cv_secondary.setVisibility(View.VISIBLE);
                    cv_secondaryStream.setVisibility(View.VISIBLE);
                    cv_jc.setVisibility(View.GONE);
                    cv_jcStream.setVisibility(View.GONE);

                }else if (checkedId == R.id.jcRB){
                    cv_secondary.setVisibility(View.GONE);
                    cv_secondaryStream.setVisibility(View.GONE);
                    cv_jc.setVisibility(View.VISIBLE);
                    cv_jcStream.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}
