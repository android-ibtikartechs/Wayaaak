package com.ibtikar.apps.wayaaak.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.gson.Gson;
import com.hosamazzam.volleysimple.VolleySimple;
import com.ibtikar.apps.wayaaak.MainActivity;
import com.ibtikar.apps.wayaaak.Models.Response.LoginResponse;
import com.ibtikar.apps.wayaaak.R;
import com.ibtikar.apps.wayaaak.Tools.WayaaakAPP;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import org.json.JSONException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hosam azzam on 03/03/2018.
 */

public class Login_Fragment extends Fragment {
    EditText email, password;
    VolleySimple volleySimple;
    CallbackManager callbackManager;
    com.facebook.FacebookCallback<LoginResult> FacebookCallback;
    LoginManager loginManager;
    TwitterLoginButton twitterLoginButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_login, container, false);

        volleySimple = VolleySimple.getInstance(getActivity());
        email = rootview.findViewById(R.id.email_edtx);
        password = rootview.findViewById(R.id.password_edtx);
        loginManager = LoginManager.getInstance();
        twitterLoginButton = new TwitterLoginButton(getContext());

        rootview.findViewById(R.id.login_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!email.getText().toString().equals("") && !password.getText().toString().equals("")) {
                    login(email.getText().toString(), password.getText().toString());
                } else {
                    Toast.makeText(getContext(), "Please fill all boxes", Toast.LENGTH_LONG).show();
                }
            }
        });


        rootview.findViewById(R.id.register_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().add(R.id.main_content, new Register_Fragment(), "").addToBackStack("").commit();
            }
        });

        rootview.findViewById(R.id.forget_txt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().add(R.id.main_content, new Forget_Fragment(), "").addToBackStack("").commit();
            }
        });

        rootview.findViewById(R.id.fb_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), getResources().getString(R.string.fetch_done), Toast.LENGTH_LONG).show();
                LoginManager.getInstance().logInWithReadPermissions(getActivity(), Arrays.asList("public_profile", "email"));
                LoginManager.getInstance().registerCallback(callbackManager, FacebookCallback);
            }
        });

        rootview.findViewById(R.id.tw_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                twitterLoginButton.performClick();
            }
        });

        loginManager.logOut();
        fbLogin();
        twLogin();
        return rootview;
    }

    public void login(String email, String pass) {
        Map<String, String> map = new HashMap<>();
        map.put("email", email);
        map.put("password", pass);
        ProgressDialog progressDialog = ProgressDialog.show(getContext(), "انتظر من فضلك", "", false, false);
        volleySimple.asyncStringPost(WayaaakAPP.BASE_URL + "login", map, new VolleySimple.NetworkListener<String>() {
            @Override
            public void onResponse(String response) {
                LoginResponse loginResponse = new Gson().fromJson(response, LoginResponse.class);
                if (loginResponse.getStatus().equals("true")) {
                    WayaaakAPP.registerUserLogin(getContext(), loginResponse.getUser());
                    getActivity().finish();
                    startActivity(new Intent(getActivity(), MainActivity.class));

                } else {
                    Toast.makeText(getContext(), "email or password is wrong", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }, progressDialog);
    }

    public void login(String token) {
        Map<String, String> map = new HashMap<>();
        map.put("socialtoken", token);
        volleySimple.asyncStringPost(WayaaakAPP.BASE_URL + "sociallogin", map, new VolleySimple.NetworkListener<String>() {
            @Override
            public void onResponse(String response) {
                LoginResponse loginResponse = new Gson().fromJson(response, LoginResponse.class);
                if (loginResponse.getStatus().equals("true")) {
                    WayaaakAPP.registerUserLogin(getContext(), loginResponse.getUser());
                    startActivity(new Intent(getActivity(), MainActivity.class));
                } else {
                    Toast.makeText(getContext(), "Account doesn't exist", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }, true);
    }


    public void fbLogin() {
        callbackManager = CallbackManager.Factory.create();
        FacebookCallback = new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                //   finish();
                System.out.println("login done  " + AccessToken.getCurrentAccessToken().getToken());
                AccessToken.getCurrentAccessToken().getToken();


                System.out.println("onSuccess");
                final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage(getResources().getString(R.string.pls_wait));
                progressDialog.show();
                String accessToken = AccessToken.getCurrentAccessToken().getToken();

                System.out.println("accessToken " + accessToken);

                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(org.json.JSONObject object, GraphResponse response) {

                        // Get facebook data from login
                        Bundle bFacebookData = getFacebookData(object);
                        System.out.println("result " + bFacebookData.toString());
                        progressDialog.dismiss();
                        login(bFacebookData.getString("idFacebook"));

                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, first_name, last_name, email,gender, birthday, location"); // Parámetros que pedimos a facebook
                request.setParameters(parameters);
                request.executeAsync();
                // App code
            }

            @Override
            public void onCancel() {
                System.out.println("user cancel");
                Toast.makeText(getContext(), getResources().getString(R.string.user_cancle), Toast.LENGTH_LONG).show();
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                System.out.println("user error");
                exception.printStackTrace();
                Toast.makeText(getContext(), getResources().getString(R.string.fetch_error), Toast.LENGTH_LONG).show();
                // App code
            }
        };

    }

    private void twLogin() {
        twitterLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // Do something with result, which provides a TwitterSession for making API calls
                String token = String.valueOf(result.data.getUserId());
                System.out.println("twitter : " + token);
                login(token);
                Toast.makeText(getContext(), getResources().getString(R.string.fetch_done), Toast.LENGTH_LONG).show();
            }

            @Override
            public void failure(TwitterException exception) {
                // Do something on failure
                Toast.makeText(getContext(), getResources().getString(R.string.fetch_error), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        System.out.println("pasued");
        onStop();
    }


    private Bundle getFacebookData(org.json.JSONObject object) {

        try {
            Bundle bundle = new Bundle();
            String id = object.getString("id");

            try {
                URL profile_pic = new URL("https://graph.facebook.com/" + id + "/picture?width=200&height=150");

                bundle.putString("profile_pic", profile_pic.toString());

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }

            bundle.putString("idFacebook", id);
            if (object.has("first_name"))
                bundle.putString("first_name", object.getString("first_name"));
            if (object.has("last_name"))
                bundle.putString("last_name", object.getString("last_name"));
            if (object.has("email"))
                bundle.putString("email", object.getString("email"));
            if (object.has("gender"))
                bundle.putString("gender", object.getString("gender"));
            if (object.has("birthday"))
                bundle.putString("birthday", object.getString("birthday"));
            if (object.has("location"))
                bundle.putString("location", object.getJSONObject("location").getString("name"));

            return bundle;
        } catch (JSONException e) {

        }
        return new Bundle();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //  Toast.makeText(getContext(), "requested", Toast.LENGTH_LONG).show();
        callbackManager.onActivityResult(requestCode, resultCode, data);
        twitterLoginButton.onActivityResult(requestCode, resultCode, data);


    }
}