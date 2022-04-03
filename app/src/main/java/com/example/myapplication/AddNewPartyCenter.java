package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class AddNewPartyCenter extends AppCompatActivity implements AdapterView.OnItemSelectedListener, NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    private Spinner spinnerDivision;
    private Spinner spinnerDistrict;
    private Spinner spinnerPoliceStation;

    private Button selectImageButton, addNewPartyCenterButton;

    private ImageView imageView;
    private Uri imageUri;

    private EditText nameOfPartyCenter, termsAndConditions, capacity, startingPrice, partyCenterEmail, partyCenterContactNo;

    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private FirebaseAuth auth;

    private String ownerId;

    private static final int IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_party_center);

        drawerLayout = findViewById(R.id.drawer_layout_add);
        navigationView = findViewById(R.id.nav_view_add);
        toolbar = findViewById(R.id.toolbar_add);

        spinnerDistrict = findViewById(R.id.add_spinner_district);
        spinnerDivision = findViewById(R.id.add_spinner_division);
        spinnerPoliceStation = findViewById(R.id.add_spinner_police_station);

        selectImageButton = findViewById(R.id.select_image);
        addNewPartyCenterButton = findViewById(R.id.add_button);

        imageView = findViewById(R.id.add_image);

        nameOfPartyCenter = findViewById(R.id.add_party_center_name);
        termsAndConditions = findViewById(R.id.add_terms_and_conditions);
        capacity = findViewById(R.id.add_capacity);
        startingPrice = findViewById(R.id.add_starting_price);
        partyCenterEmail = findViewById(R.id.add_party_center_email);
        partyCenterContactNo = findViewById(R.id.add_party_center_contact_no);

        databaseReference = FirebaseDatabase.getInstance().getReference("Party Centers");
        storageReference = FirebaseStorage.getInstance().getReference("Party Centers");


        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            ownerId = extra.getString("ownerId");
        }


        //toolbar and navigation drawer
        setSupportActionBar(toolbar);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);



        //spinner division
        String[] items = new String[]{"Dhaka", "Chattogram"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        spinnerDivision.setAdapter(adapter);
        spinnerDivision.setOnItemSelectedListener(this);


        //Image selection
        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        //sending info to firebase
        addNewPartyCenterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });
    }



    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    private void saveData() {
        String partyCenterName = nameOfPartyCenter.getText().toString();
        String terms = termsAndConditions.getText().toString();
        String maximumCapacity = capacity.getText().toString();
        String price = startingPrice.getText().toString();
        String email = partyCenterEmail.getText().toString();
        String contact = partyCenterContactNo.getText().toString();


        if (partyCenterName.isEmpty() || terms.isEmpty() || maximumCapacity.isEmpty() || price.isEmpty() || email.isEmpty()|| contact.isEmpty() || imageView.equals(R.mipmap.ic_launcher_round)) {
            Toast.makeText(this, "Empty credential", Toast.LENGTH_SHORT).show();
        } else {
            StorageReference ref = storageReference.child(System.currentTimeMillis() + "." + getExtension(imageUri));
            ref.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Task<Uri> myUri = taskSnapshot.getStorage().getDownloadUrl();
                    while (!myUri.isSuccessful()) ;
                    Uri imageUrl = myUri.getResult();


                    String uploadId = databaseReference.push().getKey();
                    AddPartyCenterHelper helper = new AddPartyCenterHelper(spinnerDivision.getSelectedItem().toString(), spinnerDistrict.getSelectedItem().toString(), spinnerPoliceStation.getSelectedItem().toString(), partyCenterName, "" ,terms, maximumCapacity, price, imageUrl.toString(), uploadId, ownerId, "Pending", email, contact);
                    databaseReference.child(uploadId).setValue(helper);
                    Toast.makeText(AddNewPartyCenter.this, "Upload successful", Toast.LENGTH_SHORT).show();
                    spinnerDivision.setEnabled(true);
                    spinnerDistrict.setEnabled(true);
                    spinnerPoliceStation.setEnabled(true);
                    imageView.setImageResource(R.mipmap.ic_launcher_round);
                    termsAndConditions.setText(null);
                    capacity.setText(null);
                    startingPrice.setText(null);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddNewPartyCenter.this, "Upload failed", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }


    private String getExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    //Image Selection
    private void openFileChooser() {
       /* spinnerDivision.setEnabled(false);
        spinnerDistrict.setEnabled(false);
        spinnerPoliceStation.setEnabled(false);*/
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            Picasso.with(this).load(imageUri).into(imageView);
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if(spinnerDivision.getSelectedItem().equals("Dhaka")){
            ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(this, R.array.Division_Dhaka, android.R.layout.simple_spinner_dropdown_item);
            spinnerDistrict.setAdapter(arrayAdapter);
        }

        spinnerDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(spinnerDistrict.getSelectedItem().equals("Dhaka")){
                    ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.District_Dhaka, android.R.layout.simple_spinner_dropdown_item);
                    spinnerPoliceStation.setAdapter(arrayAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.nav_owner_home:
                break;

            case R.id.nav_add_party_center:
                break;

        }
        return false;
    }
}