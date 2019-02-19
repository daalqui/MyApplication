package com.example.david.Servicio;

import android.content.Context;
import android.media.session.PlaybackState;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.david.myapplication.R;
import com.example.david.pojos.Quotation;

import java.util.List;

public class ArrayAdapterImplementation extends ArrayAdapter<Quotation> {

    private int layout;

    public ArrayAdapterImplementation(Context context, int resource, List<Quotation> objects) {
        super(context, resource, objects);
        this.layout = resource;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        View view = convertView;
        ViewHolder holderVistas = new ViewHolder();
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(this.layout, null);

            holderVistas.vistaAuthor = view.findViewById(R.id.textViewAuthor);
            holderVistas.vistaCita = view.findViewById(R.id.textViewCita);

            view.setTag(holderVistas);
        }
        Quotation item = getItem(position);
        holderVistas = (ViewHolder) view.getTag();
        holderVistas.vistaAuthor.setText(item.getQuoteAuthor());
        holderVistas.vistaCita.setText(item.getQuoteText());

    return view;
    }

}
