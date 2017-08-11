package com.example.tam.appnotes.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import com.example.tam.appnotes.R;
import com.example.tam.appnotes.model.Note;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tam on 8/9/2017.
 */

public class CustomAdapterPicture extends ArrayAdapter {
    private static final int itemWith = 150;
    private Context myContext;
    int mLayout;
    ArrayList<String> pathImageList = new ArrayList<>();
    private LruCache<String, Bitmap> mMemoryCache;

    public CustomAdapterPicture(Context myContext, int mLayout, ArrayList<String> pathImageList) {
        super(myContext, mLayout,pathImageList);
        this.myContext = myContext;
        this.mLayout = mLayout;
        this.pathImageList = pathImageList;
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount();
            }
        };
    }

    private class BitmapWorkerTask extends AsyncTask<Void, Void, Bitmap> {
        public String paths;
        private final WeakReference<ViewHolder> imageViewReference;

        public BitmapWorkerTask(String paths, ViewHolder imageViewReference) {
            this.paths = paths;
            this.imageViewReference = new WeakReference<ViewHolder>(imageViewReference);
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            return Ultils.getResizedBitmap(paths, CustomAdapterPicture.itemWith, CustomAdapterPicture.itemWith);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (imageViewReference != null && bitmap != null) {
                final ViewHolder view = imageViewReference.get();
                view.imgPicture.setImageBitmap(bitmap);
            }
        }
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder = new ViewHolder();
        if (convertView == null) {
            convertView = LayoutInflater.from(myContext).inflate(mLayout, parent, false);
            holder.imgPicture = (ImageView) convertView.findViewById(R.id.img_picture);
            holder.btnDelete = (Button) convertView.findViewById(R.id.btn_delete);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pathImageList.remove(position);
                notifyDataSetChanged();
            }
        });
        Bitmap b = (Bitmap) mMemoryCache.get(pathImageList.get(position));
        if (b != null) {
            holder.imgPicture.setImageBitmap(b);
        } else {
            new BitmapWorkerTask((String) this.pathImageList.get(position), holder).execute(new Void[0]);
        }
        return convertView;
    }

    static class ViewHolder {
        Button btnDelete;
        ImageView imgPicture;
    }

}
