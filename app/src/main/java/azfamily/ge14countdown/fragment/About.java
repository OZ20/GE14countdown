package azfamily.ge14countdown.fragment;


import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.TwitterAuthProvider;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import azfamily.ge14countdown.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class About extends Fragment implements View.OnClickListener{

    private static final String TAG = "TwitterLogin";

    private TextView tv_share,tv_btc,tv_ltc,tv_eth,tv_paypal,tv_like,tv_feedback,tv_rate;
    private SpannableString ss1,ss2,ss3;


    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    private TwitterLoginButton mLoginButton;

    private View view;

    private ImageView imageView1;

//    private ProgressDialog mProgressDialog;


    public About() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         view = inflater.inflate(R.layout.fragment_about, container, false);

        tv_btc = view.findViewById(R.id.tv_btc);
        tv_eth = view.findViewById(R.id.tv_eth);
        tv_ltc = view.findViewById(R.id.tv_ltc);
        tv_paypal = view.findViewById(R.id.tv_paypal);
        tv_like = view.findViewById(R.id.tv_like);
        tv_share = view.findViewById(R.id.tv_share);
        tv_rate = view.findViewById(R.id.tv_rate);
        tv_feedback = view.findViewById(R.id.tv_feedback);

        setSpannable();

        tv_btc.setOnClickListener(this);
        tv_eth.setOnClickListener(this);
        tv_ltc.setOnClickListener(this);
        tv_paypal.setOnClickListener(this);
        tv_like.setOnClickListener(this);
        tv_share.setOnClickListener(this);
        tv_rate.setOnClickListener(this);
        tv_feedback.setOnClickListener(this);


        // [START initialize_twitter_login]
        mLoginButton = view.findViewById(R.id.button_twitter_login);
        mLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                Log.d(TAG, "twitterLogin:success" + result);
//                handleTwitterSession(result.data);
            }

            @Override
            public void failure(TwitterException exception) {
                Log.w(TAG, "twitterLogin:failure", exception);
//                updateUI(null);
            }
        });

        imageView1 = view.findViewById(R.id.imageView1);
        Glide.with(getActivity()).load(R.drawable.azdev).apply(RequestOptions.circleCropTransform()).into(imageView1);



        return view;
    }

    private void setSpannable(){

        ss1 = new SpannableString(getString(R.string.rate));
        ss2 = new SpannableString(getString(R.string.share));
        ss3 = new SpannableString(getString(R.string.feedback));

        ss1.setSpan(new RelativeSizeSpan(1.2f),0,4,0);
        ss1.setSpan(new ForegroundColorSpan(Color.BLACK),0,4,0);
        ss2.setSpan(new RelativeSizeSpan(1.2f),0,6,0);
        ss2.setSpan(new ForegroundColorSpan(Color.BLACK),0,6,0);
        ss3.setSpan(new RelativeSizeSpan(1.2f),0,8,0);
        ss3.setSpan(new ForegroundColorSpan(Color.BLACK),0,8,0);

        tv_rate.setText(ss1);
        tv_share.setText(ss2);
        tv_feedback.setText(ss3);
    }



    private void handleTwitterSession(TwitterSession session) {
        Log.d(TAG, "handleTwitterSession:" + session);
        // [START_EXCLUDE silent]
//        showProgressDialog();
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
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(getActivity(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        }

                        // [START_EXCLUDE]
//                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result to the Twitter login button.
        mLoginButton.onActivityResult(requestCode, resultCode, data);
    }

    public void onClick(View view){
        switch (view.getId()){

            case R.id.tv_btc:
                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("BTC",tv_btc.getText().toString());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getActivity(),"Copied: BTC address " + clip.toString(),Toast.LENGTH_SHORT ).show();
                break;
            case R.id.tv_eth:
                ClipboardManager clipboard2 = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip2 = ClipData.newPlainText("ETH",tv_eth.getText().toString());
                clipboard2.setPrimaryClip(clip2);
                Toast.makeText(getActivity(),"Copied: ETH address " + clip2.toString(),Toast.LENGTH_SHORT ).show();
                break;
            case R.id.tv_ltc:
                ClipboardManager clipboard3 = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip3 = ClipData.newPlainText("LTC",tv_ltc.getText().toString());
                clipboard3.setPrimaryClip(clip3);
                Toast.makeText(getActivity(),"Copied: LTC address " + clip3.toString(),Toast.LENGTH_SHORT ).show();
                break;
            case R.id.tv_paypal:
                paypalIntent();
                break;
            case R.id.tv_rate:
                rateApp();
                break;
            case R.id.tv_share:
                shareApp();
                break;
            case R.id.tv_feedback:
                sendFeedback();
                break;
            case R.id.tv_like:
                dialogWhy();
                break;
        }

    }

    private void dialogWhy() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage(getString(R.string.ayatwhy));

        builder.setPositiveButton("Okay ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void shareApp(){

        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, "PRU14 Undi");
            String sAux = getString(R.string.ayatpru);
            sAux = sAux + "https://play.google.com/store/apps/details?id=azfamily.ge14countdown \n\n";
            i.putExtra(Intent.EXTRA_TEXT, sAux);
            startActivity(Intent.createChooser(i, "Share via"));
        } catch(Exception e) {
            //e.toString();
        }
    }

    private void sendFeedback(){

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "photoniasm@gmail.com" });
        intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
        intent.putExtra(Intent.EXTRA_TEXT, "Komen saya tentang aplikasi ini: ");
        startActivity(Intent.createChooser(intent, ""));
    }

    private void paypalIntent(){

        String url = "https://www.paypal.me/azdev65/1";
        Intent i = new Intent();
        i.setPackage("com.android.chrome");
        i.setAction(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    private void rateApp()
    {
        try
        {
            Intent rateIntent = rateIntentForUrl("market://details");
            startActivity(rateIntent);
        }
        catch (ActivityNotFoundException e)
        {
            Intent rateIntent = rateIntentForUrl("https://play.google.com/store/apps/details");
            startActivity(rateIntent);
        }
    }

    private Intent rateIntentForUrl(String url)
    {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("%s?id=%s", url, getActivity().getPackageName())));
        int flags = Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK;
        if (Build.VERSION.SDK_INT >= 21)
        {
            flags |= Intent.FLAG_ACTIVITY_NEW_DOCUMENT;
        }
        else
        {
            //noinspection deprecation
            flags |= Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET;
        }
        intent.addFlags(flags);
        return intent;
    }


//    public void showProgressDialog() {
//        if (mProgressDialog == null) {
//            mProgressDialog = new ProgressDialog(getActivity());
//            mProgressDialog.setMessage(getString(R.string.loading));
//            mProgressDialog.setIndeterminate(true);
//        }
//
//        mProgressDialog.show();
//    }
//
//    public void hideProgressDialog() {
//        if (mProgressDialog != null && mProgressDialog.isShowing()) {
//            mProgressDialog.dismiss();
//        }
//    }

}
