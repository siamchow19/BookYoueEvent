package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class PartyCenterDetail extends AppCompatActivity {
    private TextView partyCenterName;
    private TextView partyCenterCapacity;
    private TextView partyCenterAddress;
    private TextView partyCenterEmail;
    private TextView partyCenterMobile;
    private TextView terms;
    private TextView price;

    private ImageView imageView;

    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    private String partyCenterId, ownerId;

    private Button requestListButton;
    private Button editPartyCenterButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party_center_detail);
        partyCenterName = findViewById(R.id.party_center_detail_name);
        partyCenterCapacity = findViewById(R.id.party_center_detail_capacity);
        partyCenterAddress = findViewById(R.id.party_center_detail_address);
        partyCenterEmail = findViewById(R.id.party_center_detail_Email);
        partyCenterMobile = findViewById(R.id.party_center_detail_mobile);
        terms = findViewById(R.id.party_center_detail_terms);
        price = findViewById(R.id.party_center_detail_price);
        imageView = findViewById(R.id.detail_image);
        requestListButton = findViewById(R.id.owner_request_list);
        editPartyCenterButton = findViewById(R.id.edit_party_center);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            partyCenterId = bundle.getString("partyCenterId");
            ownerId = bundle.getString("ownerId");
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("Party Centers").child(partyCenterId);
        storageReference = FirebaseStorage.getInstance().getReference("Party Centers");

        loadData();

    }

    public void loadData(){

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("partyCenterName").getValue().toString();
                String maxCapacity = snapshot.child("capacity").getValue().toString();
                String Address = snapshot.child("address").getValue().toString();
                String imageUrl = snapshot.child("imageUrl").getValue().toString();
                String conditions = snapshot.child("termsAndPolicies").getValue().toString();
                String startingPrice = snapshot.child("startingPrice").getValue().toString();
                String emailAddress = snapshot.child("email").getValue().toString();
                String contact = snapshot.child("contactNo").getValue().toString();

                partyCenterName.setText(name);
                partyCenterCapacity.setText(maxCapacity);
                partyCenterAddress.setText(Address);
                terms.setText(conditions);
                price.setText(startingPrice);
                partyCenterEmail.setText(emailAddress);
                partyCenterMobile.setText(contact);

                Picasso.with(getApplicationContext())
                        .load(imageUrl)
                        .placeholder(R.mipmap.ic_launcher)
                        .fit()
                        .into(imageView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}