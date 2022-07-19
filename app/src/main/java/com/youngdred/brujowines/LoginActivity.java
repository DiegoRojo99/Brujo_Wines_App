package com.youngdred.brujowines;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.BeginSignInResult;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "TAG_TESTING";
    private TextView registrar, passwordOlvidada;
    private EditText etEmail, etPassword;
    private ImageView googleBtn, facebookBtn;
    private Button loginButton;

    private FirebaseAuth mAuth;

    private ProgressBar progressBar;

    private FirebaseUser usuario;

    private SignInClient oneTapClient;
    private BeginSignInRequest signInRequest;
    private static final int REQ_ONE_TAP = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        registrar=findViewById(R.id.tv_login_registrar);
        registrar.setOnClickListener(this);

        loginButton=findViewById(R.id.btn_login_brujo_wines_login);
        loginButton.setOnClickListener(this);

        etEmail=findViewById(R.id.et_login_email);
        etPassword= findViewById(R.id.et_login_password);

        progressBar= findViewById(R.id.progressBarLogin);

        passwordOlvidada=findViewById(R.id.tv_login_password_olvidada);
        passwordOlvidada.setOnClickListener(this);

        googleBtn= findViewById(R.id.google_iv);
        googleBtn.setOnClickListener(this);
        facebookBtn= findViewById(R.id.facebook_iv);
        facebookBtn.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        checkIfUserIsLoggedIn();

    }

    public void checkIfUserIsLoggedIn(){

        try {
            usuario=FirebaseAuth.getInstance().getCurrentUser();
            if (usuario != null && usuario.isEmailVerified()) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v){
            if(v.getId()==R.id.tv_login_registrar){
                startActivity(new Intent(this,RegisterActivity.class));
            }else if(v.getId()==R.id.btn_login_brujo_wines_login){
                loginUsuario();
            }else if(v.getId()==R.id.tv_login_password_olvidada){
                startActivity(new Intent(this, PasswordOlvidadaActivity.class));
            }else if(v.getId()==R.id.google_iv){
                loginGoogle();
            }else if(v.getId()==R.id.facebook_iv){
                loginFacebook();
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


            mAuth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                        if (user != null) {
                            if(user.isEmailVerified() || true){
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            }else{
                                user.sendEmailVerification()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Log.d("TAG_D", "Email sent.");
                                                }
                                            }
                                        });

                                Toast.makeText(LoginActivity.this, "Compruebe su correo para verificar su cuenta",Toast.LENGTH_LONG).show();
                            }
                        }
                    }else{
                        Toast.makeText(LoginActivity.this, "No se ha podido iniciar sesión. Comprueba tus datos",Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                }
            });

        }

    public void loginGoogle(){

        oneTapClient = Identity.getSignInClient(this);
        signInRequest = BeginSignInRequest.builder()
                .setPasswordRequestOptions(BeginSignInRequest.PasswordRequestOptions.builder()
                        .setSupported(true)
                        .build())
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        // Your server's client ID, not your Android client ID.
                        .setServerClientId(getString(R.string.default_web_client_id))
                        // Only show accounts previously used to sign in.
                        .setFilterByAuthorizedAccounts(true)
                        .build())
                // Automatically sign in when exactly one credential is retrieved.
                .setAutoSelectEnabled(true)
                .build();

        oneTapClient.beginSignIn(signInRequest)
                .addOnSuccessListener(this, new OnSuccessListener<BeginSignInResult>() {
                    @Override
                    public void onSuccess(BeginSignInResult result) {
                        try {
                            startIntentSenderForResult(
                                    result.getPendingIntent().getIntentSender(), REQ_ONE_TAP,
                                    null, 0, 0, 0);
                        } catch (IntentSender.SendIntentException e) {
                            Log.e("TAG_TESTING", "Couldn't start One Tap UI: " + e.getLocalizedMessage());
                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // No saved credentials found. Launch the One Tap sign-up flow, or
                        // do nothing and continue presenting the signed-out UI.
                        Log.d("TAG_TESTING", e.getLocalizedMessage());
                    }
                });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_ONE_TAP:
                try {
                    SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(data);
                    String idToken = credential.getGoogleIdToken();
                    String email = credential.getId();
                    String password = credential.getPassword();
                    if (idToken !=  null) {
                        // Got an ID token from Google. Use it to authenticate
                        // with Firebase.
                        AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(idToken, null);
                        mAuth.signInWithCredential(firebaseCredential)
                                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // Sign in success, update UI with the signed-in user's information
                                            Log.d(TAG, "signInWithCredential:success");
                                            FirebaseUser user = mAuth.getCurrentUser();
                                            goToInicio();
                                        } else {
                                            // If sign in fails, display a message to the user.
                                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                                            goToInicio();
                                        }
                                    }
                                });
                    } else if (password != null) {
                        // Got a saved username and password. Use them to authenticate
                        // with your backend.
                        Log.d(TAG, "Got password.");
                    }
                } catch (ApiException e) {
                    // ...
                }
                break;
            default:
                break;
        }
    }

    public void loginFacebook(){
        startActivity(new Intent(this,RegisterActivity.class));
    }
    public void goToInicio(){
        startActivity(new Intent(this,MainActivity.class));
    }
}