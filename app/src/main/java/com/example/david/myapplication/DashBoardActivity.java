package com.example.david.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import com.example.david.databases.MyRoomAbstract;

public class DashBoardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

         SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        // Si no existe el boolean la base de datos no ha sido creada y se creará con room
        if (!preferences.getBoolean("first_run",true)){
            MyRoomAbstract.getInstance(this).quotationDao().getAllQuotation();
            preferences.edit().putBoolean("first_run",false);
        }
    }

    public void showView(View v){
        Intent intent;

        switch(v.getId()){
            case R.id.button_about:
                intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.button_favouriteQuotations:
                intent = new Intent(this, FavouriteActivity.class);
                startActivity(intent);
                break;
            case R.id.button_getQuotations:
                intent = new Intent(this, QuotationActivity.class);
                startActivity(intent);
                break;
            case R.id.button_settings:
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
        }

    }
}
