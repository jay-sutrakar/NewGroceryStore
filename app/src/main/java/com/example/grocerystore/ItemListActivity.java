package com.example.grocerystore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.grocerystore.adapter.ItemRecyclerViewAdapter;
import com.example.grocerystore.util.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ItemListActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private RecyclerView recyclerView;
    private ItemRecyclerViewAdapter itemRecyclerViewAdapter;
    private List<Product> productList;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private CollectionReference collectionReference ;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        db = FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        collectionReference = db.collection("Products");

        user=firebaseAuth.getCurrentUser();
        progressBar=findViewById(R.id.progressbar);
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressBar.setVisibility(View.VISIBLE);
        //Adding product in productlist
        productList=new ArrayList<>();
        collectionReference.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(queryDocumentSnapshots.isEmpty()){
                            Log.d("ItemListActivity", "onSuccess: unable to fetch data");
                        }else{
                            for(QueryDocumentSnapshot snapshot : queryDocumentSnapshots){
                                productList.add(snapshot.toObject(Product.class));
                            }
                            itemRecyclerViewAdapter = new ItemRecyclerViewAdapter(ItemListActivity.this,productList);
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
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.cart_menu: startActivity(new Intent(ItemListActivity.this, CartActivity.class));
                break;
            case R.id.sign_out: firebaseAuth.signOut();
                startActivity(new Intent(ItemListActivity.this, MainActivity.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}