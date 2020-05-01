package com.example.grocerystore.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grocerystore.R;
import com.example.grocerystore.adapter.AddressRecycleViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class AddressDialog extends AppCompatDialogFragment {
    private EditText name;
    private EditText address;
    private EditText city;
    private EditText state;
    private EditText zipcode;
    private EditText phonenumber;
    private EditText landmark;
    private Button saveAddress;
    private Button addNewAddressButtton;
    private Context context;
    private RecyclerView recyclerView;
    private AddressRecycleViewAdapter addressRecycleViewAdapter;
    private List<UserAddress> userAddressList;
    private UserAddress userAddress;
    private AddressDialogListener listener;


    public interface AddressDialogListener{
        void OnAddressSelection(UserAddress userAddress);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (AddressDialogListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(e+"Must Implement AddressDialogListner ");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        context = getContext();
        userAddressList = new ArrayList<>();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.address_popup,null,false);
        recyclerView = view.findViewById(R.id.addressRecyclerView);
        addNewAddressButtton = view.findViewById(R.id.add_address_button);

        addNewAddressButtton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Opening another dialog for Address filling
                AlertDialog.Builder newbuilder = new AlertDialog.Builder(context);
                LayoutInflater inflater = getActivity().getLayoutInflater();
                final View view = inflater.inflate(R.layout.add_new_address,null,false);
                name = view.findViewById(R.id.name);
                address = view .findViewById(R.id.address);
                landmark =view.findViewById(R.id.landmark);
                city = view.findViewById(R.id.city);
                state = view.findViewById(R.id.state);
                zipcode = view.findViewById(R.id.zipcode);
                phonenumber = view.findViewById(R.id.phoneNumber);

                newbuilder.setView(view).
                        setPositiveButton("SAVE ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        //Creating useraddress object to store the detail of user address
                        userAddress = new UserAddress();
                        userAddress.setName(name.getText().toString().trim());
                        userAddress.setAddressline(address.getText().toString().trim());
                        userAddress.setLandmark(landmark.getText().toString().trim());
                        userAddress.setCity(city.getText().toString().trim());
                        userAddress.setState(state.getText().toString().trim());
                        userAddress.setPinCode(zipcode.getText().toString().trim());
                        userAddress.setPhonenumber(phonenumber.getText().toString().trim());
                        addNewAddress(userAddress);
                    }
                });
                 newbuilder.create().show();
            }
        });
        Log.d("outside", "onClick: "+ name);

        builder.setView(view);
        return builder.create();
    }

    public void addNewAddress(final UserAddress userAddress) {
        userAddressList.add(userAddress);
        if(userAddressList.size()>0){

            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            addressRecycleViewAdapter = new AddressRecycleViewAdapter(getContext(), userAddressList, new AddressRecycleViewAdapter.OnItemClickListener() {
                @Override
                public void OnItemClicked(UserAddress address) {
                    Log.d("itemSelected ", "OnItemClicked: "+address.getName());
                     listener.OnAddressSelection(userAddress);
                     dismiss();
                }
            });
            recyclerView.setAdapter(addressRecycleViewAdapter);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }
}
