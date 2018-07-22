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

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder> {
    List<String> chatList;
    Context mContext;

    public MainAdapter(Context context){
        this.mContext = context;
        this.chatList = new ArrayList<>();
        this.chatList.add("Literature Chat");
    }

    private Context getmContext(){return mContext;}

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.layout_child, parent,false);
        return new MainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainAdapter.MainViewHolder holder, int position) {

        //replaces content of the view
        String chat = chatList.get(position);
        holder.chatTextView.setText(chat);

    }

    @Override
    public int getItemCount() {
       if(chatList != null){
           return chatList.size();
        }else {
            return 0;
        }
    }

//    public void clearAll() {
//        messageList.clear();
//    }
//
//    public void addItem (Message m) {
//        messageList.add(m);
//        notifyItemChanged(messageList.size() - 1);
//    }
//
//    public void addAllItems(List<Message> messages) {
//        for ( Message m : messages) {
//            addItem(m);
//        }
//    }
//
//    public List<Message> getList() {
//        return this.messageList;
//    }
//
//    public Message getMessage (int position) {
//        Message m = messageList.get(position);
//        return m;
//    }

    // Inner class for creating ViewHolders
    class MainViewHolder extends RecyclerView.ViewHolder {

        // Class variables for the task description and priority TextViews
        TextView chatTextView;

        /**
         * Constructor for the TaskViewHolders.
         *
         * @param itemView The view inflated in onCreateViewHolder
         */
        public MainViewHolder(View itemView) {
            super(itemView);
            chatTextView = (TextView) itemView.findViewById(R.id.chat1);
        }

    }
}