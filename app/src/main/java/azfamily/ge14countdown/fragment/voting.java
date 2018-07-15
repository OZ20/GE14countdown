package azfamily.ge14countdown.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.TwitterAuthProvider;
import com.google.firebase.auth.UserInfo;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import azfamily.ge14countdown.ConRequest;
import azfamily.ge14countdown.EmailCheck;
import azfamily.ge14countdown.R;


public class voting extends Fragment {

    private String TAG = "LOL";
    private String LOG_TAG = "Firebase";

    private RadioGroup radioGroup;
    private Button  buttonundi;
    private RadioButton radioButton;

    private View view;

    private GoogleSignInClient mGoogleSignInClient ;

    private static final int RC_SIGN_IN = 9001;

    private FirebaseAuth mAuth;

    private ImageButton b_google,b_signout,b_twitter2,b_facebook2;

    private TextView l_with,name,status;

    private ProgressDialog mProgressDialog;

    private Fragment main = new Main();

    private int selectedId;

    private RadioButton r_1,r_2,r_3;

    private CallbackManager mCallbackManager;

    private LoginButton b_facebook;

    private TwitterLoginButton b_twitter;

    private LinearLayout layout_undi;

    private ImageView imageView;

    private String Semail,Sname,Suid,Sprovider_id,Gemail,Gname,Gchoice,Gst;

    private Uri Sphoto_url;

    private AdView mAdView;

    private RelativeLayout l_alogin;
    private InterstitialAd mInterstitialAd;
    private AdRequest adRequest;


    public voting() {
        // Required empty public constructor
    }


    public static voting newInstance(String BN, String PAKATAN,String LAIN) {
        voting fragment = new voting();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TwitterAuthConfig authConfig =  new TwitterAuthConfig(
                getString(R.string.twitter_consumer_key),
                getString(R.string.twitter_consumer_secret));

        TwitterConfig twitterConfig = new TwitterConfig.Builder(getActivity())
                .twitterAuthConfig(authConfig)
                .build();

        Twitter.initialize(twitterConfig);

        MobileAds.initialize(getActivity(), getString(R.string.admob_appid));


        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        view = inflater.inflate(R.layout.fragment_voting, container, false);

        l_with = view.findViewById(R.id.tv_lwith);
        name = view.findViewById(R.id.tv_name);
        imageView = view.findViewById(R.id.iv_profile);
        l_alogin = view.findViewById(R.id.l_alogin);

        //twitter login
        b_twitter = view.findViewById(R.id.button_twitter_login);
        b_twitter.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                Log.d(TAG, "twitterLogin:success2" + result);
                handleTwitterSession(result.data);
            }

            @Override
            public void failure(TwitterException exception) {
                Log.w(TAG, "twitterLogin:failure2", exception);
                getUserData(null);
            }
        });

        b_google = view.findViewById(R.id.b_google);
        b_facebook = view.findViewById(R.id.b_facebook);
        b_signout = view.findViewById(R.id.b_signout);
        b_twitter2 = view.findViewById(R.id.b_twitter2);
        b_facebook2 = view.findViewById(R.id.b_facebook2);

        /**** Google Sign-In ****/

        FacebookSdk.sdkInitialize(getActivity());

        mCallbackManager = CallbackManager.Factory.create();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

        layout_undi = view.findViewById(R.id.layout_undi);

        mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
//        adRequest = new AdRequest.Builder().addTestDevice("FFD4CF3F72993E855893F4B439116DD0").build();
        mAdView.loadAd(adRequest);
        mInterstitialAd = new InterstitialAd(getActivity());
