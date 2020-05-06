package com.example.grocerystore;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;

import com.example.grocerystore.adapter.ItemRecyclerViewAdapter;
import com.example.grocerystore.util.Product;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private FirebaseAuth firebaseAuth;
    private RecyclerView recyclerView;
    private ItemRecyclerViewAdapter itemRecyclerViewAdapter;
    private ArrayList<Product> productList;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private CollectionReference collectionReference ;
    private ProgressBar progressBar;
    private SearchView searchView;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        collectionReference = db.collection("Products");


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        progressBar = (ProgressBar)  view.findViewById(R.id.progressbar);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        progressBar.setVisibility(View.VISIBLE);

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
                            itemRecyclerViewAdapter = new ItemRecyclerViewAdapter(getActivity(), productList);
                            recyclerView.setAdapter(itemRecyclerViewAdapter);
//                            List<Product> p = queryDocumentSnapshots.toObjects(Product.class);
//                            productList.addAll(p);
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });
        Log.d("Size", "onCreate: "+productList.size());
        return view;
    }

}
