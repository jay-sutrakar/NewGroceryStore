package com.example.grocerystore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private Button createAccountButton;
    private EditText usernameInput, passwordInput, phoneInput, emailInput;
    private ProgressDialog loadingBar;
    private FirebaseAuth firebaseAuth;
    private TextView signInInstead;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Customers");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Initialization of firebaseauth
        firebaseAuth = FirebaseAuth.getInstance();
        Log.d("FirebaseAuth instance ", "onCreate: " + firebaseAuth);

        //Initializing
        createAccountButton = (Button) findViewById(R.id.register_button);
        usernameInput =  (EditText) findViewById(R.id.register_username_input);
        phoneInput = (EditText) findViewById(R.id.register_phone_input);
        passwordInput = (EditText) findViewById(R.id.register_password_input);
        emailInput = (EditText) findViewById(R.id.register_email_input);
        signInInstead = (TextView) findViewById(R.id.register_signin);
        loadingBar = new ProgressDialog(this);

        signInInstead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

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
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Map<String, Object> user = new HashMap<>();
                        user.put("userId", firebaseAuth.getUid());
                        user.put("username", username);
                        user.put("email", email);
                        user.put("phone", phone);
                        user.put("password", password);

                        // Here we are adding the details of user and his id in firestore cloud database
                        collectionReference.add(user)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        loadingBar.dismiss();
                                        Toast.makeText(RegisterActivity.this, "Account created", Toast.LENGTH_SHORT)
                                                .show();
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Error in sign in ", "onFailure: "+e.getMessage());
                    }
                });
    }
}
