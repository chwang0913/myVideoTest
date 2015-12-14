package com.chwang.myvideotest;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;


import org.json.JSONException;
import org.json.JSONObject;

public class PlayActivity extends YouTubeBaseActivity implements
        YouTubePlayer.OnInitializedListener {

    private static final int RECOVERY_DIALOG_REQUEST = 1;
    private final String KEY = "AIzaSyCGs7v-u8HHTdrY9NEy9fJFU3rXkDYd9Yw";
    // YouTube player view
    private YouTubePlayerView youTubeView;
    String id ="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        // Initializing video player with developer key
        youTubeView.initialize(KEY, this);
        Bundle bundle = this.getIntent().getExtras();
        String json = bundle.getString("json");

        try{
            JSONObject snippet = new JSONObject(json);
            String publishAt = snippet.getString("publishedAt").substring(0,10);
            String title = snippet.getString("title");
            String desc = snippet.getString("description");
            JSONObject resourceId = snippet.getJSONObject("resourceId");
            id = resourceId.getString("videoId");

            TextView txtpublish = (TextView)findViewById(R.id.txtpublish);
            TextView txttitle = (TextView)findViewById(R.id.txttitle);
            TextView txtdesc = (TextView)findViewById(R.id.txtdesc);
            txtpublish.setText(publishAt);
            txttitle.setText(title);
            txtdesc.setText(desc);


        } catch (JSONException e){

            Log.e("XX" , e.toString());
        }


    }


    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                        YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
            String errorMessage =  errorReason.toString();
            Log.e("XX", errorMessage);
        } else {
            String errorMessage =  errorReason.toString();
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
            Log.e("XX" , errorMessage);
        }
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                        YouTubePlayer player, boolean wasRestored) {
        if (!wasRestored) {

            // loadVideo() will auto play video
            // Use cueVideo() method, if you don't want to play it automatically
            player.loadVideo(id);

            // Hiding player controls
//            player.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_DIALOG_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(KEY, this);
        }
    }

    private YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerView) findViewById(R.id.youtube_view);
    }




}
