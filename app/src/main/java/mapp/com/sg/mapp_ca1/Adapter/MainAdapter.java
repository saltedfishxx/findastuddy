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
    //This class sets the contents of the group chat list


    List<String> chatList;
    Context mContext;

    //Constructor when calling the main adapter
    public MainAdapter(Context context){
        this.mContext = context;
        this.chatList = new ArrayList<>();
        this.chatList.add("Literature Chat");
    }

    private Context getmContext(){return mContext;}

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //creates the content view
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.layout_child, parent,false);
        return new MainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainAdapter.MainViewHolder holder, int position) {

        //replaces content of the view (chatTextView) based on chatlist
        //chatTextView is defined in the inner class MainViewHolder
        String chat = chatList.get(position);
        holder.chatTextView.setText(chat);

    }

    @Override
    public int getItemCount() {
        //returns the number of items in the list
        //chatlist.size = no. of list items shown in recyclerView
       if(chatList != null){
           return chatList.size();
        }else {
            return 0;
        }
    }

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
            //get the view from xml file
            chatTextView = (TextView) itemView.findViewById(R.id.chat1);
        }

    }
}
