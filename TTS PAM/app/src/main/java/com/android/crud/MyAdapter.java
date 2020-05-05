package com.android.crud;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends ArrayAdapter<Datacerita> {
    Context context;
    List<Datacerita> arrayListDatacerita;
    //public static ArrayList<Datacerita> dataceritaArrayList = new ArrayList<>();




    public MyAdapter(@NonNull Context context, List<Datacerita> arrayListDatacerita) {
        super(context, R.layout.custom_list_item, arrayListDatacerita);
    this.context = context;
    this.arrayListDatacerita = arrayListDatacerita;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_list_item, null, true);

        TextView tvID = view.findViewById(R.id.txt_id);
       TextView tvName = view.findViewById(R.id.txt_name);


       tvID.setText(arrayListDatacerita.get(position).getId());
       tvName.setText(arrayListDatacerita.get(position).getJudul());
        return view;
    }
}
