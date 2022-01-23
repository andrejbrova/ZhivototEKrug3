package com.example.zhivototekrug3;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText mFullName, mEmail, mPassword, mPassword2, mPhone;
    Button mRegisterBtn;
    TextView mLoginBtn;
    private FirebaseAuth fAuth;
    Spinner spinnerTypes;
    String selected_item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFullName = findViewById(R.id.fullName);
        mPhone = findViewById(R.id.phone);
        mEmail = findViewById(R.id.Email);
        mPassword = findViewById(R.id.password);
        mPassword2 = findViewById(R.id.password2);
        mRegisterBtn = findViewById(R.id.registerButton);
        mLoginBtn = findViewById(R.id.createText);
        spinnerTypes = findViewById(R.id.spinnerTypes);
        fAuth = FirebaseAuth.getInstance();

        if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        spinnerTypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_item = spinnerTypes.getSelectedItem().toString();
                ((TextView) parent.getChildAt(0)).setTextColor(Color.YELLOW);
                ((TextView) parent.getChildAt(0)).setTextSize(14);
                Toast.makeText(Register.this, "chosen: "+selected_item, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mRegisterBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String fullName = mFullName.getText().toString().trim();
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String password2 = mPassword2.getText().toString().trim();
                String type = spinnerTypes.getSelectedItem().toString();
                String number = mPhone.getText().toString().trim();
                int selected_item_position = spinnerTypes.getSelectedItemPosition();


                if (TextUtils.isEmpty(email)){
                    mEmail.setError("Email is Required");
                    return;
                }

                if (TextUtils.isEmpty(password)){
                    mPassword.setError("Password is required");
                }

                if (password.length() < 8){
                    mPassword.setError("Password must be more or equal to 8 characters!");
                    return;
                }

                if (password == password2){
                    mPassword.setError("Password does not match");
                    return;
                }



                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Register.this, "User Created", Toast.LENGTH_SHORT).show();

                            User user = new User(fullName,email,type,number);
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(Register.this, "User has been registered successfully!", Toast.LENGTH_LONG).show();
                                        if(selected_item_position == 0){
                                            Toast.makeText(getApplicationContext(), "This is Adult Activity", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(getApplicationContext(), Adult.class));
                                        }
                                        else if (selected_item_position == 1){
                                            Toast.makeText(getApplicationContext(), "This is Volunteer Activity", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(getApplicationContext(), Volunteer.class));
                                        }
                                        else{
                                            Toast.makeText(getApplicationContext(), "This is Organizer Activity", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(getApplicationContext(), Organizer.class));
                                        }
                                    }
                                    else
                                    {
                                        Toast.makeText(Register.this, "User has not been registered successfully, try again !", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });


                        }
                        else{
                            Toast.makeText(Register.this, "Error!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });


            }
        });





        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        ((TextView) parent.getChildAt(0)).setTextColor(Color.YELLOW);
        ((TextView) parent.getChildAt(0)).setTextSize(14);
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = fAuth.getCurrentUser();
        if(currentUser != null){
            currentUser.reload();
        }
    }

}