package com.chwang.myvideotest;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;

public class MainAdapter extends BaseAdapter  {


    private LayoutInflater li;
    private Context context;
    private ArrayList<ContentValues> data;
    private HashMap<String, JSONObject> map;
    public MainAdapter(Context context, ArrayList<ContentValues> data , HashMap<String, JSONObject> map) {

        this.context = context;
        this.data = data;
        this.li = LayoutInflater.from(context);
        this.map = map;

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
        TextView txttitle;
        ImageView imageView1;
        TextView txtname1;
        ImageView imageView2;
        TextView txtname2;
        ImageView imageView3;
        TextView txtname3;
        TextView txtmore;
        LinearLayout linearLayout;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = li.inflate(R.layout.adapter_main, parent, false);
            holder = new ViewHolder();
            holder.txttitle = (TextView) convertView.findViewById(R.id.txttitle);
            holder.txtname1 = (TextView) convertView.findViewById(R.id.txtname1);
            holder.txtname2 = (TextView) convertView.findViewById(R.id.txtname2);
            holder.txtname3 = (TextView) convertView.findViewById(R.id.txtname3);

            holder.imageView1 = (ImageView) convertView.findViewById(R.id.imageView1);
            holder.imageView2 = (ImageView) convertView.findViewById(R.id.imageView2);
            holder.imageView3 = (ImageView) convertView.findViewById(R.id.imageView3);
            holder.txtmore = (TextView)convertView.findViewById(R.id.txtmore);
            holder.linearLayout = (LinearLayout) convertView.findViewById(R.id.titlelayout);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ContentValues cv = getItem(position);
        String id = cv.getAsString("id");
        final String title = cv.getAsString("title");
        holder.txttitle.setText(title);


        try{
            final JSONObject jsonObject = map.get(id);
            JSONArray items = jsonObject.getJSONArray("items");


            int a,b,c;
            if (id.equals("PLEO6z3EMtwrlObmWbqno1IgiLaCSj6uxJ")){

                a=2;
                b=3;
                c=4;

            } else {
                a=0;
                b=1;
                c=2;

            }


            final JSONObject snippet0 = items.getJSONObject(a).getJSONObject("snippet");
            final JSONObject snippet1 = items.getJSONObject(b).getJSONObject("snippet");
            final JSONObject snippet2 = items.getJSONObject(c).getJSONObject("snippet");
            holder.txtname1.setText(snippet0.getString("title"));
            holder.txtname2.setText(snippet1.getString("title"));
            holder.txtname3.setText(snippet2.getString("title"));
            JSONObject default0 = snippet0.getJSONObject("thumbnails").getJSONObject("high");
            JSONObject default1 = snippet1.getJSONObject("thumbnails").getJSONObject("high");
            JSONObject default2 = snippet2.getJSONObject("thumbnails").getJSONObject("high");




            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Log.e("XX" , "@@@@@");
                    Bundle bundle = new Bundle();
                    Intent intent = new Intent(context,ListActivity.class);
                    bundle.putString("json",jsonObject.toString());
                    bundle.putString("title" , title);
                    intent.putExtras(bundle);
                    context.startActivity(intent);

                }
            });


            holder.imageView1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Bundle bundle = new Bundle();
                    Intent intent = new Intent(context,PlayActivity.class);
                    bundle.putString("json",snippet0.toString());
                    intent.putExtras(bundle);
                    context.startActivity(intent);

                }
            });
            holder.imageView2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    Intent intent = new Intent(context,PlayActivity.class);
                    bundle.putString("json",snippet1.toString());
                    intent.putExtras(bundle);
                    context.startActivity(intent);


                }
            });
            holder.imageView3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    Intent intent = new Intent(context,PlayActivity.class);
                    bundle.putString("json",snippet2.toString());
                    intent.putExtras(bundle);
                    context.startActivity(intent);

                }
            });



            try{

                Picasso.with(context).load(default0.getString("url")).into(holder.imageView1);
                Picasso.with(context).load(default1.getString("url")).into(holder.imageView2);
                Picasso.with(context).load(default2.getString("url")).into(holder.imageView3);

            } catch (Exception  e){

                e.printStackTrace();
            }


        } catch (JSONException e){

            e.printStackTrace();
        }



        return convertView;
    }


}