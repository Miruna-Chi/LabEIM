package ro.pub.cs.systems.eim.lab07.calculatorwebservice.network;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.ResponseHandler;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.BasicResponseHandler;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.protocol.HTTP;
import cz.msebera.android.httpclient.util.EntityUtils;
import ro.pub.cs.systems.eim.lab07.calculatorwebservice.general.Constants;

public class CalculatorWebServiceAsyncTask extends AsyncTask<String, Void, String> {

    private TextView resultTextView;

    public CalculatorWebServiceAsyncTask(TextView resultTextView) {
        this.resultTextView = resultTextView;
    }

    @Override
    protected String doInBackground(String... params) {


        // TODO exercise 4
        // signal missing values through error messages
        if (params[0].equals("") || params[1].equals("")) {
            return "At least one operator is missing.\n";
        }

        Float operator1 = Float.parseFloat(params[0]);
        Float operator2 = Float.parseFloat(params[1]);
        String operation = params[2];
        Integer method = Integer.parseInt(params[3]);

        String result = "";
        try {
            // create an instance of a HttpClient object
            HttpClient httpClient = new DefaultHttpClient();

            // get method used for sending request from methodsSpinner
            Log.i(Constants.TAG, result);
            if (method == 0) {
                // 1. GET
                // a) build the URL into a HttpGet object (append the operators / operations to the Internet address)
                HttpGet httpGet = new HttpGet(Constants.GET_WEB_SERVICE_ADDRESS
                        + "?" + Constants.OPERATION_ATTRIBUTE + "=" + operation
                        + "&" + Constants.OPERATOR1_ATTRIBUTE + "=" + operator1.toString()
                        + "&" + Constants.OPERATOR2_ATTRIBUTE + "=" + operator2.toString());

                // b) create an instance of a ResponseHandler object
                ResponseHandler<String> responseHandler = new BasicResponseHandler();

                // c) execute the request, thus generating the result
                String content = httpClient.execute(httpGet, responseHandler);
                result += "\n" + content;
            }
            else if(method == 1) {
                // 2. POST
                // a) build the URL into a HttpPost object
                HttpPost httpPost = new HttpPost(Constants.POST_WEB_SERVICE_ADDRESS);

                // b) create a list of NameValuePair objects containing the attributes and their values (operators, operation)
                List<NameValuePair> post_params = new ArrayList<NameValuePair>();
                post_params.add(new BasicNameValuePair(Constants.OPERATION_ATTRIBUTE, operation));
                post_params.add(new BasicNameValuePair(Constants.OPERATOR1_ATTRIBUTE, operator1.toString()));
                post_params.add(new BasicNameValuePair(Constants.OPERATOR2_ATTRIBUTE, operator2.toString()));

                // c) create an instance of a UrlEncodedFormEntity object using the list and UTF-8 encoding and attach it to the post request
                UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(post_params, HTTP.UTF_8);
                httpPost.setEntity(urlEncodedFormEntity);

                // d) create an instance of a ResponseHandler object
                ResponseHandler<String> responseHandler = new BasicResponseHandler();

                // e) execute the request, thus generating the result
                String content = httpClient.execute(httpPost, responseHandler);
                result += "\n" + content;
            }
        } catch (Exception e) {
            Log.e(Constants.TAG, e.getMessage());
            if (Constants.DEBUG) {
                e.printStackTrace();
            }
        }

        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        // display the result in resultTextView
        resultTextView.setText(result);

    }

}
