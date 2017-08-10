package com.example.tam.appnotes.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import com.example.tam.appnotes.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tam on 8/9/2017.
 */

public class CustomAdapterPicture extends BaseAdapter {
    private Context mContext;
    private int mLayout;
    private List<String> mListPicture;

    public CustomAdapterPicture(Context context, int layout, List<String> pictureList) {
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
        Bitmap bitmap = BitmapFactory.decodeFile(mListPicture.get(i));
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
