
package mapp.com.sg.mapp_ca1;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

import mapp.com.sg.mapp_ca1.Firestore.MeetupFirestoreHelper;
import mapp.com.sg.mapp_ca1.Models.Meetup;

public class CreateMeetup extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{
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

//    View.OnClickListener clickListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            switch(v.getId()){
//                //TODO: code methods for each buttons' onclick
//                case(R.id.btnMeetupDateTime):
//                    Intent i = new Intent(getApplicationContext(),PickDateTime.class);
//                    startActivity(i);
//                    break;
//
//                case(R.id.btnMeetupLocation):
//                    Intent i2 = new Intent(getApplicationContext(),PickLocation.class);
//                    startActivity(i2);
//                    break;
//
//                case(R.id.btnCreateMeetup):
//                    AddMeetUp();
//
//
//            }
//        }
//    };

    private void AddMeetUp() {
        final String TAG = "MeetupFirestoreHelper";
        meetupFirestoreHelper = new MeetupFirestoreHelper();
        meetupName = name.getText().toString();



    }

    public void onClickDate(View view) {
        Intent i = new Intent(getApplicationContext(),PickDateTime.class);
        startActivity(i);
    }

    public void onClickLocation(View view) {
        Intent i2 = new Intent(getApplicationContext(),PickLocation.class);
        startActivity(i2);
    }


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        monthOfYear = c.get(Calendar.MONTH);
        dayOfMonth = c.get(Calendar.DAY_OF_MONTH);

        Date date = new Date(year,monthOfYear,dayOfMonth);

    }

    public void onClickCreateMeet(View view) {
        AddMeetUp();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Date time = Calendar.getInstance().getTime();


    }
}
