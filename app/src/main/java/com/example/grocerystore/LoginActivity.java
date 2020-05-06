package com.example.grocerystore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grocerystore.prevalent.Prevalent;
import com.example.grocerystore.util.UserApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {
    private EditText email;
    private EditText password;
    private Button loginButton;
    private ProgressDialog loadingBar;
    private FirebaseAuth firebaseAuth;
    private TextView createNewAccount;
    private FirebaseFirestore db;
    private CollectionReference collectionReference;
    private CheckBox rememberMeCheckBox;

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
        rememberMeCheckBox = (CheckBox) findViewById(R.id.login_remember_me_checkbox);

        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection("Customers");

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
                    Toast.makeText(LoginActivity.this, "Fields Should not be Empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void getVerified(String useremail, String userpassword) {
        if (rememberMeCheckBox.isChecked()) {
            Paper.book().write(Prevalent.customerEmail, useremail);
            Paper.book().write(Prevalent.customerPassword, userpassword);
        }

        firebaseAuth.signInWithEmailAndPassword(useremail,userpassword)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        final FirebaseUser user = authResult.getUser();
                        collectionReference.whereEqualTo("userId",user.getUid())
                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                        if(e != null ){
                                            Log.d("LoginActivity", "onEvent: "+ e.getMessage() );
                                        }
                                        if(!queryDocumentSnapshots.isEmpty()){
                                            for(QueryDocumentSnapshot snapshot : queryDocumentSnapshots){
                                                UserApi userApi = UserApi.getInstance();
                                                userApi.setUsername(snapshot.getString("username"));
                                                userApi.setUserEmail(snapshot.getString("email"));
                                                userApi.setUserId(snapshot.getString("userId"));
                                                userApi.setUserContactNumber(snapshot.getString("phone"));
                                            }
                                            loadingBar.dismiss();
                                            startActivity(new Intent(LoginActivity.this, MainActivity2.class));
                                            finish();
                                        }

                            }
                        });
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
