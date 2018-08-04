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
    TextView errorMsg;
    EditText etChatName, etChatDesc;
    Button createChat;
    ImageView editImageC, chatImage;

    // Variables for firebase related
    GroupChatFirestoreHelper gc;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    FirebaseAuth firebaseAuth;
    String photoUrl;
    Uri downloadUri;

    // Variables to store stuff
    String chatName, chatDesc, uid;
    GroupChats groupChats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_chat);

        // Getting views
        errorMsg = (TextView) findViewById(R.id.tvError);
        createChat = (Button) findViewById(R.id.btncreateChat);
        etChatName = (EditText) findViewById(R.id.editChatName);
        etChatDesc = (EditText) findViewById(R.id.editChatDesc);
        editImageC = (ImageView) findViewById(R.id.editImage);
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

        //set Spinner
        Spinner spinner = (Spinner) findViewById(R.id.spinnerSubjects);
        // Create an ArrayAdapter using the string array and a default spinner
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
        if (chatName.matches("") || chatDesc.matches("")) {
            errorMsg.setText("Please fill up required fields");
        } else {
            // Add code to add Group Chats to firebase
            //TODO DEBUG THIS GAY SHIT TYVM
            List<String> members = new ArrayList<String>();
            members.add(uid);
            // downloadUri is null
            groupChats = new GroupChats(null, chatName, chatDesc, members.size(), members, downloadUri.toString());
            gc.saveData(groupChats);
        }
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