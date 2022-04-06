package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OwnerHomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    RecyclerView recyclerView;

    DatabaseReference databaseReference;
    FirebaseAuth auth;

    OwnerHomePageAdapter adapter;
    ArrayList<AddPartyCenterHelper> list;
    OwnerHomePageAdapter.OnNoteListenerHome onNoteListenerHome;

    String ownerId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_home_page);

        drawerLayout = findViewById(R.id.drawer_layout_owner_home);
        navigationView = findViewById(R.id.nav_view_owner_home);
        toolbar = findViewById(R.id.toolbar_owner_home);
       // recyclerView = findViewById(R.id.featured_recycler_owner_home);

        databaseReference = FirebaseDatabase.getInstance().getReference("Party Centers");
        auth = FirebaseAuth.getInstance();

        recyclerView = findViewById(R.id.recycler_party_center_list);
        list = new ArrayList<>();

        setSupportActionBar(toolbar);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_view_owner_home);

        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            ownerId = extra.getString("ownerId");
        }



        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    AddPartyCenterHelper helper = dataSnapshot.getValue(AddPartyCenterHelper.class);
                    if(helper.getOwnerId().equals(ownerId) && helper.getPartyCenterStatus().equals("Pending")){
                        list.add(helper);
                        }

                }

                    setOnClickListener();
                    adapter = new OwnerHomePageAdapter(list, getApplicationContext(), onNoteListenerHome);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    recyclerView.setAdapter(adapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(OwnerHomePage.this, "Error occurred on product list", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void setOnClickListener(){
        onNoteListenerHome = new OwnerHomePageAdapter.OnNoteListenerHome() {
            @Override
            public void onNoteClick(int position) {
                Log.i("productID", list.get(position).getPartyCenterId());
                Intent intent = new Intent (OwnerHomePage.this, PartyCenterDetail.class);
                intent.putExtra("partyCenterId",list.get(position).getPartyCenterId());
                intent.putExtra("ownerId",ownerId);
                startActivity(intent);
            }
        };
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){
            case R.id.nav_owner_home:
                break;

            case R.id.nav_add_party_center:
                Intent intent = new Intent(getApplicationContext(), AddNewPartyCenter.class);
                intent.putExtra("ownerId", ownerId);
                startActivity(intent);
                //finish();
                break;

        }
        return false;
    }
}