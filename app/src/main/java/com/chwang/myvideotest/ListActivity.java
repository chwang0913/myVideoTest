package com.chwang.myvideotest;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ListActivity extends BaseActivity {

    ArrayList<ContentValues> list_items = new ArrayList<ContentValues>();


    GridView gridView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Bundle bundle = this.getIntent().getExtras();
        String json = bundle.getString("json");
        String title = bundle.getString("title");
        gridView = (GridView) findViewById(R.id.gridView);
        TextView txttitle = (TextView) findViewById(R.id.txttitle);
        txttitle.setText(title);
        try{

            JSONObject jsonObject = new JSONObject(json);
            JSONArray items = jsonObject.getJSONArray("items");

            for (int i=0 ; i< items.length() ; i++){
                ContentValues cv = new ContentValues();
                JSONObject snippet = items.getJSONObject(i).getJSONObject("snippet");
                String title1 = snippet.getString("title");

                if (title1.equals("Private video")){
                    continue;
                }
                cv.put("json" , snippet.toString());
                cv.put("title",snippet.getString("title"));
                cv.put("url" , snippet.getJSONObject("thumbnails").getJSONObject("high").getString("url"));
                list_items.add(cv);
            }

            gridView.setAdapter(new ListAdapter(ListActivity.this,list_items));

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Bundle bundle = new Bundle();
                    Intent intent = new Intent(ListActivity.this    ,PlayActivity.class);

                    bundle.putString("json",list_items.get(position).getAsString("json"));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });

        } catch (JSONException e){


        }


    }



}
