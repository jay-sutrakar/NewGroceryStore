package com.example.grocerystore;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.grocerystore.adapter.CartRecyclerViewAdapter;
import com.example.grocerystore.util.Product;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Cart_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Cart_fragment extends Fragment {
    private RecyclerView recyclerView;
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private ProgressDialog progressDialog ;
    private CollectionReference collectionReference;
    private List<Product> productList;
    private Product product;
    private ImageView emptyCart;
    private Button placeOrder;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Cart_fragment() {
        // Required empty public constructor
    }
public static Cart_fragment newInstance(String param1, String param2) {
        Cart_fragment fragment = new Cart_fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

        }
        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        productList = new ArrayList<>();
        user = firebaseAuth.getCurrentUser();
        collectionReference = db.collection("Cart");
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Please wait ..");
        progressDialog.show();

        //if user click on place order button

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart_fragment,container,false);
        placeOrder = view.findViewById(R.id.placeorder);

        recyclerView = view.findViewById(R.id.cart_recyclerview);

        emptyCart= view.findViewById(R.id.emptyCart);
        emptyCart.setVisibility(View.INVISIBLE);

        if(FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivity(new Intent(getActivity(), MainActivity.class));
        }else
        collectionReference.document(user.getUid()).collection("products")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.isEmpty()) {
                            Log.d("TAG", "onSuccess: listEmpty");
                            progressDialog.dismiss();
                            placeOrder.setVisibility(View.INVISIBLE);
                            emptyCart.setVisibility(View.VISIBLE);

                        } else {

                            List<Product> p = queryDocumentSnapshots.toObjects(Product.class);
                            productList.addAll(p);
                            if(p.size() == 0){
                                emptyCart.setVisibility(View.VISIBLE);
                            }
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

                            CartRecyclerViewAdapter cartRecyclerViewAdapter = new CartRecyclerViewAdapter(getActivity(),productList);
                            recyclerView.setAdapter(cartRecyclerViewAdapter);

                            progressDialog.dismiss();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                    Log.d("CartActivity","Failed "+e.getMessage());
                progressDialog.dismiss();
            }
        });
        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),OrderSummary.class));
            }
        });

        return view;
    }

   // @Override
//    public void onClick(View v) {
//        switch(v.getId()){
//            case R.id.placeorder:
//                startActivity(new Intent(getActivity(),OrderSummary.class));
//                break;
//        }
//    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivity(new Intent(getActivity(), MainActivity.class));
        }

    }
}
