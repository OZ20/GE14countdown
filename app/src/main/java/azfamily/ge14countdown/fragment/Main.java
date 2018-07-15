package azfamily.ge14countdown.fragment;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import azfamily.ge14countdown.R;
import azfamily.ge14countdown.helper.Data;
import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;

public class Main extends Fragment implements OnChartValueSelectedListener{

    private WaveSwipeRefreshLayout mWaveSwipeRefreshLayout;

    private PieChart mChart;

    private String URL = "http://pd101.xyz/azam/retrieve_test.php";

//    private DemoBase demoBase;

    private static final String LOG_TAG = "MyActivity";

    private String EVENT_DATE_TIME = "2018-05-09 00:00:00";
    private String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private LinearLayout linear_layout_1, linear_layout_2;
    private TextView tv_days, tv_hour, tv_minute, tv_second , tv_jumlah,tv_bn,tv_pakatan,tv_lain;
    private Handler handler = new Handler();
    private Runnable runnable;

    private Typeface mTfRegular;
    private Typeface mTfLight;

    private JsonArrayRequest jsonArrayRequest ;

    private RequestQueue requestQueue ;

    private List<Data> dataList;

    private GoogleSignInClient mGoogleSignInClient ;

    private static final int RC_SIGN_IN = 9001;

    private FirebaseAuth mAuth;

    private View view;

    private Button b_google,b_facebook,b_twitter,b_signout;

    private String choice;

    private float vote;
    private int vote2;


