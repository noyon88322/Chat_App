package com.example.test;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class registration extends AppCompatActivity {


    EditText edrgusername,edrgemail,edrgpass,edrgrepass;
    Button btnreg;
    TextView rglogin;
    CircleImageView imageprofile;
    FirebaseAuth auth;
    Uri imageURI;
    String imageuri;

    String emailPattern = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$";

    FirebaseDatabase database;
    FirebaseStorage store;

    android.app.ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        //PrograsDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);


       //Firebase
        database = FirebaseDatabase.getInstance();
        store = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();

        //Button+Edit Text
        edrgusername =findViewById(R.id.edrgusername);
        edrgemail =findViewById(R.id.edrgemail);
        edrgpass =findViewById(R.id.edrgpass);
        btnreg =findViewById(R.id.btnreg);
        edrgrepass =findViewById(R.id.edrgrepass);
        rglogin =findViewById(R.id.rglogin);
        imageprofile =findViewById(R.id.imageprofile);


        //Get Data from Users

        btnreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                String namee = edrgusername.getText().toString();
                String emaill = edrgemail.getText().toString();
                String Password = edrgpass.getText().toString();
                String cPassword = edrgrepass.getText().toString();
                String status = "Hey I'm Using This Application";

                if (TextUtils.isEmpty(namee) || TextUtils.isEmpty(emaill) ||
                        TextUtils.isEmpty(Password) || TextUtils.isEmpty(cPassword)){
                    progressDialog.dismiss();
                    Toast.makeText(registration.this, "Please Enter Valid Information", Toast.LENGTH_SHORT).show();
                }else  if (!emaill.matches(emailPattern)){
                    progressDialog.dismiss();
                    edrgemail.setError("Type A Valid Email Here");
                }else if (Password.length()<6){
                    progressDialog.dismiss();
                    edrgpass.setError("Password Must Be 6 Characters Or More");
                }else if (!Password.equals(cPassword)){
                    progressDialog.dismiss();
                    edrgrepass.setError("The Password Doesn't Match");
                }else {
                    auth.createUserWithEmailAndPassword(emaill,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {


                            if (task.isSuccessful()){
                                String id = task.getResult().getUser().getUid();
                                DatabaseReference reference = database.getReference().child("user").child(id);
                                StorageReference storageReference = store.getReference().child("Upload").child(id);

                                if (imageURI!=null){
                                    storageReference.putFile(imageURI).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                            if (task.isSuccessful()){
                                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        imageuri = uri.toString();
                                                        Users users = new Users(id,namee,emaill,Password,imageuri,status);
                                                        reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()){
                                                                    progressDialog.show();

                                                                    Intent intent = new Intent(registration.this,MainActivity.class);
                                                                    startActivity(intent);
                                                                    finish();
                                                                }else {
                                                                    Toast.makeText(registration.this, "Error in creating the user", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                                    }
                                                });
                                            }
                                        }
                                    });
                                }else {
                                    String status = "Hey I'm Using This Application";
                                    imageuri = "https://firebasestorage.googleapis.com/v0/b/test-dccc8.appspot.com/o/Upload%2Fuser.png?alt=media&token=9997092c-1b8a-4fa7-9962-afcf97b4b205";
                                    Users users = new Users(id,namee,emaill,Password,imageuri,status);
                                    reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                progressDialog.show();
                                                Intent intent = new Intent(registration.this,MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }else {
                                                Toast.makeText(registration.this, "Error in creating the user", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            }else {
                                Toast.makeText(registration.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });



        ///reg kora thakle log in page jua
      rglogin.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent intent = new Intent(registration.this,login.class);
              startActivity(intent);
              finish();
          }
      });

      ////Profile Pic Upload

        imageprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Profile Picture"),10);
            }
        });




    }

    ///Image Method


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==10){
            if(data!=null){
                imageURI = data.getData();
                imageprofile.setImageURI(imageURI);
            }
        }
    }
}