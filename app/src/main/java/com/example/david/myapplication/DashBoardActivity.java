package com.example.david.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

public class DashBoardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
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
