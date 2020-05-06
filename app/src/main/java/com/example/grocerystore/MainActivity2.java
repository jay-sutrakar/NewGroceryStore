package com.example.grocerystore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grocerystore.adapter.ItemRecyclerViewAdapter;
import com.example.grocerystore.util.Product;
import com.example.grocerystore.util.UserAddress;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class MainActivity2 extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private BottomNavigationView bottomNavigationView;
    private DrawerLayout drawer;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private CollectionReference collectionReference;
    private TextView locationName;
    private TextView addressLine;
    private List<String> availableInCity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        locationName = findViewById(R.id.location);
        addressLine = findViewById(R.id.addressLine);

        //City in which our service is available
        availableInCity = new ArrayList<>();
        availableInCity.add("Tikamgarh");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.draw_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer, toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        collectionReference = db.collection("Products");
        bottomNavigationView = findViewById(R.id.bottom_navigation_view);

        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        openFragment(HomeFragment.newInstance("",""));
    }
    public void openFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.addToBackStack(null);
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }

    //On Selection of item from bottom navigation bar

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.nav_home:
                            openFragment(HomeFragment.newInstance("",""));
                            break;
                        case R.id.nav_cart:
                            openFragment(Cart_fragment.newInstance("",""));
                            break;
                        case R.id.nav_search:
                            break;
                        case R.id.nav_account:
                            openFragment(Profile_fragment.newInstance("",""));
                            break;
                    }
                    return false;
                }
            };


    @Override
    //On selection of item from side navigation Drawer
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            //Here you will write code for different navigation options
            case R.id.nav_sign_out:
                firebaseAuth.signOut();
               // Paper.book().destroy();
                startActivity(new Intent(MainActivity2.this, MainActivity.class));
                finish();
                break;

            case R.id.nav_Profile:
                startActivity(new Intent(MainActivity2.this, UserProfileActivity.class));
                break;


            case R.id.nav_delete_my_account:
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                final EditText verifyPassword = new EditText(this);
                alertDialog.setTitle("Deleting Account...");
                alertDialog.setMessage("Are you sure?");
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialog.Builder verifyPasswordDialog = new AlertDialog.Builder(alertDialog.getContext());
                        verifyPasswordDialog.setTitle("Deleting Account...");
                        verifyPasswordDialog.setMessage("Please Enter your password to confirm...");
                        verifyPasswordDialog.setView(verifyPassword);

                        verifyPasswordDialog.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                db.collection("Customers").document(user.getUid()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(MainActivity2.this, "Account deleted successfully", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(MainActivity2.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("Delete Account", "Failed with exception: " + e.toString());
                                        Toast.makeText(MainActivity2.this, "Failed...", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                        verifyPasswordDialog.show();
                    }
                });
                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertDialog.show();

                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);

    }


    @Override
    protected void onStart() {
        super.onStart();
        locationName.setText(UserAddress.getInstance().getCity());
        addressLine.setText(UserAddress.getInstance().getAddressline());
    }
}
