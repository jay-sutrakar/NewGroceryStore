package com.example.grocerystore.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grocerystore.R;
import com.example.grocerystore.util.Product;

import java.util.List;

public class OrderSummaryRecyclerView extends RecyclerView.Adapter<OrderSummaryRecyclerView.ViewHolder> {
    private Context context;
    private List<Product> productList;
    private OnAmountChangeListener listener;
    public interface OnAmountChangeListener{
         void amountChange(String amount);
    }
    public OrderSummaryRecyclerView(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
        try {
            listener = (OnAmountChangeListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(e+"Must Implement OrderSummaryRecyclerView.OnAmountChangeListener");
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.order_summary_activity_recyclerview_row,parent,false);
        return new ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.productName.setText(product.getProductName());
        holder.productBrand.setText(product.getProductBrand());
        holder.price.setText(product.getProductPrice());
        holder.discountPrice.setText(product.getProductDiscountPrice());
//        holder.quantity.setText("1");
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView price;
        private TextView discountPrice;
        private TextView productName;
        private TextView productBrand;
        private TextView quantity;
        private Button removeButton;

        public ViewHolder(@NonNull View itemView,Context context) {
            super(itemView);
            price = itemView.findViewById(R.id.product_price);
            discountPrice = itemView.findViewById(R.id.product_discount_price);
            productName = itemView.findViewById(R.id.product_name);
            productBrand = itemView.findViewById(R.id.product_brand);
            quantity = itemView.findViewById(R.id.product_quantity);
            removeButton = itemView.findViewById(R.id.product_remove);

            removeButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.product_remove:
                    Product p = productList.get(getAdapterPosition());
                    productList.remove(p);
                    listener.amountChange(p.getProductDiscountPrice());
                    notifyItemRemoved(getAdapterPosition());
                    break;
            }
        }
    }
}
