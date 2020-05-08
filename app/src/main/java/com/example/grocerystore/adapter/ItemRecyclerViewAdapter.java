package com.example.grocerystore.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grocerystore.ItemListActivity;
import com.example.grocerystore.MainActivity;
import com.example.grocerystore.R;
import com.example.grocerystore.util.Product;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.api.LogDescriptor;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ItemRecyclerViewAdapter extends RecyclerView.Adapter<ItemRecyclerViewAdapter.ViewHolder> implements Filterable {
    private Context context;
    private List<Product> productList;
    private List<Product> productListAll;

    public ItemRecyclerViewAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
        this.productListAll = new ArrayList<>(productList);
    }

    @NonNull
    @Override
    public ItemRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_item,parent,false);
        return new ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemRecyclerViewAdapter.ViewHolder holder, int position) {
        Product product = productList.get(position);
        Log.d("TAG", "onBindViewHolder: " + product.getProductName());
        holder.productName.setText(product.getProductName());
        holder.productPrice.setText(product.getProductPrice());
        holder.quantity.setText("1");
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Product> filteredProducts = new ArrayList<>();
            if (constraint.toString().isEmpty()) {
                filteredProducts.addAll(productListAll);
            } else {
                for (Product product: productListAll) {
                    if (product.getProductName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        filteredProducts.add(product);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredProducts;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            productList.clear();
            productList.addAll((Collection<? extends Product>) results.values);
            notifyDataSetChanged();
        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView productName;
        private ProgressDialog progressDialog;
        private TextView productPrice;
        private TextView quantity;
        private TextView addedToCartTextView;
        private Button addToCart;
        public ViewHolder(@NonNull View itemView,Context context) {
            super(itemView);

            productName = itemView.findViewById(R.id.product_name);
            productPrice = itemView.findViewById(R.id.product_price);
            quantity = itemView.findViewById(R.id.product_quantity);
            addToCart = itemView.findViewById(R.id.add_to_cart);
            addedToCartTextView = itemView.findViewById(R.id.item_added_to_cart);
            progressDialog = new ProgressDialog(context);
            addToCart.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            progressDialog.setTitle("Adding..");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            Product product = productList.get(getAdapterPosition());
            FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            DocumentReference documentReference = db.collection("Cart")
                    .document(user.getUid()).collection("products").document(product.getProductId());
            documentReference.set(product).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    progressDialog.dismiss();
                    addedToCartTextView.setVisibility(View.VISIBLE);
                    addToCart.setEnabled(false);
                    Log.d("TAG", "onSuccess: item Added");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });

        }
    }
}
