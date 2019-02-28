package com.example.david.tasks;

import android.os.AsyncTask;

import com.example.david.databases.MyRoomAbstract;
import com.example.david.databases.MySQLiteOpenHelper;
import com.example.david.myapplication.FavouriteActivity;
import com.example.david.pojos.Quotation;

import java.lang.ref.WeakReference;
import java.util.List;

public class MyAsynTask extends AsyncTask<Boolean,Void,List<Quotation>> {

    private WeakReference<FavouriteActivity> favouriteActivityWeakReference;
    private List<Quotation> quotationList;

    public MyAsynTask(FavouriteActivity activity) {
        this.favouriteActivityWeakReference = new WeakReference<>(activity);
    }

    @Override
    protected List<Quotation> doInBackground(Boolean... booleans) {

        if (booleans[0])quotationList = MyRoomAbstract.getInstance(favouriteActivityWeakReference.get()).quotationDao().getAllQuotation();
        else quotationList = MySQLiteOpenHelper.getInstance(favouriteActivityWeakReference.get()).getQuotations();

    return quotationList;
    }

    @Override
    protected void onPostExecute(List<Quotation> quotationList) {
        favouriteActivityWeakReference.get().listManager(quotationList);
    }


}
