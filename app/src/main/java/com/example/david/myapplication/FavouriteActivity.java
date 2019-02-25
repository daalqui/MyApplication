package com.example.david.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.david.Servicio.ArrayAdapterImplementation;
import com.example.david.databases.MySQLiteOpenHelper;
import com.example.david.pojos.Quotation;

import java.net.URLEncoder;
import java.util.ArrayList;

public class FavouriteActivity extends AppCompatActivity {

    private  ArrayList<Quotation> lista;
    private  ArrayAdapterImplementation adapterList;
    private MySQLiteOpenHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

        // metodo de la práctica 2B --> sustituido por acceso a la BD.
        //lista = getMockQuotations();
        db = MySQLiteOpenHelper.getInstance(this);
        lista = db.getQuotations();

        adapterList = new ArrayAdapterImplementation(this, R.layout.quotation_list_row,lista);

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
        vista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder alert = new AlertDialog.Builder(FavouriteActivity.this);
                alert.setMessage(R.string.delete_quotation);
                alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // elimino la quotation de la bd
                        db.deleteQuotation(lista.get(position));
                        // elimino la quotation de la interfaz
                        lista.remove(position);
                        adapterList.notifyDataSetChanged();
                    }
                });
                alert.setNegativeButton(android.R.string.no,null);
                alert.create().show();
                return true;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar_favourite_activity,menu);
        if(lista.isEmpty()){
            menu.getItem(R.id.menu_delete).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       switch (item.getItemId()){
           case R.id.menu_delete:
               AlertDialog.Builder alert = new AlertDialog.Builder(this);
               alert.setMessage(R.string.delete_all_favourite);
               alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                      // borro la interfaz
                       lista.clear();
                       // borro la base de datos
                       db.deleteAllQuotation();
                       adapterList.notifyDataSetChanged();
                   }

               });
               alert.create().show();
               item.setVisible(false);
               return true;
       }
        return super.onOptionsItemSelected(item);
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
