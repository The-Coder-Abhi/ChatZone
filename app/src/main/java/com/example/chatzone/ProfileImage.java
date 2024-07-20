package com.example.chatzone;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class ProfileImage extends AppCompatActivity {

    private CardView mgetImage;
    private ImageView mUserImage;
    private TextView Skip;
    private static int PICK_IMAGE=123;
    private Uri imagePath;
    private Button mUploadImage;
    private FirebaseAuth mAuth;
    private FirebaseStorage fbStorage;
    private StorageReference fbStorageRef;
    private String ImageUriAccessToken;
    private FirebaseFirestore fbStore;
    FirebaseUser mUser;
    ProgressBar PB;
    DatabaseReference DbRef;
    ActivityResultLauncher<String> mPick_IMG;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_image);
        PB=findViewById(R.id.progressBar03);
        mAuth = FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        fbStorage = FirebaseStorage.getInstance();
        fbStorageRef = fbStorage.getReference();
        fbStore = FirebaseFirestore.getInstance();
        mgetImage = findViewById(R.id.UpladImageCardview);
        mUserImage = findViewById(R.id.UpladImage);
        mUploadImage = findViewById(R.id.Upload);
        Skip = findViewById(R.id.skip);
        mPick_IMG = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                mUserImage.setImageURI(result);
                imagePath = result;
            }
        });

        mgetImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPick_IMG.launch("image/*");
            }
        });

        mUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imagePath==null)
                {
                    Toast.makeText(ProfileImage.this, "Select Image", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    PB.setVisibility(View.VISIBLE);
                    sendImage();
                    PB.setVisibility(View.GONE);
                    Intent i = new Intent(ProfileImage.this,MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });

        Skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(ProfileImage.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void sendImage() {
        sendImagetoStorage();
    }

    private void sendImagetoStorage() {
        StorageReference imgref = fbStorageRef.child("Images").child(mAuth.getUid()).child("Profile Pic");

        //Image compression

        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imagePath);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,25,byteArrayOutputStream);
        byte[] data = byteArrayOutputStream.toByteArray();

        UploadTask uploadTask = imgref.putBytes(data);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imgref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        ImageUriAccessToken=uri.toString();
                        Toast.makeText(ProfileImage.this, "get Uri Success", Toast.LENGTH_SHORT).show();
                        DbRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mUser.getUid());
                        HashMap<String,Object> hashMap = new HashMap<>();
                        hashMap.put("image_url",ImageUriAccessToken);
                        DbRef.updateChildren(hashMap);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ProfileImage.this, "Failed to get Uri", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProfileImage.this, "Image Upload Failure", Toast.LENGTH_SHORT).show();
            }
        });
    }

   /* private void sendDataToFireStore() {

        DocumentReference documentReference = fbStore.collection("Users").document(mAuth.getUid());
        Map<String , Object> userdata = new HashMap<>();
        userdata.put("id",User.getId());
        userdata.put("Email",User.getEmail());
        userdata.put("User_Name",User.getUsername());
        userdata.put("First_Name",User.getFirst_Name());
        userdata.put("Last_Name",User.getLast_Name());
        userdata.put("Birth_Date",User.getBirth_Date());
        userdata.put("Phone_Number",User.getPhone_Number());
        userdata.put("image_url",User.getURL());
        userdata.put("status",User.getStatus());
        userdata.put("search",User.getSearch());

        documentReference.set(userdata).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(ProfileImage.this, "Data Stored on the Firestore ", Toast.LENGTH_SHORT).show();
            }
        });

    }*/

   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode==PICK_IMAGE && requestCode==RESULT_OK)
        {
            imagePath = data.getData();
            mUserImage.setImageURI(imagePath);
        }
        else {
            Toast.makeText(this, "Unsuccessfull", Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }*/



}