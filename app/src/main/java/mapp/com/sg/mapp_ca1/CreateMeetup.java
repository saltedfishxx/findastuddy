
package mapp.com.sg.mapp_ca1;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.Date;

import mapp.com.sg.mapp_ca1.Firestore.MeetupFirestoreHelper;
import mapp.com.sg.mapp_ca1.Models.Meetup;

public class CreateMeetup extends AppCompatActivity{
    private EditText name;
    private Button meetupDateAndTime, meetupLocation, createMeetup;

    private Date dateTime;
    private String groupChatID;
    private GeoPoint location;
    private String meetupName;
    private int noPpl;

    MeetupFirestoreHelper meetupFirestoreHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_meetup);

        name = (EditText) findViewById(R.id.editMeetup);
        meetupDateAndTime = (Button) findViewById(R.id.btnMeetupDateTime);
        meetupLocation = (Button) findViewById(R.id.btnMeetupLocation);
        createMeetup = (Button) findViewById(R.id.btnCreateMeetup);



    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                //TODO: code methods for each buttons' onclick
                case(R.id.btnMeetupDateTime):
                    Intent i = new Intent(getApplicationContext(),PickDateTime.class);
                    startActivity(i);
                    break;

                case(R.id.btnMeetupLocation):
                    Intent i2 = new Intent(getApplicationContext(),PickLocation.class);
                    startActivity(i2);
                    break;

                case(R.id.btnCreateMeetup):
                    AddMeetUp();


            }
        }
    };

    private void AddMeetUp() {
        final String TAG = "MeetupFirestoreHelper";
        meetupFirestoreHelper = new MeetupFirestoreHelper();
        meetupName = name.getText().toString();



    }
}
