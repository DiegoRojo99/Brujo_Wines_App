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
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView banner;
    private Button registrarUsuario;
    private EditText etNombre, etEmail, etPassword;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        banner = (TextView) findViewById(R.id.tv_register_app_name);
        banner.setOnClickListener(this);

        registrarUsuario=(Button)findViewById(R.id.btn_registrar_brujo_wines_registrar);
        registrarUsuario.setOnClickListener(this);

        etNombre=(EditText) findViewById(R.id.et_register_nombre);
        etEmail=(EditText) findViewById(R.id.et_register_email);
        etPassword=(EditText) findViewById(R.id.et_register_password);

        progressBar=(ProgressBar) findViewById(R.id.progressBarRegistrar);
    }

    @Override
    public void onClick(View view) {

        if(view.getId()==R.id.tv_register_app_name){
            startActivity(new Intent(this, MainActivity.class));
        }else if(view.getId()==R.id.btn_registrar_brujo_wines_registrar){
            registrarUsuario();
        }
    }

    public void registrarUsuario(){
        String nombre=etNombre.getText().toString().trim();
        String email=etEmail.getText().toString().trim();
        String password=etPassword.getText().toString().trim();

        if(nombre.isEmpty()){
            etNombre.setError("Se requiere un nombre");
            etNombre.requestFocus();
            return;
        }
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
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Usuario usuario= new Usuario(nombre, email, password);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(usuario).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(RegisterActivity.this, "Usuario registrado correctamente", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.VISIBLE);

                                        //Dirigir a login
                                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                    }else{
                                        Toast.makeText(RegisterActivity.this, "No se ha podido registrar al usuario", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.VISIBLE);
                                    }
                                }
                            });
                        }else{
                            Toast.makeText(RegisterActivity.this, "No se ha podido registrar al usuario",Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }
}