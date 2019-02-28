package com.example.david.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
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
import com.example.david.databases.MyRoomAbstract;
import com.example.david.databases.MySQLiteOpenHelper;
import com.example.david.pojos.Quotation;
import com.example.david.tasks.MyAsynTask;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class FavouriteActivity extends AppCompatActivity {

    private  ArrayList<Quotation> lista = new ArrayList<>();
    private Menu menu;
    private  ArrayAdapterImplementation adapterList;
    private ListView vista;
    private MySQLiteOpenHelper db;
    private MyRoomAbstract room;
    private SharedPreferences preferences;
    private String dbOption;
    MenuItem menu_delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);


        // instancias de objetos para realizar operaciones a la BD
        db = MySQLiteOpenHelper.getInstance(this);
        room = MyRoomAbstract.getInstance(this);

        // accedo al archivo de configuración
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        // guardo la opción de base de datos seleccionada en settings.
        dbOption = preferences.getString("database", "0");

        lista = new ArrayList<>();
        adapterList = new ArrayAdapterImplementation(this, R.layout.quotation_list_row,lista);
        vista = findViewById(R.id.listviewCitas);
        vista.setAdapter(adapterList);

        MyAsynTask asynTask = new MyAsynTask(this);
        asynTask.execute(dbOption.matches("0"));

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
                alert.setMessage(R.string.delete_quotation );
                alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                Quotation quotationDeleted = lista.get(position);
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                                // elimino la quotation de la bd de un modo u otro
                                if(dbOption.matches("0"))room.quotationDao().deleteQuotation(quotationDeleted);
                                else db.deleteQuotation(quotationDeleted);
                            }
                        }).start();
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

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar_favourite_activity,menu);
        this.menu = menu;
        menu_delete = menu.findItem(R.id.menu_delete);
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

                       new Thread(new Runnable() {
                           @Override
                           public void run() {
                               // borro la base de datos de un modo u otro
                               if (dbOption.matches("0")) room.quotationDao().deleteAllQuotation();
                               else db.deleteAllQuotation();
                           }
                       }).start();
                       // borro la interfaz
                       lista.clear();
                       adapterList.notifyDataSetChanged();
                   }

               });
               alert.create().show();
               item.setVisible(false);
               return true;
       }
        return super.onOptionsItemSelected(item);
    }
    public void listManager(List<Quotation> quotationList){
        adapterList.addAll(quotationList);
        vista.setAdapter(adapterList);


    }


}
