package com.example.david.myapplication;

import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.david.databases.MyRoomAbstract;
import com.example.david.databases.MySQLiteOpenHelper;
import com.example.david.pojos.Quotation;
import com.example.david.tasks.HTTPAsyncTask;

import android.os.Handler;
import android.widget.Toast;


public class QuotationActivity extends AppCompatActivity {

    Menu menu;
    MenuItem menu_add, menu_refresh;
    TextView tQuotation, tAuthor;
    boolean addVisible;
    private MySQLiteOpenHelper db;
    private MyRoomAbstract room;
    private SharedPreferences preferences;
    Handler handler;
    ProgressBar progressBar;

    private String dbOption;

    // callback que se ejecuta al crearse la actividad
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

    // callback que carga el toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar_quotation_activity,menu);
        this.menu = menu;
        menu_add = menu.findItem(R.id.menu_add);
        menu_refresh = menu.findItem(R.id.menu_refresh);
        return true;
    }



    // Callback que ejecuta las opciones del toolbar
    @RequiresApi(api = Build.VERSION_CODES.M)
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

                if (isNetworkConnected()){
                    //acceso al servicio web en segundo plano
                    HTTPAsyncTask httpAsyncTask = new HTTPAsyncTask(this);
                    httpAsyncTask.execute(preferences.getString("language", "0"));
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Metodo llamado cuando la actividad va a ser destruida.
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("quotation", tQuotation.getText().toString());
        outState.putString("author", tAuthor.getText().toString());
        outState.putBoolean("addVisible",menu.findItem(R.id.menu_add).isVisible());
    }

    // Método que oculta el actionBar mientras carga el progressBar.
    public void chargeProgressBar(){

        //no es necesario el casting a ProgressBar.
        progressBar =  findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE); //nose si funciona con este VIEW.
        menu_add.setVisible(false);
        menu_refresh.setVisible(false);
    }

    // Metodo que actualiza las etiquetas de cita y autor.
    public void refreshQuotation(final Quotation quotation){
        //actualizo las etiquetas
        tQuotation.setText(quotation.getQuoteText());
        tAuthor.setText(quotation.getQuoteAuthor());
        //hago invisible le progressbar y visible el reresh
        progressBar.setVisibility(View.INVISIBLE);
        menu_refresh.setVisible(true);

        // compruebo si una cita de la web ya está en la db
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Si una cita no existe en la BD hago visible la opción add
                if(!db.quotationExistInBD(quotation.getQuoteText())){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            // hacemos visible los menus
                            menu_add.setVisible(true);

                        }
                    });

                }
            }
        }).start();
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean isNetworkConnected(){
        ConnectivityManager manager = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if(networkInfo == null || !networkInfo.isConnected()) return false;// no tenemos acceso a internet.
    return true;
    }
}
