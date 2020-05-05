package com.android.crud;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

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

public class AdminActivity extends AppCompatActivity {
    ListView listView;
    MyAdapteruser adapter;
    public  static ArrayList<Datauser> datauserArrayList = new ArrayList<>();
    String url ="http://192.168.43.52/android_register_login/retrieveuser.php";
    String URL_delete ="http://192.168.43.52/android_register_login/deleteuser.php";
    Datauser datauser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        listView = findViewById(R.id.mylisView);
        adapter = new MyAdapteruser(this, datauserArrayList);
        listView.setAdapter(adapter);
        retrieveData();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                ProgressDialog progressDialog = new ProgressDialog(view.getContext());

                CharSequence[] dialogItem = {"View User", "Delet User" };
                builder.setTitle(datauserArrayList.get(position).getName());
                builder.setItems(dialogItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        switch (i){
                            case 0:
                                startActivity(new Intent(getApplicationContext(), DetailUserActivity.class)
                                        .putExtra("position", position));


                                break;
                            case 1:
//                                startActivity(new Intent(getApplicationContext(), EditUserActivity.class).putExtra("position", position));
                                deletData(datauserArrayList.get(position).getId());
                                break;
//                            case 2:
//
//
//                                break;
                        }


                    }
                });

                builder.create().show();


            }
        });
    }

    private void deletData(final String position) {
        StringRequest request = new StringRequest(Request.Method.POST, URL_delete,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response)  {

                        if(response.equalsIgnoreCase("Data Deleted")){
                            Toast.makeText(AdminActivity.this, "Data Deleted Succesfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(AdminActivity.this, AdminActivity.class));
                        }else {
                            Toast.makeText(AdminActivity.this, "Data Not Delete", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AdminActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

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
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                     datauserArrayList.clear();
                     try {

                         JSONObject jsonObject = new JSONObject(response);
                         String success = jsonObject.getString("success");
                         JSONArray jsonArray = jsonObject.getJSONArray("data");

                         if(success.equals("1")){

                             for (int i =0; i<jsonArray.length(); i++){
                                 JSONObject object = jsonArray.getJSONObject(i);
                                 String id = object.getString("id");
                                 String name = object.getString("name");
                                 String email = object.getString("email");
                                 String password = object.getString("password");
                                 String photo = object.getString("photo");

                                 datauser = new Datauser(id, name, email, password, photo);
                                 datauserArrayList.add(datauser);
                                 adapter.notifyDataSetChanged();
                             }
                         }

                     }catch (JSONException e){
                         e.printStackTrace();
                     }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AdminActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.optionmenuadmin, menu);
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;

    }

    public boolean onOptionsItemSelected(MenuItem item) {
         if (item.getItemId() == R.id.about) {
            startActivity(new Intent(this, AboutActivity.class));
        } else if (item.getItemId() == R.id.logout) {
            startActivity(new Intent(this, LoginActivity.class));
        }

        return true;
    }

}
