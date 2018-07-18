package mapp.com.sg.mapp_ca1.Firestore;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mapp.com.sg.mapp_ca1.ChatRoomActivity;
import mapp.com.sg.mapp_ca1.MainActivity;
import mapp.com.sg.mapp_ca1.Models.Message;

import static android.content.ContentValues.TAG;

public class FirestoreHelper {
    List<Message> messageList;
    static CollectionReference messagesCollection = FirebaseFirestore.getInstance().collection("messages");

    public FirestoreHelper(ChatRoomActivity r) {
        final ChatRoomActivity reference = r;

        messagesCollection
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        messageList = new ArrayList<>();
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                String name = document.getString("name");
                                String text = document.getString("text");
                                String url = document.getString("photoUrl");
                                String time = document.getString("timestamp");

                                Message fm = new Message(name,text,url, time);
                                messageList.add(fm);

                                reference.UpdateList(messageList);
                                reference.updateTasks();

                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }

                    }
                });
    }

    public FirestoreHelper(){

    }


    public void saveData(Message f){
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("name", f.getName());
        data.put("text", f.getText());
        data.put("photoUrl", f.getPhotoUrl());
        data.put("timestamp", f.getTimestamp());
        messagesCollection.document().set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("FirestoreHelper", "Document has been saved!");
            }
        });
    }

}