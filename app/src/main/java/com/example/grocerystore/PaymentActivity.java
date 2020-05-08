package com.example.grocerystore;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class PaymentActivity extends AppCompatActivity{
    private TextView paymentAmount;
    private Button payButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        payButton = findViewById(R.id.pay);

        paymentAmount = findViewById(R.id.amount);
        paymentAmount.setText(getIntent().getExtras().getString("amount"));
    }



}
