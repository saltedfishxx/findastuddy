
package mapp.com.sg.mapp_ca1;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import mapp.com.sg.mapp_ca1.Firestore.MeetupFirestoreHelper;
import mapp.com.sg.mapp_ca1.Models.Meetup;

public class CreateMeetup extends AppCompatActivity{
    private EditText meetupName;
    private Button meetupDateAndTime, meetupLocation, createMeetup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_meetup);

        meetupName = (EditText) findViewById(R.id.editMeetup);
        meetupDateAndTime = (Button) findViewById(R.id.btnMeetupDateTime);
        meetupLocation = (Button) findViewById(R.id.btnMeetupLocation);
        createMeetup = (Button) findViewById(R.id.btnCreateMeetup);

        MeetupFirestoreHelper meetupFirestoreHelper;

    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                //TODO: code methods for each buttons' onclick
                case(R.id.btnMeetupDateTime):

                    break;

                case(R.id.btnMeetupLocation):
                    Intent i = new Intent(getApplicationContext(),PickLocation.class);
                    startActivity(i);
                    break;

                case(R.id.btnCreateMeetup):
                    AddMeetUp();


            }
        }
    };

    private void AddMeetUp() {


    }
}
