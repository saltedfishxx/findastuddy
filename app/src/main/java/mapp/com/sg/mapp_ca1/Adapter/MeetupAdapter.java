package mapp.com.sg.mapp_ca1.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import co.ceryle.segmentedbutton.SegmentedButtonGroup;
import mapp.com.sg.mapp_ca1.ChatDetails;
import mapp.com.sg.mapp_ca1.ChatMeetupFragment;
import mapp.com.sg.mapp_ca1.Firestore.MeetupFirestoreHelper;
import mapp.com.sg.mapp_ca1.Models.Meetup;
import mapp.com.sg.mapp_ca1.R;

public class MeetupAdapter extends RecyclerView.Adapter<MeetupAdapter.MeetupViewHolder>{
    private List<Meetup> meetupList;
    private Context mContext;
    private MeetupFirestoreHelper meetupFirestoreHelper;


    public MeetupAdapter(Context context, List<Meetup> meetupList){
        this.mContext = context;
        this.meetupList = new ArrayList<>();
    }

    public Context getmContext() {
        return mContext;
    }

    @NonNull
    @Override
    public MeetupAdapter.MeetupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.layout_meetup, parent, false);
        return new MeetupAdapter.MeetupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MeetupAdapter.MeetupViewHolder holder, int position) {
        Meetup meetup = meetupList.get(position);
        //Setting content for each view
        if(meetup.getMeetupName() != null){
            holder.txtMeetupName.setText(meetup.getMeetupName());
        }
        if(meetup.getDateTime() != null){
            holder.txtDateTime.setText(meetup.getDateTime().toString());
        }
        if(meetup.getLocation() != null){
            holder.txtLocation.setText(meetup.getLocation().toString());
        }
        String ppl = String.valueOf(meetup.getNoPpl());
        holder.txtNoPpl.setText(ppl + " people are going");

    }

    @Override
    public int getItemCount() {
        //returns the number of items in the list
        if (meetupList != null) {
            return meetupList.size();
        } else {
            return 0;
        }
    }

    //clearall, additem, and addallitems are methods used to update the list in the adapter
    public void clearAll() {
        if (meetupList != null) {
            meetupList.clear();
        }
    }

    public void addItem(Meetup m) {
        if (meetupList != null) {
            meetupList.add(m);
            notifyItemChanged(meetupList.size() - 1);
        }
    }

    public void addAllItems(List<Meetup> meetUps) {
        for (Meetup m : meetUps) {
            addItem(m);
        }
    }

    public class MeetupViewHolder extends RecyclerView.ViewHolder {

        TextView txtMeetupName, txtNoPpl, txtDateTime, txtLocation;
        SegmentedButtonGroup sbg;
        int pos = getAdapterPosition() +1;
        final Meetup meetup = meetupList.get(pos);
        int num = meetup.getNoPpl();

        public MeetupViewHolder(View itemView) {
            super(itemView);
            txtMeetupName = (TextView)itemView.findViewById(R.id.tvMeetupName);
            txtNoPpl = (TextView)itemView.findViewById(R.id.tvNoPpl);
            txtDateTime = (TextView)itemView.findViewById(R.id.tvDateTime);
            txtLocation = (TextView)itemView.findViewById(R.id.tvLocation);
            sbg = (SegmentedButtonGroup)itemView.findViewById(R.id.btnGrp);
            sbg.setOnClickedButtonPosition(new SegmentedButtonGroup.OnClickedButtonPosition() {
                @Override
                public void onClickedButtonPosition(int position) {
                    //not going decrease 1 from noPpl
                    if(position == 0){
                        num--;
                    //going increase 1 from noPpl
                    }else if(position == 1){
                        num++;
                    }
                    txtNoPpl.setText(num + " people are going");
                }
            });
            Meetup updatedMeetup = new Meetup(meetup.getMeetId(), meetup.getMeetupName(),
                    meetup.getDateTime(),meetup.getGroupChatID(),meetup.getLocation(), num, meetup.getUserids());
            meetupFirestoreHelper = new MeetupFirestoreHelper();

            meetupFirestoreHelper.updateData(updatedMeetup);

        }
    }
}
