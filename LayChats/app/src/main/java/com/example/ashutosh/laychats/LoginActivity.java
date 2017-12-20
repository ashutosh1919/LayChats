package com.example.ashutosh.laychats;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DatabaseReference;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LoginActivity extends AppCompatActivity
{

    @BindView(R.id.main_toolbar) Toolbar toolbar;
    @BindView(R.id.email_et) EditText emailET;
    @BindView(R.id.password_et) EditText passwordET;
    @BindView(R.id.login_btn) Button loginBtn;
    @BindView(R.id.register_tv) TextView registerTV;
    @BindView(R.id.activity_main) RelativeLayout parent;
    @BindView(R.id.error_tv) TextView errorTV;
    String userName,password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                         .setDefaultFontPath("fonts/RobotoBold.ttf")
                         .setFontAttrId(R.attr.fontPath)
                         .build());
    }

    @OnClick(R.id.email_et)
    public void emailClicked()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
        {
            emailET.setBackground(getResources().getDrawable(R.drawable.border_blue));
            passwordET.setBackground(getResources().getDrawable(R.drawable.border_grey));
        }
    }

    @OnClick(R.id.password_et)
    public void passwordClicked()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
        {
            emailET.setBackground(getResources().getDrawable(R.drawable.border_grey));
            passwordET.setBackground(getResources().getDrawable(R.drawable.border_blue));
        }
    }

    @OnClick(R.id.login_btn)
    public void loginClicked()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
        {
            emailET.setBackground(getResources().getDrawable(R.drawable.border_grey));
            passwordET.setBackground(getResources().getDrawable(R.drawable.border_grey));
        }

        userName=emailET.getText().toString();
        password=passwordET.getText().toString();

        if(userName=="" && password=="")
        {
            errorTV.setText("You can not Log in without Filling Details!");
            errorTV.setVisibility(View.VISIBLE);
        }
        else if(userName=="")
        {
            errorTV.setText("User Name is Empty!!");
            errorTV.setVisibility(View.VISIBLE);
        }
        else if(password=="")
        {
            errorTV.setText("User Name is Empty!!");
            errorTV.setVisibility(View.VISIBLE);
        }
        else
        {
            String url="https://laychats-180e4.firebaseio.com/user.json";
            final ProgressDialog pd = new ProgressDialog(LoginActivity.this);
            pd.setMessage("Loading...");
            pd.show();

            StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response)
                {
                    if(response.equals("null"))
                    {
                        errorTV.setText("Given user is not Registered! Please Register first.");
                        errorTV.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        try
                        {
                            JSONObject loginObject=new JSONObject(response);

                            if(!loginObject.has(userName))
                            {
                                errorTV.setText("Given user is not Registered! Please Register first.");
                                errorTV.setVisibility(View.VISIBLE);
                            }
                            else if(loginObject.getJSONObject(userName).getString("password").equals(password))
                            {
                                UserDetail.userName=userName;
                                UserDetail.password=password;
                                startActivity(new Intent(LoginActivity.this,Users.class));
                            }
                            else
                            {
                                errorTV.setText("Incorrect Password!!");
                                errorTV.setVisibility(View.VISIBLE);
                            }
                            pd.dismiss();
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(LoginActivity.this, ""+error.toString(), Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });

            RequestQueue rQueue = Volley.newRequestQueue(LoginActivity.this);
            rQueue.add(request);

        }

    }

    @OnClick(R.id.register_tv)
    public void registerClicked()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
        {
            emailET.setBackground(getResources().getDrawable(R.drawable.border_grey));
            passwordET.setBackground(getResources().getDrawable(R.drawable.border_grey));
        }

        startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
    }

    @OnClick(R.id.activity_main)
    public void parentClicked()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
        {
            emailET.setBackground(getResources().getDrawable(R.drawable.border_grey));
            passwordET.setBackground(getResources().getDrawable(R.drawable.border_grey));
        }
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
