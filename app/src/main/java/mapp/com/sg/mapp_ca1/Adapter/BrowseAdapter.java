package mapp.com.sg.mapp_ca1.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mapp.com.sg.mapp_ca1.Firestore.GroupChatFirestoreHelper;
import mapp.com.sg.mapp_ca1.Models.GroupChats;
import mapp.com.sg.mapp_ca1.Models.Message;
import mapp.com.sg.mapp_ca1.R;

/**
 * Created by Lewis on 23/7/2018.
 */

public class BrowseAdapter extends RecyclerView.Adapter<BrowseAdapter.BrowseViewHolder> {

    List<GroupChats> browseList;
    Context bContext;

    //Constructor when calling the main adapter
    public BrowseAdapter(Context context, List<GroupChats> groupChats) {
        this.bContext = context;
        this.browseList = groupChats;
    }

    private Context getbContext() {
        return bContext;
    }

    @NonNull
    @Override
    public BrowseAdapter.BrowseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //creates the content view
        View view = LayoutInflater.from(bContext)
                .inflate(R.layout.layout_browse, parent, false);
        return new BrowseAdapter.BrowseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BrowseAdapter.BrowseViewHolder holder, int position) {

        //replaces content of the view (chatTextView) based on chatlist
        //chatTextView is defined in the inner class MainViewHolder
        GroupChats chat = browseList.get(position);
        // Set contents for each view
        holder.tvChatName.setText(chat.getChatName());
        holder.tvChatDesc.setText(chat.getChatDesc());
        holder.tvMembers.setText(String.format("%s/5", chat.getMemCount()));
    }

    @Override
    public int getItemCount() {
        //returns the number of items in the list
        if (browseList != null) {
            return browseList.size();
        } else {
            return 0;
        }
    }
    public void clearAll() {
        browseList.clear();
    }

    public void addItem (GroupChats gc) {
        browseList.add(gc);
        notifyItemChanged(browseList.size() - 1);
    }

    public void addAllItems(List<GroupChats> groupChats) {
        for ( GroupChats g: groupChats) {
            addItem(g);
        }
    }

    // Inner class for creating ViewHolders
    class BrowseViewHolder extends RecyclerView.ViewHolder {

        // Class variables for the task description and priority TextViews
        TextView tvChatName, tvChatDesc, tvMembers;

        /**
         * Constructor for the TaskViewHolders.
         *
         * @param itemView The view inflated in onCreateViewHolder
         */
        public BrowseViewHolder(View itemView) {
            super(itemView);
            //get needed views from xml file
            tvChatName = (TextView) itemView.findViewById(R.id.bchat1);
            //TODO : Add the rest
            tvChatDesc = (TextView) itemView.findViewById(R.id.btxtDesc);
            tvMembers = (TextView) itemView.findViewById(R.id.btxtMemCount);

        }

    }
}
