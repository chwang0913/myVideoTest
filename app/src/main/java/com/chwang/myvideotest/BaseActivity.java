package com.chwang.myvideotest;

import android.content.ContentValues;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.NetworkOnMainThreadException;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;

import com.google.common.reflect.AbstractInvocationHandler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Set;

/**
 * Created by w11w11w11 on 2015/10/28.
 */
public abstract class BaseActivity extends AppCompatActivity {
//GET https://www.googleapis.com/youtube/v3/playlistItems?part=snippet%2CcontentDetails%2Cstatus&playlistId=UUK8sQmJBp8GCxrOtXWBpyEA

    public String callAPI(ContentValues cv , String function) {


        final String apiURL = getResources().getString(R.string.api_url);
        final String _URL =  apiURL +function +"?" + getQuery(cv) ;
        final int READTIMEOUT = 10000;
        final int CONNECTTIMEOUT = 15000;

//        Log.e("@@", _URL);
        String result = "";
        HttpURLConnection conn = null;
        try {
            URL url = new URL(_URL);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(READTIMEOUT);
            conn.setConnectTimeout(CONNECTTIMEOUT);
            conn.setRequestMethod("GET");
            int status = conn.getResponseCode();
//            result = jsonResult(conn);

//            Log.e("@@" , String.valueOf(status) );
//            Log.e("@@" , result );


            switch (status) {
                case 200:
                case 201:
                    result = jsonResult(conn);
                    break;
                case 404:
                    result = "{\"errorCode\": 999}";
                    break;
            }

        } catch (NetworkOnMainThreadException ne) {
            throw ne;
        } catch (Exception e) {
            result = "{\"error\":" + e.toString() + "}";
//            Log.e("callAPI0", e.toString());
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return result;
    }


    public String jsonResult(HttpURLConnection conn) throws Exception {
        String jsonResult;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            br.close();
            jsonResult = sb.toString();

            return jsonResult;
        } catch (IOException e) {
            Log.e("XX" , e.toString());
            throw e;
        }
    }

    /**
     * Author :
     * Date :
     * Parsing ContentValues to Http format
     */
    private String getQuery(ContentValues params) {

        StringBuilder result = new StringBuilder();
        boolean first = true;
        String encode = "UTF-8";
        Set<String> keys = params.keySet();

        for (String key : keys) {

            if (first)
                first = false;
            else
                result.append("&");

            try {
                result.append(URLEncoder.encode(key, encode));
                result.append("=");
                result.append(URLEncoder.encode(params.getAsString(key), encode));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
//        Log.e("@@" , result.toString());

        return result.toString();
    }


    public class MyRunnable implements Runnable {
        ContentValues cv;
        int what;
        Handler handler;
        String function;

        public MyRunnable(Handler handler, ContentValues cv, int what,String function ) {
            this.cv = cv;
            this.what = what;
            this.handler = handler;
            this.function = function;
        }

        @Override
        public void run() {

            String result = "";
            boolean ex = true;
            try {

                result = callAPI(cv,function);

            } catch (NetworkOnMainThreadException e) {
                ex = false;
                new Thread(new MyRunnable(handler, cv, what, function)).start();
            }
            if (ex){
                Message msg = new Message();
                msg.what = what;
                Bundle data = new Bundle();
                data.putString("result", result);
                if (what ==1){

                    data.putString("playlistId", cv.getAsString("playlistId"));
                }
                msg.setData(data);
                handler.sendMessage(msg);
            }

        }
    }


    public class MyRunnable1 implements Runnable {
        ContentValues cv;
        int what;
        Handler handler;
        String function;

        public MyRunnable1(Handler handler, ContentValues cv, int what,String function ) {
            this.cv = cv;
            this.what = what;
            this.handler = handler;
            this.function = function;
        }

        @Override
        public void run() {

            String result = "";
            boolean ex = true;
            try {

                result = callAPI(cv,function);

            } catch (NetworkOnMainThreadException e) {
                ex = false;
                new Thread(new MyRunnable(handler, cv, what, function)).start();
            }
            if (ex){
                Message msg = new Message();
                msg.what = what;
                Bundle data = new Bundle();
                data.putString("result", result);
                if (what ==1){
                    data.putString("id", cv.getAsString("id"));
                }
                msg.setData(data);
                handler.sendMessage(msg);
            }

        }
    }



}
