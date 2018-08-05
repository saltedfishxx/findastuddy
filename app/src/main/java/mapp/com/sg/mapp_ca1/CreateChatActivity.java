package mapp.com.sg.mapp_ca1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

import mapp.com.sg.mapp_ca1.Firestore.GroupChatFirestoreHelper;
import mapp.com.sg.mapp_ca1.Models.GroupChats;

public class CreateChatActivity extends AppCompatActivity {

    private static final int RC_PHOTO_PICKER = 2;

    // Variables for Views
    private TextView errorMsg;
    private EditText etChatName, etChatDesc;
    private ImageView chatImage;
    //set Spinner
    private Spinner spinner;

    // Variables for firebase related
    private GroupChatFirestoreHelper gc;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private String photoUrl;
    private Uri downloadUri;

    // Variables to store stuff
    private String chatName, chatDesc, uid, chatSub;
    private GroupChats groupChats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_chat);

        // Getting views
        errorMsg = (TextView) findViewById(R.id.tvError);
        etChatName = (EditText) findViewById(R.id.editChatName);
        etChatDesc = (EditText) findViewById(R.id.editChatDesc);
        chatImage = (ImageView) findViewById(R.id.chatImage);

        // Setting variables
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference().child("chat_profile_images");

        //set the views
        firebaseAuth = FirebaseAuth.getInstance();
        uid = firebaseAuth.getCurrentUser().getUid();

        //set toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.chatToolbar);
        CreateChatActivity.this.setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Create an ArrayAdapter using the string array and a default spinner
        spinner = (Spinner) findViewById(R.id.spinnerSubjects);
        ArrayAdapter<CharSequence> spinAdapter = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.spinnerItems, android.R.layout.simple_spinner_dropdown_item);
        // Specify layout to use when the list of choices appears
        spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply adapter to spinner
        spinner.setAdapter(spinAdapter);
    }

    // When Image is pressed
    public void onClickEdit(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
    }

    // When Create Chat button is pressed
    public void OnClick(View view) {
        chatName = etChatName.getText().toString();
        chatName = chatName.trim();
        chatDesc = etChatDesc.getText().toString();
        chatDesc = chatDesc.trim();
        chatSub = spinner.getSelectedItem().toString();
        if (chatName.matches("") || chatDesc.matches("")) {
            errorMsg.setText("Please fill up required fields");
        } else {
            gc = new GroupChatFirestoreHelper();
            List<String> members = new ArrayList<>();
            members.add(uid);
            // check if photo is null
            if (photoUrl != null) {
                groupChats = new GroupChats("", chatName, chatDesc, chatSub, members.size(), members, photoUrl);
            } else {
                groupChats = new GroupChats("", chatName, chatDesc, chatSub, members.size(), members, null);
            }
            //create groupchat in firestore
            gc.saveData(groupChats);
        }
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();
            StorageReference photoRef = storageReference.child(selectedImageUri.getLastPathSegment());

            //save photo
            final StorageTask<UploadTask.TaskSnapshot> uploadTask = photoRef.putFile(selectedImageUri).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    downloadUri = taskSnapshot.getDownloadUrl();
                    photoUrl = downloadUri.toString();
                    Glide.with(chatImage.getContext())
                            .load(downloadUri)
                            .apply(RequestOptions.circleCropTransform())
                            .into(chatImage);
                }
            });
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