package ro.pub.cs.systems.eim.lab07.xkcdcartoondisplayer.network;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.ResponseHandler;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.BasicResponseHandler;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import ro.pub.cs.systems.eim.lab07.xkcdcartoondisplayer.entities.XKCDCartoonInformation;
import ro.pub.cs.systems.eim.lab07.xkcdcartoondisplayer.general.Constants;

public class XKCDCartoonDisplayerAsyncTask extends AsyncTask<String, Void, XKCDCartoonInformation> {

    private TextView xkcdCartoonTitleTextView;
    private ImageView xkcdCartoonImageView;
    private TextView xkcdCartoonUrlTextView;
    private Button previousButton, nextButton;

    private class XKCDCartoonButtonClickListener implements Button.OnClickListener {

        private String xkcdComicUrl;

        public XKCDCartoonButtonClickListener(String xkcdComicUrl) {
            this.xkcdComicUrl = xkcdComicUrl;
        }

        @Override
        public void onClick(View view) {
            new XKCDCartoonDisplayerAsyncTask(xkcdCartoonTitleTextView, xkcdCartoonImageView, xkcdCartoonUrlTextView, previousButton, nextButton).execute(xkcdComicUrl);

        }

    }

    public XKCDCartoonDisplayerAsyncTask(TextView xkcdCartoonTitleTextView, ImageView xkcdCartoonImageView, TextView xkcdCartoonUrlTextView, Button previousButton, Button nextButton) {
        this.xkcdCartoonTitleTextView = xkcdCartoonTitleTextView;
        this.xkcdCartoonImageView = xkcdCartoonImageView;
        this.xkcdCartoonUrlTextView = xkcdCartoonUrlTextView;
        this.previousButton = previousButton;
        this.nextButton = nextButton;
    }

    @Override
    public XKCDCartoonInformation doInBackground(String... urls) {
        XKCDCartoonInformation xkcdCartoonInformation = new XKCDCartoonInformation();


        try {
            // TODO exercise 5a)
            // 1. obtain the content of the web page (whose Internet address is stored in urls[0])
            // - create an instance of a HttpClient object

            HttpClient httpClient = new DefaultHttpClient();

            // - create an instance of a HttpGet object
            HttpGet httpGet = new HttpGet(urls[0]);

            // - create an instance of a ResponseHandler object
            ResponseHandler<String> responseHandler = new BasicResponseHandler();

            // - execute the request, thus obtaining the web page source code
            String pageSourceCode = httpClient.execute(httpGet, responseHandler);



            // 2. parse the web page source code
            Document document = Jsoup.parse(pageSourceCode);
            Element htmlTag = document.child(0);

            // - cartoon title: get the tag whose id equals "ctitle"
            Element divTagIdCtitle = htmlTag.getElementsByAttributeValue(Constants.ID_ATTRIBUTE,
                    Constants.CTITLE_VALUE).first();
            xkcdCartoonInformation.setCartoonTitle(divTagIdCtitle.ownText());



            // - cartoon url
            //   * get the first tag whose id equals "comic"
            Element divTagIdComic = htmlTag.getElementsByAttributeValue(Constants.ID_ATTRIBUTE,
                    Constants.COMIC_VALUE).first();

            //   * get the embedded <img> tag
            //   * get the value of the attribute "src"
            String cartoonInternetAddress = divTagIdComic.getElementsByTag(Constants.IMG_TAG).attr(Constants.SRC_ATTRIBUTE);

            //   * prepend the protocol: "http:"
            String cartoonUrl = Constants.HTTP_PROTOCOL + cartoonInternetAddress;
            xkcdCartoonInformation.setCartoonUrl(cartoonUrl);



            // - cartoon bitmap (only if using Apache HTTP Components)

            //   * create the HttpGet object
            HttpGet httpImageGet = new HttpGet(cartoonUrl);

            //   * execute the request and obtain the HttpResponse object
            HttpResponse httpGetResponse = httpClient.execute(httpImageGet);

            //   * get the HttpEntity object from the response
            HttpEntity httpEntity = httpGetResponse.getEntity();

            //   * get the bitmap from the HttpEntity stream (obtained by getContent()) using Bitmap.decodeStream() method
            if (httpEntity != null) {
                xkcdCartoonInformation.setCartoonBitmap(BitmapFactory.decodeStream(httpEntity.getContent()));
            }


            // - previous cartoon address

            //   * get the first tag whose rel attribute equals "prev"
            Element divTagRelPrev = htmlTag.getElementsByAttributeValue(Constants.REL_ATTRIBUTE,
                    Constants.PREVIOUS_VALUE).first();

            //   * get the href attribute of the tag
            String hrefAttr = divTagRelPrev.attr(Constants.HREF_ATTRIBUTE);

            //   * prepend the value with the base url: http://www.xkcd.com
            xkcdCartoonInformation.setPreviousCartoonUrl(Constants.XKCD_INTERNET_ADDRESS + hrefAttr);

            //   * attach the previous button a click listener with the address attached
            //   this has to be done on the UI Thread => onPostExecute()


            // - next cartoon address
            //   * get the first tag whole rel attribute equals "next"
            Element divTagRelNext = htmlTag.getElementsByAttributeValue(Constants.REL_ATTRIBUTE,
                    Constants.NEXT_VALUE).first();

            //   * get the href attribute of the tag
            hrefAttr = divTagRelNext.attr(Constants.HREF_ATTRIBUTE);

            //   * prepend the value with the base url: http://www.xkcd.com
            xkcdCartoonInformation.setNextCartoonUrl(Constants.XKCD_INTERNET_ADDRESS + hrefAttr);

            //   * attach the next button a click listener with the address attached


        } catch (Exception exception) {
            Log.e(Constants.TAG, exception.getMessage());
            if (Constants.DEBUG) {
                exception.printStackTrace();
            }
        }

        return  xkcdCartoonInformation;
    }

    @Override
    protected void onPostExecute(final XKCDCartoonInformation xkcdCartoonInformation) {

        if (xkcdCartoonInformation != null) {
            // TODO exercise 5b)
            // map each member of xkcdCartoonInformation object to the corresponding widget
            // cartoonTitle -> xkcdCartoonTitleTextView
            xkcdCartoonTitleTextView.setText(xkcdCartoonInformation.getCartoonTitle());

            // cartoonBitmap -> xkcdCartoonImageView (only if using Apache HTTP Components)
            xkcdCartoonImageView.setImageBitmap(xkcdCartoonInformation.getCartoonBitmap());

            // cartoonUrl -> xkcdCartoonUrlTextView
            xkcdCartoonUrlTextView.setText(xkcdCartoonInformation.getCartoonUrl());

            // based on cartoonUrl fetch the bitmap
            // and put it into xkcdCartoonImageView

            Log.d(Constants.TAG, xkcdCartoonInformation.getPreviousCartoonUrl());
            Log.d(Constants.TAG, xkcdCartoonInformation.getNextCartoonUrl());
            // previousCartoonUrl, nextCartoonUrl -> set the XKCDCartoonUrlButtonClickListener for previousButton, nextButton
            previousButton.setOnClickListener(new XKCDCartoonButtonClickListener(xkcdCartoonInformation.getPreviousCartoonUrl()));
            nextButton.setOnClickListener(new XKCDCartoonButtonClickListener(xkcdCartoonInformation.getNextCartoonUrl()));

        }
    }

}
