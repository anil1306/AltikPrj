package com.app.altimerikprj.Utilities;

import com.android.volley.VolleyError;

/**
 * Created by Anil on 12/3/2017.
 */
//callback interface to get the result back in activity
public interface InterfaceResult {
    public void notifySuccess(String requestType, String response);
    public void notifyError(String requestType, VolleyError error);
}
