package mapp.com.sg.mapp_ca1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class termsandconditions extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_termsandconditions);
        //set toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.tcToolBar);
        termsandconditions.this.setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myIntent = new Intent(getApplicationContext(), Signup.class);
        startActivityForResult(myIntent, 0);
        return true;

    }

}
