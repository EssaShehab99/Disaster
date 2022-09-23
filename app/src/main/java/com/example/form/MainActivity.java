package com.example.form;

import static android.app.PendingIntent.getActivity;
import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
   public static final String TAG = "TAG";

   EditText Name,number_Family,number_Relative,Email,Password;
   Button bu_register;
   Button bu_login;
   FirebaseAuth fAuth;
   ProgressBar progressBar;
    FirebaseFirestore fStore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Name=findViewById(R.id.editTextTextPersonName);
        number_Family=findViewById(R.id.Numberid_Family);
        number_Relative=findViewById(R.id.Numberid_Relative);
        Email=findViewById(R.id.Emailid);
        Password=findViewById(R.id.Passwordid);
        bu_register=findViewById(R.id.bu_Registerid);
        bu_login= (Button) findViewById(R.id.bu_loginid);

         fAuth= FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progressBar);


        if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),MainActivity2.class));
            finish();
        }


        bu_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 String email = Email.getText().toString().trim();
                 String password = Password.getText().toString().trim();
                 String name = Name.getText().toString();
                 String Family    = number_Family.getText().toString();
                 String Relative    = number_Relative.getText().toString();



                if(TextUtils.isEmpty(email)){
                   Email.setError("Email is Required.");
                 return;
            }

                if(TextUtils.isEmpty(password)){
                   Password.setError("Password is Required.");
                    return;
                }

                if(password.length() < 6){
                    Password.setError("Password Must be greater than 6 Characters");
                   return;
                }

                if(TextUtils.isEmpty(name)){
                    Name.setError("Name is Required.");
                    return;
                }

                if(TextUtils.isEmpty(Family)){
                    number_Family.setError("number of Family is Required.");
                    return;
                }
                if(TextUtils.isEmpty(Relative)){
                    number_Relative.setError("number of Relative is Required.");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);


                // register the user in firebase
            fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        // send verification link
                        progressBar.setVisibility(View.GONE);

                                    // send verification link
//
//                        FirebaseUser fuser = fAuth.getCurrentUser();
//                        fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void aVoid) {
//                                Toast.makeText(MainActivity.this, "Verification Email Has been Sent.", Toast.LENGTH_SHORT).show();
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Log.d(TAG, "onFailure: Email not sent " + e.getMessage());
//                            }
//                        });




                        Toast.makeText( MainActivity.this, "Verification Email Has been Sent.", Toast.LENGTH_SHORT).show();
                        userID = fAuth.getCurrentUser().getUid();
                        DocumentReference documentReference = fStore.collection("users").document(userID);
                        Map<String,Object> user = new HashMap<>();
                        user.put("Namep",name);
                        user.put("emailp",email);
                        user.put("Familyp",Family);
                        user.put("Relativep",Relative);
                        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d(TAG, "onSuccess: user Profile is created for "+ userID);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "onFailure: " + e.toString());
                            }
                        });
                        Intent intent = new Intent(MainActivity.this,WeatherAndDisasterActivity.class);
                        startActivity(intent);

                    }else{
                        Toast.makeText( MainActivity.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);


                    }
                }
            });


            }
        });


        bu_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
       Intent intent = new Intent(MainActivity.this, Activity_login.class);
       startActivity(intent);
            }
        });

    }

}


//    public void onClick(View view) {
//        Intent intent = new Intent(MainActivity.this, Activity_login.class);
//        startActivity(intent);
//    }













