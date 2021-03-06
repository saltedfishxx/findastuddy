package mapp.com.sg.mapp_ca1.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import mapp.com.sg.mapp_ca1.ChatRoomActivity;
import mapp.com.sg.mapp_ca1.Models.GroupChats;
import mapp.com.sg.mapp_ca1.R;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder> {
    //This class sets the contents of the group chat list in home fragment

    private List<GroupChats> chatsList;
    private Context mContext;

    //Constructor when calling the main adapter
    public MainAdapter(Context context, List<GroupChats> chatsList) {
        this.mContext = context;
        this.chatsList = new ArrayList<>();
    }

    private Context getmContext() {
        return mContext;
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //creates the content view
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.layout_child, parent, false);

        return new MainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainAdapter.MainViewHolder holder, int position) {

        //replaces content of the view (chatTextView) based on chatlist
        //chatTextView is defined in the inner class MainViewHolder
        GroupChats chat = chatsList.get(position);
        holder.chatTextView.setText(chat.getChatName());
        if (chat.getPicURL() != null) {
            Glide.with(holder.groupPic.getContext())
                    .load(chat.getPicURL())
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.groupPic);
        } else {
            Glide.with(holder.groupPic.getContext())
                    .load(R.drawable.circleprofile)
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.groupPic);
        }

    }


    //clearall, additem, and addallitems are methods used to update the list in the adapter
    @Override
    public int getItemCount() {
        //returns the number of items in the list
        //chatlist.size = no. of list items shown in recyclerView
        if (chatsList != null) {
            return chatsList.size();
        } else {
            return 0;
        }
    }

    public void clearAll() {
        if (chatsList != null) {
            chatsList.clear();
        }
    }

    public void addItem(GroupChats gc) {
        if (chatsList != null) {
            chatsList.add(gc);
            notifyItemChanged(chatsList.size() - 1);
        }
    }

    public List<GroupChats> getList (){return this.chatsList;}

    public void addAllItems(List<GroupChats> groupChats) {
        for (GroupChats g : groupChats) {
            addItem(g);
        }
    }
    public void removeItem (int position) {
        GroupChats deleteChat = chatsList.get(position);
        if (position > -1){
            chatsList.remove(position);
        }

    }

    // Inner class for creating ViewHolders
    class MainViewHolder extends RecyclerView.ViewHolder {

        // Class variables
        TextView chatTextView;
        ImageView groupPic;

        public MainViewHolder(View itemView) {
            super(itemView);
            //get the view from xml file
            chatTextView = (TextView) itemView.findViewById(R.id.chat1);
            groupPic = (ImageView) itemView.findViewById(R.id.profileimage);

            //gets list position --> directs to selected chat based on user's clicked item
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    GroupChats chat = chatsList.get(pos);
                    Intent intent = new Intent(getmContext(), ChatRoomActivity.class);
                    intent.putExtra("chat", chat);
                    getmContext().startActivity(intent);
                }
            });
        }

    }
}
