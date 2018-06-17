package mapp.com.sg.mapp_ca1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mapp.com.sg.mapp_ca1.Adapter.MyAdapter;
import mapp.com.sg.mapp_ca1.Models.TitleChild;
import mapp.com.sg.mapp_ca1.Models.TitleCreator;
import mapp.com.sg.mapp_ca1.Models.TitleParent;


public class MainActivity extends AppCompatActivity {
//    private ExpandableListView listView;
//    private ExpandableListAdapter listAdapter;
//    private List<String> listDataHeader;
//    private HashMap<String,List<String>> listHash;
    RecyclerView recyclerView;


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ((MyAdapter)recyclerView.getAdapter()).onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        listView = (ExpandableListView)findViewById(R.id.listChats);
//        initData();
//        listAdapter = new ExpandableListAdapter(this,listDataHeader,listHash);
//        listView.setAdapter(listAdapter);

        recyclerView = (RecyclerView)findViewById(R.id.mRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        MyAdapter adapter = new MyAdapter(this,initData());
        adapter.setParentClickableViewAnimationDefaultDuration();
        adapter.setParentAndIconExpandOnClick(true);

        recyclerView.setAdapter(adapter);

       // getSupportActionBar().setDisplayShowHomeEnabled(true);
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private List<ParentObject> initData() {
        TitleCreator titleCreator = TitleCreator.get(this);
        List<TitleParent> titles = titleCreator.getAll();
        List<ParentObject> parentObjects = new ArrayList<>();
        for(TitleParent title:titles){
            List<Object> childList = new ArrayList<>();
            childList.add(new TitleChild("Literature Chat"));
            childList.add(new TitleChild("Math Chat"));
            title.setChildObjectList(childList);
            parentObjects.add(title);
        }
        return parentObjects;
    }

//    private void initData() {
//        listDataHeader = new ArrayList<>();
//        listHash = new HashMap<>();
//
//        listDataHeader.add("Chats created by me");
//        listDataHeader.add("Chats you are currently in");
//
//        List<String> myChats = new ArrayList<>();
//        myChats.add("Literature Chat");
//
//        List<String> chatsIncluded = new ArrayList<>();
//        chatsIncluded.add("A Math Discussion");
//
//        listHash.put(listDataHeader.get(0),myChats);
//        listHash.put(listDataHeader.get(1),chatsIncluded);
//    }

   //@Override
   /* public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if(id == android.R.id.home){
            //ends the activity
            this.finish();
        }
        return true;
    }*/
}
