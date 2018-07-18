package mapp.com.sg.mapp_ca1;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import mapp.com.sg.mapp_ca1.Adapter.MessageAdapter;
import mapp.com.sg.mapp_ca1.Firestore.FirestoreHelper;
import mapp.com.sg.mapp_ca1.Models.Message;

public class ChatRoomActivity extends AppCompatActivity {
    private static final String TAG = "ChatRoom";

    public static final String ANONYMOUS = "anonymous";
    public static final int DEFAULT_MSG_LENGTH_LIMIT = 1000;

    ChatRoomActivity r;
    //views
    MessageAdapter mAdapter;
    RecyclerView mRecyclerView;

    private ProgressBar mProgressBar;
    private ImageButton mPhotoPickerButton;
    private EditText mMessageEditText;
    private Button mSendButton;

    private String mUsername;
    List<Message> messageList;

    //firestore
    private FirestoreHelper firestoreHelper;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);
        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() != null){
            mUsername = firebaseAuth.getCurrentUser().getDisplayName();
        }else{
            mUsername = ANONYMOUS;
        }

        // Initialize references to views
        //set recycler view to its coressponding view
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewTasks);

        //set layout for recyclerview to be linear layout
        //measures and positions items within the view into a linear list
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        //initialize adapter and attach to recycler view
        mAdapter = new MessageAdapter(this);
        mRecyclerView.setAdapter(mAdapter);


        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mPhotoPickerButton = (ImageButton) findViewById(R.id.photoPickerButton);
        mMessageEditText = (EditText) findViewById(R.id.messageEditText);
        mSendButton = (Button) findViewById(R.id.sendButton);

        // Initialize progress bar
        mProgressBar.setVisibility(ProgressBar.INVISIBLE);

        // ImagePickerButton shows an image picker to upload a image for a message
        mPhotoPickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Fire an intent to show an image picker
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
                DateFormat dateFormatter = new SimpleDateFormat("hh:mm");
                dateFormatter.setLenient(false);
                Date currentTime = Calendar.getInstance().getTime();
                String s = dateFormatter.format(currentTime);
                Message message = new Message(mMessageEditText.getText().toString(),mUsername,null, s);
                firestoreHelper.saveData(message);
                // Clear input box
                mMessageEditText.setText("");
                firestoreHelper = new FirestoreHelper(r);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        r = this;
        firestoreHelper = new FirestoreHelper(r);
    }

    public void UpdateList (List<Message> msg) {
        mAdapter.clearAll();
        mAdapter.addAllItems(msg);

    }

    public void updateTasks () {
        mRecyclerView.getRecycledViewPool().clear();
        mAdapter.notifyDataSetChanged();
    }
}