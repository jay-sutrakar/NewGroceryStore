package com.example.grocerystore.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.grocerystore.R;

public class AddNewAddressDialog extends AppCompatDialogFragment {
    private EditText name;
    private EditText address;
    private EditText city;
    private EditText state;
    private EditText zipcode;
    private EditText phonenumber;
    private EditText landmark;
    private Button saveAddress;

    private AddNewAddressDialogListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (AddNewAddressDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()+ "Must implement AddnewAddressDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_new_address,null,false);

        name = view.findViewById(R.id.name);
        address = view .findViewById(R.id.address);
        landmark =view.findViewById(R.id.landmark);
        city = view.findViewById(R.id.city);
        state = view.findViewById(R.id.state);
        zipcode = view.findViewById(R.id.zipcode);
        phonenumber = view.findViewById(R.id.phoneNumber);
        saveAddress = view.findViewById(R.id.saveAddress);


        //Creating useraddress object to store the detail of user address
        final UserAddress userAddress = new UserAddress();
        userAddress.setName(name.getText().toString().trim());
        userAddress.setAddressline(address.getText().toString().trim());
        userAddress.setLandmark(landmark.getText().toString().trim());
        userAddress.setCity(city.getText().toString().trim());
        userAddress.setState(state.getText().toString().trim());
        userAddress.setPinCode(zipcode.getText().toString().trim());
        userAddress.setPhonenumber(phonenumber.getText().toString().trim());

        builder.setView(view).setPositiveButton("SAVE ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.addNewAddress(userAddress);
            }
        });
        return builder.create();
    }
    public interface AddNewAddressDialogListener{
        void addNewAddress(UserAddress userAddress);
    }
}
