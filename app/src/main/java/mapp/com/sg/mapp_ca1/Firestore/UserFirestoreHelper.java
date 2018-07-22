package mapp.com.sg.mapp_ca1.Firestore;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mapp.com.sg.mapp_ca1.ChatRoomActivity;
import mapp.com.sg.mapp_ca1.Models.Message;
import mapp.com.sg.mapp_ca1.Models.Users;
import mapp.com.sg.mapp_ca1.Signup;

import static android.content.ContentValues.TAG;

public class UserFirestoreHelper {
    List<Users> usersList;
    static CollectionReference usersCollection = FirebaseFirestore.getInstance().collection("users");

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

                                Users users = new Users(id,username,education_level,study_year,stream);
                                //update list

                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }

                    }
                });
    }

    public UserFirestoreHelper(){

    }
    public void saveData(Users f){
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("username", f.getUsername());
        data.put("education_level", f.getEducation_lvl());
        data.put("study_year", f.getYear());
        data.put("stream", f.getStream());
        usersCollection.document(f.getUid()).set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("FirestoreHelper", "Document has been saved!");
            }
        });
    }
}
