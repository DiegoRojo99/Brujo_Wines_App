package com.youngdred.brujowines;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class PasswordOlvidadaActivity extends AppCompatActivity {

    private EditText emailEt;
    private ImageView logo;
    private Button resetPasswordButton;
    private ProgressBar progressBar;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_olvidada);

        emailEt=(EditText) findViewById(R.id.et_password_olvidada_email);
        resetPasswordButton=(Button) findViewById(R.id.btn_password_olvidada_restablecer);
        progressBar=(ProgressBar) findViewById(R.id.progressBarPasswordOlvidada);

        auth=FirebaseAuth.getInstance();

        logo=(ImageView)findViewById(R.id.iv_password_olvidada_app_name);
        logo.setOnClickListener(view -> startActivity(new Intent(PasswordOlvidadaActivity.this, LoginActivity.class)));

        resetPasswordButton.setOnClickListener(v -> resetPassword());
    }
    
    private void resetPassword(){
        String email=emailEt.getText().toString().trim();
        if(email.isEmpty()){
            emailEt.setError("Se requiere un email");
            emailEt.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEt.setError("Introduce un email válido");
            emailEt.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(PasswordOlvidadaActivity.this,
                            "Comprueba tu correo",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(PasswordOlvidadaActivity.this,
                            "Algo ha fallado",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}