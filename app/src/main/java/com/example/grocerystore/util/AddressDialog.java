package com.example.grocerystore.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.grocerystore.R;

public class AddressDialog extends AppCompatDialogFragment implements AddNewAddressDialog.AddNewAddressDialogListener{
    private Button addNewAddressButtton;
    private Context context;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        context = getContext();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.address_popup,null,false);

        addNewAddressButtton= view.findViewById(R.id.add_address_button);

        addNewAddressButtton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewAddressDialog();
            }
        });
        builder.setView(view);
        return builder.create();
    }

    private void openNewAddressDialog() {
        AddNewAddressDialog addNewAddressDialog = new AddNewAddressDialog();
        addNewAddressDialog.show(getActivity().getSupportFragmentManager(),"AddNewAddressDialog");
    }

    @Override
    public void addNewAddress(UserAddress userAddress) {

    }
}
