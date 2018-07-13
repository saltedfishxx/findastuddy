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

import java.util.ArrayList;
import java.util.List;

import mapp.com.sg.mapp_ca1.Models.Message;
import mapp.com.sg.mapp_ca1.R;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    List<Message> messageList;
    Context mContext;

    public MessageAdapter(Context context){
        this.mContext = context;
        this.messageList = new ArrayList<>();
    }

    private Context getmContext(){return mContext;};

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_message, parent,false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.MessageViewHolder holder, int position) {

        //replaces content of the view
        Message message = messageList.get(position);
        boolean isPhoto = message.getPhotoUrl() != null;
        if (isPhoto) {
            holder.messageTextView.setVisibility(View.GONE);
            holder.photoImageView.setVisibility(View.VISIBLE);
            Glide.with(holder.photoImageView.getContext())
                    .load(message.getPhotoUrl())
                    .into(holder.photoImageView);
        } else {
            holder.messageTextView.setVisibility(View.VISIBLE);
            holder.photoImageView.setVisibility(View.GONE);
            holder.messageTextView.setText(message.getText());
        }
        holder.authorTextView.setText(message.getName());

    }

    @Override
    public int getItemCount() {
        if(messageList != null){
            return messageList.size();
        }else {
            return 0;
        }
    }

    public void clearAll() {
        messageList.clear();
    }

    public void addItem (Message m) {
        messageList.add(m);
        notifyItemChanged(messageList.size() - 1);
    }

    public void addAllItems(List<Message> messages) {
        for ( Message m : messages) {
            addItem(m);
        }
    }

    public List<Message> getList() {
        return this.messageList;
    }

    public Message getMessage (int position) {
        Message m = messageList.get(position);
        return m;
    }

    // Inner class for creating ViewHolders
    class MessageViewHolder extends RecyclerView.ViewHolder {

        // Class variables for the task description and priority TextViews
        ImageView photoImageView;
        TextView messageTextView;
        TextView authorTextView;

        /**
         * Constructor for the TaskViewHolders.
         *
         * @param itemView The view inflated in onCreateViewHolder
         */
        public MessageViewHolder(View itemView) {
            super(itemView);
            photoImageView = (ImageView) itemView.findViewById(R.id.photoImageView);
            messageTextView = (TextView) itemView.findViewById(R.id.messageTextView);
            authorTextView = (TextView) itemView.findViewById(R.id.nameTextView);
        }

    }
}
