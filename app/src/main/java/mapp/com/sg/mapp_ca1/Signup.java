package mapp.com.sg.mapp_ca1;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import mapp.com.sg.mapp_ca1.Firestore.UserFirestoreHelper;
import mapp.com.sg.mapp_ca1.Models.Users;

public class Signup extends AppCompatActivity {
    private CardView cv_secondary, cv_jc, cv_secondaryStream, cv_jcStream;
    private LinearLayout layout;
    private TextView terms;
    private LinearLayout air;
    private EditText email, password, repassword, username;
    private RadioGroup streamSec, streamJC, yearSec, yearJC, education;
    private CheckBox checkBox;
    private String stream, year, edu;
    private Button signup;

    //firebase components
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private UserFirestoreHelper userFirestoreHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        //set views
        air = (LinearLayout) findViewById(R.id.space);
        layout = (LinearLayout) findViewById(R.id.termsLayout);
        checkBox = (CheckBox) findViewById(R.id.cbTerms);
        signup = (Button) findViewById(R.id.signupbtn);

        email = (EditText) findViewById(R.id.editEmail);
        username = (EditText) findViewById(R.id.editUsername);
        password = (EditText) findViewById(R.id.editPass);
        repassword = (EditText) findViewById(R.id.editPass2);

        cv_secondary = (CardView) findViewById(R.id.secondaryYearCard);
        cv_jc = (CardView) findViewById(R.id.jcYearCard);

        cv_secondaryStream = (CardView) findViewById(R.id.streamCard);
        cv_jcStream = (CardView) findViewById(R.id.streamJCCard);

        cv_jc.setVisibility(View.GONE);
        cv_jcStream.setVisibility(View.GONE);

        terms = (TextView) findViewById(R.id.terms);
        terms.setOnClickListener(clickaction);

        education = (RadioGroup) findViewById(R.id.RGEducation);
        streamSec = (RadioGroup) findViewById(R.id.RGStream);
        streamJC = (RadioGroup) findViewById(R.id.RGStreamJC);
        yearSec = (RadioGroup) findViewById(R.id.RGsecondary);
        yearJC = (RadioGroup) findViewById(R.id.RGjc);

        checkBox.setOnClickListener(clickaction);
        signup.setOnClickListener(clickaction);

        education.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected

