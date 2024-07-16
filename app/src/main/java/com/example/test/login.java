package com.example.test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity {



    Button btnlog;
    EditText edlogemail,edlogpass;
     FirebaseAuth auth;
     TextView tx_singup;

    String emailPattern = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$";

    android.app.ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);


        auth = FirebaseAuth.getInstance();
        btnlog = findViewById(R.id.btnlog);
        edlogemail = findViewById(R.id.edlogemail);
        edlogpass = findViewById(R.id.edlogpass);
        tx_singup = findViewById(R.id.tx_singup);



       tx_singup.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(login.this,registration.class);
               startActivity(intent) ;
               finish();

           }
       });



        btnlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email = edlogemail.getText().toString();
                String pass = edlogpass.getText().toString();

                if((TextUtils.isEmpty(Email))){
                    progressDialog.dismiss();
                    Toast.makeText(login.this, "Enter Email", Toast.LENGTH_SHORT).show();
                }else if((TextUtils.isEmpty(pass))){
                    Toast.makeText(login.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                } else if (!Email.matches(emailPattern)) {
                    edlogemail.setError("Enter Vaild Email");
                    progressDialog.dismiss();
                } else if (pass.length()<6) {
                    edlogpass.setError("More Than 6 Charecter");
                    progressDialog.dismiss();
                    
                }else{
                    auth.signInWithEmailAndPassword(Email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                progressDialog.show();

                                try {

                                    Intent intent = new Intent(login.this,MainActivity.class);
                                    startActivity(intent);
                                    finish();

                                }catch (Exception e){
                                    Toast.makeText(login.this,e.getMessage(),Toast.LENGTH_LONG).show();
                                }
                            }else{
                                Toast.makeText(login.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }





            }
        });


    }
}