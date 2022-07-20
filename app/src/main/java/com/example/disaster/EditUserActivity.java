package com.example.disaster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class EditUserActivity extends AppCompatActivity {

    EditText Name,number_Family,number_Relative,Email;
    Button Save;
    String userId;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirebaseUser user;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

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


        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Name.getText().toString().isEmpty() ||number_Family .getText().toString().isEmpty() || number_Relative.getText().toString().isEmpty()||Email.getText().toString().isEmpty()){
                    Toast.makeText(EditUserActivity.this, "One or Many fields are empty.", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(EditUserActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(),EditUserActivity.class));
                                Toast.makeText(EditUserActivity.this,"Email is changed.", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
//                        Toast.makeText(EditUserActivity.this, "Email is changed.", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditUserActivity.this,"please. Log in again from the (Login) page before modifying the data in order for the process to succeed.", Toast.LENGTH_SHORT).show();
                    }
                });
//e.getMessage()

            }
        });


    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
    }


}