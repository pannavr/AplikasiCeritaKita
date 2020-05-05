package com.android.crud;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class MyAdapteruser extends ArrayAdapter<Datauser> {
    Context context;
    List<Datauser> arrayListDatauser;
    public MyAdapteruser(@NonNull Context context, List<Datauser> arrayListDatauser) {
        super(context, R.layout.custom_list_user, arrayListDatauser);

        this.context = context;
        this.arrayListDatauser = arrayListDatauser;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_list_user, null, true);

        TextView tvID = view.findViewById(R.id.text_id);
        TextView tvName = view.findViewById(R.id.text_name);

        tvID.setText(arrayListDatauser.get(position).getId());
        tvName.setText(arrayListDatauser.get(position).getName());

        return view;
    }
}
