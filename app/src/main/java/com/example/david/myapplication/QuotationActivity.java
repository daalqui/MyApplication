package com.example.david.myapplication;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.david.databases.MySQLiteOpenHelper;
import com.example.david.pojos.Quotation;

public class QuotationActivity extends AppCompatActivity {

    private int numCitasRecibidas = 0;
    Menu menu;
    TextView tQuotation, tAuthor;
    boolean addVisible = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotation);
        tQuotation = findViewById(R.id.textView4);
        tAuthor = findViewById(R.id.textView3);


        if(savedInstanceState != null){
            numCitasRecibidas = savedInstanceState.getInt("numCitasRecibidas");
            tQuotation.setText(savedInstanceState.getString("quotation"));
            tAuthor.setText(savedInstanceState.getString("author"));
            // falta hacer que la opción add sea visible.
            addVisible = savedInstanceState.getBoolean("addVisible");

        }
        else {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            String textQuotation = tQuotation.getText().toString();
            String user = preferences.getString("username", "");
            if (user.matches("")) {
                textQuotation = textQuotation.replaceAll("%1s", "nameless One");
            } else {
                textQuotation = textQuotation.replaceAll("%1s", preferences.getString("username", ""));

            }

            tQuotation.setText(textQuotation);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar_quotation_activity,menu);
        this.menu = menu;
        return true;
    }



    // Callback que ejecuta las opciones del toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //instancia de la BD
        MySQLiteOpenHelper bd = MySQLiteOpenHelper.getInstance(this);

        switch (item.getItemId()){
            // añade una cita a favoritos (por completar)
            case R.id.menu_add:
                Quotation q = new Quotation();
                q.setQuoteAuthor(tAuthor.getText().toString());
                q.setQuoteText(tQuotation.getText().toString());
                bd.addQuotation(q);
                // desactivo la opcion add despues del primer uso.
                item.setVisible(false);
              return true;
              //refresca la información del view
            case R.id.menu_refresh:

                // Actualiza las citas
                String quotation = getResources().getString(R.string.sample_quotation).replaceAll("%1d", String.valueOf(numCitasRecibidas));
                tQuotation.setText(quotation);
                String author = getResources().getString(R.string.sample_author).replaceAll("%1d", String.valueOf(numCitasRecibidas));
                tAuthor.setText(author);
                numCitasRecibidas += 1;

                // Si una cita no existe en la BD hago visible la opción add
                if(!bd.quotationExistInBD(quotation)){
                    menu.findItem(R.id.menu_add).setVisible(addVisible);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Metodo llamado cuando la actividad va a ser destruida.
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("numCitasRecibidas", numCitasRecibidas);
        outState.putString("quotation", tQuotation.getText().toString());
        outState.putString("author", tAuthor.getText().toString());
        outState.putBoolean("addVisible",menu.findItem(R.id.menu_add).isVisible());
    }
}
