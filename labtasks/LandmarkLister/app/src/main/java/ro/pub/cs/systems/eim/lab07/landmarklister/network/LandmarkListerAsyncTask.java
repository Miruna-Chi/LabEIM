package ro.pub.cs.systems.eim.lab07.landmarklister.network;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.ResponseHandler;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.BasicResponseHandler;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import ro.pub.cs.systems.eim.lab07.landmarklister.controller.LandmarkInformationAdapter;
import ro.pub.cs.systems.eim.lab07.landmarklister.general.Constants;
import ro.pub.cs.systems.eim.lab07.landmarklister.model.LandmarkInformation;

public class LandmarkListerAsyncTask extends AsyncTask<String, Void, List<LandmarkInformation>> {

    private ListView landmarkListView;

    public LandmarkListerAsyncTask(ListView landmarkListView) {
        this.landmarkListView = landmarkListView;
    }

    @Override
    protected List<LandmarkInformation> doInBackground(String... params) {

        ArrayList<LandmarkInformation> landMarkInformationList = new ArrayList<LandmarkInformation>();

        try {
            // TODO exercise 7
            // - create an instance of a HttpClient object
            HttpClient httpClient = new DefaultHttpClient();

            // - create the URL to the web service, appending the bounding box coordinates and the username to the base Internet address
            String url = Constants.LANDMARK_LISTER_WEB_SERVICE_INTERNET_ADDRESS
                    + Constants.NORTH + params[Constants.NORTH_INDEX] + "&"
                    + Constants.SOUTH + params[Constants.SOUTH_INDEX] + "&"
                    + Constants.EAST + params[Constants.EAST_INDEX] + "&"
                    + Constants.WEST + params[Constants.WEST_INDEX] + "&"
                    + Constants.CREDENTIALS;

            // - create an instance of a HttGet object
            HttpGet httpGet = new HttpGet(url);

            // - create an instance of a ReponseHandler object
            ResponseHandler<String> responseHandler = new BasicResponseHandler();

            // - execute the request, thus obtaining the response
            String content = httpClient.execute(httpGet, responseHandler);


            // - get the JSON object representing the response
            JSONObject result = new JSONObject(content);

            // - get the JSON array (the value corresponding to the "geonames" attribute)
            JSONArray jsonArray = result.getJSONArray(Constants.GEONAMES);

            // - iterate over the results list and create a LandmarkInformation for each element
            for (int k = 0; k < jsonArray.length(); k++) {
                JSONObject jsonObject = jsonArray.getJSONObject(k);
                landMarkInformationList.add(new LandmarkInformation(
                        jsonObject.getDouble(Constants.LATITUDE),
                        jsonObject.getDouble(Constants.LONGITUDE),
                        jsonObject.getString(Constants.TOPONYM_NAME),
                        jsonObject.getLong(Constants.POPULATION),
                        jsonObject.getString(Constants.CODE_NAME),
                        jsonObject.getString(Constants.NAME),
                        jsonObject.getString(Constants.WIKIPEDIA_WEB_PAGE_ADDRESS),
                        jsonObject.getString(Constants.COUNTRY_CODE)));
            }

        } catch (Exception exception) {
            Log.e(Constants.TAG, exception.getMessage());
            if (Constants.DEBUG) {
                exception.printStackTrace();
            }
        }
        return landMarkInformationList;
    }

    @Override
    protected void onPostExecute(List<LandmarkInformation> landmarkInformationList) {

        // TODO exercise 7
        // create a LandmarkInformationAdapter with the array and attach it to the landmarksListView
        LandmarkInformationAdapter landmarkInformationAdapter = new LandmarkInformationAdapter(
                landmarkListView.getContext(), landmarkInformationList);

        landmarkListView.setAdapter(landmarkInformationAdapter);
    }

}
