package ro.pub.cs.systems.eim.lab07.googlesearcher.network;

import android.os.AsyncTask;
import android.util.Log;
import android.webkit.WebView;

import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.ResponseHandler;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.BasicResponseHandler;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import ro.pub.cs.systems.eim.lab07.googlesearcher.general.Constants;

public class GoogleSearcherAsyncTask extends AsyncTask<String, Void, String> {

    private WebView googleResultsWebView;

    public GoogleSearcherAsyncTask(WebView googleResultsWebView) {
        this.googleResultsWebView = googleResultsWebView;
    }

    @Override
    protected String doInBackground(String... params) {

        String result = "";
        // TODO exercise 6b)
        try {

            // create an instance of a HttpClient object
            HttpClient httpClient = new DefaultHttpClient();

            // create an instance of a HttpGet object, encapsulating the base Internet address (http://www.google.com) and the keyword
            HttpGet httpGet = new HttpGet(Constants.GOOGLE_INTERNET_ADDRESS + params[0]);

            // create an instance of a ResponseHandler object
            ResponseHandler<String> responseHandler = new BasicResponseHandler();

            // execute the request, thus generating the result
            result = httpClient.execute(httpGet, responseHandler);

        } catch (Exception exception) {
            Log.e(Constants.TAG, exception.getMessage());
            if (Constants.DEBUG) {
                exception.printStackTrace();
            }
        }

        return result;
    }

    @Override
    public void onPostExecute(String content) {

        // TODO exercise 6b)
        // display the result into the googleResultsWebView through loadDataWithBaseURL() method
        googleResultsWebView.loadDataWithBaseURL("http://www.google.com", content, "text/html", "UTF-8", null);

        // - base Internet address is http://www.google.com
        // - page source code is the response
        // - mimetype is text/html
        // - encoding is UTF-8
        // - history is null

    }
}
