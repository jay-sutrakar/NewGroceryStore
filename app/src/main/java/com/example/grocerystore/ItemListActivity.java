package com.example.grocerystore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.grocerystore.adapter.ItemRecyclerViewAdapter;
import com.example.grocerystore.util.Product;
import com.example.grocerystore.util.UserApi;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class ItemListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private FirebaseAuth firebaseAuth;
    private RecyclerView recyclerView;
    private ItemRecyclerViewAdapter itemRecyclerViewAdapter;
    private ArrayList<Product> productList;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private CollectionReference collectionReference ;
    private ProgressBar progressBar;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.draw_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer, toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        Log.d("UserApi", "onCreate: " + UserApi.getInstance().getUserContactNumber());

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        collectionReference = db.collection("Products");

        user = firebaseAuth.getCurrentUser();
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressBar.setVisibility(View.VISIBLE);
        searchView = (SearchView) findViewById(R.id.search_button);


        //Adding product in productlist
        productList = new ArrayList<>();
        collectionReference.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.isEmpty()){
                            Log.d("ItemListActivity", "onSuccess: unable to fetch data");
                        } else {
                            for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots){
                                productList.add(snapshot.toObject(Product.class));
                            }
                            itemRecyclerViewAdapter = new ItemRecyclerViewAdapter(ItemListActivity.this, productList);
                            recyclerView.setAdapter(itemRecyclerViewAdapter);
//                            List<Product> p = queryDocumentSnapshots.toObjects(Product.class);
//                            productList.addAll(p);
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });
        Log.d("Size", "onCreate: "+productList.size());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.cart_menu: startActivity(new Intent(ItemListActivity.this, CartActivity.class));
                break;

            case R.id.search_button:
                searchView = (SearchView) item.getActionView();
                searchView.setQueryHint("Search");
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        itemRecyclerViewAdapter.getFilter().filter(newText);
                        return false;
                    }
                });
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            //Here you will write code for different navigation options
            case R.id.nav_sign_out:
                firebaseAuth.signOut();
                Paper.book().destroy();
                startActivity(new Intent(ItemListActivity.this, MainActivity.class));
                finish();
                break;

            case R.id.nav_Profile:
                startActivity(new Intent(ItemListActivity.this, UserProfileActivity.class));
                break;


            case R.id.nav_delete_my_account:
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}