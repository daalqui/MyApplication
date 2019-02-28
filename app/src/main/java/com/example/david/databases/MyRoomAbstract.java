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
            myRoomAbstract = Room.databaseBuilder(context, MyRoomAbstract.class, "quotation_table").build();
        }
        return myRoomAbstract;
    }

    public abstract QuotationDao quotationDao();
}
