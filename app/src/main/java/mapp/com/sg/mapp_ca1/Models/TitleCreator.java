package mapp.com.sg.mapp_ca1.Models;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class TitleCreator {
    static TitleCreator _titleCreator;
    List<TitleParent> _titleParent;

    public TitleCreator(Context context) {
        _titleParent = new ArrayList<>();
        _titleParent.add(new TitleParent("Chats created by me"));
        _titleParent.add(new TitleParent("Chats you are currently in"));
    }

    public static TitleCreator get(Context context){
        if(_titleCreator == null){
            _titleCreator = new TitleCreator(context);

        }return _titleCreator;
    }

    public List<TitleParent> getAll() {
        return _titleParent;
    }
}
