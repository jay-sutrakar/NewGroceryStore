package com.example.grocerystore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class UserProfileActivity extends AppCompatActivity {
    private ImageView profileImage;
    final private int galleryPick = 1000;
    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private Button changePassword;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        profileImage = (ImageView) findViewById(R.id.user_profile_image);
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        changePassword = (Button) findViewById(R.id.user_profile_change_password);
        user = firebaseAuth.getCurrentUser();

//        StorageReference profileReference = storageReference.child("Customers/" + firebaseAuth.getCurrentUser().getUid() + "profile.jpg");
//        profileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//                Picasso.get().load(uri).into(profileImage);
//            }
//        });

//        profileImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent openGallleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(openGallleryIntent, galleryPick);
//            }
//        });

//        changePassword.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final EditText resetPassword = new EditText(v.getContext());
//                final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
//                passwordResetDialog.setTitle("Change Password");
//                passwordResetDialog.setMessage("Enter new password");
//                passwordResetDialog.setView(resetPassword);
//
//                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        String newPassword = resetPassword.getText().toString();
//                        user.updatePassword(newPassword).addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void aVoid) {
//                                Toast.makeText(UserProfileActivity.this, "Password changed successfully", Toast.LENGTH_SHORT).show();
//
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Toast.makeText(UserProfileActivity.this, "Failed...", Toast.LENGTH_SHORT).show();
//                                Log.d("Passoword changing:", "Failed with exception: " + e.toString());
//                            }
//                        });
//                    }
//                });
//                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                });
//
//                passwordResetDialog.create().show();
//            }
//        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == galleryPick && resultCode == Activity.RESULT_OK) {
            Uri imageUri = data.getData();
            profileImage.setImageURI(imageUri);

            uploadImageToFirebaseStorage(imageUri);
        }
    }

    private void uploadImageToFirebaseStorage(Uri imageUri) {
        final StorageReference fileReference = storageReference.child("Customers/" + firebaseAuth.getCurrentUser().getUid() + "profile.jpg");
        fileReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(profileImage);
                    }
                });
                Toast.makeText(UserProfileActivity.this, "Image uploaded Successfully...", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UserProfileActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                Log.d("UserProfileActivity", "Failed with Exception" + e.toString());
            }
        });
    }
}
