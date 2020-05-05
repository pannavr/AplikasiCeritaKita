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
import android.widget.TextView;
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

public class DetailUserActivity extends AppCompatActivity {
    TextView tvID, name, email, tvPassword, tvphoto;
    int position;
    private String imageLink;
    private Bitmap bitmap;
    String getId;

    CircleImageView profile_image;
    private static String URL_READ = "http://192.168.43.52/android_register_login/read_detail.php";
    private static String URL_UPLOAD = "http://192.168.43.52/android_register_login/upload.php";
    private static String TAG = DetailUserActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_user);


        name = findViewById(R.id.txtnama);
        email = findViewById(R.id.txtemail);
        tvPassword = findViewById(R.id.txtpassword);
       // tvphoto = findViewById(R.id.txtphoto);
        profile_image = findViewById(R.id.profilePic);




        Intent intent = getIntent();
        position = intent.getExtras().getInt("position");

        // tvID.setText("ID"+HomeActivity.dataceritaArrayList.get(position).getId);
  //      profile_image.setImageResource(0);
//        Picasso.get()
//                .load(imageLink)
//                .placeholder(R.drawable.ic_name)
//                .networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_CACHE)
//                .into(profile_image);
//        getId= user.get(sessionManager.ID);
        getId = (AdminActivity.datauserArrayList.get(position).getId());
        name.setText(AdminActivity.datauserArrayList.get(position).getName());
        email.setText(AdminActivity.datauserArrayList.get(position).getEmail());
       // tvphoto.setText("Lokasi Photo : "+AdminActivity.datauserArrayList.get(position).getPhoto());
        tvPassword.setText(AdminActivity.datauserArrayList.get(position).getPassword());


    }

    private void getUserDetail(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_READ,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        name.setFocusableInTouchMode(false);
                        email.setFocusableInTouchMode(false);
                        name.setFocusable(false);
                        email.setFocusable(false);

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

                                    name.setText(strName);
                                    email.setText(strEmail);
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
                            Toast.makeText(DetailUserActivity.this,"Error reading details 1! "+e.toString(), Toast.LENGTH_SHORT).show();

                        }
                    }
                }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();
                Toast.makeText(DetailUserActivity.this,"Error reading details 2! "+error.toString(), Toast.LENGTH_SHORT).show();

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
                                Toast.makeText(DetailUserActivity.this,"Upload Success!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(DetailUserActivity.this,"Error 1! "+e.toString(), Toast.LENGTH_SHORT).show();
                            getUserDetail();
//                            Picasso.get().load(photo).into(profile_image);

                        }
                    }
                }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();
                Toast.makeText(DetailUserActivity.this,"Error 2! "+error.toString(), Toast.LENGTH_SHORT).show();
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


    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK && data !=null && data.getData() !=null){
            Uri filePath = data.getData();
            try{
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                profile_image.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

            UploadPicture(getId, getStringImage(bitmap));
        }
    }

    public String getStringImage(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,65,byteArrayOutputStream);

        byte[] imageByteArray = byteArrayOutputStream.toByteArray();
        String encodedImage = Base64.encodeToString(imageByteArray,Base64.DEFAULT);


        return encodedImage;
    }

//
//    private void getUserDetail(){
//        final ProgressDialog progressDialog = new ProgressDialog(this);
//        progressDialog.setMessage("Loading...");
//        progressDialog.show();
//
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_READ,
//                new Response.Listener<String>(){
//                    @Override
//                    public void onResponse(String response) {
//                        name.setFocusableInTouchMode(false);
//                        email.setFocusableInTouchMode(false);
//                        name.setFocusable(false);
//                        email.setFocusable(false);
//
//                        progressDialog.dismiss();
//                        Log.i(TAG, response.toString());
//
//                        try{
//                            JSONObject jsonObject = new JSONObject(response);
//                            String success = jsonObject.getString("success");
//                            JSONArray jsonArray = jsonObject.getJSONArray("read");
//
//                            if(success.equals("1")){
//                                for (int i = 0; i < jsonArray.length(); i++) {
//                                    JSONObject object = jsonArray.getJSONObject(i);
//
//                                    String strName = object.getString("name").trim();
//                                    String strEmail = object.getString("email").trim();
//
//
//                                    //Add this for fetch image from json
//                                    String strImage = object.getString("image").trim();
//
//                                    name.setText(strName);
//                                    email.setText(strEmail);
//                                    imageLink = strImage;
//
//                                    if(!strImage.isEmpty()){
//                                        //display image from string url
//                                        //Picasso.get()
//                                        //      .load(strImage)
//                                        //    .placeholder(R.drawable.ic_name)
//                                        //  .networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE)
//                                        //.into(profile_image);
//                                        Picasso.get().load(strImage).into(profile_image);
//                                    }
//                                }
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            progressDialog.dismiss();
//                            Toast.makeText(DetailUserActivity.this,"Error reading details 1! "+e.toString(), Toast.LENGTH_SHORT).show();
//
//                        }
//                    }
//                }, new Response.ErrorListener(){
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//                progressDialog.dismiss();
//                Toast.makeText(DetailUserActivity.this,"Error reading details 2! "+error.toString(), Toast.LENGTH_SHORT).show();
//
//            }
//        })
//        {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("id", getId);
//
//                return params;
//            }
//        };
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        requestQueue.add(stringRequest);
//    }
}
