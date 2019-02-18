package com.example.david.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.david.Servicio.ArrayAdapterImplementation;
import com.example.david.pojos.Quotation;

import java.util.ArrayList;

public class FavouriteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_favourite);
        ArrayAdapterImplementation adapterList = new ArrayAdapterImplementation(this, R.layout.activity_favourite,getMockQuotations());

        ListView vista = findViewById(R.id.listviewCitas);
        vista.setAdapter(adapterList);
        /* método onclick practica 2A
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://en.wikipedia.org/wiki/Special:Search?search=" + "Albert Einstein"));

            if(intent.resolveActivity(getPackageManager()) != null){
                startActivity(intent);
            }

            }

        });*/
    }
    public ArrayList<Quotation> getMockQuotations(){
        ArrayList<Quotation> listaQ = new ArrayList<Quotation>(10);
        for (int i = 0; i < 10; i++){
            Quotation q = new Quotation();
            q.setQuoteAuthor("David");
            q.setQuoteText("cita: " + i);
            listaQ.set(i, q);
        }

        return listaQ;
    }

}
