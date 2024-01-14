package com.rula.cloudlib;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rula.cloudlib.obj.User;

public class MainActivity extends AppCompatActivity {

    private boolean regscr;

    private Button buttondown;
    private Button buttonup;
    private EditText inputname;
    private EditText inputmail;
    private EditText inputpassword;
    private EditText inputrepassword;
    private EditText inputid;
    private View vmarg;

    private FirebaseAuth Auth;
    private DatabaseReference dbr;

    private final String TAG = "Verification";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = Auth.getCurrentUser();
        if(currentUser != null){
            startActivity(new Intent(this, PrimaryActivity.class));
            finish();
        }
        setContentView(R.layout.activity_main);

        inputname = findViewById(R.id.inputname);
        inputmail = findViewById(R.id.inputmail);
        inputpassword = findViewById(R.id.inputpassword);
        inputrepassword = findViewById(R.id.inputreppassword);
        inputid = findViewById(R.id.inputid);
        vmarg = findViewById((R.id.vmarg));
        regscr = false;
        buttonup = findViewById(R.id.buttonup);
        buttondown = findViewById(R.id.buttondown);

        buttondown.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if((regscr)){
                    buttondown.setText("Registration");
                    buttonup.setText("SignIn");
                    vmarg.setVisibility(View.GONE);
                    inputname.setVisibility(View.GONE);
                    inputrepassword.setVisibility(View.GONE);
                    inputid.setVisibility(View.GONE);
                    regscr = false;
                }else{
                    buttondown.setText("SignIn");
                    buttonup.setText("Registration");
                    vmarg.setVisibility(View.VISIBLE);
                    inputname.setVisibility(View.VISIBLE);
                    inputrepassword.setVisibility(View.VISIBLE);
                    inputid.setVisibility(View.VISIBLE);
                    regscr = true;
                }
            }
        });
        buttonup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (regscr){
                    if(!inputname.getText().equals("") &&
                            !inputmail.getText().equals("") &&
                            !inputpassword.getText().equals("") &&
                            !inputrepassword.getText().equals("") &&
                            !inputid.getText().equals("")) {
                        createUser(inputmail.getText().toString(), inputpassword.getText().toString());
                    }else{
                        Toast.makeText(MainActivity.this, "Заполните все поля", Toast.LENGTH_LONG).show();
                    }
                }else{
                    if(!inputmail.getText().equals("") &&
                            !inputpassword.getText().equals("")) {
                        signIn(inputmail.getText().toString(), inputpassword.getText().toString());
                    }else{
                        Toast.makeText(MainActivity.this, "Заполните все поля", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
    private void createUser(String email, String password){
        Auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = Auth.getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(inputname.getText().toString()).build();
                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "User profile updated.");
                                                writeNewUser(user.getDisplayName(), inputid.getText().toString(), user.getUid());
                                                startActivity(new Intent(MainActivity.this, PrimaryActivity.class));
                                                finish();
                                            }
                                        }
                                    });


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
    private void signIn(String email, String password){
        Auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = Auth.getCurrentUser();
                            startActivity(new Intent(MainActivity.this, PrimaryActivity.class));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
    public void writeNewUser(String name, String id, String uid) {
        User userobj = new User(name, id);
        dbr = FirebaseDatabase.getInstance().getReference();
        dbr.child("users").child(uid).setValue(userobj);
    }

}