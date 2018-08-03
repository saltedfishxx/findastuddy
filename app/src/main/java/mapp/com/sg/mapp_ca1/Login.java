package mapp.com.sg.mapp_ca1;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity implements View.OnClickListener {
    EditText emailInput, passwordInput;
    TextView errortxt;
    Button eye;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        emailInput = (EditText) findViewById(R.id.editEmail);
        passwordInput = (EditText) findViewById(R.id.editPass);
        errortxt = (TextView) findViewById(R.id.errortxt);
        eye = (Button) findViewById(R.id.eye);

        //init firebase auth
        auth = FirebaseAuth.getInstance();

        //check if logged in
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(Login.this, MainActivity.class));
        }

        eye.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        passwordInput.setInputType(InputType.TYPE_CLASS_TEXT);
                        break;
                    case MotionEvent.ACTION_UP:
                        passwordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        loginUser(emailInput.getText().toString(), passwordInput.getText().toString());
    }

    private void loginUser(String email, String pass) {
        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    errortxt.setText("Error: Invalid email or password");
                    emailInput.setText("");
                    passwordInput.setText("");
                } else {
                    startActivity(new Intent(Login.this, MainActivity.class));
                }
            }
        });
    }
}
