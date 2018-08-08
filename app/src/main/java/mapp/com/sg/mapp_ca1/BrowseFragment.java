package mapp.com.sg.mapp_ca1;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import mapp.com.sg.mapp_ca1.Adapter.BrowseAdapter;
import mapp.com.sg.mapp_ca1.Firestore.GroupChatFirestoreHelper;
import mapp.com.sg.mapp_ca1.Models.GroupChats;


/**
 * A simple {@link Fragment} subclass.
 * Browse chat screen
 */
public class BrowseFragment extends Fragment {

    private Button btnCreateChat;
    private RecyclerView bRecyclerView;
    private BrowseAdapter bAdapter;
    private List<GroupChats> browsechats;

    //firebase components
    private GroupChatFirestoreHelper groupChatFirestoreHelper;
    //private FirebaseAuth firebaseAuth;
    private static String UserUid;

    public BrowseFragment() {
        // Required empty public constructor

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_browse, container, false);

        //init firebase components
        //firebaseAuth = FirebaseAuth.getInstance();
        UserUid = getActivity().getIntent().getStringExtra("UserUid");
        groupChatFirestoreHelper = new GroupChatFirestoreHelper(this);

        //init recycler view
        bRecyclerView = (RecyclerView) view.findViewById(R.id.bRecyclerView);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        bRecyclerView.setLayoutManager(layoutManager);

        //init adapter
        browsechats = new ArrayList<>();
        bAdapter = new BrowseAdapter(getContext(), browsechats);
        bRecyclerView.setAdapter(bAdapter);

        //add toolbar
        Toolbar myToolbar = (Toolbar) view.findViewById(R.id.browseToolBar);
        ((MainActivity) getActivity()).setSupportActionBar(myToolbar);

        btnCreateChat = view.findViewById(R.id.btncreateChat);

        //create chat event
        btnCreateChat.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent startCreate = new Intent(getContext(), CreateChatActivity.class);
                startActivity(startCreate);
            }});

        return view;

    }

    @Override
    public void onStart() {
        super.onStart();

    }

    //updates adapter list
    public void UpdateList(List<GroupChats> gc) {
        bAdapter.clearAll();
        bAdapter.addAllItems(gc);

    }

    public void updateTasks() {
        bRecyclerView.getRecycledViewPool().clear();
        bAdapter.notifyDataSetChanged();

    }






}
