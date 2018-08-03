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

import mapp.com.sg.mapp_ca1.R;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.MemberViewHolder> {

    List<String> memberList;
    List<String> picList;
    Context context;

    public MemberAdapter (Context context, List<String> memberList, List<String> picList){
        this.context = context;
        this.memberList = new ArrayList<>();
        this.memberList = memberList;
        this.picList = new ArrayList<>();
        this.picList = picList;
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
        String member = memberList.get(position);
        holder.memberName.setText(member);

        for(int i =0; i < picList.size(); i++ ){
            String pic = picList.get(i);
            if(pic != null){
                Glide.with(holder.memberPic.getContext())
                        .load(pic)
                        .apply(RequestOptions.circleCropTransform())
                        .into(holder.memberPic);
            }
        }

    }

    @Override
    public int getItemCount() {
        return memberList.size();
    }

    class MemberViewHolder  extends RecyclerView.ViewHolder{

        TextView memberName;
        ImageView memberPic;


        public MemberViewHolder(View itemView) {
            super(itemView);
            memberName = (TextView) itemView.findViewById(R.id.memberName);
            memberPic = (ImageView) itemView.findViewById(R.id.memberPic);

        }
    }
}