    public Main() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Main.
     */
    // TODO: Rename and change types and number of parameters
    public static Main newInstance(String param1, String param2) {
        Main fragment = new Main();
        Bundle args = new Bundle();
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTfLight = Typeface.createFromAsset(getActivity().getAssets(),"OpenSans-Light.ttf");
        mTfRegular = Typeface.createFromAsset(getActivity().getAssets(),"OpenSans-Regular.ttf");

        dataList = new ArrayList<>();



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         view = inflater.inflate(R.layout.fragment_main, container, false);



//        initUI();
        countDownStart();

        mWaveSwipeRefreshLayout = (WaveSwipeRefreshLayout) view.findViewById(R.id.mySwipeRefreshLayout);
        mWaveSwipeRefreshLayout.setColorSchemeColors(Color.GRAY, Color.WHITE);
        mWaveSwipeRefreshLayout.setWaveColor(Color.argb(100,255,255,255));
        mWaveSwipeRefreshLayout.setOnRefreshListener(new WaveSwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                // Do work to refresh the list here.
                Log.i(LOG_TAG, "onRefresh called from SwipeRefreshLayout");
                myUpdateOperation();
            }
        });

        linear_layout_2 = view.findViewById(R.id.linear_layout_2);
        tv_days = view.findViewById(R.id.tv_days);
        tv_hour = view.findViewById(R.id.tv_hour);
        tv_minute = view.findViewById(R.id.tv_minute);
        tv_second = view.findViewById(R.id.tv_second);
        tv_jumlah = view.findViewById(R.id.tv_jumlah);
        tv_bn  = view.findViewById(R.id.tv_bn);
        tv_pakatan = view.findViewById(R.id.tv_pakatan);
        tv_lain = view.findViewById(R.id.tv_lain);



        pollChart();
        JSON_DATA_WEB_CALL();

        return view;
    }

    public void pollChart(){

        /***** PieChart ******/

        mChart = (PieChart) view.findViewById(R.id.chart);
        mChart.setUsePercentValues(true);
        mChart.getDescription().setEnabled(false);
        mChart.setExtraOffsets(5, 10, 5, 5);

        mChart.setDragDecelerationFrictionCoef(0.95f);

        mChart.setCenterTextTypeface(mTfLight);
        mChart.setCenterText(generateCenterSpannableText());

        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(Color.WHITE);

        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(110);

        mChart.setHoleRadius(58f);
        mChart.setTransparentCircleRadius(61f);

        mChart.setDrawCenterText(true);

        mChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mChart.setRotationEnabled(true);
        mChart.setHighlightPerTapEnabled(true);

        // mChart.setUnit(" €");
        // mChart.setDrawUnitsInChart(true);

        // add a selection listener
        mChart.setOnChartValueSelectedListener(this);



        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        // mChart.spin(2000, 0, 360);

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // entry label styling
        mChart.setEntryLabelColor(Color.BLACK);
        mChart.setEntryLabelTypeface(mTfRegular);
        mChart.setEntryLabelTextSize(12f);

    }

    public void JSON_DATA_WEB_CALL(){

        jsonArrayRequest = new JsonArrayRequest(URL,

                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

//                        progressBar.setVisibility(View.GONE);

                        JSON_PARSE_DATA_AFTER_WEBCALL(response);

                        Log.d(LOG_TAG,"Berjaya");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.d(LOG_TAG,"Gagal",error);
                    }
                });

        requestQueue = Volley.newRequestQueue(getActivity());

        requestQueue.add(jsonArrayRequest);

    }

    public void JSON_PARSE_DATA_AFTER_WEBCALL(JSONArray array){
        int vote3,vote2,bn,pakatan,lain ;
        vote3 = 0;
        vote2 = 0;
//        bn = 0;
//        pakatan =0;
//        lain =0;
        ArrayList<PieEntry> yEntrys = new ArrayList<>();
        ArrayList<Integer> data1 = new ArrayList<>();
//        int[] data = new int[0];

        for(int i = 0; i<array.length(); i++) {

            try {

                JSONObject json = array.getJSONObject(i);

                 choice = json.getString("choice");

                 vote = json.getInt("COUNT(user_id)");
//                 vote2 += json.getInt("COUNT(user_id)");
                 vote3 += json.getInt("COUNT(user_id)");
                 vote2 = json.getInt("COUNT(user_id)");

                yEntrys.add(new PieEntry(vote,choice));
                data1.add(vote2);



            }
            catch (JSONException e) {

                e.printStackTrace();
            }



//            Log.d(LOG_TAG,"Sum of vote: " + vote2);


        }

        PieDataSet dataSet = new PieDataSet(yEntrys, "Keputusan Undian");

        dataSet.setDrawIcons(false);

        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        dataSet.setSelectionShift(15f);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);
        data.setValueTypeface(mTfLight);
        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);
        mChart.notifyDataSetChanged();
        mChart.invalidate();

        Log.d(LOG_TAG,"Sum of vote: " + vote3);
        Log.d(LOG_TAG,"Sum of vote: " + data1);
        bn = data1.get(0);
        lain = data1.get(1);
        pakatan = data1.get(2);
        Log.d(LOG_TAG,"bn: " + bn);
        Log.d(LOG_TAG,"lain: " + lain);
        Log.d(LOG_TAG,"pakatan: " + pakatan);
        tv_jumlah.setText(vote3 + " Undi");
        tv_pakatan.setText(pakatan  + " Undi");
        tv_bn.setText(bn + " Undi");
        tv_lain.setText(lain + " Undi");
    }



    private SpannableString generateCenterSpannableText() {

        SpannableString s = new SpannableString("PRU14 POLL\ndeveloped by AZDEV");
        s.setSpan(new RelativeSizeSpan(1.7f), 0, 10, 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), 10, s.length() - 9, 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 10, s.length() - 9, 0);
        s.setSpan(new RelativeSizeSpan(.8f), 10, s.length() - 9, 0);
        s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 9, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(Color.RED), s.length() - 9, s.length(), 0);
        return s;
    }

    private void setData(int count, float range) {

        float mult = range;


    }


    private void myUpdateOperation( ){

//        initUI();
        countDownStart();
        pollChart();
        JSON_DATA_WEB_CALL();
        mWaveSwipeRefreshLayout.setRefreshing(false);
    }

    private void initUI() {


    }

    private void countDownStart() {
        runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    handler.postDelayed(this, 1000);
                    SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
                    Date event_date = dateFormat.parse(EVENT_DATE_TIME);
                    Date current_date = new Date();
                    if (!current_date.after(event_date)) {
                        long diff = event_date.getTime() - current_date.getTime();
                        long Days = diff / (24 * 60 * 60 * 1000);
                        long Hours = diff / (60 * 60 * 1000) % 24;
                        long Minutes = diff / (60 * 1000) % 60;
                        long Seconds = diff / 1000 % 60;
                        //
                        tv_days.setText(String.format("%02d", Days));
                        tv_hour.setText(String.format("%02d", Hours));
                        tv_minute.setText(String.format("%02d", Minutes));
                        tv_second.setText(String.format("%02d", Seconds));
                    } else {
                        linear_layout_1.setVisibility(View.VISIBLE);
                        linear_layout_2.setVisibility(View.GONE);
                        handler.removeCallbacks(runnable);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        handler.postDelayed(runnable, 1000);
    }

    public void onStop() {
        super.onStop();
        handler.removeCallbacks(runnable);
    }

    public void onResume(){
        super.onResume();
        myUpdateOperation();
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

        if (e == null)
            return;
        Log.i("VAL SELECTED",
                "Value: " + e.toString() + ", Value2: " + h.getY() + ", index: " + h.getX()
                        + ", DataSet index: " + h.getDataSetIndex());

//        Toast.makeText(getActivity(),"Value: " + e.getY() + ", Party: " + h.getX(),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected() {
        Log.i("PieChart", "nothing selected");
    }


}
