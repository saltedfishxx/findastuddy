package mapp.com.sg.mapp_ca1;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import mapp.com.sg.mapp_ca1.Firestore.UserFirestoreHelper;
import mapp.com.sg.mapp_ca1.Models.Users;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private Button editProfile, logout;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ImageView userPic;
    private TextView displayEducation, displayYear, displayStream;

    //firebase components
    private UserFirestoreHelper userFirestoreHelper;
    private FirebaseAuth firebaseAuth;
    private Users user;

    public ProfileFragment() {
        firebaseAuth = FirebaseAuth.getInstance();
        final String userID = firebaseAuth.getCurrentUser().getUid();

        CollectionReference usersCollection = FirebaseFirestore.getInstance().collection("users");

        usersCollection
                .document(userID)
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


                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }


                    }
                });
    }

    public void getUser(Users u) {
        this.user = u;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        //init views
        collapsingToolbarLayout = (CollapsingToolbarLayout) view.findViewById(R.id.collapsingBar);
        userPic = (ImageView) view.findViewById(R.id.imgUser);
        displayEducation = (TextView) view.findViewById(R.id.displayEducation);
        displayYear = (TextView) view.findViewById(R.id.displayYear);
        displayStream = (TextView) view.findViewById(R.id.displayStream);
        logout = (Button) view.findViewById(R.id.logoutBtn);

        //set content
        if (user != null) {

            collapsingToolbarLayout.setTitle(user.getUsername() + "'s Profile");
            Glide.with(userPic.getContext())
                    .load(user.getProfileUrl())
                    .apply(RequestOptions.circleCropTransform())
                    .into(userPic);
            displayEducation.setText(user.getEducation_lvl());
            displayYear.setText(user.getYear());
            displayStream.setText(user.getStream());

        }
        editProfile = (Button) view.findViewById(R.id.editProfile);

        //if user clicks log out --> logs out user
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(getContext());
                } else {
                    builder = new AlertDialog.Builder(getContext());
                }
                builder.setTitle("Log out")
                        .setMessage("Do you wish to Log out?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with adding
                                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                                firebaseAuth.signOut();
                                Intent intent = new Intent(getContext(), Login.class);
                                startActivity(intent);

                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                                dialog.cancel();
                            }
                        })
                        .show();
            }
        });

        //if user wants to edit profile
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EditProfile.class);
                intent.putExtra("imageLink", user.getProfileUrl());
                startActivity(intent);
            }
        });
        return view;
    }

}
