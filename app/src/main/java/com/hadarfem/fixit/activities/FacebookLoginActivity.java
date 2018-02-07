package com.hadarfem.fixit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.hadarfem.fixit.R;
import com.hadarfem.fixit.models.User;
import com.facebook.CallbackManager;
import com.facebook.Profile;
import com.facebook.ProfileTracker;

public class FacebookLoginActivity extends AppCompatActivity {
    public static final String FACEBOOK_PICTURE_URL_FORMAT = "https://graph.facebook.com%s?type=large";
    public static final int PROFILE_PICTURE_WIDTH = 300;
    public static final int PROFILE_PICTURE_HEIGHT = 300;
    private CallbackManager callbackManager;
    private ProfileTracker profileTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.facebook_activity_login);

        callbackManager = CallbackManager.Factory.create();

        Profile profile = Profile.getCurrentProfile();

        if (profile != null) {
            login(profile);
        }

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                if (currentProfile != null) {
                    login(currentProfile);
                }
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Necessary to trigger facebook's profile tracker
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void login(Profile profile) {
        String profilePictureUrl =
                String.format(FACEBOOK_PICTURE_URL_FORMAT, profile.getProfilePictureUri(
                        PROFILE_PICTURE_WIDTH, PROFILE_PICTURE_HEIGHT).getPath());

        User user = new User().setName(profile.getName()).
                setProfilePictureUrl(profilePictureUrl);

        Intent intent = new Intent(FacebookLoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Bundle intentData = new Bundle();
        intentData.putSerializable(getString(R.string.intent_logged_user), user);
        intent.putExtras(intentData);

        startActivity(intent);

        finish();
    }
}
