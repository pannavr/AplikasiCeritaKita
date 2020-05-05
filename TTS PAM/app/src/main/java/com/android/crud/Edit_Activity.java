   package com.android.crud;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import java.util.HashMap;
import java.util.Map;

import static com.android.crud.App.CHANNEL_1_ID;

   public class Edit_Activity extends AppCompatActivity {
    EditText edId, edJudul, edLokasi, edCerita, edlat, edlon, lat, lon;
    private int position;
       private NotificationManagerCompat notificationManager;
    ProgressDialog progressDialog;
    GPSTracker gps;
    String xLatitude, xLongitude;
    Button Lokasi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_);


        notificationManager = NotificationManagerCompat.from(this);
         edId = findViewById(R.id.ed_id);
        edJudul = findViewById(R.id.ed_judul);
        edLokasi = findViewById(R.id.ed_lokasi);
        edCerita = findViewById(R.id.ed_cerita);
       lat = (EditText) findViewById(R.id.lati);
       lon = (EditText) findViewById(R.id.lngt);

        Intent intent = getIntent();
        position = intent.getExtras().getInt("position");


       edId.setText(HomeActivity.dataceritaArrayList.get(position).getId());
        edJudul.setText(HomeActivity.dataceritaArrayList.get(position).getJudul());
        edLokasi.setText(HomeActivity.dataceritaArrayList.get(position).getLokasi());
        edCerita.setText(HomeActivity.dataceritaArrayList.get(position).getCerita());
        lat.setText(HomeActivity.dataceritaArrayList.get(position).getLat());
        lon.setText(HomeActivity.dataceritaArrayList.get(position).getLon());


        Lokasi = findViewById(R.id.Lokasi);
        Lokasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                gps = new GPSTracker(Edit_Activity.this);

                if(gps.canGetLocation()){

                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();

                    lat.setText(Double.toString(latitude));
                    lon.setText(Double.toString(longitude));

                    Toast.makeText(getApplicationContext(), "Your Location is - \nLat: "
                            + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                }else{
                    gps.showSettingsAlert();
                }
            }
        });

    }

    public void btn_updateData(View view) {
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_one)
                .setContentTitle("Update Cerita")
                .setContentText("Cerita baru telah di Update")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();
        notificationManager.notify(1, notification);

        final String judul = edJudul.getText().toString();
        final String lokasi = edLokasi.getText().toString();
        final String cerita = edLokasi.getText().toString();
        final Double lat = Double.valueOf(this.lat.getText().toString().trim());
        final Double lon = Double.valueOf(this.lon.getText().toString().trim());
        final String id = edId.getText().toString();

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Updating....");
        progressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, "http://192.168.43.52/android_register_login/update.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(Edit_Activity.this, response, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                        finish();
                        progressDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Edit_Activity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> Params = new HashMap<>();

                Params.put("id", id);
                Params.put("judul", judul);
                Params.put("lokasi", lokasi);
                Params.put("lat", String.valueOf(lat));
                Params.put("lon", String.valueOf(lon));
                Params.put("cerita", cerita);
                return Params;

            }

            };


        RequestQueue requestQueue = Volley.newRequestQueue(Edit_Activity.this);
        requestQueue.add(request);
    }
}