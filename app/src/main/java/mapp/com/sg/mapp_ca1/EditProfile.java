package mapp.com.sg.mapp_ca1;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import mapp.com.sg.mapp_ca1.Firestore.FirestoreHelper;
import mapp.com.sg.mapp_ca1.Firestore.UserFirestoreHelper;
import mapp.com.sg.mapp_ca1.Models.Message;
import mapp.com.sg.mapp_ca1.Models.Users;

public class EditProfile extends AppCompatActivity {

    private static final int RC_PHOTO_PICKER =  2;
    CardView cv_secondary, cv_jc, cv_secondaryStream, cv_jcStream;
    EditText username;
    RadioGroup streamSec, streamJC, yearSec, yearJC, education;
    CheckBox checkBox;
    String stream, year, edu, uid, profileUrl;
    Button signup;
    TextView cancel, done;
    ImageView editImage, userImage;

    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    FirebaseAuth firebaseAuth;
    String photoUrl;

    Uri downloadUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);

        //TODO: add username view and set it to username
        //TODO: add logic to get profile image

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference().child("user_profile_pics");

        //set the views
        firebaseAuth = FirebaseAuth.getInstance();
        uid = firebaseAuth.getCurrentUser().getUid();

        username = (EditText) findViewById(R.id.editName);
        editImage = (ImageView) findViewById(R.id.editImage);
        userImage = (ImageView) findViewById(R.id.userImage);

        username.setText(firebaseAuth.getCurrentUser().getDisplayName());

        cv_secondary = (CardView) findViewById(R.id.secondaryYearCard);
        cv_jc = (CardView) findViewById(R.id.jcYearCard);

        cv_secondaryStream = (CardView) findViewById(R.id.streamCard);
        cv_jcStream = (CardView) findViewById(R.id.streamJCCard);

        cv_jc.setVisibility(View.GONE);
        cv_jcStream.setVisibility(View.GONE);

        education = (RadioGroup) findViewById(R.id.RGEducation);
        streamSec = (RadioGroup) findViewById(R.id.RGStream);
        streamJC = (RadioGroup) findViewById(R.id.RGStreamJC);
        yearSec = (RadioGroup) findViewById(R.id.RGsecondary);
        yearJC = (RadioGroup) findViewById(R.id.RGjc);

        done = (TextView) findViewById(R.id.done);
        cancel = (TextView) findViewById(R.id.cancel);

        done.setOnClickListener(clickaction);
        cancel.setOnClickListener(clickaction);

        photoUrl = getIntent().getStringExtra("imageLink");
        if(photoUrl != null){
            Glide.with(userImage.getContext())
                    .load(photoUrl)
                    .apply(RequestOptions.circleCropTransform())
                    .into(userImage);
        }
        education.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected

                if (checkedId == R.id.secondaryRB) {
                    cv_secondary.setVisibility(View.VISIBLE);
                    cv_secondaryStream.setVisibility(View.VISIBLE);
                    cv_jc.setVisibility(View.GONE);
                    cv_jcStream.setVisibility(View.GONE);


                } else if (checkedId == R.id.jcRB) {
                    cv_secondary.setVisibility(View.GONE);
                    cv_secondaryStream.setVisibility(View.GONE);
                    cv_jc.setVisibility(View.VISIBLE);
                    cv_jcStream.setVisibility(View.VISIBLE);

                }
            }
        });

        // editImage shows an image picker to upload a image to set profile
        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Fire an intent to show an image picker
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY,true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
            }
        });


    }



    //TODO: add validation for user input
    public void validation() {
        //user select secondary
        if (cv_jc.getVisibility() == View.GONE) {
            if (education.getCheckedRadioButtonId() == -1 || streamSec.getCheckedRadioButtonId() == -1
                    || yearSec.getCheckedRadioButtonId() == -1
                    || username.getText().toString().isEmpty() ) {

                AlertDialog alertDialog = new AlertDialog.Builder(EditProfile.this).create();
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
                getRadioSelection();

                if(downloadUri == null){
                    profileUrl = null;
                }else {
                    profileUrl = downloadUri.toString();
                }
                Users users = new Users(uid, username.getText().toString(), edu,year,stream,profileUrl);
                updateUser(users);
            }
        } else {
            //user selec jc
            if (education.getCheckedRadioButtonId() == -1 || streamJC.getCheckedRadioButtonId() == -1
                    || yearJC.getCheckedRadioButtonId() == -1
                    || username.getText().toString().isEmpty()) {
                AlertDialog alertDialog = new AlertDialog.Builder(EditProfile.this).create();
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
                getRadioSelection();
                if(downloadUri == null){
                    profileUrl = photoUrl;
                }else {
                    profileUrl = downloadUri.toString();
                }
                Users users = new Users(uid, username.getText().toString(), edu,year,stream,profileUrl);
                updateUser(users);
            }
        }
    }
    View.OnClickListener clickaction = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                //TODO: add click event for checkbox and login
                case R.id.done:
                    validation();
                    break;
                case R.id.cancel:
                    finish();
            }
        }
    };

    public void updateUser(Users users){
        UserFirestoreHelper firestoreHelper = new UserFirestoreHelper();
        firestoreHelper.updateData(users);
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("position", 2);
        intent.putExtra("isNewItem", true);
        startActivity(intent);
        finish();
    }

    //TODO: pass integers based on radio button selection
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK){
            Uri selectedImageUri = data.getData();
            StorageReference photoRef = storageReference.child(selectedImageUri.getLastPathSegment());

            //save photo
            final StorageTask<UploadTask.TaskSnapshot> uploadTask = photoRef.putFile(selectedImageUri).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    downloadUri = taskSnapshot.getDownloadUrl();
                    Glide.with(userImage.getContext())
                            .load(downloadUri)
                            .apply(RequestOptions.circleCropTransform())
                            .into(userImage);
                }

            });


        }

    }
}
