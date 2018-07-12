package khalidelq.app.codingchallenge.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import khalidelq.app.codingchallenge.R;
import khalidelq.app.codingchallenge.adapters.SimpleGrid_Adapter;
import khalidelq.app.codingchallenge.models.Album;
import khalidelq.app.codingchallenge.models.Photo;

public class AlbumActivity extends AppCompatActivity {

    public GridView imagesGrid ;
    public String AlbumId ;
    SimpleGrid_Adapter gridAdapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        // Initialize all Views
        initViews();

        // make the API call
        getAllPhotos(AlbumId);

        // Listen for a click on a specific photo
        imagesGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                // get the clicked Photo and pass it's ID to the Photo Activity to display the photos in HighRes FullScreen
                Photo photo = (Photo) parent.getItemAtPosition(position);
                Intent fullScreenIntent = new Intent(AlbumActivity.this, PhotoActivity.class);
                fullScreenIntent.putExtra("id",photo.getId());
                startActivity(fullScreenIntent);
            }
        });

    }

    // Initialize all Views
    private void initViews() {
        // get intent Data (Album Id )
        Intent intent = getIntent();
        AlbumId = intent.getStringExtra("id");
        // init teh Grid View
        imagesGrid = (GridView) findViewById(R.id.imagesGrid);
    }

    // get all photos of an album
    public  void getAllPhotos (String albumId){
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/"+albumId+"/photos?fields=picture{url}",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        Log.d("response", "onCompleted: " + response);
                        try {
                            // Get the response and parse the data into a JSONObject Object
                            JSONObject data = response.getJSONObject();
                            // Create and Arraylist to hold the response
                            ArrayList<Photo> photos = new ArrayList<Photo>();
                            // iterate over the response that we got from the API
                            JSONArray jsonArray = (JSONArray) data.getJSONArray("data");
                            for (int i = 0, size = jsonArray.length(); i < size; i++)
                            {
                                JSONObject objectInArray = jsonArray.getJSONObject(i);
                                photos.add(new Photo(
                                        objectInArray.get("picture").toString(),
                                        objectInArray.get("id").toString())
                                );
                            }
                            // Set the adapter with the Data that we Got from the API
                            gridAdapter = new SimpleGrid_Adapter (AlbumActivity.this,photos);
                            // Set the List view's adapter
                            imagesGrid.setAdapter(gridAdapter);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
        ).executeAsync();
    }

}
