package com.example.tam.appnotes.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tam.appnotes.R;
import com.example.tam.appnotes.model.Note;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * Created by tam on 8/9/2017.
 */

public class CustomAdapterPicture extends BaseAdapter {
    private Context mContext;
    private int mLayout;
    private List<Note> mListPicture;

    public CustomAdapterPicture(Context context, int layout, List<Note> pictureList) {
        this.mContext = context;
        this.mLayout = layout;
        this.mListPicture = pictureList;
    }
    @Override
    public int getCount() {
        return mListPicture.size();
    }

    @Override
    public Object getItem(int i) {
        return mListPicture.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    private class ViewHolde{
        ImageView imgPicture;
        Button btnDelete;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = view;
        ViewHolde holde = new ViewHolde();
        if(row==null){
            row =inflater.inflate(mLayout,null);
            holde.imgPicture = (ImageView)row.findViewById(R.id.img_picture);
            holde.btnDelete = (Button)row.findViewById(R.id.btn_delete);
            row.setTag(holde);
        }else {
            holde = (ViewHolde)row.getTag();
        }
        Note picture = mListPicture.get(i);
        Bitmap bitmap = BitmapFactory.decodeFile(picture.picture);
        holde.imgPicture.setImageBitmap(bitmap);
        notifyDataSetChanged();
        holde.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListPicture.remove(i);
                notifyDataSetChanged();
            }
        });
        return row;
    }

}
