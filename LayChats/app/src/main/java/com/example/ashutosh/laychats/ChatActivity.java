package com.example.ashutosh.laychats;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.analytics.FirebaseAnalytics;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import java.util.*;

/**
 * Created by Ashutosh on 14-12-2017.
 */
public class ChatActivity extends AppCompatActivity
{
    @BindView(R.id.layout1) LinearLayout linearLayout;
    @BindView(R.id.layout2) RelativeLayout relativeLayout;
    @BindView(R.id.send_btn) ImageView sendBtn;
    @BindView(R.id.message_area) EditText messageArea;
    @BindView(R.id.scroll_view) ScrollView scrollView;
    Firebase reference1,reference2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);

        Firebase.setAndroidContext(this);
        reference1 = new Firebase("https://laychats-180e4.firebaseio.com/message/" + UserDetail.userName + "_" + UserDetail.chatWith);
        reference2 = new Firebase("https://laychats-180e4.firebaseio.com/message/" + UserDetail.chatWith + "_" + UserDetail.userName);

        reference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map=dataSnapshot.getValue(Map.class);
                String message=map.get("message").toString();
                String userName=map.get("user").toString();

                if(userName.equals(UserDetail.userName))
                {
                    addMessageBox("You\n" + message, 1);
                }
                else{
                    addMessageBox(UserDetail.chatWith + "\n" + message, 2);
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }

    private void addMessageBox(String message, int type)
    {
        TextView textView = new TextView(ChatActivity.this);
        textView.setText(message);

        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp2.weight = 1.0f;

        if(type == 1) {
            lp2.gravity = Gravity.LEFT;
            textView.setBackgroundResource(R.drawable.bubble_in);
        }
        else{
            lp2.gravity = Gravity.RIGHT;
            textView.setBackgroundResource(R.drawable.bubble_out);
        }
        textView.setLayoutParams(lp2);
        linearLayout.addView(textView);
        scrollView.fullScroll(View.FOCUS_DOWN);
    }

    @OnClick(R.id.send_btn)
    public void sendBtnClicked()
    {
        String messageText=messageArea.getText().toString();
        if(!messageText.equals(""))
        {
            Map<String,String> map=new HashMap<String,String>();
            map.put("message",messageText);
            map.put("user",UserDetail.userName);
            reference1.push().setValue(map);
            reference2.push().setValue(map);
            messageArea.setText("");
        }
    }

}