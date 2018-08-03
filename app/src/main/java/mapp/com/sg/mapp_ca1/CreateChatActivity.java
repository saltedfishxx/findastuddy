package mapp.com.sg.mapp_ca1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class CreateChatActivity extends AppCompatActivity {

    TextView errorMsg;
    EditText etChatName, etChatDesc;
    String chatName, chatDesc;
    Button createChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_chat);

        // Getting views
        errorMsg = (TextView) findViewById(R.id.tvError);
        createChat = (Button) findViewById(R.id.btncreateChat);
        etChatName = (EditText) findViewById(R.id.editChatName);
        etChatDesc = (EditText) findViewById(R.id.editChatDesc);

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
        spinner.setAdapter(spinAdapter);

//        createChat.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                chatName = tvChatName.getText().toString();
//                chatDesc = tvChatDesc.getText().toString();
//                if (chatName == "" || chatDesc == ""){
//                    errorMsg.setText("Please fill up required fills");
//                }else{
//                    errorMsg.setText(chatName + "\n" + chatDesc);
//                }
//            }
//        });

    }

    public void OnClick(View view) {
        chatName = etChatName.getText().toString();
        chatName = chatName.trim();
        chatDesc = etChatDesc.getText().toString();
        chatDesc = chatDesc.trim();
        if (chatName.matches("")|| chatDesc.matches("")) {
            errorMsg.setText("Please fill up required fields");
        } else {
            errorMsg.setText(chatName + "\n" + chatDesc);
        }
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