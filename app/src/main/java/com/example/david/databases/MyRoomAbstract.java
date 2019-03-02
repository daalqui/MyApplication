package com.example.david.databases;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.david.pojos.Quotation;

@Database(version = 1, entities = {Quotation.class})
public abstract class MyRoomAbstract extends RoomDatabase {

    private static MyRoomAbstract myRoomAbstract;

    public synchronized static MyRoomAbstract getInstance(Context context){
        if(myRoomAbstract == null){
            myRoomAbstract = Room.databaseBuilder(context, MyRoomAbstract.class, "quotation_database").build();
        }
        return myRoomAbstract;
    }

    public boolean quotationExistInBD(Quotation quotation){
        return quotationDao().getQuotation(quotation.getQuoteText()) != null;
    }

    public abstract QuotationDao quotationDao();
}
