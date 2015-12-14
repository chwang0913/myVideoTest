package com.chwang.myvideotest;

import android.content.ContentValues;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.google.api.client.json.Json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends BaseActivity {

    private final String KEY = "AIzaSyBuIYLmB86i1KUs65W7Mi9Cr5Eu6JQvN2I";
    private final String CHANNEL_ID = "UCq1T8bXVAEOzQ3VkXbo8L-w";

    ArrayList<ContentValues> list_items = new ArrayList<ContentValues>();
    HashMap<String,JSONObject> jsonMap = new HashMap<String,JSONObject>();

    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        ContentValues cv = new ContentValues();

        //GET https://www.googleapis.com/youtube/v3/playlists?part=snippet%2CcontentDetails&channelId=UCBgdVDkSsiPBVop4-Ms0ihQ

        listView = (ListView)findViewById(R.id.listView);
        cv.put("part","snippet,contentDetails");
        cv.put("channelId", CHANNEL_ID);
        cv.put("key", KEY);

        new Thread(new MyRunnable(mHandler, cv, 0, "playlists")).start();

    }



    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("result");
//            Log.e("@@@", "Main result:" + new java.util.Date() + "--" + val);
            if (msg.what ==0){

                try {
                    JSONObject jsonObject=null;
                    jsonObject = new JSONObject(val);
                    JSONArray items = jsonObject.getJSONArray("items");
                    for (int i=0;  i < items.length() ; i++){
                        ContentValues cv = new ContentValues();
                        JSONObject jsonObject1 = items.getJSONObject(i);
                        JSONObject snippet = jsonObject1.getJSONObject("snippet");
                        JSONObject thumbnails = snippet.getJSONObject("thumbnails");
                        JSONObject _default = thumbnails.getJSONObject("default");
                        cv.put("id", jsonObject1.getString("id"));
                        cv.put("title", snippet.getString("title"));
                        cv.put("thumbnails", _default.getString("url"));
                        list_items.add(cv);

                        ContentValues cv1 = new ContentValues();
                        cv1.put("part", "snippet,contentDetails,status");
                        cv1.put("playlistId", jsonObject1.getString("id"));
                        cv1.put("maxResults", 50);

//
//                        Log.e("@@@@@@@@", jsonObject1.getString("id"));
                        cv1.put("key", KEY);
                        new Thread(new MyRunnable(mHandler, cv1,1, "playlistItems")).start();
                    }
                    Log.e("@@@", String.valueOf(list_items.size()) );
                    Log.e("@@@", String.valueOf(jsonMap.size()) );



//

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else if (msg.what ==1){

                String id = data.getString("playlistId");

                try{
                    JSONObject jsonObject = new JSONObject(val);
//                    Log.e("@@@@@@@@" , id);

                   jsonMap.put(id, jsonObject);
                } catch (JSONException e){
                    e.printStackTrace();
                }
                if (jsonMap.size() == list_items.size()){

                    listView.setAdapter(new MainAdapter(MainActivity.this,list_items,jsonMap));

                }



//                Log.e("@@@", String.valueOf(jsonMap.size()) );

            }


        }
    };

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
