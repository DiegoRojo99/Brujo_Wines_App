package com.youngdred.brujowines;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
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
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView registrar;
    private EditText etEmail, etPassword;
    private Button loginButton;

    private FirebaseAuth mAuth;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        registrar=(TextView) findViewById(R.id.tv_login_registrar);
        registrar.setOnClickListener(this);

        loginButton=(Button) findViewById(R.id.btn_login_brujo_wines_login);
        loginButton.setOnClickListener(this);

        etEmail=(EditText) findViewById(R.id.et_login_email);
        etPassword=(EditText) findViewById(R.id.et_login_password);

        progressBar=(ProgressBar) findViewById(R.id.progressBarLogin);

        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void onClick(View v){
            if(v.getId()==R.id.tv_login_registrar){
                startActivity(new Intent(this,RegisterActivity.class));
            }else if(v.getId()==R.id.btn_login_brujo_wines_login){
                loginUsuario();
            }

    }

    public void loginUsuario(){
        String email=etEmail.getText().toString().trim();
        String password=etPassword.getText().toString().trim();


        if(email.isEmpty()){
            etEmail.setError("Se requiere un email");
            etEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etEmail.setError("El e-mail debe ser válido");
            etEmail.requestFocus();
            return;
        }
        if(password.isEmpty()){
            etPassword.setError("Se requiere una contraseña");
            etPassword.requestFocus();
            return;
        }
        if(password.length()<6){
            etPassword.setError("La contraseña debe ser de al menos 6 caracteres");
            etPassword.requestFocus();
            return;
        }


        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                }else{
                    Toast.makeText(LoginActivity.this, "No se ha podido iniciar sesión. Comprueba tus datos",Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}