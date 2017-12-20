package com.example.ashutosh.laychats;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import java.util.*;

/**
 * Created by Ashutosh on 14-12-2017.
 */
public class Users extends AppCompatActivity
{
    ListView userList;
    ArrayList<String> arrayListUsers=new ArrayList<>();
    int totalUsers=0;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        ButterKnife.bind(this);

        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Loading...");
        progressDialog.show();

        userList=(ListView)findViewById(R.id.user_list);

        String url="https://laychats-180e4.firebaseio.com/user.json";

        StringRequest request=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                performOnSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(Users.this, ""+error, Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

        RequestQueue rQueue= Volley.newRequestQueue(this);
        rQueue.add(request);

        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                UserDetail.chatWith=arrayListUsers.get(i);
                startActivity(new Intent(Users.this,ChatActivity.class));
            }
        });

    }

    private void performOnSuccess(String response)
    {
        try
        {
            JSONObject userObject=new JSONObject(response);

            Iterator it=userObject.keys();
            String key="";

            while(it.hasNext())
            {
                key=it.next().toString();
                Log.d("user : ",key);
                if(!key.equals(UserDetail.userName))
                {
                    arrayListUsers.add(key);
                }
                totalUsers++;
            }

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        if(arrayListUsers.size()>=1)
        {
            userList.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrayListUsers));
        }
        progressDialog.dismiss();
    }
}
