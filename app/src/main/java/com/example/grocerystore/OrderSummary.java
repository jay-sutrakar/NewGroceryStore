package com.example.grocerystore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grocerystore.adapter.OrderSummaryRecyclerView;
import com.example.grocerystore.util.AddressDialog;
import com.example.grocerystore.util.Product;
import com.example.grocerystore.util.UserAddress;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class OrderSummary extends AppCompatActivity implements View.OnClickListener, AddressDialog.AddressDialogListener {
    private FirebaseFirestore db;
    private FirebaseUser user;
    private CollectionReference collectionReference;
    private List<Product> productList;

    private RecyclerView recyclerView;
    private OrderSummaryRecyclerView adapter;
    private ProgressBar progressBar;
    private TextView amountText;
    private TextView selectAddressTextview;
    private Button paymentButton;
    private Button selectAddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);

        //Initialize database variables
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        collectionReference = db.collection("Cart").document(user.getUid()).collection("products");

        //Intialize views
        recyclerView = findViewById(R.id.order_summary_recycleview);
        progressBar = findViewById(R.id.progressbar);
        amountText = findViewById(R.id.amount);
        paymentButton = findViewById(R.id.payment_button);
        selectAddress = findViewById(R.id.change_address_button);
        selectAddressTextview = findViewById(R.id.change_address_textview);
        productList = new ArrayList<>();



        //Fetching data from Cart collection of Firestore
        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(queryDocumentSnapshots.isEmpty()){
                    Log.d("ListEmpty", "onSuccess: Empty");
                    Toast.makeText(OrderSummary.this,"Add Some item to Cart ",Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.INVISIBLE);

                }else{
                    List<Product> p=queryDocumentSnapshots.toObjects(Product.class);
                    productList.addAll(p);
                    Log.d("size", "onSuccess: "+productList.size());
                    //Here we will call the Recycler view Adapter
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(OrderSummary.this));
                    adapter = new OrderSummaryRecyclerView(OrderSummary.this, productList);
                    recyclerView.setAdapter(adapter);
                    double totalAmount=0;
                    for(Product pr : p){
                        totalAmount=totalAmount+Double.parseDouble(pr.getProductDiscountPrice());
                    }
                    amountText.setText(String.valueOf(totalAmount));
                    progressBar.setVisibility(View.INVISIBLE);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("OrderSummaryActivity", "onFailure: failed in fetching data ");
            }
        });

        paymentButton.setOnClickListener(this);
        selectAddress.setOnClickListener(this);
        selectAddressTextview.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.change_address_button:
                openAddressDialog();
                break;
            case R.id.change_address_textview:
                openAddressDialog();
                break;
            case R.id.payment_button:
                startActivity(new Intent(OrderSummary.this,PaymentActivity.class));
                break;
        }
    }

    private void openAddressDialog() {
        AddressDialog addressDialog = new AddressDialog();
        addressDialog.show(getSupportFragmentManager(),"AddressDialog");
    }

    @Override
    public void OnAddressSelection(UserAddress userAddress) {
        selectAddress.setVisibility(View.INVISIBLE);
        paymentButton.setVisibility(View.VISIBLE);
        selectAddressTextview.setVisibility(View.VISIBLE);
    }
}
