package com.example.david.tasks;

import android.net.Uri;
import android.os.AsyncTask;

import com.example.david.myapplication.QuotationActivity;
import com.example.david.pojos.Quotation;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HTTPAsyncTask extends AsyncTask<String, Void, Quotation> {

    WeakReference<QuotationActivity> quotationActivityWeakReference;


    public HTTPAsyncTask(QuotationActivity quotationActivity) {
        this.quotationActivityWeakReference = new WeakReference<>(quotationActivity);
    }

    @Override
    protected Quotation doInBackground(String...params) {
        String lang = "en";
        if (params[0].matches("1")) lang = "ru";
        Quotation quotation = new Quotation();
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https");
        builder.authority("api.forismatic.com");
        builder.appendPath("api");
        builder.appendPath("1.0");
        builder.appendPath("");
        builder.appendQueryParameter("method", "getQuote");
        builder.appendQueryParameter("format","json");
        builder.appendQueryParameter("lang",lang);

            try {
                URL url = new URL(builder.build().toString());
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET") ;
                connection.setDoInput(true);
                // Get response
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    quotation = new Gson().fromJson(reader, Quotation.class);
                    reader.close();
                }
                connection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }



        return quotation;
    }

    @Override
    protected void onPreExecute() {
        quotationActivityWeakReference.get().chargeProgressBar();
    }

    @Override
    protected void onPostExecute(Quotation quotation) {
        quotationActivityWeakReference.get().refreshQuotation(quotation);
    }
}
