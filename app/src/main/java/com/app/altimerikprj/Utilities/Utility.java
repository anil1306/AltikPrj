package com.app.altimerikprj.Utilities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.provider.Settings;
import android.widget.Toast;
import com.app.altimerikprj.Activities.NewsFeedActivity;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by Anil on 12/3/2017.
 * This Class Stores General purpose methods / utilities
 */

public class Utility {
    private Context context;

    public Utility(Context context) {
        this.context = context;
    }

    /****
     * Used to display alert dialog
     * @param context
     * @param msg
     */
    public static void alert_msgtoEnd(final Context context, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("" + msg/*+ "\n"+"Press Yes to Restart"+ "\n"+"Press 'No' to see the score"*/);
        builder.setCancelable(true);
        builder.setNegativeButton(
                "Go To Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        ((Activity) context).startActivity(intent);
                        ((Activity) context).finish();
                    }
                });
        builder.setPositiveButton(
                "Close App",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent();
                        intent.setClass(context, NewsFeedActivity.class);
                        ((Activity) context).finish();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    public static void trustAllSSLConnections() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }

                        public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        }

                        public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        }
                    }
            };
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };
            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /*******
     * This method is used to put delay if Required
     * @param secs
     * @param delayCallback
     */
    public static void delay(int secs, final DelayCallback delayCallback) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                delayCallback.afterDelay();
            }
        }, secs * 1000); // afterDelay will be executed after (secs*1000) milliseconds.
    }

    /*Method to check the Internet Connection*/
    public Boolean CheckInternet() {
        ConnectivityManager cn = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cn.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected() == true) {
            return true;
        } else {
            Toast.makeText(context, "No internet connection.!", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public interface DelayCallback {
        void afterDelay();
    }


}
