package com.example.david.tasks;

import android.os.AsyncTask;

import com.example.david.myapplication.QuotationActivity;
import com.example.david.pojos.Quotation;

import java.lang.ref.WeakReference;

public class HTTPAsyncTask extends AsyncTask<Void, Void, Quotation> {

    WeakReference<QuotationActivity> quotationActivityWeakReference;


    public HTTPAsyncTask(QuotationActivity quotationActivity) {
        this.quotationActivityWeakReference = new WeakReference<>(quotationActivity);
    }

    @Override
    protected Quotation doInBackground(Void... voids) {
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        quotationActivityWeakReference.get().chargeProgressBar();
    }

    @Override
    protected void onPostExecute(Quotation quotation) {
        super.onPostExecute(quotation);
        Quotation q = new Quotation();
        q.setQuoteText("quote asyntask");
        q.setQuoteAuthor("author asyntask");
        quotationActivityWeakReference.get().refreshQuotation(q);
    }
}
