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

import java.util.List;

import mapp.com.sg.mapp_ca1.Adapter.BrowseAdapter;
import mapp.com.sg.mapp_ca1.Firestore.GroupChatFirestoreHelper;
import mapp.com.sg.mapp_ca1.Models.GroupChats;


/**
 * A simple {@link Fragment} subclass.
 */
public class BrowseFragment extends Fragment {

    Button btnCreateChat;
    private RecyclerView bRecyclerView;
    private BrowseAdapter bAdapter;
    List<GroupChats> allChats;
    List<GroupChats> browseChats_Withduplicate;
    List<GroupChats> browsechats;

    GroupChatFirestoreHelper groupChatFirestoreHelper;
    FirebaseAuth firebaseAuth;

    public BrowseFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_browse, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        groupChatFirestoreHelper = new GroupChatFirestoreHelper(this);

        //init recycler view
        bRecyclerView = (RecyclerView) view.findViewById(R.id.bRecyclerView);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        bRecyclerView.setLayoutManager(layoutManager);


        bAdapter = new BrowseAdapter(getContext(), browsechats);
        bRecyclerView.setAdapter(bAdapter);

        //add toolbar
        Toolbar myToolbar = (Toolbar) view.findViewById(R.id.browseToolBar);
        ((MainActivity) getActivity()).setSupportActionBar(myToolbar);

        btnCreateChat = view.findViewById(R.id.btncreateChat);

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

    public void UpdateList(List<GroupChats> gc) {
        bAdapter.clearAll();
        bAdapter.addAllItems(gc);

    }

    public void updateTasks() {
        bRecyclerView.getRecycledViewPool().clear();
        bAdapter.notifyDataSetChanged();

    }




}
