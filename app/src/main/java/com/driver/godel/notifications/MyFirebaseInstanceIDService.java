/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.driver.godel.notifications;

import android.content.SharedPreferences;
import android.util.Log;

import com.driver.godel.RefineCode.RefineUtils.SharedValues;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
//public class MyFirebaseInstanceIDService extends FirebaseMessagingService {
    // private static final String TAG = "MyFirebaseIIDService";
    SharedPreferences settings;
    SharedPreferences.Editor editor;
    final String TAG = "NOTIFICATIONS_TAG";

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

//        FirebaseInstanceId.getInstance().getInstanceId()
//                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
//                        if (!task.isSuccessful()) {
//                            Log.w(TAG, "getInstanceId failed", task.getException());
//                            return;
//                        }
//
//                        // Get new Instance ID token
//                        String token = task.getResult().getToken();
//
//                        // Log and toast
//                        String msg = getString(R.string.msg_token_fmt, token);
//                        Log.d(TAG, msg);
////                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
//                    }
//                });

        sendRegistrationToServer(refreshedToken);
        Log.d(TAG, "--------refreshedToken---------" +refreshedToken );
    }

//    @Override
//    public void onNewToken(String s) {
//        super.onNewToken(s);
//    FirebaseInstanceId.getInstance().getInstanceId()
//                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
//        @Override
//        public void onComplete(@NonNull Task<InstanceIdResult> task) {
//            if (!task.isSuccessful()) {
//                Log.w(TAG, "getInstanceId failed", task.getException());
//                return;
//            }
//
//            // Get new Instance ID token
//            String token = task.getResult().getToken();
//
//            // Log and toast
//            String msg = getString(R.string.msg_token_fmt, token);
//            Log.d(TAG, msg);
////                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
//        }
//    });

//        Log.d("NEW_TOKEN",s);
//    }

    private void sendRegistrationToServer(String token) {
        Log.d(TAG, "--------token---------" +token );
        // TODO: Implement this method to send token to your app server.
        settings = getSharedPreferences(SharedValues.PREF_NAME, MODE_PRIVATE);
        editor = settings.edit();
        editor.putString(SharedValues.FCM_TOKEN, token);
        editor.commit();
    }
}
