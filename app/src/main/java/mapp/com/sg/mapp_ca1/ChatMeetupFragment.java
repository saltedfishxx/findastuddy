package mapp.com.sg.mapp_ca1;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import mapp.com.sg.mapp_ca1.Adapter.MemberAdapter;
import mapp.com.sg.mapp_ca1.Models.GroupChats;


public class ChatMeetupFragment extends Fragment{
    private View view;
    private Context context;
    private Button createMeetup;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context = getContext();
        }
        view = inflater.inflate(R.layout.activity_chat_meetup_fragment, container, false);

        createMeetup = view.findViewById(R.id.CreateMeetup);
        createMeetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CreateMeetup.class);
                startActivity(intent);
            }
        });
        return view;
    }

}
