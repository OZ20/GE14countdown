package azfamily.ge14countdown;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationViewPager;

import azfamily.ge14countdown.fragment.About;
import azfamily.ge14countdown.fragment.Main;
import azfamily.ge14countdown.fragment.voting;
import azfamily.ge14countdown.helper.AppRater;
import azfamily.ge14countdown.helper.BottomBarAdapter;

public class MainActivity extends AppCompatActivity {


    private AHBottomNavigationViewPager viewPager;
    private AHBottomNavigation bottomNavigation;
    private BottomBarAdapter adapter;

    private Toolbar toolbar;

    private Main fragment1 = new Main();
    private voting fragment2 = new voting();
    private About fragment3 = new About();

    private ActionBar actionBar;
    private TextView title1,title2,title3;
    private RelativeLayout.LayoutParams layoutparams;

    private ImageView imageView1;

    private int[] tabColors;


//    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
//            = new BottomNavigationView.OnNavigationItemSelectedListener() {
//
//        Fragment fragment;
//
//
//        @Override
//        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//            switch (item.getItemId()) {
//                case R.id.navigation_home:
//                    toolbar.setTitle("PRU 14");
//                    loadFragment(fragment1);
//                    return true;
//                case R.id.navigation_dashboard:
//                    toolbar.setTitle("Voting");
//                    loadFragment(fragment2);
//                    return true;
//                case R.id.navigation_notifications:
//                    loadFragment(fragment3);
//                    return true;
//            }
////            viewPager.setCurrentItem(item.getOrder());
//            return true;
//        }
//    };

//    @Override
//    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//    }
//
//    @Override
//    public void onPageSelected(int position) {
//
//        navigation.getMenu().getItem(position).setChecked(true);
//    }
//
//    @Override
//    public void onPageScrollStateChanged(int state) {
//
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        TwitterAuthConfig authConfig =  new TwitterAuthConfig(
//                getString(R.string.twitter_consumer_key),
//                getString(R.string.twitter_consumer_secret));
//
//        TwitterConfig twitterConfig = new TwitterConfig.Builder(this)
//                .twitterAuthConfig(authConfig)
//                .build();
//
//        Twitter.initialize(twitterConfig);

        setContentView(R.layout.activity_main);


        actionBar = getSupportActionBar();

//        actionBar.setTitle("Home");
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM );
        actionBar.setCustomView(R.layout.custom_logo);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        bottomNavigation = findViewById(R.id.navigation);

        AppRater.app_launched(this);
        setupViewPager();

        tabColors = getApplicationContext().getResources().getIntArray(R.array.tab_colors);


        AHBottomNavigationItem item1 = new AHBottomNavigationItem("Home", R.drawable.ic_home_black_24dp, R.color.color_tab_1);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem("Vote", R.drawable.v1_2, R.color.color_tab_2);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem("About", R.drawable.p1_2, R.color.color_tab_3);
        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);


        bottomNavigation.setTranslucentNavigationEnabled(false);
        bottomNavigation.setBehaviorTranslationEnabled(true);
        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_HIDE);
        bottomNavigation.setCurrentItem(0);
        bottomNavigation.setAccentColor(Color.parseColor("#F63D2B"));
        bottomNavigation.setInactiveColor(Color.parseColor("#747474"));
        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {

                if (!wasSelected){

                    viewPager.setCurrentItem(position);
                }
                if(position == 0){

                    actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM );
                    actionBar.setCustomView(R.layout.custom_logo);
                }
                if(position == 1){

                    ActionBarTitleGravity("Undi");


                }
                if(position == 2){

                    ActionBarTitleGravity("Info");
                }
                return true;
            }
        });

//        bottomNavigation.setOnNavigationPositionListener(new AHBottomNavigation.OnNavigationPositionListener() {
//            @Override public void onPositionChange(int y) {
//                // Manage the new y position
//
//                if(y < 1 ){
//
//                    bottomNavigation.restoreBottomNavigation(true);
//                }
//            }
//        });





//        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
//
//            @Override
//            public Fragment getItem(int position) {
//                switch (position) {
//                    case 0:
//                        return fragment1;
//                    case 1:
//                        return fragment2;
//                    case 2:
//                        return fragment3;
//
//                }
//                return null;
//            }
//
//            @Override
//            public int getCount() {
//                return 3;
//            }
//        });
    }

    private void setupViewPager(){

        viewPager = findViewById(R.id.content_fragment);
        viewPager.setPagingEnabled(false);
        adapter = new BottomBarAdapter(getSupportFragmentManager());
        adapter.addFragments(fragment1);
        adapter.addFragments(fragment2);
        adapter.addFragments(fragment3);
        viewPager.setAdapter(adapter);
    }

//        final Toolbar toolbar2 = (Toolbar) findViewById(R.id.toolbar5);
//        setSupportActionBar(toolbar2);
//        getSupportActionBar().setTitle("Profile Update");

//        mySwipeRefreshLayout = findViewById(R.id.mySwipeRefreshLayout);
//
//        mySwipeRefreshLayout.setOnRefreshListener(
//                new SwipeRefreshLayout.OnRefreshListener() {
//                    @Override
//                    public void onRefresh() {
//                        Log.i(LOG_TAG, "onRefresh called from SwipeRefreshLayout");
//
//                        // This method performs the actual data-refresh operation.
//                        // The method calls setRefreshing(false) when it's finished.
//                        myUpdateOperation();
//                    }
//                }
//        );


//    private void loadFragment(Fragment fragment) {
//        // load fragment
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.content_fragment, fragment);
//        transaction.addToBackStack(null);
//        transaction.commit();
//    }

    @Override
    protected void onActivityResult ( int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        FragmentManager fragment = getSupportFragmentManager();
        if (fragment != null) {
            fragment.findFragmentById(R.id.content_fragment).onActivityResult(requestCode, resultCode, data);

        } else Log.d("Twitter", "fragment is null");
    }

    private void ActionBarTitleGravity(String title) {
        // TODO Auto-generated method stub


        title1 = new TextView(this);

        layoutparams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        title1.setLayoutParams(layoutparams);
        title1.setText(title);
        title1.setTextColor(Color.BLACK);
        title1.setGravity(Gravity.CENTER);
        title1.setTextSize(20);
        title1.setTypeface(Typeface.DEFAULT_BOLD);
//        title1.setTextAppearance(R.style.CardView_Dark);

        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        actionBar.setCustomView(title1);

    }

//    public void showProgressDialog() {
//        if (mProgressDialog == null) {
//            mProgressDialog = new ProgressDialog(this);
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

