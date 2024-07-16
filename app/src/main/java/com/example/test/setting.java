package com.example.test;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class setting extends AppCompatActivity {

    ImageView settingprofile;
    EditText settingname,settingstatus;

    Button donebutt;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    String email,password;

    Uri setimageUri;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        //ProgeessBar

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saving...");
        progressDialog.setCancelable(false);

        ///Fire Base
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        ////Identity
        settingprofile = findViewById(R.id.settingprofile);
        settingname = findViewById(R.id.settingname);
        settingstatus = findViewById(R.id.settingstatus);
        donebutt = findViewById(R.id.donebutt);


        DatabaseReference reference = database.getReference().child("user").child(firebaseAuth.getUid());
        StorageReference storageReference = storage.getReference().child("upload").child(firebaseAuth.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                email = snapshot.child("mail").getValue().toString();
                password = snapshot.child("password").getValue().toString();
                String name = snapshot.child("userName").getValue().toString();
                String profilepic = snapshot.child("profilepic").getValue().toString();
                String status = snapshot.child("status").getValue().toString();

                settingname.setText(name);
                settingstatus.setText(status);
                Picasso.get().load(profilepic).into(settingprofile);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ////Image Set in profile

        settingprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Choose a Picture"),10);
            }

        });

        donebutt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                String name = settingname.getText().toString();
                String status = settingstatus.getText().toString();
                if(setimageUri!=null){
                    storageReference.putFile(setimageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String finalimageUri = uri.toString();
                                    Users users = new Users(firebaseAuth.getUid(),name,email,password,finalimageUri,status);
                                    reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                progressDialog.dismiss();
                                                Toast.makeText(setting.this, "Data Is Saved", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(setting.this,MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }else{
                                                progressDialog.dismiss();
                                                Toast.makeText(setting.this, "Something Went Wrong!!!!", Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    });
                                }
                            });
                        }
                    });
                }else {
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String finalImageUri = uri.toString();
                            Users users = new Users(firebaseAuth.getUid(), name,email,password,finalImageUri,status);
                            reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        progressDialog.dismiss();
                                        Toast.makeText(setting.this, "Data Is Saved", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(setting.this,MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }else{
                                        progressDialog.dismiss();
                                        Toast.makeText(setting.this, "Something Went Wrong!!!!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });
                }
            }
        });




    }

    //////Image method


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
          if( requestCode == 10){
              if( data != null){
                  setimageUri = data.getData();
                  settingprofile.setImageURI(setimageUri);
              }
          }


    }
}