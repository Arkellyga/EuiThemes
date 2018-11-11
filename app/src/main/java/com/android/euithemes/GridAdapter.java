package com.android.euithemes;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class GridAdapter extends BaseAdapter {
    private ArrayList<Theme> mObjects;
    private Context mContext;
    private LayoutInflater mInflater;

    GridAdapter(Context context, ArrayList<Theme> objects) {
        mObjects = objects;
        mContext = context;

        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mObjects.size();
    }

    @Override
    public Object getItem(int position) {
        return mObjects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = mInflater.inflate(R.layout.file_item, parent, false);
        }
        Theme theme = getTheme(position);
        Resources res = mContext.getResources();
        final int backId = res.getIdentifier(theme.getPicture(), "drawable",
                mContext.getPackageName());
        view.findViewById(R.id.background_file_item).setBackground(res.getDrawable(backId, mContext.getTheme()));
        view.findViewById(R.id.image_view_apply_file_item).setVisibility(theme.isDownloaded() ? View.VISIBLE : View.INVISIBLE);
        ((TextView) view.findViewById(R.id.text_view_file_item)).setText(theme.getName());
        return view;
    }

    private Theme getTheme(int position) {
        return mObjects.get(position);
    }
}
