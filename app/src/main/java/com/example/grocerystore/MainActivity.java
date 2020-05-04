package com.example.grocerystore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.grocerystore.prevalent.Prevalent;
import com.example.grocerystore.util.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import au.com.bytecode.opencsv.CSVReader;
import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {
    private Button mainLoginButton, mainJoinNowButton;
    private ProgressDialog loadingBar;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainJoinNowButton = (Button) findViewById(R.id.main_join_now_btn);
        mainLoginButton = (Button) findViewById(R.id.main_login_btn);
        loadingBar = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        Paper.init(this);

        mainJoinNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        mainLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        String userEmailKey = Paper.book().read(Prevalent.customerEmail);
        String userPasswordKey = Paper.book().read(Prevalent.customerPassword);

        if (userEmailKey != "" && userPasswordKey != "") {
            if (!TextUtils.isEmpty(userEmailKey) && !TextUtils.isEmpty(userPasswordKey)) {
                allowAccess(userEmailKey, userPasswordKey);
                loadingBar.setTitle("Already Logged in");
                loadingBar.setMessage("Please wait...");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
            }
        }


    }

    private void allowAccess(String useremail, String userpassword) {
        firebaseAuth.signInWithEmailAndPassword(useremail, userpassword)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        loadingBar.dismiss();
                        startActivity(new Intent(MainActivity.this, ItemListActivity.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loadingBar.dismiss();
                //              Snackbar.make(LoginActivity.this,"Invalid Credentials ",BaseTransientBottomBar.LENGTH_SHORT).show();
                Toast.makeText(MainActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            startActivity(new Intent(this,ItemListActivity.class));
            finish();
        }
    }
}
