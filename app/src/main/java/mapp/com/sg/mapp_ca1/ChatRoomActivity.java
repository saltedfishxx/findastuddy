package mapp.com.sg.mapp_ca1;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Document;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import mapp.com.sg.mapp_ca1.Adapter.MessageAdapter;
import mapp.com.sg.mapp_ca1.Firestore.FirestoreHelper;
import mapp.com.sg.mapp_ca1.Firestore.UserFirestoreHelper;
import mapp.com.sg.mapp_ca1.Models.GroupChats;
import mapp.com.sg.mapp_ca1.Models.Message;
import mapp.com.sg.mapp_ca1.Models.Users;

import static android.content.ContentValues.TAG;
import static mapp.com.sg.mapp_ca1.ChatInfoFragment.collectionReference;

public class ChatRoomActivity extends AppCompatActivity {
    private static final String TAG = "ChatRoom";

    public static final String ANONYMOUS = "anonymous";
    public static final int DEFAULT_MSG_LENGTH_LIMIT = 1000;
    private static final int RC_PHOTO_PICKER = 2;

    private ChatRoomActivity r;
    //views
    private MessageAdapter mAdapter;
    private RecyclerView mRecyclerView;

    private ProgressBar mProgressBar;
    private ImageButton mPhotoPickerButton;
    private EditText mMessageEditText;
    private Button mSendButton;
    private TextView chatTitle;
    private ImageView chatdp;
    private ImageButton info;

    private String mUsername;
    private static String UserUid;
    private List<Message> messageList;

    //firebase components
    private FirestoreHelper firestoreHelper;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private CollectionReference collectionReference;

    private ProgressDialog nDialog;
    private Users user;
    private List<Users> usersList;
    private GroupChats selectedChat;
    private String groupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);

        //set views
        selectedChat = (GroupChats) getIntent().getSerializableExtra("chat");
        groupId = selectedChat.getChatId();
        info = (ImageButton) findViewById(R.id.infoButton);
        chatTitle = (TextView) findViewById(R.id.chatTitle);
        chatdp = (ImageView) findViewById(R.id.chatdp);

        //init firebase components
        firebaseStorage = FirebaseStorage.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        UserUid = getIntent().getStringExtra("UserUid");
        storageReference = firebaseStorage.getReference().child("chat_photos");

        chatTitle.setText(selectedChat.getChatName());

        usersList = new ArrayList<>();

        //set groupchat pic
        if (selectedChat.getPicURL() != null) {
            Glide.with(chatdp.getContext())
                    .load(selectedChat.getPicURL())
                    .apply(RequestOptions.circleCropTransform())
                    .into(chatdp);
        } else {
            Glide.with(chatdp.getContext())
                    .load(R.drawable.circleprofile)
                    .apply(RequestOptions.circleCropTransform())
                    .into(chatdp);
        }

        //set toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.groupToolBar);
        ChatRoomActivity.this.setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //get all users
        CollectionReference usersCollection = FirebaseFirestore.getInstance().collection("users");
        usersList = new ArrayList<>();
        for (String m : selectedChat.getMembers()) {
            usersCollection
                    .document(m)
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

                                user = new Users(id, username, education_level, study_year, stream, profileUrl);
                                //update list
                                usersList.add(user);

                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }

                        }
                    });
        }

        //get current user data
        for (Users m : usersList) {
            if (m.getUid().equals(firebaseAuth.getCurrentUser().getUid())) {
                user = m;
            }
        }

        if (firebaseAuth.getCurrentUser().getUid() != null) {
            mUsername = firebaseAuth.getCurrentUser().getDisplayName();
            Log.d("Current User : ", mUsername);
        } else {
            mUsername = ANONYMOUS;
        }

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
                Message message = new Message(user.getUid(), mMessageEditText.getText().toString(), mUsername, null, s, user.getProfileUrl(), selectedChat.getChatId(), selectedChat.getChatName());
                firestoreHelper.saveData(message);
                // Clear input box
                mMessageEditText.setText("");
                firestoreHelper = new FirestoreHelper(r, groupId);
            }
        });

        //when user click on chat info
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ChatDetails.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("selectedChat", selectedChat);
                bundle.putSerializable("allusers", (Serializable) usersList);
                bundle.putString("UserUid", UserUid);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        collectionReference = FirebaseFirestore.getInstance().collection("Messages").document(selectedChat.getChatId()).collection("messageList");
        //update messages
        collectionReference
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {


            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    System.err.println("Listen failed:" + e);
                    return;
                }

                messageList = new ArrayList<>();
                for (DocumentSnapshot document : queryDocumentSnapshots) {
                    if (document.getData() != null) {
                        String id = document.getString("uid");
                        String name = document.getString("name");
                        String text = document.getString("text");
                        String url = document.getString("photoUrl");
                        Date t = document.getDate("timestamp");
                        String profilePic = document.getString("profileUrl");
                        String chatid = document.getString("chatid");
                        String chatname = document.getString("chatname");

                        SimpleDateFormat format = new SimpleDateFormat("hh:mm");
                        String time = format.format(t);

                        //create message object and add to list
                        Message fm = new Message(id, text, name, url, time, profilePic, chatid, chatname);
                        messageList.add(fm);
                        updateTasks();
                        UpdateList(messageList);
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK) {
            nDialog.show();
            final Uri selectedImageUri = data.getData();
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
                    Message message = new Message(user.getUid(), null, mUsername, downloadUri.toString(), s, user.getProfileUrl(), selectedChat.getChatId(),selectedChat.getChatName());
                    firestoreHelper.saveData(message);
                    firestoreHelper = new FirestoreHelper(r, groupId);

                }

            });
        }

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        r = this;
        firestoreHelper = new FirestoreHelper(r, groupId);
        mProgressBar.setVisibility(ProgressBar.GONE);
        // mRecyclerView.getLayoutManager().smoothScrollToPosition(mRecyclerView, new RecyclerView.State(), mRecyclerView.getAdapter().getItemCount());


    }

    //updates list
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