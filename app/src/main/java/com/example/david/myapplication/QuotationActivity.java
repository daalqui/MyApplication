package com.example.david.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class QuotationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotation);

        TextView tv = findViewById(R.id.textView4);
        String text = tv.getText().toString();

        text= text.replaceAll("%1s", String.valueOf(R.string.user));
        tv.setText(text);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar_quotation_activity,menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_add:
              return true;
            case R.id.menu_refresh:
                // Refresca la información del view
                TextView tQuotation = findViewById(R.id.textView4);
                tQuotation.setText(R.string.sample_quotation);

                TextView tAuthor = findViewById(R.id.textView3);
                tAuthor.setText(R.string.sample_author);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
