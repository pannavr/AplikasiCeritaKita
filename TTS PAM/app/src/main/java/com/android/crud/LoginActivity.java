package com.android.crud;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.media.session.MediaSessionManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private EditText email, password;
    private Button btn_login;
    private ProgressBar loading;
    private TextView link_regist;
    private static String URL_LOGIN="http://192.168.43.52/android_register_login/login.php";
   SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        sessionManager = new SessionManager(this);
       loading = findViewById(R.id.loading);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btn_login = findViewById(R.id.btn_login);
        link_regist =findViewById(R.id.link_regist);

//        link_regist.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(LoginActivity.this, MainActivity.class));
//            }
//        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mEmail = email.getText().toString().trim();
                String mPass = password.getText().toString().trim();
                if (mEmail.equals("admin@gmail.com")&& mPass.equals("admin")){
                    Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                   LoginActivity.this.startActivity(intent);
                    finish();
                }
               else if(!mEmail.isEmpty() || !mPass.isEmpty()){
                    Login(mEmail, mPass );
                }else {
                    email.setError("Tolong masukkan email");
                    password.setError("Tolong masukkan password");
                }
            }
        });

        link_regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }
        });

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.optionmenu, menu);
//        //getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//
//    }
//
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId()==R.id.about){
//            startActivity(new Intent(this, MainActivity.class));
//        } else if (item.getItemId() == R.id.setting) {
//            startActivity(new Intent(this, AdminHomeActivity.class));
//        } else if (item.getItemId() == R.id.logout) {
//            startActivity(new Intent(this, LoginActivity.class));
//        }
//
//        return true;
//    }


    private void Login(final String email, final String password) {

        loading.setVisibility(View.VISIBLE);
       btn_login.setVisibility(View.GONE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("login");

                            if (success.equals("1")){
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String name = object.getString("name").trim();
                                    String email = object.getString("email").trim();
                                    String id = object.getString("id").trim();

                                    sessionManager.creatSession(name,email,id);

                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                intent.putExtra("name", name);
                                intent.putExtra("email", email);
                                startActivity(intent);


//                                     Toast.makeText(LoginActivity.this, "succes Login. \n Nama : "+name+"\n Email : "+email, Toast.LENGTH_SHORT).show();
//                                    loading.setVisibility(View.GONE);
                                }
                            }//else{
//////                                  loading.setVisibility(View.GONE);
//////                                  btn_login.setVisibility(View.VISIBLE);
//                                  Toast.makeText(LoginActivity.this, "error"+toString(), Toast.LENGTH_SHORT).show();
//                            }

                        }//catch (JSONException e){
                        catch (JSONException e) {
                            e.printStackTrace();
                            loading.setVisibility(View.GONE);
                            btn_login.setVisibility(View.VISIBLE);
                            Toast.makeText(LoginActivity.this, "error"+e.toString(), Toast.LENGTH_SHORT).show();
                        }
//                            e.printStackTrace();
////                            loading.setVisibility(View.GONE);
////                            btn_login.setVisibility(View.VISIBLE);
//                            Toast.makeText(LoginActivity.this, "error"+e.toString(), Toast.LENGTH_SHORT).show();
//                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.setVisibility(View.GONE);
                        btn_login.setVisibility(View.VISIBLE);
                        Toast.makeText(LoginActivity.this, "error"+error.toString(), Toast.LENGTH_SHORT).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue( this);
        requestQueue.add(stringRequest);
    }
    }



