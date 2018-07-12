package khalidelq.app.codingchallenge.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;

import khalidelq.app.codingchallenge.R;
import khalidelq.app.codingchallenge.models.Photo;

public class PhotoActivity extends AppCompatActivity {

    ImageView fullScreenImageView ;
    private String photoId;
    private String photoUrl = "";
    ProgressDialog progressDialog;
    Bitmap bitmapImage ;

    //creating reference to firebase storage
    FirebaseStorage storage  ;
    StorageReference storageRef ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        // Initialize all Views
        initViews();
        storage = FirebaseStorage.getInstance();
        storageRef =  storage.getReferenceFromUrl(getString(R.string.FirebaseUrl));

        // API call to get a specific photo
        getPhoto(photoId);

    }

    // Initialize all Views
    private void initViews() {

        // get intent Data (Album Id )
        Intent intent = getIntent();
        photoId = intent.getStringExtra("id");

        // init teh Image View
        fullScreenImageView = (ImageView) findViewById(R.id.fullScreenImageView);

        // init progress Dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading....");
//      progressDialog.setCancelable(true); /* Cancelble when back button clicked */
        progressDialog.setCanceledOnTouchOutside(false);
    }

    // get a specific photo
    public void getPhoto (String PhotoId){
        final Photo p = new Photo();
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/"+PhotoId+"/?fields=images",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        try {
                            // parse the JSON data
                            JSONObject data = response.getJSONObject();
                            JSONObject j = (JSONObject) data.getJSONArray("images").get(0);
                            photoUrl = j.get("source").toString();

                            // Creat a target to be used by picasoo to load image as a bitmap
                            Target target = new Target() {
                                @Override
                                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                    fullScreenImageView.setImageBitmap(bitmap);
                                    bitmapImage = bitmap ;
                                }

                                @Override
                                public void onBitmapFailed(Drawable errorDrawable) {
                                    fullScreenImageView.setImageResource(R.drawable.image_placeholder);
                                }

                                @Override
                                public void onPrepareLoad(Drawable placeHolderDrawable) {
                                    fullScreenImageView.setImageResource(R.drawable.image_placeholder);
                                }
                            };


                            // Load the image
                            Picasso.with(PhotoActivity.this)
                                    .load(j.get("source").toString())
                                    .placeholder(R.drawable.image_placeholder)
                                    .error(R.drawable.image_placeholder)
                                    .into(target);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        ).executeAsync();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.main, menu);
        return (super.onCreateOptionsMenu(menu));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.uploadToFireBase) {
            uploadImage(bitmapImage);
        }
        return super.onOptionsItemSelected(item);
    }

    // upload image on storage.
    private void uploadImage(Bitmap bitmap) {
        // test if the image is loaded if not try to reload it
        if (null == bitmap){
            Toast.makeText(this, "Image is not loaded yet please wait ", Toast.LENGTH_SHORT).show();
            getPhoto(photoId);
            return;
        }else {
            // Genrate random name to be saved as
            StorageReference childRef = storageRef.child(UUID.randomUUID().toString()+"_image.jpg");

            // Showing progressDialog.
            progressDialog.show();

            // turn the Bitmap Image into a byte array to be uploaded
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            byte[] data = outputStream.toByteArray();

            // uploading the image
            UploadTask uploadTask = childRef.putBytes(data);

            // Adding addOnSuccessListener to second UplaodTask.
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    Toast.makeText(PhotoActivity.this, "Upload successful", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(PhotoActivity.this, "Upload Failed -> " + e, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}


