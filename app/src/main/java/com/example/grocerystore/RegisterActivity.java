package com.example.grocerystore;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity {
    private Button createAccountButton;
    private EditText usernameInput, passwordInput, phoneInput, emailInput;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Initializing
        createAccountButton = (Button) findViewById(R.id.register_button);
        usernameInput = (EditText) findViewById(R.id.register_username_input);
        phoneInput = (EditText) findViewById(R.id.register_phone_input);
        passwordInput = (EditText) findViewById(R.id.register_password_input);
        emailInput = (EditText) findViewById(R.id.register_email_input);
        loadingBar = new ProgressDialog(this);

        //On click create account
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount();
            }
        });
    }

    private void createAccount() {
        String username, password, phone, email;

        username = usernameInput.getText().toString();
        phone = phoneInput.getText().toString();
        password = passwordInput.getText().toString();
        email = emailInput.getText().toString();

        //check if credentials are empty
        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, "Please Enter your username", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "Please Enter your phone", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "PLease Enter your email", Toast.LENGTH_SHORT).show();
        } else {
            loadingBar.setTitle("Creating Account");
            loadingBar.setMessage("Please wait while we are checking the credentials");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            validateUser(username, phone, password, email);
        }
    }

    //write your code here for adding customer into database
    private void validateUser(final String username, final String phone, final String password, final String email) {

    }
}
