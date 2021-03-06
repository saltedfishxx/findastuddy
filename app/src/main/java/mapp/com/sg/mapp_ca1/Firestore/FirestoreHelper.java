package mapp.com.sg.mapp_ca1.Firestore;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mapp.com.sg.mapp_ca1.ChatRoomActivity;
import mapp.com.sg.mapp_ca1.Models.Message;

import static android.content.ContentValues.TAG;

/**
 * This firestorehelper is used to read and write chat messages
 */

public class FirestoreHelper {
    private List<Message> messageList;
    private String groupid;
    private FirebaseAuth firebaseAuth;
    //get the reference for collection in firestore
    static CollectionReference messagesCollection = FirebaseFirestore.getInstance().collection("Messages");

    //constructor to call when want to retrieve data --> gets activity reference so can update lists
    public FirestoreHelper(ChatRoomActivity r, String id) {

        final ChatRoomActivity reference = r;
        groupid = id;

        messagesCollection.document(groupid).collection("messageList")
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        messageList = new ArrayList<>();
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                //for each document retrieved from collection pass it to the variables
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
                                Message fm = new Message(id, text, name, url, time, profilePic,chatid, chatname);
                                messageList.add(fm);

                                //update adapter based on new list
                                reference.UpdateList(messageList);
                                reference.updateTasks();

                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }

                    }
                });
    }

    //empty constructor
    public FirestoreHelper() {

    }

    public List<Message> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<Message> messageList) {
        this.messageList = messageList;
    }

    //called when saving message object
    public void saveData(Message f) {
        firebaseAuth = FirebaseAuth.getInstance();
        Date timestamp = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        try {
            timestamp = format.parse(f.getTimestamp());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("uid", firebaseAuth.getCurrentUser().getUid());
        data.put("name", f.getName());
        data.put("text", f.getText());
        data.put("photoUrl", f.getPhotoUrl());
        data.put("timestamp", timestamp);
        data.put("profileUrl", f.getProfileUrl());
        data.put("chatid", f.getChatid());
        data.put("chatname", f.getChatName());
        messagesCollection.document(groupid).collection("messageList").document().set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("FirestoreHelper", "Document has been saved!");
            }
        });
    }

}