package mapp.com.sg.mapp_ca1;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import mapp.com.sg.mapp_ca1.Adapter.BrowseAdapter;
import mapp.com.sg.mapp_ca1.Adapter.MainAdapter;
import mapp.com.sg.mapp_ca1.Firestore.GroupChatFirestoreHelper;
import mapp.com.sg.mapp_ca1.Models.GroupChats;
import mapp.com.sg.mapp_ca1.Models.Message;


/**
 * A simple {@link Fragment} subclass.
 */
public class BrowseFragment extends Fragment {

    private RecyclerView bRecyclerView;
    private BrowseAdapter bAdapter;
    List<GroupChats> allChats;
    List<GroupChats> browseChats_Withduplicate;
    List<GroupChats> browsechats;

    GroupChatFirestoreHelper groupChatFirestoreHelper;
    FirebaseAuth firebaseAuth;

    public BrowseFragment() {
        // Required empty public constructor
        groupChatFirestoreHelper = new GroupChatFirestoreHelper(this);
        allChats = groupChatFirestoreHelper.getGcList();
        if(allChats != null) {
            for (GroupChats g : allChats) {
                for (String memberUid : g.getMembers()) {
                    if (!memberUid.equals(firebaseAuth.getCurrentUser().getUid())) {
                        browseChats_Withduplicate.add(g);
                    }
                }
            }
        }

        Set<GroupChats> browse = new HashSet<>(browseChats_Withduplicate);
        browsechats.addAll(browse);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_browse, container,false);

        firebaseAuth = FirebaseAuth.getInstance();


        //init recycler view
        bRecyclerView = (RecyclerView) view.findViewById(R.id.bRecyclerView);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        bRecyclerView.setLayoutManager(layoutManager);



        bAdapter = new BrowseAdapter(getContext(), browsechats);
        bRecyclerView.setAdapter(bAdapter);

        //add toolbar
        Toolbar myToolbar = (Toolbar) view.findViewById(R.id.browseToolBar);
        ((MainActivity)getActivity()).setSupportActionBar(myToolbar);


        return view;
    }

    public void UpdateList (List<GroupChats> gc) {
        bAdapter.clearAll();
        bAdapter.addAllItems(gc);

    }

    public void updateTasks () {
        bRecyclerView.getRecycledViewPool().clear();
        bAdapter.notifyDataSetChanged();

    }


}
