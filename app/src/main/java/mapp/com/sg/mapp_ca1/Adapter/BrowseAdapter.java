package mapp.com.sg.mapp_ca1.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import mapp.com.sg.mapp_ca1.ChatRoomActivity;
import mapp.com.sg.mapp_ca1.Firestore.GroupChatFirestoreHelper;
import mapp.com.sg.mapp_ca1.Models.GroupChats;
import mapp.com.sg.mapp_ca1.R;

/**
 * Created by Lewis on 23/7/2018.
 */

public class BrowseAdapter extends RecyclerView.Adapter<BrowseAdapter.BrowseViewHolder> {

    private List<GroupChats> browseList;
    private Context bContext;
    private FirebaseAuth firebaseAuth;
    private GroupChatFirestoreHelper groupChatFirestoreHelper;

    //Constructor when calling the main adapter
    public BrowseAdapter(Context context, List<GroupChats> groupChats) {
        this.bContext = context;
        this.browseList = new ArrayList<>();
    }

    //gets context
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
        //views are defined in the inner class MainViewHolder
        GroupChats chat = browseList.get(position);
        // Set contents for each view
        holder.tvChatName.setText(chat.getChatName());
        holder.tvChatDesc.setText(chat.getChatDesc());
        holder.tvSubject.setText(String.format("Subject: %s", chat.getChatSubject()));
        holder.tvMembers.setText(String.format("Members: %s/5", chat.getMemCount()));
        //checks if pic url is null
        if (chat.getPicURL() != null) {
            Glide.with(holder.chatPic.getContext())
                    .load(chat.getPicURL())
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.chatPic);
        }else{
            Glide.with(holder.chatPic.getContext())
                    .load(R.drawable.circleprofile)
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.chatPic);
        }
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

    //clearall, additem, and addallitems are methods used to update the list in the adapter
    public void clearAll() {
        if (browseList != null) {
            browseList.clear();
        }
    }

    public void addItem(GroupChats gc) {
        if (browseList != null) {
            browseList.add(gc);
            notifyItemChanged(browseList.size() - 1);
        }
    }

    public void addAllItems(List<GroupChats> groupChats) {
        for (GroupChats g : groupChats) {
            addItem(g);
        }
    }

    // Inner class for creating ViewHolders
    class BrowseViewHolder extends RecyclerView.ViewHolder {

        // Class variables
        TextView tvChatName, tvChatDesc, tvMembers, tvSubject;
        ImageView chatPic;

        public BrowseViewHolder(View itemView) {
            super(itemView);
            //get needed views from xml file
            tvChatName = (TextView) itemView.findViewById(R.id.bchat1);
            //TODO : Add the rest
            tvSubject = (TextView) itemView.findViewById(R.id.subject);
            tvChatDesc = (TextView) itemView.findViewById(R.id.btxtDesc);
            tvMembers = (TextView) itemView.findViewById(R.id.btxtMemCount);
            chatPic = (ImageView) itemView.findViewById(R.id.bprofileimage);

            //if user wants to join the chat, get list position
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    final GroupChats groupChats = browseList.get(pos);

                    AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(getbContext());
                    } else {
                        builder = new AlertDialog.Builder(getbContext());
                    }
                    builder.setTitle("Join Chat")
                            .setMessage("Are you sure you want to join this group?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with adding
                                    firebaseAuth = FirebaseAuth.getInstance();

                                    //add member to group chat then save to firestore
                                    String currentm = firebaseAuth.getCurrentUser().getUid();
                                    List<String> members = groupChats.getMembers();
                                    members.add(currentm);

                                    //update memberlist to firestore
                                    GroupChats updatedGC = new GroupChats(groupChats.getChatId(), groupChats.getChatName()
                                            , groupChats.getChatDesc(), groupChats.getChatSubject(), groupChats.getMemCount(), members, groupChats.getPicURL());
                                    groupChatFirestoreHelper = new GroupChatFirestoreHelper();

                                    groupChatFirestoreHelper.updateData(updatedGC);
                                    Intent intent = new Intent(getbContext(), ChatRoomActivity.class);
                                    intent.putExtra("chat", updatedGC);
                                    getbContext().startActivity(intent);
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                    dialog.cancel();
                                }
                            })
                            .show();
                }
            });
        }

    }
}
