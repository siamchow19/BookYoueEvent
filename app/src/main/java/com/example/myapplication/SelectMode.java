package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class SelectMode extends AppCompatActivity {

    private TextView ownerMode, customerMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_mode);

        ownerMode = findViewById(R.id.owner_mode);
        customerMode = findViewById(R.id.customer_mode);

        ownerMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectMode.this, OwnerLogIn.class);
                startActivity(intent);

            }
        });
    }
}