package khalidelq.app.codingchallenge.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.fujiyuu75.sequent.Animation;
import com.fujiyuu75.sequent.Sequent;

import khalidelq.app.codingchallenge.R;

public class LoginActivity extends AppCompatActivity {

    private LoginButton loginButton;
    private CallbackManager callbackManager ;
    private LinearLayout lay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        // Test if the User is already logged in
        if (AccessToken.getCurrentAccessToken() != null){
            goToMainScreen();
        }

        // Views initialisation
        callbackManager =  CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_button);
       // lay = (LinearLayout) findViewById(R.id.layout);


        // Animations
        //Sequent.origin(lay).anim(getApplicationContext(), Animation.FADE_IN_UP).start();


        // Set permissions for facebook login
        loginButton.setReadPermissions("email");
        loginButton.setReadPermissions("user_photos ");


        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                goToMainScreen();
            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, "You just cancel , you need to be loged in to view your albums", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(LoginActivity.this, "An unexpected error has accrued ", Toast.LENGTH_SHORT).show();
            }
        });

    }


    // Go to the main Activity
    private void goToMainScreen() {
        Intent intent = new Intent(this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK );
        startActivity(intent);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

}
