package com.example.david.myapplication;

import android.content.SharedPreferences;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.david.databases.MyRoomAbstract;
import com.example.david.databases.MySQLiteOpenHelper;
import com.example.david.pojos.Quotation;
import android.os.Handler;


public class QuotationActivity extends AppCompatActivity {

    private int numCitasRecibidas = 0;
    private String sAuthor, sQuotation;
    Menu menu;
    MenuItem menu_add;
    TextView tQuotation, tAuthor;
    boolean addVisible;
    private MySQLiteOpenHelper db;
    private MyRoomAbstract room;
    private SharedPreferences preferences;
    android.os.Handler handler;

    private String dbOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //
        handler = new Handler();

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        // guardo la opción de base de datos seleccionada en settings.
        dbOption = preferences.getString("database", "0");

        setContentView(R.layout.activity_quotation);
        tQuotation = findViewById(R.id.textView4);
        tAuthor = findViewById(R.id.textView3);

        //instancia de objeto SQLite para operaciones a la bd
        db = MySQLiteOpenHelper.getInstance(this);
        // instancia de objeto room para operaciones a la bd.
        room = MyRoomAbstract.getInstance(this);

        if(savedInstanceState != null){
            numCitasRecibidas = savedInstanceState.getInt("numCitasRecibidas");
            tQuotation.setText(savedInstanceState.getString("quotation"));
            tAuthor.setText(savedInstanceState.getString("author"));
            // falta hacer que la opción add sea visible.
            addVisible = savedInstanceState.getBoolean("addVisible");

        }
        else {

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
        menu_add = menu.findItem(R.id.menu_add);
        return true;
    }



    // Callback que ejecuta las opciones del toolbar
    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        switch (item.getItemId()){
            // añade una cita a favoritos
            case R.id.menu_add:

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Quotation q = new Quotation();
                        q.setQuoteAuthor(tAuthor.getText().toString());
                        q.setQuoteText(tQuotation.getText().toString());

                        // guardo una cita de un modo u otro.
                        if (dbOption.matches("0")) room.quotationDao().addQuotation(q);
                        else db.addQuotation(q);

                    }
                }).start();
                // desactivo la opcion add despues del primer uso.
                item.setVisible(false);
              return true;
              //refresca la información del view
            case R.id.menu_refresh:

                // Actualiza las citas
                sQuotation = getResources().getString(R.string.sample_quotation).replaceAll("%1d", String.valueOf(numCitasRecibidas));
                sAuthor = getResources().getString(R.string.sample_author).replaceAll("%1d", String.valueOf(numCitasRecibidas));
                numCitasRecibidas += 1;
                tQuotation.setText(sQuotation);
                tAuthor.setText(sAuthor);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // Si una cita no existe en la BD hago visible la opción add
                        if(!db.quotationExistInBD(sQuotation)){
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    // hacemos visible el menu add.
                                   menu_add.setVisible(true);
                                }
                            });

                        }
                    }
                }).start();



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
