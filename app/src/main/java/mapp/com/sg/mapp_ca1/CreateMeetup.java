package mapp.com.sg.mapp_ca1;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.GeoPoint;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import mapp.com.sg.mapp_ca1.Firestore.MeetupFirestoreHelper;
import mapp.com.sg.mapp_ca1.Models.GroupChats;
import mapp.com.sg.mapp_ca1.Models.Meetup;


public class CreateMeetup extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private static Button meetupDateAndTime, meetupLocation, createMeetup;
    MeetupFirestoreHelper meetupFirestoreHelper;
    private EditText name;
    private Date meetupDateTime;
    private String groupChatID, dateDisplay, timeDisplay, uid;
    private GeoPoint location;
    private String meetupName;
    private int noPpl;
    private GroupChats selectedChatId;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private FirebaseAuth firebaseAuth;
    private Calendar mDate, mTime;
    private int PLACE_PICKER_REQUEST = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_meetup);

        Bundle bundle = getIntent().getExtras();
        selectedChatId = (GroupChats) bundle.getSerializable("selectedChats");


        name = (EditText) findViewById(R.id.editMeetup);
        meetupDateAndTime = (Button) findViewById(R.id.btnMeetupDateTime);
        meetupLocation = (Button) findViewById(R.id.btnMeetupLocation);
        createMeetup = (Button) findViewById(R.id.btnCreateMeetup);

        // get current user
        firebaseAuth = FirebaseAuth.getInstance();
        uid = firebaseAuth.getCurrentUser().getUid();


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

    //Adding meetup to FireStore
    private void AddMeetUp() {
        final String TAG = "MeetupFirestoreHelper";
        meetupFirestoreHelper = new MeetupFirestoreHelper();
        meetupName = name.getText().toString();
        List<String> peopleGoing = new ArrayList<>();
        peopleGoing.add(uid);
        //TODO: get groupChatId of group that created the meetup
        //Converting date and time into a timestamp
        final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
        String date = dateFormat.format(mDate.getTime());
        final SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm");
        String time = timeFormat.format(mTime.getTime());
        String dateTime = (date + time);
        try {
            final SimpleDateFormat finalFormat = new SimpleDateFormat("dd/MM/yy hh:mm");
            Date parsedDate = finalFormat.parse(dateTime);
            Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
            meetupDateTime = timestamp;
        } catch (Exception e) {
            e.printStackTrace();
        }
        Meetup meetup = new Meetup(null, meetupName, meetupDateTime, groupChatID, location, peopleGoing.size(), peopleGoing);
        meetupFirestoreHelper.saveData(meetup);
    }


    public void onClickDate(View view) {
        Calendar mCurrentDate = Calendar.getInstance();
        int mYear = mCurrentDate.get(Calendar.YEAR);
        int mMonth = mCurrentDate.get(Calendar.MONTH);
        int mDay = mCurrentDate.get(Calendar.DAY_OF_MONTH);

        Calendar mCurrentTime = Calendar.getInstance();
        int hour = mCurrentTime.get(Calendar.HOUR);
        int minute = mCurrentTime.get(Calendar.MINUTE);


        datePickerDialog = new DatePickerDialog(CreateMeetup.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, CreateMeetup.this, mYear, mMonth, mDay);
        datePickerDialog.show();
        timePickerDialog = new TimePickerDialog(CreateMeetup.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, CreateMeetup.this, hour, minute, false);
        timePickerDialog.show();

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        meetupDateAndTime.setText(dayOfMonth + "/" + (month + 1) + "/" + year + "   " + meetupDateAndTime.getText());
        mDate = Calendar.getInstance();
        mDate.set(year, month, dayOfMonth);
    }

    public void onClickLocation(View view) {
//        Intent i = new Intent(getApplicationContext(),PickLocation.class);
//        startActivity(i);
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        Intent intent;
        try {
            intent = builder.build(CreateMeetup.this);
            startActivityForResult(intent, PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                String address = String.format("%s", place.getAddress());
                location = new GeoPoint(place.getLatLng().latitude, place.getLatLng().longitude);
                meetupLocation.setText(address);
            }
        }
    }


    public void onClickCreateMeet(View view) {
        AddMeetUp();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        mTime = Calendar.getInstance();
        mTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
        mTime.set(Calendar.MINUTE, minute);

        meetupDateAndTime.setText(String.format("%02d:%02d", hourOfDay, minute));

    }
}
