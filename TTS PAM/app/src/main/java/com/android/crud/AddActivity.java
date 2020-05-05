package com.android.crud;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.android.crud.App.CHANNEL_1_ID;

public class AddActivity extends AppCompatActivity {

    private EditText id, judul, lokasi, cerita;
    private Button btn_simpan;
    private ProgressBar loading;
    private TextView loginText, lat, lon;
    private static String URL_REGIST="http://192.168.43.52/android_register_login/add.php";
    GPSTracker gps;
    String xLatitude, xLongitude;
    Button Lokasi;
    private NotificationManagerCompat notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        notificationManager = NotificationManagerCompat.from(this);
        judul =findViewById(R.id.judul);
        lokasi = findViewById(R.id.lokasi);
        cerita = findViewById(R.id.cerita);
        btn_simpan = findViewById(R.id.btn_simpan);
        lat = (TextView) findViewById(R.id.lati);
        lon = (TextView) findViewById(R.id.lngt);


        Lokasi = findViewById(R.id.Lokasi);
        Lokasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                gps = new GPSTracker(AddActivity.this);

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
        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addData();

            }
        });

    }

    private void addData(){
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_one)
                .setContentTitle("ADD Cerita")
                .setContentText("Cerita baru telah di tambahkan")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();
        notificationManager.notify(1, notification);


        btn_simpan.setVisibility(View.GONE);


        final Double lat = Double.valueOf(this.lat.getText().toString().trim());
        final Double lon = Double.valueOf(this.lon.getText().toString().trim());

        final String judul = this.judul.getText().toString().trim();
        final String lokasi = this.lokasi.getText().toString().trim();
        final String cerita = this.cerita.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            if ( success.equals("1")) {
                                Toast.makeText(AddActivity.this, "Data Tersimpan",  Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(AddActivity.this, HomeActivity.class));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(AddActivity.this, "Register Error"+ e.toString(),  Toast.LENGTH_SHORT).show();
                            btn_simpan.setVisibility(View.VISIBLE);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(AddActivity.this, "Register Error"+ error.toString(),  Toast.LENGTH_SHORT).show();
                        btn_simpan.setVisibility(View.VISIBLE);
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> Params = new HashMap<>();

                Params.put("judul", judul);
                Params.put("lokasi", lokasi);
                Params.put("lat", String.valueOf(lat));
                Params.put("lon", String.valueOf(lon));
                Params.put("cerita", cerita);
                return Params;

            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
