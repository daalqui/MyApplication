package com.example.david.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.david.pojos.Quotation;

import java.util.ArrayList;
import java.util.List;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {

    private static MySQLiteOpenHelper mySQLiteOpenHelper;

    private MySQLiteOpenHelper(Context contexto) {
        super(contexto, "quotation_database", null, 1);
    }

    public synchronized static MySQLiteOpenHelper getInstance(Context context) {
        if (mySQLiteOpenHelper == null) {
            mySQLiteOpenHelper = new MySQLiteOpenHelper(context);
        }
        return mySQLiteOpenHelper;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db = getWritableDatabase();
            db.execSQL("CREATE   TABLE   quotation_table   (id   INTEGER   PRIMARY   KEY AUTOINCREMENT, quote TEXT NOT NULL, author TEXT, UNIQUE(quote));");
            db.close();
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public ArrayList<Quotation> getQuotations() {
        ArrayList<Quotation> quotationList = new ArrayList<>();

        //Accedo a la bd
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("quotation_table", new String[]{"quote", "author"}, null, null, null, null, null);

        // por cada fila de la tabla creo un quotation y lo añado a la lista
        while (cursor.moveToNext()) {
            Quotation quotation = new Quotation();
            quotation.setQuoteText(cursor.getString(0));
            quotation.setQuoteAuthor(cursor.getString(1));
            quotationList.add(quotation);
        }
        cursor.close();
        db.close();
        return quotationList;
    }

    // Compruebo si una quotation ya eiste en la BD
    public boolean quotationExistInBD(String quotation) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("quotation_table", null, "quote=?", new String[]{quotation}, null, null, null, null);
        cursor.close();
        db.close();
        return cursor.getCount() > 0;
    }

    // inserto una quotation en la BD
    public void addQuotation(Quotation quotation){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("quote",quotation.getQuoteText());
        values.put("author",quotation.getQuoteAuthor());
        db.insert("quotation_table", null, values);
        db.close();
    }

    public void deleteAllQuotation(){
        SQLiteDatabase db = getWritableDatabase();
        db.delete("quotation_table",null,null);
        db.close();
    }
}
