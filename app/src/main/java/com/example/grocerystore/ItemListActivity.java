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
import android.view.View.OnClickListener;

import com.example.grocerystore.adapter.ItemRecyclerViewAdapter;
import com.example.grocerystore.util.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ItemListActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private RecyclerView recyclerView;
    private ItemRecyclerViewAdapter itemRecyclerViewAdapter;
    private List<Product> productList;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser user;
    private CollectionReference collectionReference = db.collection("CartItems");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        firebaseAuth=FirebaseAuth.getInstance();

        user=firebaseAuth.getCurrentUser();
        Log.d("ItemListActivity", "onCreate: "+ user.getUid());

        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Adding product in productList
        productList=new ArrayList<>();

        Product product;

        product = new Product();
        product.setProductName("Rin Shoap");
        product.setProductPrice("10");
        productList.add(product);

        product = new Product();
        product.setProductName("Handwash");
        product.setProductPrice("80");
        productList.add(product);

        product = new Product();
        product.setProductName("wheat");
        product.setProductPrice("400");
        productList.add(product);

        product = new Product();
        product.setProductName("Shoap");
        product.setProductPrice("20");
        productList.add(product);

        product = new Product();
        product.setProductName("poha");
        product.setProductPrice("20");
        productList.add(product);

        product = new Product();
        product.setProductName("maggie");
        product.setProductPrice("20");
        productList.add(product);

        product = new Product();
        product.setProductName("Shoap");
        product.setProductPrice("20");
        productList.add(product);



        itemRecyclerViewAdapter = new ItemRecyclerViewAdapter(ItemListActivity.this,productList);
        recyclerView.setAdapter(itemRecyclerViewAdapter);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.cart_menu:
                startActivity(new Intent(ItemListActivity.this,CartActivity.class));
                break;
            case R.id.sign_out:
                firebaseAuth.signOut();
                startActivity(new Intent(ItemListActivity.this,MainActivity.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}