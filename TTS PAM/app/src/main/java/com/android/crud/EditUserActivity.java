package com.android.crud;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


import de.hdodenhof.circleimageview.CircleImageView;

public class EditUserActivity extends AppCompatActivity {
    EditText edId, edPassword, edEmail, edNama, edphoto;
    private int position;
    CircleImageView profile_image;
    String getId;
    private Bitmap bitmap;
    private String imageLink;
    ProgressDialog progressDialog;
    private static String URL_READ = "http://192.168.43.52/android_register_login/read_detail.php";
    private static String URL_UPLOAD = "http://192.168.43.52/android_register_login/upload.php";
    private static String TAG = DetailUserActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);


        edId = findViewById(R.id.ed_id);
        edNama = findViewById(R.id.ed_name);
        edEmail = findViewById(R.id.ed_email);
        edPassword = findViewById(R.id.ed_password);
      //  edphoto = findViewById(R.id.ed_photo);
        profile_image = findViewById(R.id.profilePic);


        Intent intent = getIntent();
        position = intent.getExtras().getInt("position");

        getId = (AdminActivity.datauserArrayList.get(position).getId());
        edId.setText(AdminActivity.datauserArrayList.get(position).getId());
        edNama.setText(AdminActivity.datauserArrayList.get(position).getName());
        edEmail.setText(AdminActivity.datauserArrayList.get(position).getEmail());
        edPassword.setText(AdminActivity.datauserArrayList.get(position).getPassword());
      //  edphoto.setText(AdminActivity.datauserArrayList.get(position).getPhoto());
    }

    public void btn_updateData(View view) {

        final String photo = edphoto.getText().toString();
        final String name = edNama.getText().toString();
        final String email = edEmail.getText().toString();
        final String password = edPassword.getText().toString();
        final String id = edId.getText().toString();

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Updating....");
        progressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, "http://192.168.43.52/android_register_login/updateuser.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(EditUserActivity.this, response, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),AdminActivity.class));
                        finish();
                        progressDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EditUserActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> Params = new HashMap<>();

                Params.put("id", id);
                Params.put("name", name);
                Params.put("email", email);
                Params.put("password", password);
                Params.put("photo", photo);
                return Params;

            }

        };


        RequestQueue requestQueue = Volley.newRequestQueue(EditUserActivity.this);
        requestQueue.add(request);
    }

    private void getUserDetail(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_READ,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        edNama.setFocusableInTouchMode(false);
                        edEmail.setFocusableInTouchMode(false);
                        edNama.setFocusable(false);
                        edNama.setFocusable(false);

                        progressDialog.dismiss();
                        Log.i(TAG, response.toString());

                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("read");

                            if(success.equals("1")){
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String strName = object.getString("name").trim();
                                    String strEmail = object.getString("email").trim();


                                    //Add this for fetch image from json
                                    String strImage = object.getString("image").trim();

                                    edNama.setText(strName);
                                    edEmail.setText(strEmail);
                                    imageLink = strImage;

                                    if(!strImage.isEmpty()){
                                        //display image from string url
                                        //Picasso.get()
                                        //      .load(strImage)
                                        //    .placeholder(R.drawable.ic_name)
                                        //  .networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE)
                                        //.into(profile_image);
                                        Picasso.get().load(strImage).into(profile_image);
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(EditUserActivity.this,"Error reading details 1! "+e.toString(), Toast.LENGTH_SHORT).show();

                        }
                    }
                }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();
                Toast.makeText(EditUserActivity.this,"Error reading details 2! "+error.toString(), Toast.LENGTH_SHORT).show();

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", getId);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    protected void onResume() {
        super.onResume();
        getUserDetail();
    }

    private void UploadPicture(final String id, final String photo ){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_UPLOAD,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.i(TAG, response.toString());

                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if(success.equals("1")){
                                //getUserDetail();
                                profile_image.setImageResource(0);
                                Picasso.get()
                                        .load(imageLink)
                                        .placeholder(R.drawable.ic_name)
                                        .networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_CACHE)
                                        .into(profile_image);
                                Toast.makeText(EditUserActivity.this,"Upload Success!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(EditUserActivity.this,"Error 1! "+e.toString(), Toast.LENGTH_SHORT).show();
                            getUserDetail();
//                            Picasso.get().load(photo).into(profile_image);

                        }
                    }
                }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();
                Toast.makeText(EditUserActivity.this,"Error 2! "+error.toString(), Toast.LENGTH_SHORT).show();
                getUserDetail();
//                            Picasso.get().load(photo).into(profile_image);

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                params.put("photo", photo);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if(requestCode == 1 && resultCode == RESULT_OK && data !=null && data.getData() !=null){
//            Uri filePath = data.getData();
//            try{
//                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
//                profile_image.setImageBitmap(bitmap);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            UploadPicture(getId, getStringImage(bitmap));
//        }
//    }

    public String getStringImage(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,65,byteArrayOutputStream);

        byte[] imageByteArray = byteArrayOutputStream.toByteArray();
        String encodedImage = Base64.encodeToString(imageByteArray,Base64.DEFAULT);


        return encodedImage;
    }



}
