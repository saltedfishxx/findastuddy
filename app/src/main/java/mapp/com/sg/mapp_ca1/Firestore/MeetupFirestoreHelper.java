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
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mapp.com.sg.mapp_ca1.CreateMeetup;
import mapp.com.sg.mapp_ca1.Models.Meetup;

import static android.support.constraint.Constraints.TAG;

/**
 * This firestorehelper is used to read, write and update meetups
 */

public class MeetupFirestoreHelper {

    static CollectionReference meetupCollection = FirebaseFirestore.getInstance().collection("Meetup");
    private List<Meetup> listMeetup;
    private Meetup meetup;

    //constructor to read data from meetup collection
    public MeetupFirestoreHelper(CreateMeetup cm) {
        final CreateMeetup createMeetup = cm;

        meetupCollection
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        listMeetup = new ArrayList<>();
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                String meetId = document.getId();
                                Date date = document.getDate("date");
                                String groupChatId = document.getString("groupChatID");
                                GeoPoint location = document.getGeoPoint("location");
                                String meetupName = document.getString("meetupName");
                                List<String> peopleGoing = (List<String>) document.get("peopleGoing");
                                Meetup meetup = new Meetup(null, meetupName, date, groupChatId, location, peopleGoing.size(), peopleGoing);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    //empty constructor
    public MeetupFirestoreHelper() {

    }

    public void saveData(Meetup m) {
        Map<String, Object> data = new HashMap<>();
        data.put("meetupName", m.getMeetupName());
        data.put("date", m.getDateTime());
        data.put("groupChatId", m.getGroupChatID());
        data.put("location", m.getLocation());
        data.put("peopleGoing", m.getUserids());
        data.put("noPpl", m.getNoPpl());
        meetupCollection.document(m.getMeetId()).set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("FirestoreHelper", "Document has been saved");
            }
        });
    }

    public void deleteMeetup(Meetup m) {
        meetupCollection.document(m.getMeetId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "Document has been deleted");
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error while deleting document");
                    }
                });


    }

    public void updateData(Meetup m) {
        // update who is going
        meetupCollection.document(m.getMeetId())
                .update("peopleGoing", m.getUserids()
                ).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "DocumentSnapshot successfully updated");
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document");
                    }
                });
        // Update no of people going
        meetupCollection.document(m.getMeetId())
                .update("noPpl", m.getNoPpl()
                ).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "DocumentSnapshot successfully updated");
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document");
                    }
                });
    }


}