//        mInterstitialAd.setAdUnitId(getString(R.string.ad_unitid2));
        mInterstitialAd.setAdUnitId(getString(R.string.ad_unitid2));

        return view;
    }

    public void onViewCreated(final View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        final View v = view;

        radioGroup = (RadioGroup) view.findViewById(R.id.r_undi);
        buttonundi = (Button) view.findViewById(R.id.b_undi);
        r_1 = view.findViewById(R.id.r_1);
        r_2 = view.findViewById(R.id.r_2);
        r_3 = view.findViewById(R.id.r_3);


        buttonundi.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // get selected radio button from radioGroup

                selectedId = radioGroup.getCheckedRadioButtonId();
//                Log.d(TAG,"selectId" + selectedId);

                // find the radiobutton by returned id
                radioButton = (RadioButton) view.findViewById(selectedId);
                final String choice ;


//                Log.d(TAG,"radioButton" + radioButton);
//                Log.d(TAG,"radioButton.getText()" + radioButton.getText());

                if(r_1.isChecked() || r_2.isChecked() || r_3.isChecked()) {

                    choice = radioButton.getText().toString();

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                    builder.setMessage("Adakah anda pasti? pilihan: \n\n" + choice);

                    builder.setNegativeButton("Ya", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (Semail != null) {

//                                LottieAnimationView animationView = new LottieAnimationView(getActivity());
//
//                                animationView.setAnimation(R.raw.motorcycle);
//                                animationView.playAnimation();

                                final Response.Listener<String> responseListener = new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject jsonResponse = new JSONObject(response);
                                            boolean success = jsonResponse.getBoolean("success");
                                            if (success) {

                                                Log.d(TAG, "Berjaya Upload");

                                                Toast.makeText(getActivity(), choice, Toast.LENGTH_SHORT).show();
                                            } else {

                                                Log.d(TAG, "Gagal Upload");
                                                Toast.makeText(getActivity(),"Anda telah mengundi , dilarang untuk mengundi lebih dari sekali",Toast.LENGTH_SHORT).show();


                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            Log.d(TAG, "JsonException", e);
                                        }
                                    }
                                };

                                ConRequest cRequest = new ConRequest(choice, Semail, Sname, Suid, responseListener);
                                RequestQueue queue = Volley.newRequestQueue(getActivity());
                                queue.add(cRequest);

                                mInterstitialAd.loadAd(new AdRequest.Builder().build());
//                                AdRequest adRequest = new AdRequest.Builder().addTestDevice("FFD4CF3F72993E855893F4B439116DD0").build();
//                                mInterstitialAd.loadAd(adRequest);
                                mInterstitialAd.show();

                            } else  {

                                Toast.makeText(getActivity(), "Sila Login Terlebih dahulu", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    builder.setPositiveButton("Tidak", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d(TAG,"Tidak ditekan");
                        }
                    });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                }

                else {

                    Toast.makeText(getActivity(),"Sila pilih salah satu",Toast.LENGTH_SHORT).show();
                }

            }
        });


    }


    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        b_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        //facebook login
        b_facebook2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b_facebook.performClick();
            }
        });
        b_facebook.setReadPermissions("email","public_profile");
        b_facebook.setFragment(this);
        b_facebook.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                // [START_EXCLUDE]
                getUserData(null);
                // [END_EXCLUDE]

            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                // [START_EXCLUDE]
                getUserData(null);
                // [END_EXCLUDE]
            }
        });

        b_twitter2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b_twitter.performClick();
            }
        });

        b_signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

    }

    private void signOut() {
        // Firebase sign out


        mAuth.signOut();
        // reset data
        resetUser();
        //facebook signOut
        LoginManager.getInstance().logOut();
        // twitter signOut
        TwitterCore.getInstance().getSessionManager().clearActiveSession();
        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(getActivity(),
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        getUserData(null);
                    }
                });

        buttonundi.setClickable(true);
        l_with.setVisibility(View.VISIBLE);
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void onStart(){
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        getUserData(currentUser);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        b_twitter.onActivityResult(requestCode, resultCode, data);

        // facebook
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);

            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(LOG_TAG, "Google sign in failed", e);
                // [START_EXCLUDE]
                getUserData(null);
                // [END_EXCLUDE]
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(LOG_TAG, "firebaseAuthWithGoogle:" + acct.getId());
        // [START_EXCLUDE silent]
        showProgressDialog();
        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(LOG_TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            getUserData(user);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(LOG_TAG, "signInWithCredential:failure", task.getException());
                            Snackbar.make(view.findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                        }

                        hideProgressDialog();
                    }
                });

    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);
        showProgressDialog();

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            getUserData(user);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(getActivity(), "Authentication failed.",Toast.LENGTH_SHORT).show();
                            getUserData(null);
                        }
                        hideProgressDialog();
                    }
                });

    }

    private void handleTwitterSession(TwitterSession session) {
        Log.d(TAG, "handleTwitterSession2:" + session);
        // [START_EXCLUDE silent]
        showProgressDialog();
        // [END_EXCLUDE]

        AuthCredential credential = TwitterAuthProvider.getCredential(
                session.getAuthToken().token,
                session.getAuthToken().secret);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            getUserData(user);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(getActivity(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            getUserData(null);
                        }
                        // [START_EXCLUDE]
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public void onResume(){
        super.onResume();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        getUserData(currentUser);

    }



    private void getUserData(FirebaseUser currentUser){


        if (currentUser != null) {
            for (UserInfo profile : currentUser.getProviderData()) {
                // Id of the provider (ex: google.com)
                Sprovider_id = profile.getProviderId();

                // UID specific to the provider
                Suid = profile.getUid();

                // Name, email address, and profile photo Url
                Sname = profile.getDisplayName();
                Semail = profile.getEmail();
                Sphoto_url = profile.getPhotoUrl();


                name.setText("Login sebagai: \n" + Sname);
                Glide.with(getActivity()).load(Sphoto_url).apply(RequestOptions.circleCropTransform()).into(imageView);
                Snackbar.make(view.findViewById(R.id.main_layout),"Login sebagai " + Sname,Snackbar.LENGTH_SHORT).show();

                b_facebook2.setVisibility(View.GONE);
                b_google.setVisibility(View.GONE);
                b_twitter2.setVisibility(View.GONE);
                b_signout.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.VISIBLE);
                name.setVisibility(View.VISIBLE);
                l_with.setVisibility(View.GONE);
                l_alogin.setVisibility(View.VISIBLE);

//                emailCheck(Semail);

                Log.d(TAG,"Update data");
            }


        }else {
//            status.setText(R.string.signed_out);
//            mDetailTextView.setText(null);
            l_with.setText("Login dengan:  ");

            b_facebook2.setVisibility(View.VISIBLE);
            b_google.setVisibility(View.VISIBLE);
            b_twitter2.setVisibility(View.VISIBLE);
            b_signout.setVisibility(View.GONE);
            imageView.setVisibility(View.GONE);
            name.setVisibility(View.GONE);
            l_alogin.setVisibility(View.GONE);
//            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);

            Log.w(TAG,"Can't get data ");
        }
    }

    private void emailCheck(String Semail){

        final Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    if (success) {

                        Gemail = jsonResponse.getString("email");
                        Gname = jsonResponse.getString("name");
                        Gchoice = jsonResponse.getString("choice");
                        Gst = jsonResponse.getString("SubmitTime");

                    }else {
                        Log.w(TAG,"Gagal dapat info");

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };

        EmailCheck emailCheck = new EmailCheck(Semail, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(emailCheck);

        Log.d(TAG,"email: " + Gemail);
        Log.d(TAG,"name: " + Gname);
        Log.d(TAG,"choice: " + Gchoice);
        Log.d(TAG,"SubmitTime: " + Gst);

    }


    private void resetUser(){

        Sname = null;
        Sphoto_url = null;
        Semail = null;
        Sprovider_id = null;
        Suid = null;
//        Gemail = null;


        name.setText(Sname);

    }


//    private void loadFragment(Fragment fragment) {
//        // load fragment
//        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.content_fragment, fragment);
//        transaction.addToBackStack(null);
//        transaction.commit();
//    }

}
