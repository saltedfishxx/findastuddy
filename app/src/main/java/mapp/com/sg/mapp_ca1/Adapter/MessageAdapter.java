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
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import mapp.com.sg.mapp_ca1.Models.Message;
import mapp.com.sg.mapp_ca1.R;

public class MessageAdapter extends RecyclerView.Adapter {
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;
    List<Message> messageList;
    Context mContext;
    private FirebaseAuth firebaseAuth;


    //constructor for adapter when called
    public MessageAdapter(Context context, List<Message> messageList){
        this.mContext = context;
        this.messageList = new ArrayList<>();
    }

    private Context getmContext(){return mContext;};

    // Determines the appropriate ViewType according to the sender of the message.
    @Override
    public int getItemViewType(int position) {
        Message message = (Message) messageList.get(position);
        firebaseAuth = FirebaseAuth.getInstance();

        //TODO: add uid to message and use it to compare instead
        if (message.getName().equals(firebaseAuth.getCurrentUser().getDisplayName())) {
            // If the current user is the sender of the message
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            // If some other user sent the message
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        //if the message content is sent by the current user --> create sender UI view
        //else create receiver UI view
        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message, parent, false);
            return new SentMessageHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_received, parent, false);
            return new ReceivedMessageHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        //replaces content of the view
        Message message = messageList.get(position);

        //if message item is sent by sender --> it is bind to sentMessageHolder
        //else the message received is bind to receiveMessageHolder
        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessageHolder) holder).bind(message);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedMessageHolder) holder).bind(message);
                break;
        }
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
    class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText, nameText;
        ImageView profileImage, photoImageView;

        ReceivedMessageHolder(View itemView) {
            super(itemView);
            //get name of views from XMl side
            messageText = (TextView) itemView.findViewById(R.id.messageTextView);
            timeText = (TextView) itemView.findViewById(R.id.timestamp);
            nameText = (TextView) itemView.findViewById(R.id.nameTextView);
            profileImage = (ImageView) itemView.findViewById(R.id.image_message_profile);
            photoImageView = (ImageView) itemView.findViewById(R.id.photoImageView);
        }

        void bind(Message message) {
            //bind the content to the view
            //isPhoto is used to check if the message sent is a photo or a text message
            boolean isPhoto = message.getPhotoUrl() != null;
            if (isPhoto) {
                messageText.setVisibility(View.GONE);
                photoImageView.setVisibility(View.VISIBLE);
                Glide.with(photoImageView.getContext())
                        .load(message.getPhotoUrl())
                        .into(photoImageView);
            } else {
                messageText.setVisibility(View.VISIBLE);
                photoImageView.setVisibility(View.GONE);
                messageText.setText(message.getText());
            }
            nameText.setText(message.getName());
            timeText.setText(message.getTimestamp());

                  }
    }

    class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText;
        ImageView photoImageView;

        SentMessageHolder(View itemView) {
            super(itemView);
            messageText = (TextView) itemView.findViewById(R.id.messageTextView);
            timeText = (TextView) itemView.findViewById(R.id.timestamp);
            photoImageView = (ImageView) itemView.findViewById(R.id.photoImageView);
        }

        void bind(Message message) {
            boolean isPhoto = message.getPhotoUrl() != null;
            if (isPhoto) {
                messageText.setVisibility(View.GONE);
                photoImageView.setVisibility(View.VISIBLE);
                Glide.with(photoImageView.getContext())
                        .load(message.getPhotoUrl())
                        .into(photoImageView);
            } else {
                messageText.setVisibility(View.VISIBLE);
                photoImageView.setVisibility(View.GONE);
                messageText.setText(message.getText());
            }
            timeText.setText(message.getTimestamp());

            }
    }
}
