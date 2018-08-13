package mapp.com.sg.mapp_ca1;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import mapp.com.sg.mapp_ca1.Adapter.MeetupAdapter;
import mapp.com.sg.mapp_ca1.Firestore.MeetupFirestoreHelper;
import mapp.com.sg.mapp_ca1.Models.GroupChats;
import mapp.com.sg.mapp_ca1.Models.Meetup;

import static android.content.ContentValues.TAG;


public class ChatMeetupFragment extends Fragment {
    private View view;
    private Context context;
    private Button createMeetup;
    private RecyclerView aRecyclerView;
    private MeetupAdapter meetupAdapter;
    private ArrayList<Meetup> browseMeetups;
    private GroupChats selectedChat;

    private MeetupFirestoreHelper meetupFirestoreHelper;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context = getContext();
        }

        //get data from activity to current fragment
        if (getArguments() != null) {
            selectedChat = (GroupChats) getArguments().getSerializable("chat2");
        } else {
            Log.d(TAG, "chat not retrieved");
        }

        view = inflater.inflate(R.layout.activity_chat_meetup_fragment, container, false);
        //get data from activity to current fragment
        if (getArguments() != null) {
            selectedChat = (GroupChats) getArguments().getSerializable("chat2");
        } else Log.d(TAG, "chat not retrieved");
        meetupFirestoreHelper = new MeetupFirestoreHelper(this, selectedChat);

        createMeetup = view.findViewById(R.id.CreateMeetup);

        createMeetup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CreateMeetup.class);
                startActivity(intent);
            }
        });

        //init recycler view
        aRecyclerView = view.findViewById(R.id.meetupList);

        // Create recycler view
        final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        aRecyclerView.setLayoutManager(layoutManager);

        //init Adapter
        browseMeetups = new ArrayList<>();
        meetupAdapter = new MeetupAdapter(context, browseMeetups);
        aRecyclerView.setAdapter(meetupAdapter);

        return view;
    }

    //updates adapter list
    public void UpdateList(List<Meetup> meetups) {
        meetupAdapter.clearAll();
        meetupAdapter.addAllItems(meetups);

    }

    public void updateTasks() {
        aRecyclerView.getRecycledViewPool().clear();
        meetupAdapter.notifyDataSetChanged();
    }


}

