package com.example.tam.appnotes.presenter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tam.appnotes.R;
import com.example.tam.appnotes.model.Note;
import com.example.tam.appnotes.ui.MainActivity;

import java.util.List;

/**
 * Created by tam on 8/3/2017.
 */

public class CustomAdapterNote extends BaseAdapter {
    private Context mContext;
    private int mLayout;
    private List<Note> mListNotes;

    public CustomAdapterNote(Context context, int layout, List<Note> noteList) {
        this.mContext = context;
        this.mLayout = layout;
        this.mListNotes = noteList;
    }




    @Override
    public int getCount() {
        return mListNotes.size();
    }

    @Override
    public Object getItem(int i) {
        return mListNotes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    private class ViewHolde{
        TextView title, note, dateCurrent;
        LinearLayout linearLayout;
        ImageView imageNote;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = view;
        ViewHolde holde = new ViewHolde();
        if(row==null){
            row =inflater.inflate(mLayout,null);
            holde.title =(TextView)row.findViewById(R.id.item_title);
            holde.note=(TextView) row.findViewById(R.id.item_Note);
            holde.dateCurrent = (TextView)row.findViewById(R.id.item_dateCurrent);
            holde.linearLayout = (LinearLayout)row.findViewById(R.id.item_layout);
            holde.imageNote = (ImageView)row.findViewById(R.id.item_image);
            row.setTag(holde);
        }else {
            holde = (ViewHolde)row.getTag();
        }
        Note note = mListNotes.get(i);
        holde.title.setText(note.title);
        holde.note.setText(note.note);
        holde.dateCurrent.setText(note.getDateCurrent());
        if(note.color == 0) {
            holde.linearLayout.setBackgroundColor(Color.WHITE);
        }else {
            holde.linearLayout.setBackgroundColor(note.color);
        }
        if(note.alarm.equals(" ")) {
            holde.imageNote.setVisibility(View.GONE);
        }else  {
            holde.imageNote.setVisibility(View.VISIBLE);

        }
        return row;
    }
}
