package com.android.crud;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.session.MediaSessionManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;



import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    private String imageLink;

    private TextView name, email;
    private Button btn_logout, btn_photo_upload, manageBtn;
    private Menu menu_action;
    private Bitmap bitmap;

    CircleImageView profile_image;


    SessionManager sessionManager;

    String getId;
    private static String URL_READ = "http://192.168.43.52/android_register_login/read_detail.php";
    private static String URL_EDIT = "http://192.168.43.52/android_register_login/edit_detail.php";
    private static String URL_UPLOAD = "http://192.168.43.52/android_register_login/upload.php";
    private static String TAG = ProfileActivity.class.getSimpleName();


     @Override
    protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_profile);
         sessionManager = new SessionManager(this);
         sessionManager.checkLogin();

         name = findViewById(R.id.profileName);
         email = findViewById(R.id.profileEmail);
         btn_photo_upload = findViewById(R.id.uploadBtn);
         profile_image = findViewById(R.id.profilePic);


         HashMap<String, String> user = sessionManager.getUserDetail();
         getId= user.get(sessionManager.ID);

         String mName = user.get(sessionManager.NAME);
         String mEmail = user.get(sessionManager.EMAIL);

         name.setText(mName);
         email.setText(mEmail);

         btn_photo_upload.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 chooseFile();
             }
         });



//           Intent intent = getIntent();
////           String extraName = intent.getStringExtra("name");
////           String extraEmail = intent.getStringExtra("email");
////
////       name.setText(extraName);
////       email.setText(extraEmail);

//        btn_logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });


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
                            Toast.makeText(ProfileActivity.this,"Error reading details 1! "+e.toString(), Toast.LENGTH_SHORT).show();

                        }
                    }
                }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();
                Toast.makeText(ProfileActivity.this,"Error reading details 2! "+error.toString(), Toast.LENGTH_SHORT).show();

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
    @Override
    protected void onResume() {
        super.onResume();
        getUserDetail();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_action, menu);

        menu_action = menu;
        menu_action.findItem(R.id.menu_save).setVisible(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.menu_edit:

                name.setFocusableInTouchMode(true);
                email.setFocusableInTouchMode(true);

                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(name, InputMethodManager.SHOW_IMPLICIT);

                menu_action.findItem(R.id.menu_edit).setVisible(false);
                menu_action.findItem(R.id.menu_save).setVisible(true);

                return true;

            case R.id.menu_save:

                SaveEditDetail();
                menu_action.findItem(R.id.menu_edit).setVisible(true);
                menu_action.findItem(R.id.menu_save).setVisible(false);

                name.setFocusableInTouchMode(false);
                email.setFocusableInTouchMode(false);
                name.setFocusable(false);
                email.setFocusable(false);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void SaveEditDetail(){
        final String name = this.name.getText().toString().trim();
        final String email = this.email.getText().toString().trim();
        final String id = getId;

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saving...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_EDIT,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if(success.equals("1")){
                                Toast.makeText(ProfileActivity.this,"Success!", Toast.LENGTH_SHORT).show();
                                sessionManager.creatSession(name, email, id);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(ProfileActivity.this,"Error! "+e.toString(), Toast.LENGTH_SHORT).show();

                        }
                    }
                }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();
                Toast.makeText(ProfileActivity.this,"Error reading details! "+error.toString(), Toast.LENGTH_SHORT).show();

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name",name);
                params.put("email",email);
                params.put("id",id);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void chooseFile()     {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),1);

    }
    @Override
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
                                Toast.makeText(ProfileActivity.this,"Upload Success!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(ProfileActivity.this,"Error 1! "+e.toString(), Toast.LENGTH_SHORT).show();
                            getUserDetail();
//                            Picasso.get().load(photo).into(profile_image);

                        }
                    }
                }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();
                Toast.makeText(ProfileActivity.this,"Error 2! "+error.toString(), Toast.LENGTH_SHORT).show();
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



    public String getStringImage(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,65,byteArrayOutputStream);

        byte[] imageByteArray = byteArrayOutputStream.toByteArray();
        String encodedImage = Base64.encodeToString(imageByteArray,Base64.DEFAULT);


        return encodedImage;
    }
//
//
//
//        edId = findViewById(R.id.ed_id);
//        edNama = findViewById(R.id.ed_name);
//        edEmail = findViewById(R.id.ed_email);
////        edPassword = findViewById(R.id.ed_password);
//
//        Intent intent = getIntent();
//        position = intent.getExtras().getInt("position");
//
//
//        edId.setText(AdminActivity.datauserArrayList.get(position).getId());
//        edNama.setText(AdminActivity.datauserArrayList.get(position).getName());
//        edEmail.setText(AdminActivity.datauserArrayList.get(position).getEmail());
////        edPassword.setText(AdminActivity.datauserArrayList.get(position).getPassword());
//    }
//
//    public void btn_updateData(View view) {
//
//
//        final String name = edNama.getText().toString();
//        final String email = edEmail.getText().toString();
////        final String password = edPassword.getText().toString();
//        final String id = edId.getText().toString();
//
//        final ProgressDialog progressDialog = new ProgressDialog(this);
//        progressDialog.setMessage("Updating....");
//        progressDialog.show();
//
//        StringRequest request = new StringRequest(Request.Method.POST, "http://192.168.43.52/android_register_login/updateuser.php",
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//
//                        Toast.makeText(ProfileActivity.this, response, Toast.LENGTH_SHORT).show();
//                        startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
//                        finish();
//                        progressDialog.dismiss();
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(ProfileActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
//                progressDialog.dismiss();
//
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> Params = new HashMap<>();
//
//                Params.put("id", id);
//                Params.put("name", name);
//                Params.put("email", email);
////                Params.put("password", password);
//                return Params;
//
//            }
//
//        };
//
//
//        RequestQueue requestQueue = Volley.newRequestQueue(ProfileActivity.this);
//        requestQueue.add(request);
//
//    }


}
