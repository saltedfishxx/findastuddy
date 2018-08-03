package mapp.com.sg.mapp_ca1.Firestore;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import mapp.com.sg.mapp_ca1.BrowseFragment;
import mapp.com.sg.mapp_ca1.HomeFragment;
import mapp.com.sg.mapp_ca1.Models.GroupChats;

import static android.content.ContentValues.TAG;

/**
 * Created by Lewis on 30/7/2018.
 */

public class  GroupChatFirestoreHelper {
    static CollectionReference gcCollection = FirebaseFirestore.getInstance().collection("groupchat");
    List<GroupChats> gcList;
    List<GroupChats> mychats;
    List<GroupChats> browse;
    List<GroupChats> browseDuplicate;
    GroupChats gc;
    FirebaseAuth firebaseAuth;

    //constructor to call when want to retrieve data
    public GroupChatFirestoreHelper(BrowseFragment r) {
        final BrowseFragment reference = r;

        firebaseAuth = FirebaseAuth.getInstance();
        gcCollection
                .orderBy("chatname", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        gcList = new ArrayList<>();
                        browse = new ArrayList<>();
                        browseDuplicate = new ArrayList<>();

                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                String chatId = document.getId();
                                String chatName = document.getString("chatname");
                                String chatDesc = document.getString("chatdesc");
                                // Need to find out how to get
                                List<String> members = (List<String>) document.get("members");
                                int memCount = members.size();
                                String picUrl = document.getString("picUrl");

                                //update list
                                gc = new GroupChats(chatId, chatName, chatDesc, memCount, members, picUrl);
                                gcList.add(gc);
                            }
                            if(gcList != null) {
                                for (GroupChats g : gcList) {
                                    if(!g.getMembers().contains(firebaseAuth.getCurrentUser().getUid()) && g.getMemCount() < 6)
                                    {
                                        browseDuplicate.add(g);

                                    }
                                }
                            }
                                Set<GroupChats> browseUnique = new HashSet<>(browseDuplicate);
                                browse.addAll(browseUnique);

                            reference.updateTasks();
                            reference.UpdateList(browse);

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }


                    }
                });
    }

    public GroupChatFirestoreHelper(HomeFragment r) {
        firebaseAuth = FirebaseAuth.getInstance();
        final HomeFragment reference = r;
        gcCollection
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        gcList = new ArrayList<>();
                        mychats = new ArrayList<>();
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                String chatId = document.getId();
                                String chatName = document.getString("chatname");
                                String chatDesc = document.getString("chatdesc");
                                // Need to find out how to get
                                List<String> members = (List<String>) document.get("members");
                                int memCount = members.size();
                                String picUrl = document.getString("picUrl");

                                //update list
                                gc = new GroupChats(chatId, chatName, chatDesc, memCount, members, picUrl);
                                gcList.add(gc);

                            }
                            if(gcList != null) {
                                for (GroupChats g : gcList) {
                                    for (String memberUid : g.getMembers()) {
                                        if (memberUid.equals(firebaseAuth.getCurrentUser().getUid())) {
                                            mychats.add(g);
                                        }
                                    }
                                }
                            }
                            reference.updateTasks();
                            reference.UpdateList(mychats);

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }


                    }
                });
    }

    public GroupChatFirestoreHelper() {
    }

    public List<GroupChats> getGcList() {
        return gcList;
    }

    public void saveData(GroupChats f) {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("chatname", f.getChatName());
        data.put("chatdesc", f.getChatDesc());
        data.put("members", f.getMembers());
        data.put("picUrl", f.getPicURL());
        gcCollection.document(f.getChatId()).set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("FirestoreHelper", "Document has been saved!");
            }
        });
    }

    public void updateData(GroupChats f) {
        gcCollection.document(f.getChatId())
                .update("members", f.getMembers()
                ).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "DocumentSnapshot successfully updated!");
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });
    }

    public void deleteData(GroupChats f) {
        if (f.getMemCount() == 0) {
            gcCollection.document(f.getChatId()).delete();
            Log.d(TAG, "DocumentSnapshot successfully updated!");
        } else {
            Log.w(TAG, "Error updating document");

        }
    }
}