                if (checkedId == R.id.secondaryRB) {
                    cv_secondary.setVisibility(View.VISIBLE);
                    cv_secondaryStream.setVisibility(View.VISIBLE);
                    cv_jc.setVisibility(View.GONE);
                    cv_jcStream.setVisibility(View.GONE);

                    ViewGroup.LayoutParams params = air.getLayoutParams();
                    params.height = 3520;
                    air.requestLayout();

                } else if (checkedId == R.id.jcRB) {
                    cv_secondary.setVisibility(View.GONE);
                    cv_secondaryStream.setVisibility(View.GONE);
                    cv_jc.setVisibility(View.VISIBLE);
                    cv_jcStream.setVisibility(View.VISIBLE);

                    ViewGroup.LayoutParams params = air.getLayoutParams();
                    params.height = 3020;
                    air.requestLayout();

                }
            }
        });
    }


    //if user clicks on T&Cs
    View.OnClickListener clickaction = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                //TODO: add click event for checkbox and login
                case R.id.terms:
                    // it was the first button
                    Intent intent = new Intent(getApplicationContext(), termsandconditions.class);
                    startActivity(intent);
                    break;
                case R.id.signupbtn:
                    validation();

            }
        }
    };

    //checkbox validation
    public void termsValidation() {
        if (!checkBox.isChecked()) {
            AlertDialog alertDialog = new AlertDialog.Builder(Signup.this).create();
            alertDialog.setTitle("Error");
            alertDialog.setMessage("Please agree to the Terms and Conditions");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }

    }

    //validation for user input
    public void validation() {
        //user select secondary
        if (cv_jc.getVisibility() == View.GONE) {
            if (education.getCheckedRadioButtonId() == -1 || streamSec.getCheckedRadioButtonId() == -1
                    || yearSec.getCheckedRadioButtonId() == -1 || email.getText().toString().isEmpty()
                    || username.getText().toString().isEmpty() || password.getText().toString().isEmpty()
                    || repassword.getText().toString().isEmpty()) {

                AlertDialog alertDialog = new AlertDialog.Builder(Signup.this).create();
                alertDialog.setTitle("Error");
                alertDialog.setMessage("Please key in all the Details");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            } else {
                termsValidation();
                createUser();
            }
        } else {
            //user selec jc
            if (education.getCheckedRadioButtonId() == -1 || streamJC.getCheckedRadioButtonId() == -1
                    || yearJC.getCheckedRadioButtonId() == -1 || email.getText().toString().isEmpty()
                    || username.getText().toString().isEmpty() || password.getText().toString().isEmpty()
                    || repassword.getText().toString().isEmpty()) {
                AlertDialog alertDialog = new AlertDialog.Builder(Signup.this).create();
                alertDialog.setTitle("Error");
                alertDialog.setMessage("Please key in all the Details");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            } else {
                validatePassword();
                termsValidation();
                createUser();
            }
        }
    }

    public void validatePassword() {
        if (!repassword.getText().toString().equals(password.getText().toString())) {
            AlertDialog alertDialog = new AlertDialog.Builder(Signup.this).create();
            alertDialog.setTitle("Error");
            alertDialog.setMessage("Passwords do not match");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }
    }

    public void getRadioSelection() {
        //get education
        int selectedEdu = education.getCheckedRadioButtonId();
        RadioButton selectedEduBtn = (RadioButton) findViewById(selectedEdu);
        edu = selectedEduBtn.getText().toString();

        //get year and stream
        if (cv_jc.getVisibility() == View.GONE) {

            int selectedSec = yearSec.getCheckedRadioButtonId();
            RadioButton selectedSecBtn = (RadioButton) findViewById(selectedSec);
            year = selectedSecBtn.getText().toString();

            int selectedSecStream = streamSec.getCheckedRadioButtonId();
            RadioButton selectedSecStreamBtn = (RadioButton) findViewById(selectedSecStream);
            stream = selectedSecStreamBtn.getText().toString();


        } else {
            int selectedJC = yearJC.getCheckedRadioButtonId();
            RadioButton selectedJCBtn = (RadioButton) findViewById(selectedJC);
            year = selectedJCBtn.getText().toString();

            int selectedJCStream = streamJC.getCheckedRadioButtonId();
            RadioButton selectedJCStreamBtn = (RadioButton) findViewById(selectedJCStream);
            stream = selectedJCStreamBtn.getText().toString();
        }

    }

    public void createUser() {
        //check if email exists
        final String TAG = "FirebaseAuth";
        // one of the radio buttons is checked
        mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    try {
                                        throw task.getException();

                                    } catch (FirebaseAuthUserCollisionException existEmail) {
                                        Log.d(TAG, "onComplete: exist_email");

                                        AlertDialog alertDialog = new AlertDialog.Builder(Signup.this).create();
                                        alertDialog.setTitle("Error");
                                        alertDialog.setMessage("Email used has already existed.");
                                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                });
                                        alertDialog.show();

                                    } catch (Exception e) {
                                        Log.d(TAG, "onComplete: " + e.getMessage());
                                    }
                                } else {
                                    //save user data
                                    //sign in user and go to home screen
                                    getRadioSelection();
                                    Users newUser = new Users(mAuth.getUid(), username.getText().toString(), edu, year, stream, null);
                                    userFirestoreHelper = new UserFirestoreHelper();
                                    userFirestoreHelper.saveData(newUser);

                                    // Sign in success
                                    FirebaseUser user = mAuth.getCurrentUser();

                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(username.getText().toString()).build();

                                    if (user != null) {
                                        user.updateProfile(profileUpdates);
                                    }

                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                }
                            }
                        }
                );
    }

}
