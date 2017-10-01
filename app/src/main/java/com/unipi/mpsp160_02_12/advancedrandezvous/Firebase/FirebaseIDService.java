package com.unipi.mpsp160_02_12.advancedrandezvous.Firebase;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Nick on 22/9/2017.
 */

public class FirebaseIDService extends FirebaseInstanceIdService {

    private static final String TAG = "FirebaseIDService";

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String instanceId = FirebaseInstanceId.getInstance().getToken();
        System.out.println("InstanceID: " + instanceId);
        Log.d("@@@@", "onTokenRefresh: " + instanceId);

        // Access Shared Preferences
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();

        // Save to SharedPreferences
        editor.putString("instanceId", instanceId);
        editor.apply();

//        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        if (firebaseUser != null) {
//            FirebaseDatabase.getInstance().getReference()
//                    .child("users")
//                    .child(firebaseUser.getUid())
//                    .child("instanceId")
//                    .setValue(instanceId);
//        }
    }

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // Add custom implementation, as needed.
    }
}
