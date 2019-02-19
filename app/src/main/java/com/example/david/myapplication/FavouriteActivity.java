package com.example.david.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.david.Servicio.ArrayAdapterImplementation;
import com.example.david.pojos.Quotation;

import java.net.URLEncoder;
import java.util.ArrayList;

public class FavouriteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);
        ArrayList<Quotation> lista = getMockQuotations();
        ArrayAdapterImplementation adapterList = new ArrayAdapterImplementation(this, R.layout.quotation_list_row,lista);

        ListView vista = findViewById(R.id.listviewCitas);
        vista.setAdapter(adapterList);

        vista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                String author = URLEncoder.encode(((TextView)view.findViewById(R.id.textViewAuthor)).getText().toString());


                if (author.equals("") || author == null)
                Toast.makeText(FavouriteActivity.this, " No es posible obtener información " ,Toast.LENGTH_SHORT).show();
                else {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("https://en.wikipedia.org/wiki/Special:Search?search=" + "author"));
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    }
                }



            }
        });
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
        ArrayList<Quotation> listaQ = new ArrayList<>();
        for (int i = 1; i <= 10; i++){
            Quotation q = new Quotation();
            if (i == 4) {
                q.setQuoteAuthor("");
                q.setQuoteText("cita: " + i);
                listaQ.add(q);
            }
            else {
                q.setQuoteAuthor("David" + i);
                q.setQuoteText("cita: " + i);
                listaQ.add(q);
            }
        }

        return listaQ;
    }

}
