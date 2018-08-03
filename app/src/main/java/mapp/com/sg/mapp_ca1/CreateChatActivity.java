package mapp.com.sg.mapp_ca1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class CreateChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_chat);

        //set toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.chatToolbar);
        CreateChatActivity.this.setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //set Spinner
        Spinner spinner = (Spinner) findViewById(R.id.spinnerSubjects);
        // Create an ArrayAdapter using the string array and a default spinner
        ArrayAdapter<CharSequence> spinAdapter = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.spinnerItems, android.R.layout.simple_spinner_dropdown_item);
        // Specify layout to use when the list of choices appears
        spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply adapter to spinner
        spinner.setPrompt("Please select ");
        spinner.setAdapter(spinAdapter);
    }

    // Close activity and sends back to Browse fragment
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}
