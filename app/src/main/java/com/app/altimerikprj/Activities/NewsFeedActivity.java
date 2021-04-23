package com.app.altimerikprj.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.app.altimerikprj.Adapters.RecyclerViewAdapter;
import com.app.altimerikprj.Connection.VolleyService;
import com.app.altimerikprj.Model.NewsFeed;
import com.app.altimerikprj.R;
import com.app.altimerikprj.Utilities.InterfaceResult;
import com.app.altimerikprj.Utilities.Utility;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;
import java.util.List;
//import static com.app.altimerikprj.Constants.Constants.HTTP_JSON_ID;
import static com.app.altimerikprj.Constants.Constants.HTTP_JSON_ID;
import static com.app.altimerikprj.Constants.Constants.HTTP_JSON_URL;
import static com.app.altimerikprj.Constants.Constants.TIMES;


/**
 * Created by Anil on 12/3/2017.
 */

public class NewsFeedActivity extends AppCompatActivity {
    Activity a1 = new Activity();
    ProgressBar progressBar;
    RecyclerViewAdapter mAdapter;
    InterfaceResult resultCallback ;
    VolleyService mVolleyService;
    JSONArray jsonarray;
    private List<String> mIDList = null;
    int secs=2;
    private List<NewsFeed> mListNewsFeed;
    // The minimum amount of items to have below your current scroll position before loading more.
    private int visibleThreshold = 1;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    int increCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("News Feed");
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        progressBar.setVisibility(View.VISIBLE);
        initVolleyCallback();
        mVolleyService = new VolleyService(resultCallback, this);
        mVolleyService.getDataVolley("GET_CALL_FOR_ID", /*BuildConfig.Base_URL*/HTTP_JSON_ID);

        mAdapter = new RecyclerViewAdapter();
        mAdapter.setOnItemClickListener(new RecyclerViewAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (mListNewsFeed != null) {
                    //Toast.makeText(getApplicationContext(), "" + position, Toast.LENGTH_SHORT).show();
                    Intent myIntent = new Intent(NewsFeedActivity.this, DisplayNewsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("POSITION", position);
                    bundle.putString("NEWSURL",mListNewsFeed.get(position).getUrl());
                    myIntent.putExtras(bundle);
                    startActivity(myIntent);
                }
            }

        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)  ) {
                   if((totalItemCount+TIMES)<=mIDList.size()) {
                        progressBar.setVisibility(View.VISIBLE);
                        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        loadNextDataFromApi(TIMES);
                    }
                    loading = true;
                    Log.i("AdapterScrolled", "onScrolled: End reached");
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
                    // Do something
                } else if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    // Do something
                } else {
                    // Do something
                }
            }
        });

        recyclerView.setAdapter(mAdapter);
    }

    /********
     * method is used to handle Interface callbacds
     * basded on that call parser function
     */
/*void initVolleyCallback(){
    resultCallback = new InterfaceResult() {
        @Override
        public void notifySuccess(String requestType, String response) {

        }

        @Override
        public void notifyError(String requestType, VolleyError error) {

        }
    };

}*/
   void initVolleyCallback() {
        resultCallback = new InterfaceResult() {
            private String TAG = "MainActivity";

            @Override
            public void notifySuccess(String requestType, String response) {
                Log.d(TAG, "requestype " + requestType);
                Log.d(TAG, "response" + response);
                if (requestType.equals("GET_CALL_FOR_NEWS_FEED")) {
                    parseNewsFeed(response);
                } else {
                    parseResult(response);
                }
            }

            @Override
            public void notifyError(String requestType, VolleyError volleyError) {
                Log.d(TAG, "Volley requester " + requestType);
                Log.d(TAG, "Volley JSON post" + "That didn't work!");
                String message = null;
                if (volleyError instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (volleyError instanceof ServerError) {
                    message = "The server could not be found. Please try again after some time!!";
                } else if (volleyError instanceof AuthFailureError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (volleyError instanceof ParseError) {
                    message = "Parsing error! Please try again after some time!!";
                } else if (volleyError instanceof NoConnectionError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (volleyError instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection.";
                }
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                Utility.alert_msgtoEnd(NewsFeedActivity.this, message);
            }
        };
    }

    /***************
     *  Send an API request to retrieve appropriate paginated data
     *  Send the request including an offset value (i.e `itemCount`) as a query parameter.
     *  construct new model objects from the API response
     *  Append the new data objects to the existing set of items inside the array of items
     * @param itemCount
     */
    public void loadNextDataFromApi(int itemCount) {
        try {
            String URL = "";
            progressBar.setVisibility(View.VISIBLE);
            for (int i = increCounter; i < increCounter + itemCount; i++) {
                if (itemCount != jsonarray.length()) {
                    URL = HTTP_JSON_URL + "/" + mIDList.get(i) + ".json";
                 //   Log.d("TAGS", "" + URL + " ... " + increCounter);
                }
                mVolleyService = new VolleyService(resultCallback, this);
                mVolleyService.getDataVolley("GET_CALL_FOR_NEWS_FEED", URL);

            }
            increCounter += itemCount;

        } catch (Exception e) {
        }
    }

    /*********
     * This Method is used to parse the first response of ID request.
     * @param result
     */
    private void parseResult(String result) {
        try {
            jsonarray = new JSONArray(result);
            mIDList = new ArrayList<>();
            if (jsonarray != null) {
                for (int i = 0; i < jsonarray.length(); i++) {
                    mIDList.add(jsonarray.getString(i));
                }
            }
            loadNextDataFromApi(TIMES);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /***********
     * This Method is used to parse the response of Urls generated from IDs.
     * @param response
     */
    private void parseNewsFeed(String response) {
        try {
            Gson gson = new GsonBuilder().create();
            NewsFeed value = gson.fromJson(response, NewsFeed.class);
            if (mListNewsFeed == null) {
                mListNewsFeed = new ArrayList<>();
            }
            mListNewsFeed.add(value);
            Utility.delay(secs, new Utility.DelayCallback() {
                @Override
                public void afterDelay() {
                    mAdapter.RecyclerViewAdapter(mListNewsFeed, getApplicationContext(), NewsFeedActivity.this);
                    mAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    setLoaded();
                }
            });
        } catch (Exception e) {
        }

    }
    public void setLoaded() {
        loading = false;
    }

    @Override
    public void onBackPressed() {
      /*  finish();*/
        super.onBackPressed();
    }
}


