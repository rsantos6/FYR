package com.github.fyr;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterUser extends AppCompatActivity implements View.OnClickListener {
    protected Button buttonRegister;
    protected EditText editTextEmail;
    protected EditText editTextPassword;
    protected TextView textViewSignin;
    protected ProgressDialog progressDialog;
    protected FirebaseAuth firebaseAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        this.firebaseAuth = FirebaseAuth.getInstance();

        this.progressDialog = new ProgressDialog(this);

        this.buttonRegister = (Button) findViewById(R.id.buttonRegister);

        this.editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        this.editTextPassword = (EditText) findViewById(R.id.editTextPassword);

        this.textViewSignin = (TextView) findViewById(R.id.textViewSignin);

        this.buttonRegister.setOnClickListener(this);
        this.textViewSignin.setOnClickListener(this);
    }

    public void registerUser(){
        String email = this.editTextEmail.getText().toString().trim();
        String password = this.editTextPassword.getText().toString().trim();
        if (TextUtils.isEmpty(email)){
            //email is empty
            Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show();
            return;//stop from further execution
        }
        if (TextUtils.isEmpty(password)) {
            //password is empty
            Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show();
            return;//stop from further execution
        }
        this.progressDialog.setMessage("Registering User...");
        progressDialog.show();
        this.firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //user successful registered
                            Toast.makeText(RegisterUser.this, "Successfully registered!", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(RegisterUser.this, "Failed to register. Please try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onClick(View view) {
        if (view == this.buttonRegister){
            this.registerUser();
        }

        if (view == this.textViewSignin){
            //will open login activity here
        }
    }
}
