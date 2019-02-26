package com.example.david.databases;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.david.pojos.Quotation;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface QuotationDao {

    @Insert
    public void addQuotation(Quotation quotation);

    @Delete
    public void deleteQuotation(Quotation quotation);

    @Query("SELECT * FROM quotation_table")
    public List<Quotation> getAllQuotation();

    @Query("SELECT * FROM quotation_table WHERE quote = quotation")
    public Quotation getQuotation(String quotation);

    @Query("DELETE FROM quotation_table")
    public void deleteAllQuotation();
}
