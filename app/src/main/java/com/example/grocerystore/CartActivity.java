//package com.example.grocerystore;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.app.ProgressDialog;
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//
//import com.example.grocerystore.adapter.CartRecyclerViewAdapter;
//import com.example.grocerystore.util.Product;
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.firestore.CollectionReference;
//import com.google.firebase.firestore.DocumentChange;
//import com.google.firebase.firestore.EventListener;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.FirebaseFirestoreException;
//import com.google.firebase.firestore.QueryDocumentSnapshot;
//import com.google.firebase.firestore.QuerySnapshot;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class CartActivity extends AppCompatActivity implements View.OnClickListener {
//    private RecyclerView recyclerView;
//    private FirebaseFirestore db;
//    private FirebaseAuth firebaseAuth;
//    private FirebaseUser user;
//    private ProgressDialog progressDialog ;
//    private CollectionReference collectionReference;
//    private List<Product> productList;
//    private Button placeorder;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_cart);
//        db= FirebaseFirestore.getInstance();
//        firebaseAuth = FirebaseAuth.getInstance();
//
//        recyclerView = findViewById(R.id.cart_recyclerview);
//        placeorder = findViewById(R.id.placeorder);
//
//        Product product;
//        productList = new ArrayList<>();
//
//        user = firebaseAuth.getCurrentUser();
//
//        //Fethching data of cart product from database
//
//        collectionReference = db.collection("Cart");
//        collectionReference.document(user.getUid()).collection("products")
//                .get()
//                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                        if (queryDocumentSnapshots.isEmpty()) {
//                                Log.d("TAG", "onSuccess: listEmpty");
//                        } else {
//
//                            List<Product> p = queryDocumentSnapshots.toObjects(Product.class);
//                            productList.addAll(p);
//
//                            recyclerView.setHasFixedSize(true);
//                            recyclerView.setLayoutManager(new LinearLayoutManager(CartActivity.this));
//
//                            CartRecyclerViewAdapter cartRecyclerViewAdapter = new CartRecyclerViewAdapter(CartActivity.this,productList);
//                            recyclerView.setAdapter(cartRecyclerViewAdapter);
//
//                            progressDialog.dismiss();
//                        }
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.d("CartActivity","Failed "+e.getMessage());
//                        progressDialog.dismiss();
//                    }
//                });
//
//        //if user click on place order button
//        placeorder.setOnClickListener(this);
//
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        progressDialog = new ProgressDialog(this);
//        progressDialog.setTitle("Please wait ..");
//        progressDialog.show();
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch(v.getId()){
//            case R.id.placeorder:
//                startActivity(new Intent(CartActivity.this,OrderSummary.class));
//                break;
//        }
//    }
//}
