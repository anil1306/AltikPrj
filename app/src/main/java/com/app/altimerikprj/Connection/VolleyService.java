package com.app.altimerikprj.Connection;
import android.content.Context;
import android.util.Log;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.altimerikprj.Utilities.InterfaceResult;
/**
 * Created by Anil on 12/3/2017.
 */
public class VolleyService {
    private InterfaceResult mResultCallback = null;
    private final Context mContext;
    private RequestQueue requestQueue;

    public VolleyService(InterfaceResult resultCallback, Context context) {
        mResultCallback = resultCallback;
        mContext = context;
    }
    /*********
     * GET call to server to recieve JSON response
     * @param requestType
     * @param url
     */
    public void getDataVolley(final String requestType, String url) {
        try {
            final StringRequest jsonStrObj = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @SuppressWarnings("FinalizeCalledExplicitly")
                @Override
                public void onResponse(String response) {
                    Log.d("CLICK", "response: " + response);
                    if (mResultCallback != null)
                        mResultCallback.notifySuccess(requestType, response);
                    try {
                        finalize();
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (mResultCallback != null) {
                        mResultCallback.notifyError(requestType, error);
                    }
                }
            });
            jsonStrObj.setShouldCache(false);
            jsonStrObj.setRetryPolicy(
                    new DefaultRetryPolicy(
                            20000,//time to wait for it in this case 20s
                            20,//tryies in case of error
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                    )
            );
            if (requestQueue == null) {
                requestQueue = Volley.newRequestQueue(mContext);
            }
            requestQueue.add(jsonStrObj);
        } catch (Exception ignored) {

        }
    }
}
