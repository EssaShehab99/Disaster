package com.example.form;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class MainActivity2 extends AppCompatActivity {
   AlertDialog.Builder builder;
    EditText Name,number_Family,number_Relative,Email;
    Button Save , logout;
    String userId;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirebaseUser user;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        user = fAuth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();
      userId = fAuth.getCurrentUser().getUid();

        Name= findViewById(R.id.Names);
        number_Family = findViewById(R.id.Numberid_Family);
        number_Relative = findViewById(R.id.Numberid_Relative);
        Email = findViewById(R.id.Emailid);
        Save = findViewById(R.id.bu_Editid);
        logout = findViewById(R.id.Logout);
    builder = new AlertDialog.Builder(this);

//        DocumentReference documentReference = fStore.collection("users").document(userId);
//        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
//                if (documentSnapshot.exists()) {
//                    Name.setText(documentSnapshot.getString("Namep"));
//                    number_Family.setText(documentSnapshot.getString("Familyp"));
//                    number_Relative.setText(documentSnapshot.getString("Relativep"));
//                    Email.setText(documentSnapshot.getString("emailp"));
//
//                }
//            }
//        });
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel channel = new NotificationChannel("My Notification", "My Notification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager= getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Name.getText().toString().isEmpty() ||number_Family .getText().toString().isEmpty() || number_Relative.getText().toString().isEmpty()||Email.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity2.this, "One or Many fields are empty.", Toast.LENGTH_SHORT).show();
                    return;
                }

                final String email = Email.getText().toString();
                user.updateEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        DocumentReference docRef = fStore.collection("users").document(user.getUid());
                        Map<String,Object> edited = new HashMap<>();
                        edited.put("emailp",Email);
                        edited.put("Namep",Name.getText().toString());
                        edited.put("Familyp",number_Family.getText().toString());
                        edited.put("Relativep",number_Relative.getText().toString());
                        docRef.update(edited).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(MainActivity2.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(),MainActivity2.class));
                                Toast.makeText(MainActivity2.this,"Email is changed.", Toast.LENGTH_SHORT).show();
                                finish();
                      }
                        });
                        Toast.makeText(MainActivity2.this, "Email is changed.", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity2.this,"please. Log in again from the (Login) page before modifying the data in order for the process to succeed.", Toast.LENGTH_SHORT).show();
                    }
                });
//e.getMessage()

                NotificationCompat.Builder  builder = new NotificationCompat.Builder(MainActivity2.this,"My Notification");
                builder.setContentTitle("Disaster mangement system");
                builder.setContentText("Your email is changed");
                builder.setSmallIcon(R.drawable.nontify3);
                builder.setAutoCancel(true);

                NotificationManagerCompat managerCompat = NotificationManagerCompat.from(MainActivity2.this);
                managerCompat.notify(1,builder.build());

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                openDialog();
                builder.setMessage("Do you want to logout?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                FirebaseAuth.getInstance().signOut();
                              startActivity(new Intent(getApplicationContext(),Activity_login.class));
                                finish();

                            }
                        })
                     .setNegativeButton("No", new DialogInterface.OnClickListener() {
                         @Override
                         public void onClick(DialogInterface dialogInterface, int i) {
                             dialogInterface.cancel();
                         }
                     });
                AlertDialog alert = builder.create();
                alert.setTitle("Logout");
                alert.show();
            }
        });



        //end popup
    }

//    public  void openDialog()
//    {
//
//    }

//    public void logout(View view) {
//        FirebaseAuth.getInstance().signOut();
//        startActivity(new Intent(getApplicationContext(),Activity_login.class));
//    }


}