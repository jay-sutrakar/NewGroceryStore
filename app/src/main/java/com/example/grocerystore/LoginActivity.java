package com.example.grocerystore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private EditText email;
    private EditText password;
    private Button loginButton;
    private ProgressDialog loadingBar;
    private FirebaseAuth firebaseAuth;
    private TextView createNewAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        //Initialization
        email = (EditText) findViewById(R.id.login_email_input);
        password = (EditText) findViewById(R.id.login_password_input);
        loginButton = (Button) findViewById(R.id.login_button);
        loadingBar = new ProgressDialog(this);
        createNewAccount = (TextView) findViewById(R.id.login_register);

        createNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingBar.setTitle("Logging in");
                loadingBar.setMessage("Please wait while we are checking the credentials");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();

                String useremail = email.getText().toString().trim();
                String userpassword = password.getText().toString().trim();

                if (!TextUtils.isEmpty(useremail) && !TextUtils.isEmpty(userpassword)) {
                    getVerified(useremail, userpassword);
                } else {
                    loadingBar.dismiss();
                    Toast.makeText(LoginActivity.this, "Fields Should be Empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void getVerified(String useremail, String userpassword) {
        firebaseAuth.signInWithEmailAndPassword(useremail,userpassword)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        loadingBar.dismiss();
                        startActivity(new Intent(LoginActivity.this, ItemListActivity.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loadingBar.dismiss();
  //              Snackbar.make(LoginActivity.this,"Invalid Credentials ",BaseTransientBottomBar.LENGTH_SHORT).show();
                Toast.makeText(LoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
