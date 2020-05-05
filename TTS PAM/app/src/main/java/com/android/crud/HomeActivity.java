package com.android.crud;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.android.crud.App.CHANNEL_1_ID;
import static com.android.crud.R.id.mylist;

public class HomeActivity extends AppCompatActivity {
//    private TextView name, email;
    private Button bt_add_activity;

    ListView listView;
    MyAdapter adapter;
    public static ArrayList<Datacerita> dataceritaArrayList = new ArrayList<>();
    private static String URL_Preview="http://192.168.43.52/android_register_login/retrieve.php";
    private static String URL_delete="http://192.168.43.52/android_register_login/delete.php";
    Datacerita datacerita;



//    loginText.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            Intent loginIntent = new Intent(MainActivity.this, HomeActivity.class);
//            startActivity(loginIntent);
//        }
//    });

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //buat slide samping
//        Toolbar toolbar = findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);

        listView = findViewById(mylist);
        adapter = new MyAdapter(this,dataceritaArrayList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                ProgressDialog progressDialog = new ProgressDialog(view.getContext());

                CharSequence[] dialogItem = {"View Data", "Edit Data", "Delet Data"};
                builder.setTitle(dataceritaArrayList.get(position).getJudul());
                builder.setItems(dialogItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        switch (i){
                            case 0:
                                startActivity(new Intent(getApplicationContext(), DetailActivity.class)
                                .putExtra("position", position));


                                break;

                            case 1:
                                startActivity(new Intent(getApplicationContext(), Edit_Activity.class).putExtra("position", position));
                                break;
                            case 2:
                                deletData(dataceritaArrayList.get(position).getId());


                             // deletData(position);
                                break;

                        }


                    }
                });

                builder.create().show();


            }
        });



//        name = findViewById(R.id.name);
//        email = findViewById(R.id.email);
//        btn_logout = findViewById(R.id.btn_logout);
//
//        Intent intent = getIntent();
//        String extraName = intent.getStringExtra("name");
//        String extraEmail = intent.getStringExtra("email");
//
//        name.setText(extraName);
//        email.setText(extraEmail);
//
//        btn_logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });




    retrieveData();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.optionmenu, menu);
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.profil){
            startActivity(new Intent(this, ProfileActivity.class));
        } else if (item.getItemId() == R.id.about) {
            startActivity(new Intent(this, AboutActivity.class));
        } else if (item.getItemId() == R.id.logout) {
            startActivity(new Intent(this, LoginActivity.class));
        }

        return true;
    }

    private void deletData(final String position) {
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_profile)
                .setContentTitle("Delete Data")
                .setContentText("Data berhasil di hapus")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();

        StringRequest request = new StringRequest(Request.Method.POST, URL_delete,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response)  {

                        if(response.equalsIgnoreCase("Data Deleted")){
                            Toast.makeText(HomeActivity.this, "Data Deleted Succesfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(HomeActivity.this,HomeActivity.class));

                        }else {
                            Toast.makeText(HomeActivity.this, "Data Not Delete", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(HomeActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> Params = new HashMap<>();
                Params.put("id", String.valueOf(position));

                return Params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);

    }

    public void retrieveData(){
        StringRequest request = new StringRequest(Request.Method.POST, URL_Preview,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    dataceritaArrayList.clear();
                    try {

                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("data");

                            if(success.equals("1")){
                                for(int i=0; i<jsonArray.length();i++ ){


                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String id = object.getString("id");
                                    String judul = object.getString("judul");
                                  String lat = object.getString("lat");
                                  String lon = object.getString("lon");
                                    String lokasi = object.getString("lokasi");
                                    String cerita = object.getString("cerita");

                                  datacerita = new Datacerita(id, judul, lokasi, cerita, lat, lon);
                                   dataceritaArrayList.add(datacerita);
                                   adapter.notifyDataSetChanged();
                                }


                            }

                        }
                        catch (JSONException e){
                        e.printStackTrace();
                        }



                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(HomeActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);


    }

    public void bt_add_activity(View view){
        startActivity(new Intent(getApplicationContext(), AddActivity.class));
    }
}
