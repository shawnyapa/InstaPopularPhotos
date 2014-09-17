package com.shawnyapa.instaphotos;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;


public class InstaPhotosActivity extends Activity {
	
	public static final String CLIENT_ID = "2c20e447eeed401ea9380d62d8f3b6cf";
	private ArrayList<Photo> popularPhotos;
	private PopularPhotosAdapter aPhotos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insta_photos);
        
        fetchInstagramPopularPhotos();
    }


    private void fetchInstagramPopularPhotos() {
    	popularPhotos = new ArrayList<Photo>();
    	aPhotos = new PopularPhotosAdapter(this, popularPhotos);
    	ListView lvPhotos = (ListView) findViewById(R.id.lvPhotos);
    	lvPhotos.setAdapter(aPhotos);
    	// Setup endpoint, Create Network Client, Send Network Request, Parse Response  	
    	String popularURL = "https://api.instagram.com/v1/media/popular?client_id=" + CLIENT_ID;
    	AsyncHttpClient client = new AsyncHttpClient();
    	client.get(popularURL, new JsonHttpResponseHandler() {
    		// Success
    		@Override
    		public void onSuccess(int statusCode, Header[] headers,
    				JSONObject response) {
    			// super.onSuccess(statusCode, headers, response);
    			// "data"->[x]->"user"->"username"
    			// "data"->[x]->"images"->"standard_resolution"->"url"
    			// "data"->[x]->"images"->"standard_resolution"->"height"
    			// "data"->[x]->"caption"->"text"
    			// "data"->[x]->"likes"->"count" 
    			// "data"->[x]->"user"->"profile_picture"
    			
    			JSONArray photosJSON = null;
    			try {
    				popularPhotos.clear();
    				photosJSON = response.getJSONArray("data");
    				for (int i=0; i < photosJSON.length(); i++) {
    					JSONObject photoJSON = photosJSON.getJSONObject(i);
    					Photo photo = new Photo();
    					if (photoJSON.optJSONObject("user") != null) {
    						photo.username = "    User: "+photoJSON.getJSONObject("user").getString("username");
    						photo.profileimage = photoJSON.getJSONObject("user").getString("profile_picture");
    					}
    					if (photoJSON.optJSONObject("images").getJSONObject("standard_resolution") != null) {
    						photo.photoimage = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
    						photo.photoheight = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getInt("height");
    					}
    					if (photoJSON.optJSONObject("caption") != null) {
    						photo.caption = photoJSON.getJSONObject("caption").getString("text");
    					}
    					if (photoJSON.optJSONObject("likes") != null) {
    						photo.likeCount = photoJSON.getJSONObject("likes").getInt("count");
    					}
    					else {photo.likeCount = 0;}
    					popularPhotos.add(photo); 
    					//Log.i("DEBUG", photo.toString());
    				}
    			    	
    			} catch (JSONException e) {
    				e.printStackTrace();
    			}
    			
    			aPhotos.notifyDataSetChanged();
    			
    		}
    		
    		@Override
    		public void onFailure(int statusCode, Header[] headers,
    				Throwable throwable, JSONArray errorResponse) {
    			super.onFailure(statusCode, headers, throwable, errorResponse);
    		}
    		
    	});
    	
		
	}


	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.insta_photos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
