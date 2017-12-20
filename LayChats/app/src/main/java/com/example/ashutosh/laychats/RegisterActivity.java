package com.example.ashutosh.laychats;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.client.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Ashutosh on 14-12-2017.
 */
public class RegisterActivity extends AppCompatActivity
{
    @BindView(R.id.username_et) EditText userNameET;
    @BindView(R.id.password_et) EditText passwordET;
    @BindView(R.id.register_btn) Button registerBtn;
    @BindView(R.id.login_tv) TextView loginTV;
    String userName,password;

    //DatabaseReference databaseReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        Firebase.setAndroidContext(this);

    }

    @OnClick(R.id.login_tv)
    public void loginclicked()
    {
        startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
    }

    @OnClick(R.id.register_btn)
    public void registerClicked()
    {
        userName=userNameET.getText().toString();
        password=passwordET.getText().toString();

        if(userName.equals("")){
            userNameET.setError("can't be blank");
        }
        else if(password.equals("")){
            passwordET.setError("can't be blank");
        }
        else if(!userName.matches("[A-Za-z0-9]+")){
            userNameET.setError("only alphabet or number allowed");
        }
        else if(userName.length()<5)
        {
            userNameET.setError("at least 5 characters long");
        }
        else if(password.length()<5){
            passwordET.setError("at least 5 characters long");
        }
        else
        {
            final ProgressDialog progressDialog=new ProgressDialog(RegisterActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.show();



            String url="https://laychats-180e4.firebaseio.com/user.json";

            StringRequest request=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Firebase reference = new Firebase("https://laychats-180e4.firebaseio.com/user");

                    if(response.equals("null"))
                    {
                        reference.child(userName).child("password").setValue(password);
                        Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        try
                        {
                            JSONObject object=new JSONObject(response);

                            if(!object.has(userName))
                            {
                                reference.child(userName).child("password").setValue(password);
                                Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                userNameET.setError("Username already Exists.");
                            }

                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }

                    progressDialog.dismiss();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(RegisterActivity.this, ""+error, Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });

            RequestQueue rQueue= Volley.newRequestQueue(this);
            rQueue.add(request);

        }
    }

}
