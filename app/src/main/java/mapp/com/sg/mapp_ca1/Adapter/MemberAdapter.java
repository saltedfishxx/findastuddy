package mapp.com.sg.mapp_ca1.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import mapp.com.sg.mapp_ca1.Firestore.UserFirestoreHelper;
import mapp.com.sg.mapp_ca1.Models.GroupChats;
import mapp.com.sg.mapp_ca1.Models.Users;
import mapp.com.sg.mapp_ca1.R;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.MemberViewHolder> {

    private List<Users> members;
    private Context context;

    public MemberAdapter(Context context, List<Users> memberList) {
        this.context = context;
        this.members = new ArrayList<>();
    }

    private Context getContext() {
        return context;
    }

    @NonNull
    @Override
    public MemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.member_list, parent, false);
        return new MemberAdapter.MemberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberViewHolder holder, int position) {
        Users user = members.get(position);
        holder.memberName.setText(user.getUsername());

        if (user.getProfileUrl() != null) {
            Glide.with(holder.memberPic.getContext())
                    .load(user.getProfileUrl())
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.memberPic);
        } else {
            Glide.with(holder.memberPic.getContext())
                    .load(R.drawable.circleprofile)
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.memberPic);
        }

    }

    //clearall, additem, and addallitems are methods used to update the list in the adapter
    @Override
    public int getItemCount() {
        return members.size();
    }

    public void clearAll() {
        if (members != null) {
            members.clear();
        }
    }

    public void addItem(Users gc) {
        if (members != null) {
            members.add(gc);
            notifyItemChanged(members.size() - 1);
        }
    }

    public void addAllItems(List<Users> users) {
        for (Users u : users) {
            addItem(u);
        }
    }

    class MemberViewHolder extends RecyclerView.ViewHolder {

        //class vairables
        TextView memberName;
        ImageView memberPic;


        public MemberViewHolder(View itemView) {
            super(itemView);
            //gets the view from xml file
            memberName = (TextView) itemView.findViewById(R.id.memberName);
            memberPic = (ImageView) itemView.findViewById(R.id.memberPic);

        }
    }
}
