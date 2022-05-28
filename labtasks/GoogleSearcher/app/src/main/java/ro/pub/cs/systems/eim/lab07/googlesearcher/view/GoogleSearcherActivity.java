package ro.pub.cs.systems.eim.lab07.googlesearcher.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ro.pub.cs.systems.eim.lab07.googlesearcher.R;
import ro.pub.cs.systems.eim.lab07.googlesearcher.general.Constants;
import ro.pub.cs.systems.eim.lab07.googlesearcher.network.GoogleSearcherAsyncTask;

public class GoogleSearcherActivity extends AppCompatActivity {

    private EditText keywordEditText;
    private WebView googleResultsWebView;
    private Button searchGoogleButton;

    private SearchGoogleButtonClickListener searchGoogleButtonClickListener = new SearchGoogleButtonClickListener();
    private class SearchGoogleButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            // TODO exercise 6a)
            // obtain the keyword from keywordEditText
            String key_sentence = keywordEditText.getText().toString();

            // signal an empty keyword through an error message displayed in a Toast window
            if (key_sentence == null || key_sentence.isEmpty())
                Toast.makeText(getApplicationContext(), Constants.EMPTY_KEYWORD_ERROR_MESSAGE, Toast.LENGTH_LONG).show();

            // split a multiple word (separated by space) keyword and link them through +
            // prepend the keyword with "search?q=" string
            String[] arrOfStr = key_sentence.split(" ");
            String search_format = Constants.SEARCH_PREFIX;

            for (String a : arrOfStr) {
                search_format += a + "+";
            }
            search_format = search_format.substring(0, search_format.length() - 1);

            // start the GoogleSearcherAsyncTask passing the keyword
            new GoogleSearcherAsyncTask(googleResultsWebView).execute(search_format);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_searcher);

        keywordEditText = (EditText)findViewById(R.id.keyword_edit_text);
        googleResultsWebView = (WebView)findViewById(R.id.google_results_web_view);

        googleResultsWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });

        searchGoogleButton = (Button)findViewById(R.id.search_google_button);
        searchGoogleButton.setOnClickListener(searchGoogleButtonClickListener);
    }
}
