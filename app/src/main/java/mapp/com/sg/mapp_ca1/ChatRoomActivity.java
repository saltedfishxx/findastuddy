package mapp.com.sg.mapp_ca1;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import mapp.com.sg.mapp_ca1.Adapter.MessageAdapter;
import mapp.com.sg.mapp_ca1.Firestore.FirestoreHelper;
import mapp.com.sg.mapp_ca1.Firestore.UserFirestoreHelper;
import mapp.com.sg.mapp_ca1.Models.GroupChats;
import mapp.com.sg.mapp_ca1.Models.Message;
import mapp.com.sg.mapp_ca1.Models.Users;

public class ChatRoomActivity extends AppCompatActivity {
    private static final String TAG = "ChatRoom";

    public static final String ANONYMOUS = "anonymous";
    public static final int DEFAULT_MSG_LENGTH_LIMIT = 1000;
    private static final int RC_PHOTO_PICKER = 2;

    ChatRoomActivity r;
    //views
    MessageAdapter mAdapter;
    RecyclerView mRecyclerView;

    private ProgressBar mProgressBar;
    private ImageButton mPhotoPickerButton;
    private EditText mMessageEditText;
    private Button mSendButton;
    private TextView chatTitle;
    private ImageView chatdp;

    private String mUsername;
    List<Message> messageList;

    //firebase
    private FirestoreHelper firestoreHelper;
    private UserFirestoreHelper userFirestoreHelper;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    ProgressDialog nDialog;
    Users user;
    GroupChats selectedChat;
    String groupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);

        selectedChat = (GroupChats) getIntent().getSerializableExtra("chat");
        groupId = selectedChat.getChatId();

        //init firebase components
        firebaseStorage = FirebaseStorage.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = firebaseStorage.getReference().child("chat_photos");

        chatTitle = (TextView) findViewById(R.id.chatTitle);
        chatdp = (ImageView) findViewById(R.id.chatdp);

        chatTitle.setText(selectedChat.getChatName());

        if (selectedChat.getPicURL() != null) {
            Glide.with(chatdp.getContext())
                    .load(selectedChat.getPicURL())
                    .apply(RequestOptions.circleCropTransform())
                    .into(chatdp);
        } else {
            Glide.with(chatdp.getContext())
                    .load(R.drawable.ic_group_black_24dp)
                    .apply(RequestOptions.circleCropTransform())
                    .into(chatdp);
        }

        //set toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.groupToolBar);
        ChatRoomActivity.this.setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        CollectionReference usersCollection = FirebaseFirestore.getInstance().collection("users");

        usersCollection
                .document(firebaseAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            String id = document.getId();
                            String username = document.getString("username");
                            String education_level = document.getString("education_level");
                            String study_year = document.getString("study_year");
                            String stream = document.getString("stream");
                            String profileUrl = document.getString("profileUrl");

                            if (id.equals(firebaseAuth.getCurrentUser().getUid())) {
                                user = new Users(id, username, education_level, study_year, stream, profileUrl);
                            }


                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }


                    }
                });

        if (firebaseAuth.getCurrentUser() != null) {
            mUsername = firebaseAuth.getCurrentUser().getDisplayName();
            Log.d("Current User : ", mUsername);
        } else {
            mUsername = ANONYMOUS;
        }


        //TODO: when group chat has set up set toolbar title as group name
        //TODO: add up button

        //create progress dialog when sending images
        nDialog = new ProgressDialog(ChatRoomActivity.this);
        nDialog.setMessage("Loading..");
        nDialog.setTitle("Sending...");
        nDialog.setIndeterminate(false);
        nDialog.setCancelable(false);


        // Initialize references to views
        //set recycler view to its coressponding view
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewTasks);

        //set layout for recyclerview to be linear layout
        //measures and positions items within the view into a linear list
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        //initialize adapter and attach to recycler view
        mAdapter = new MessageAdapter(this, messageList);
        mRecyclerView.setAdapter(mAdapter);

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mPhotoPickerButton = (ImageButton) findViewById(R.id.photoPickerButton);
        mMessageEditText = (EditText) findViewById(R.id.messageEditText);
        mSendButton = (Button) findViewById(R.id.sendButton);

        // Initialize progress bar
        mProgressBar.setVisibility(ProgressBar.VISIBLE);


        // ImagePickerButton shows an image picker to upload a image for a message
        mPhotoPickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Fire an intent to show an image picker
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
            }
        });

        // Enable Send button when there's text to send
        mMessageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    mSendButton.setEnabled(true);
                } else {
                    mSendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        mMessageEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(DEFAULT_MSG_LENGTH_LIMIT)});

        // Send button sends a message and clears the EditText
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Send messages on click
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                if (inputManager != null) {
                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }

                DateFormat dateFormatter = new SimpleDateFormat("hh:mm:ss");
                dateFormatter.setLenient(false);
                Date currentTime = Calendar.getInstance().getTime();
                String s = dateFormatter.format(currentTime);
                Message message = new Message(user.getUid(), mMessageEditText.getText().toString(), mUsername, null, s, user.getProfileUrl());
                firestoreHelper.saveData(message);
                // Clear input box
                mMessageEditText.setText("");
                firestoreHelper = new FirestoreHelper(r, groupId);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK) {
            nDialog.show();
            Uri selectedImageUri = data.getData();
            StorageReference photoRef = storageReference.child(selectedImageUri.getLastPathSegment());

            //save photo
            final StorageTask<UploadTask.TaskSnapshot> uploadTask = photoRef.putFile(selectedImageUri).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    DateFormat dateFormatter = new SimpleDateFormat("hh:mm:ss");
                    dateFormatter.setLenient(false);
                    Date currentTime = Calendar.getInstance().getTime();
                    String s = dateFormatter.format(currentTime);
                    Uri downloadUri = taskSnapshot.getDownloadUrl();
                    Message message = new Message(user.getUid(), null, mUsername, downloadUri.toString(), s, user.getProfileUrl());
                    firestoreHelper.saveData(message);
                    firestoreHelper = new FirestoreHelper(r, groupId);

                }

            });
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        r = this;
        firestoreHelper = new FirestoreHelper(r, groupId);
        mProgressBar.setVisibility(ProgressBar.GONE);
        // mRecyclerView.getLayoutManager().smoothScrollToPosition(mRecyclerView, new RecyclerView.State(), mRecyclerView.getAdapter().getItemCount());

    }

    public void UpdateList(List<Message> msg) {
        mAdapter.clearAll();
        mAdapter.addAllItems(msg);

    }

    public void updateTasks() {
        mRecyclerView.getRecycledViewPool().clear();
        mAdapter.notifyDataSetChanged();
        mRecyclerView.getLayoutManager().smoothScrollToPosition(mRecyclerView, new RecyclerView.State(), mRecyclerView.getAdapter().getItemCount());
        nDialog.dismiss();
    }
}