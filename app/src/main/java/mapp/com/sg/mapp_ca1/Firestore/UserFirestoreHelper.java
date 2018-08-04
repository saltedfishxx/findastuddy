package mapp.com.sg.mapp_ca1.Firestore;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mapp.com.sg.mapp_ca1.ChatInfoFragment;
import mapp.com.sg.mapp_ca1.Models.GroupChats;
import mapp.com.sg.mapp_ca1.Models.Users;
import mapp.com.sg.mapp_ca1.Signup;

import static android.content.ContentValues.TAG;

/**
 * This firestorehelper is used to read, write and update users and chat members
 */

public class UserFirestoreHelper {
    static CollectionReference usersCollection = FirebaseFirestore.getInstance().collection("users");
    private List<Users> usersList;
    private List<Users> members;


    //constructor to call when want to retrieve data
    public UserFirestoreHelper(Signup r) {
        final Signup reference = r;

        usersCollection
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        usersList = new ArrayList<>();
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                String id = document.getId();
                                String username = document.getString("username");
                                String education_level = document.getString("education_level");
                                String study_year = document.getString("study_year");
                                String stream = document.getString("stream");
                                String profileUrl = document.getString("profileUrl");

                                Users users = new Users(id, username, education_level, study_year, stream, profileUrl);
                                //update list

                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }

                    }
                });
    }

    public UserFirestoreHelper() {

    }

    public UserFirestoreHelper(ChatInfoFragment r, final GroupChats selectedChat) {
        members = new ArrayList<>();
        final ChatInfoFragment reference = r;
        GroupChats chat = selectedChat;
        usersCollection
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        usersList = new ArrayList<>();
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                String id = document.getId();
                                String username = document.getString("username");
                                String education_level = document.getString("education_level");
                                String study_year = document.getString("study_year");
                                String stream = document.getString("stream");
                                String profileUrl = document.getString("profileUrl");

                                Users users = new Users(id, username, education_level, study_year, stream, profileUrl);
                                //update list
                                usersList.add(users);
                            }
                            for (Users u : usersList) {
                                for (String m : selectedChat.getMembers()) {
                                    if (u.getUid().equals(m)) {
                                        members.add(u);
                                    }
                                }
                            }
                            reference.updateTasks();
                            reference.UpdateList(members);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }

                    }
                });


    }

    public List<Users> getMembers() {
        return members;
    }

    public void saveData(Users f) {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("username", f.getUsername());
        data.put("education_level", f.getEducation_lvl());
        data.put("study_year", f.getYear());
        data.put("stream", f.getStream());
        data.put("profileUrl", f.getProfileUrl());
        usersCollection.document(f.getUid()).set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("FirestoreHelper", "Document has been saved!");
            }
        });
    }

    public void updateData(final Users f) {
        if (f.getProfileUrl() == null) {
            usersCollection.document(f.getUid())
                    .update("username", f.getUsername(),
                            "education_level", f.getEducation_lvl(),
                            "study_year", f.getYear(),
                            "stream", f.getStream()
                    ).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(f.getUsername()).build();

                    if (user != null) {
                        user.updateProfile(profileUpdates);
                    }

                    Log.d(TAG, "DocumentSnapshot successfully updated!");
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error updating document", e);
                        }
                    });
        } else {
            usersCollection.document(f.getUid())
                    .update("username", f.getUsername(),
                            "education_level", f.getEducation_lvl(),
                            "study_year", f.getYear(),
                            "stream", f.getStream(),
                            "profileUrl", f.getProfileUrl()
                    ).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(f.getUsername()).build();

                    if (user != null) {
                        user.updateProfile(profileUpdates);
                    }

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

    }
}
