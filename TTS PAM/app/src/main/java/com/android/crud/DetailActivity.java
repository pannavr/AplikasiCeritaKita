package com.android.crud;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    TextView tvID, tvJudul, tvLokasi, tvCerita, tvlat, tvlon, alamat;
    int position;
    GPSTracker gps;
    String xLatitude, xLongitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


      //  tvID = findViewById(R.id.txt_id);
        tvJudul = findViewById(R.id.txtjudul);
        tvLokasi = findViewById(R.id.txtlokasi);
        tvCerita = findViewById(R.id.txtcerita);
        tvlat = findViewById(R.id.id_lat);
        tvlon = findViewById(R.id.id_lon);
        alamat = (TextView) findViewById(R.id.alamat);

        Intent intent = getIntent();
        position = intent.getExtras().getInt("position");

        //  tvID.setText("ID"+HomeActivity.dataceritaArrayList.get(position).getId());

        tvJudul.setText("Judul : "+HomeActivity.dataceritaArrayList.get(position).getJudul());
        tvlat.setText(""+HomeActivity.dataceritaArrayList.get(position).getLat());
        tvlon.setText(""+HomeActivity.dataceritaArrayList.get(position).getLon());
        tvLokasi.setText("Tanggal : "+HomeActivity.dataceritaArrayList.get(position).getLokasi());
        tvCerita.setText("Cerita :"+HomeActivity.dataceritaArrayList.get(position).getCerita());

        alamat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                intent.putExtra("latitude",HomeActivity.dataceritaArrayList.get(position).getLon());
                intent.putExtra("longitude", HomeActivity.dataceritaArrayList.get(position).getLat());
                startActivity(intent);
            }
        });
    }
}
