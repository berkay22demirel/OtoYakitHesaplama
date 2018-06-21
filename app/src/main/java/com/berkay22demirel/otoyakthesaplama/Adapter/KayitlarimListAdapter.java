package com.berkay22demirel.otoyakthesaplama.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.berkay22demirel.otoyakthesaplama.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by BerkayDemirel on 14.02.2018.
 */

public class KayitlarimListAdapter extends BaseExpandableListAdapter {
    ArrayList<String> plakalarHeader;
    HashMap<String, List<String>> kayitlarChild;
    Context context;
    TextView textViewHeader;
    TextView textViewChild;
    LayoutInflater layoutInflater;

    public KayitlarimListAdapter(Context context, ArrayList<String> plakalarHeader,HashMap<String,List<String>> kayitlarChild) {
        this.context = context;
        this.plakalarHeader = plakalarHeader;
        this.kayitlarChild = kayitlarChild;
    }

    public void setPlakalarHeader(ArrayList<String> plakalarHeader) {
        this.plakalarHeader = plakalarHeader;
    }

    public void setKayitlarChild(HashMap<String, List<String>> kayitlarChild) {
        this.kayitlarChild = kayitlarChild;
    }

    @Override
    public int getGroupCount() {
        return plakalarHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return kayitlarChild.get(plakalarHeader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return plakalarHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return kayitlarChild.get(plakalarHeader.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String plaka = (String)getGroup(groupPosition);

        if(convertView == null)
        {
            layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_item_kayitlarim_header,null);
        }

        textViewHeader = (TextView)convertView.findViewById(R.id.textViewKayitlarimListViewHeader);
        textViewHeader.setText(plaka);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String kayit = (String)getChild(groupPosition, childPosition);
        if(convertView==null)
        {
            layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_item_kayitlarim_child, null);
        }
        textViewChild = (TextView)convertView.findViewById(R.id.textViewKayitlarimListViewChild);
        textViewChild.setText(kayit);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
