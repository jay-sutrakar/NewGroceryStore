package com.example.grocerystore.adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grocerystore.R;
import com.example.grocerystore.util.Product;

import java.util.List;

public class CartRecyclerViewAdapter extends RecyclerView.Adapter<CartRecyclerViewAdapter.ViewHolder> {
    private Context context;
    private List<Product> productList;
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;

    public CartRecyclerViewAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_item_cart,parent,false);
        return new ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product=productList.get(position);
        Log.d("CartRecyclerViewAdapter", "onBindViewHolder: " + product.getProductName());
        holder.productName.setText(product.getProductName());
        holder.productPrice.setText(product.getProductPrice());
        holder.quantity.setText("1");
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView productName;
        private ProgressDialog progressDialog;
        private TextView productPrice;
        private TextView quantity;
        private ImageButton deleteButton;


        public ViewHolder(@NonNull View itemView,Context context) {
            super(itemView);
            productName = itemView.findViewById(R.id.product_name);
            productPrice = itemView.findViewById(R.id.product_price);
            quantity = itemView.findViewById(R.id.product_quantity);
            deleteButton = itemView.findViewById(R.id.product_delete);
            progressDialog = new ProgressDialog(context);
            deleteButton.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            progressDialog.setTitle("Removing...");
            progressDialog.show();

            // Switch case is use to work for all click events in viewholder

            switch (v.getId()) {
                case R.id.product_delete :
                    builder = new AlertDialog.Builder(context);

                    // here we are getting the view of conformation layout

                    View view = LayoutInflater.from(context)
                            .inflate(R.layout.conformation_dialog,null);

                    //yes and no buttons from conformation_dailog layout

                    Button yes = view.findViewById(R.id.conf_yes_button);
                    Button no = view.findViewById( R.id.conf_no_button);

                    // setting alertDialog with builder

                    builder.setView(view);
                    alertDialog = builder.create();
                    alertDialog.show();

                    //Here two case arises either user choose yes or no
                    yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            removeItem(getAdapterPosition());
                            alertDialog.dismiss();
                            progressDialog.dismiss();
                        }
                    });
                    no.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                            progressDialog.dismiss();
                        }
                    });
                    break;
            }

        }


        //Function to remove product from productlist

        private void removeItem(int adapterPosition) {
            Product product = productList.get(adapterPosition);
            productList.remove(product);

            notifyItemRemoved(adapterPosition);
        }
    }
}
