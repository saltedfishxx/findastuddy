package mapp.com.sg.mapp_ca1;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import mapp.com.sg.mapp_ca1.Adapter.MainAdapter;
import mapp.com.sg.mapp_ca1.Firestore.GroupChatFirestoreHelper;
import mapp.com.sg.mapp_ca1.Models.GroupChats;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private ImageButton helpBtn;

    private RecyclerView mRecyclerView;
    private MainAdapter mAdapter;

    private List<GroupChats> allChats;
    private List<GroupChats> mychats;

    //firebase componenets
    private GroupChatFirestoreHelper groupChatFirestoreHelper;
    private FirebaseAuth firebaseAuth;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //init firebase components
        firebaseAuth = FirebaseAuth.getInstance();
        groupChatFirestoreHelper = new GroupChatFirestoreHelper(this);

        //when user presses help icon
        helpBtn = (ImageButton) view.findViewById(R.id.helpButton);
        //open help event
        helpBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent startHelp= new Intent(getContext(), HelpScreen.class);
                startActivity(startHelp);
            }});

        //init recycler view
        mRecyclerView = (RecyclerView) view.findViewById(R.id.mRecyclerView);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new MainAdapter(getContext(), mychats);
        mRecyclerView.setAdapter(mAdapter);

        //add toolbar
        Toolbar myToolbar = (Toolbar) view.findViewById(R.id.homeToolBar);
        ((MainActivity) getActivity()).setSupportActionBar(myToolbar);

        //deletes chat when user swipes left for chat list item
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition(); //get position which is swipe

                if (direction == ItemTouchHelper.LEFT) {    //if swipe left

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext()); //alert for confirm to delete
                    builder.setMessage("Are you sure to delete and leave chat?");    //set message

                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() { //when click on DELETE
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            mAdapter.notifyItemRemoved(position);    //item removed from recylcerview
                            GroupChats chats = mAdapter.getList().get(position); //get deleted chat
                            mAdapter.removeItem(position); //remove deleted chat from list
                            groupChatFirestoreHelper.removeMember(chats); //delete data

                            return;
                        }
                    }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {  //not removing items if cancel is done
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mAdapter.notifyItemRemoved(position + 1);    //notifies the RecyclerView Adapter that data in adapter has been removed at a particular position.
                            mAdapter.notifyItemRangeChanged(position, mAdapter.getItemCount());   //notifies the RecyclerView Adapter that positions of element in adapter has been changed from position(removed element index to end of list), please update it.
                            return;
                        }
                    }).show();  //show alert dialog

                }
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView); //set swipe to recylcerview


        return view;
    }

    //updates list
    public void UpdateList(List<GroupChats> gc) {
        mAdapter.clearAll();
        mAdapter.addAllItems(gc);

    }

    public void updateTasks() {
        mRecyclerView.getRecycledViewPool().clear();
        mAdapter.notifyDataSetChanged();

    }


}
