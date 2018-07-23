package mapp.com.sg.mapp_ca1;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mapp.com.sg.mapp_ca1.Adapter.BrowseAdapter;
import mapp.com.sg.mapp_ca1.Adapter.MainAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class BrowseFragment extends Fragment {

    private RecyclerView bRecyclerView;
    private BrowseAdapter bAdapter;


    public BrowseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_browse, container,false);

        //init recycler view
        bRecyclerView = (RecyclerView) view.findViewById(R.id.bRecyclerView);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        bRecyclerView.setLayoutManager(layoutManager);

        bAdapter = new BrowseAdapter(getContext());
        bRecyclerView.setAdapter(bAdapter);

        //add toolbar
        Toolbar myToolbar = (Toolbar) view.findViewById(R.id.browseToolBar);
        ((MainActivity)getActivity()).setSupportActionBar(myToolbar);


        return view;
    }


}
