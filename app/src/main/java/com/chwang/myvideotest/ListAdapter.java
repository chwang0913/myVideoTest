package com.chwang.myvideotest;

import android.content.ContentValues;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import java.util.ArrayList;


public class ListAdapter extends BaseAdapter  {


    private LayoutInflater li;
    private Context context;
    private ArrayList<ContentValues> data;
    public ListAdapter(Context context, ArrayList<ContentValues> data ) {

        this.context = context;
        this.data = data;
        this.li = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public ContentValues getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private static class ViewHolder {
        TextView txtname1;
        ImageView imageView1;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = li.inflate(R.layout.adapter_list, parent, false);
            holder = new ViewHolder();
            holder.txtname1 = (TextView) convertView.findViewById(R.id.txtname1);
            holder.imageView1 = (ImageView) convertView.findViewById(R.id.imageView1);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.txtname1.setText(data.get(position).getAsString("title"));
        Picasso.with(context).load(data.get(position).getAsString("url")).into(holder.imageView1);
        return convertView;
    }


}