package com.app.altimerikprj.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.app.altimerikprj.R;

/**
 * Created by Anil on 12/3/2017.
 */
public class DisplayNewsActivity extends Activity {

    public WebView webView;
    int position;
    String recievedNewsURL = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_display_activity);
        Bundle bundle = this.getIntent().getExtras();
        position = bundle.getInt("POSITION");
        recievedNewsURL = bundle.getString("NEWSURL");
        webView = (WebView) findViewById(R.id.webView_news);
        webView.loadUrl(recievedNewsURL);
        myWebClient webViewClient = new myWebClient(this, recievedNewsURL);
        webView.setWebViewClient(webViewClient);
        setContentView(webView);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Exit!")
                    .setMessage("Are you sure you want to close?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }

                    })
                    .setNegativeButton("No", null)
                    .show();
        }
    }
}

class myWebClient extends WebViewClient {
    Context context;
    String URL1;
    ProgressDialog progressDialog;

    public myWebClient(Context context, String URL) {
        this.context = context;
        URL1 = URL;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        return false;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Loading...");
            progressDialog.setIndeterminate(true);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }
        super.onPageStarted(view, url, favicon);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        // view.getTitle();
        try {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
                progressDialog = null;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        Toast.makeText(context, "Please provide a valid URL", Toast.LENGTH_LONG).show();
    }

}


