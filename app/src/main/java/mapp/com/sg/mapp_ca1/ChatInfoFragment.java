package mapp.com.sg.mapp_ca1;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import mapp.com.sg.mapp_ca1.Adapter.MemberAdapter;
import mapp.com.sg.mapp_ca1.Adapter.MessageAdapter;
import mapp.com.sg.mapp_ca1.Firestore.UserFirestoreHelper;
import mapp.com.sg.mapp_ca1.Models.GroupChats;
import mapp.com.sg.mapp_ca1.Models.Users;

import static android.content.ContentValues.TAG;

public class ChatInfoFragment extends Fragment {
    View view;
    Context context;

    String memberName;
    String picUrl;
    List<String> memberList;
    List<String> membernames;
    List<String> picList;
    GroupChats selectedChat;
    List<Users> allusers;

    TextView tvMember;
    ImageView memberPic;

    RecyclerView recyclerView;
    MemberAdapter memberAdapter;

    static CollectionReference collectionReference = FirebaseFirestore.getInstance().collection("groupchat");
    List<Users> usersList;
    UserFirestoreHelper userFirestoreHelper;


    public ChatInfoFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context = getContext();
        }


        if (getArguments() != null) {
            selectedChat = (GroupChats) getArguments().getSerializable("chat");
        } else {
            Log.d(TAG, "chat not retrieved");
        }

        userFirestoreHelper = new UserFirestoreHelper(this, selectedChat);

        view = inflater.inflate(R.layout.chat_info_fragment, container, false);
        tvMember = (TextView) view.findViewById(R.id.memberName);
        memberPic = (ImageView) view.findViewById(R.id.memberPic);
        memberList = selectedChat.getMembers();


        // Initialize references to views
        //set recycler view to its coressponding view
        recyclerView = (RecyclerView) view.findViewById(R.id.memberList);

        //set layout for recyclerview to be linear layout
        //measures and positions items within the view into a linear list
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        //initialize adapter and attach to recycler view
        memberAdapter = new MemberAdapter(context, allusers);
        recyclerView.setAdapter(memberAdapter);

        final DocumentReference docRef = collectionReference.document(selectedChat.getChatId());
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    Log.d(TAG, "Current data: " + snapshot.getData());
                    updateTasks();
                    UpdateList(userFirestoreHelper.getMembers());
                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });

        return view;
    }

    public void UpdateList(List<Users> gc) {
        memberAdapter.clearAll();
        memberAdapter.addAllItems(gc);

    }

    public void updateTasks() {
        recyclerView.getRecycledViewPool().clear();
        memberAdapter.notifyDataSetChanged();

    }

}
