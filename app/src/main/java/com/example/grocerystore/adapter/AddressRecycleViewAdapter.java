package com.example.grocerystore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grocerystore.R;
import com.example.grocerystore.util.UserAddress;

import java.util.List;

public class AddressRecycleViewAdapter extends RecyclerView.Adapter<AddressRecycleViewAdapter.ViewHolder> {
    private Context context;
    private List<UserAddress> userAddressList;
    private OnItemClickListener listener;

    public interface OnItemClickListener{
        void OnItemClicked(UserAddress address);
    }
    public AddressRecycleViewAdapter(Context context, List<UserAddress> userAddressList,OnItemClickListener listener) {
        this.listener = listener;
        this.context = context;
        this.userAddressList = userAddressList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.address_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserAddress userAddress = userAddressList.get(position);
        holder.deleveryName.setText(userAddress.getName());
        holder.deleveryZipcode.setText(userAddress.getPinCode());
        holder.deleveryAddress.setText(userAddress.getAddressline());
        holder.bind(userAddress,listener);
    }

    @Override
    public int getItemCount() {
        return userAddressList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView deleveryName;
        private TextView deleveryAddress;
        private TextView deleveryZipcode;
        private TextView delveryNumber;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            deleveryAddress = itemView.findViewById(R.id.addressdescription);
            deleveryName = itemView.findViewById(R.id.delveryname);
            deleveryZipcode = itemView.findViewById(R.id.deleveryZipcode);

        }

        public void bind(final UserAddress userAddress, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.OnItemClicked(userAddress);
                }
            });
        }
    }
}
