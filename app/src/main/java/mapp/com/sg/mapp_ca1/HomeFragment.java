package mapp.com.sg.mapp_ca1;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.firebase.auth.FirebaseAuth;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import mapp.com.sg.mapp_ca1.Adapter.MainAdapter;
import mapp.com.sg.mapp_ca1.Firestore.GroupChatFirestoreHelper;
import mapp.com.sg.mapp_ca1.Models.GroupChats;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private MainAdapter mAdapter;

    //call groupchat firestore
    List<GroupChats> allChats;
    List<GroupChats> mychats;
    GroupChatFirestoreHelper groupChatFirestoreHelper;
    FirebaseAuth firebaseAuth;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container,false);

        firebaseAuth = FirebaseAuth.getInstance();
        groupChatFirestoreHelper = new GroupChatFirestoreHelper(this);
        //allChats = groupChatFirestoreHelper.getGcList();


        //init recycler view
        mRecyclerView = (RecyclerView) view.findViewById(R.id.mRecyclerView);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new MainAdapter(getContext(), mychats);
        mRecyclerView.setAdapter(mAdapter);

        //add toolbar
        Toolbar myToolbar = (Toolbar) view.findViewById(R.id.homeToolBar);
        ((MainActivity)getActivity()).setSupportActionBar(myToolbar);


        return view;
    }

    public void UpdateList (List<GroupChats> gc) {
        mAdapter.clearAll();
        mAdapter.addAllItems(gc);

    }

    public void updateTasks () {
        mRecyclerView.getRecycledViewPool().clear();
        mAdapter.notifyDataSetChanged();

    }

}
