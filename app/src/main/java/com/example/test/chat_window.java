package com.example.test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class chat_window extends AppCompatActivity {

    String reciverimg,recivername,reciveruid,SenderUID;
    CircleImageView chat_img;
    TextView chat_name;

    CardView btnsend;
    EditText typechat;

    FirebaseAuth firebaseAuth;

    FirebaseDatabase database;

    public  static  String senderImg;
    public  static  String receverIMG;

    String reciverRoom,senderRoom;

    RecyclerView rcl_sms;

    ArrayList<msgModelClass> messageArrayList  = new ArrayList<>();
    messagesAdapter messagesAdapter;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();



        btnsend = findViewById(R.id.btnsend);
        typechat = findViewById(R.id.typechat);

          //Get Data
        recivername = getIntent().getStringExtra("nameee");
        reciverimg = getIntent().getStringExtra("reciverimg");
        reciveruid = getIntent().getStringExtra("uid");


        ///messageAdapter

        rcl_sms = findViewById(R.id.rcl_sms);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        rcl_sms.setLayoutManager(linearLayoutManager);
        messagesAdapter = new messagesAdapter(chat_window.this,messageArrayList);
        rcl_sms.setAdapter(messagesAdapter);


        chat_name = findViewById(R.id.chat_name);
        chat_img = findViewById(R.id.chat_img);

        Picasso.get()
                .load(reciverimg)
                .into(chat_img);
        chat_name.setText(""+recivername);

       //Room

        SenderUID = firebaseAuth.getUid();

        senderRoom = SenderUID+reciveruid;
        reciverRoom = reciveruid+SenderUID;


        ///Firebase
        DatabaseReference reference = database.getReference().child("user").child(firebaseAuth.getUid());
        DatabaseReference chatReferance = database.getReference().child("chats").child(senderRoom).child("messages");

        chatReferance.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                messageArrayList.clear();

                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    msgModelClass message = dataSnapshot.getValue(msgModelClass.class);
                    messageArrayList.add(message);

                }
                messagesAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                senderImg = snapshot.child("profilepic").getValue().toString();
                receverIMG  = reciverimg;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ///Message Send Button



        btnsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = typechat.getText().toString();
                if (message.isEmpty()){
                    Toast.makeText(chat_window.this, "Enter The Message First", Toast.LENGTH_SHORT).show();
                    return;
                }
                typechat.setText("");
                Date date = new Date();
                msgModelClass messagess = new msgModelClass(message,SenderUID,date.getTime());

                database=FirebaseDatabase.getInstance();
                database.getReference().child("chats")
                        .child(senderRoom)
                        .child("messages")
                        .push().setValue(messagess).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                database.getReference().child("chats")
                                        .child(reciverRoom)
                                        .child("messages")
                                        .push().setValue(messagess).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                            }
                                        });
                            }
                        });
            }
        });


    }
}