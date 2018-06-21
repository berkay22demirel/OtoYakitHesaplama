package com.berkay22demirel.otoyakthesaplama.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.berkay22demirel.otoyakthesaplama.R;

import java.util.ArrayList;

/**
 * Created by BerkayDemirel on 18.02.2018.
 */

public class RaporListAdapter extends ArrayAdapter<String> {

    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<String> plakalar;

    public RaporListAdapter(Context context, int resource, ArrayList<String> plakalar) {
        super(context, resource,plakalar);
        this.context = context;
        this.plakalar = plakalar;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(R.layout.list_item_rapor_plakalar,null);

        TextView textView = (TextView) convertView.findViewById(R.id.textViewRaporListItem);

        if(plakalar.get(position) == "Tüm Plakalar"){
            textView.setTextSize(30);
        }


        textView.setText(plakalar.get(position));

        return convertView;
    }
}
