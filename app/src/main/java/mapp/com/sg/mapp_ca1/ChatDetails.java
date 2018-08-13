package mapp.com.sg.mapp_ca1;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.Serializable;
import java.util.List;

import mapp.com.sg.mapp_ca1.Models.GroupChats;
import mapp.com.sg.mapp_ca1.Models.Users;

public class ChatDetails extends AppCompatActivity  {
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private GroupChats selectedChat;
    private List<Users> allusers;
    private ImageView chatdp;
    private TextView chatname, chatdesc;
    private ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_details);

        //gets selected specific group chat and all users from intent
        Bundle bundle = getIntent().getExtras();
        selectedChat = (GroupChats) bundle.getSerializable("selectedChat");
        allusers = (List<Users>) bundle.getSerializable("allusers");

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        //setup views
        chatdp = (ImageView) findViewById(R.id.imgChat);
        chatname = (TextView) findViewById(R.id.chatName);
        chatdesc = (TextView) findViewById(R.id.chatDesc);
        back = (ImageButton) findViewById(R.id.back);

        //set view content
        if (selectedChat.getPicURL() != null) {
            Glide.with(chatdp.getContext())
                    .load(selectedChat.getPicURL())
                    .apply(RequestOptions.circleCropTransform())
                    .into(chatdp);
        }
        chatname.setText(selectedChat.getChatName());
        chatdesc.setText(selectedChat.getChatDesc());


        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

    }

    //back button event
    public void onClickBtn(View view) {
        finish();
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0:
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("chat", selectedChat);
                    ChatInfoFragment chatInfoFragment = new ChatInfoFragment();
                    chatInfoFragment.setArguments(bundle);
                    return chatInfoFragment;
                case 1:
                    Bundle bundle2 = new Bundle();
                    bundle2.putSerializable("chat2", selectedChat);
                    ChatMeetupFragment chatMeetupFragment = new ChatMeetupFragment();
                    chatMeetupFragment.setArguments(bundle2);
                    return chatMeetupFragment;
                default:
                    return null;
            }

        }


        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }
    }
}
