package mapp.com.sg.mapp_ca1.ViewHolders;

import android.view.View;
import android.widget.TextView;

import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

import mapp.com.sg.mapp_ca1.R;

public class TitleChildViewHolder extends com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder{
    public TextView chat1;

    public TitleChildViewHolder(View itemView) {
        super(itemView);
        chat1 = (TextView)itemView.findViewById(R.id.chat1);
    }


}
