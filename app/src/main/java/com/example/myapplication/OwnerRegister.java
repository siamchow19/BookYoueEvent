package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class OwnerRegister extends AppCompatActivity {

    private EditText OwnerName, OwnerMobile, OwnerEmail, OwnerPass, OwnerAddress;
    private Button ownerRegisterButton;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_register);

        OwnerName = findViewById(R.id.owner_name);
        OwnerMobile = findViewById(R.id.owner_mobile);
        OwnerEmail = findViewById(R.id.owner_email);
        OwnerPass = findViewById(R.id.owner_password);
        OwnerAddress = findViewById(R.id.owner_address);
        ownerRegisterButton = findViewById(R.id.owner_register_button);
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Owner");

        ownerRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadData();
            }
        });

    }

    private void loadData() {
        String ownerName, ownerEmail, ownerPassword;
        ownerName = OwnerName.getText().toString();
        ownerEmail = OwnerEmail.getText().toString();
        ownerPassword = OwnerPass.getText().toString();

        if(TextUtils.isEmpty(ownerEmail) || TextUtils.isEmpty(ownerName) || TextUtils.isEmpty(ownerPassword)){
            Toast.makeText(this, "Empty Credential", Toast.LENGTH_SHORT).show();
        }else if(ownerPassword.length() < 6){
            Toast.makeText(this, "Password too small", Toast.LENGTH_SHORT).show();
        }else if(!Patterns.EMAIL_ADDRESS.matcher(ownerEmail.trim()).matches()){
            Toast.makeText(this,"Invalid Email", Toast.LENGTH_SHORT).show();
        }else{
            ownerRegister(ownerEmail, ownerPassword, ownerName);
        }
    }



    private void ownerRegister(String email, String password, String name) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(OwnerRegister.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                    String uploadId = databaseReference.push().getKey();
                    OwnerInfo ownerInfo = new OwnerInfo(name, email, uploadId);
                    databaseReference.child(uploadId).setValue(ownerInfo);

                    Intent intent = new Intent(getApplicationContext(), OwnerHomePage.class);
                    intent.putExtra("ownerId", uploadId);
                    startActivity(intent);

                    finish();

                }else{
                    Toast.makeText(OwnerRegister.this, "Registration failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}