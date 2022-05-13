package com.youngdred.brujowines.ui.usuario;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.youngdred.brujowines.LoginActivity;
import com.youngdred.brujowines.databinding.FragmentUsuarioBinding;

public class UsuarioFragment extends Fragment {

    private UsuarioViewModel usuarioViewModel;
    private FragmentUsuarioBinding binding;

    private FirebaseUser usuario;
    private DatabaseReference reference;
    private String userId;
    private Button signOutButton;

    private TextView saludoTV, nombreTV, emailTV;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
       usuarioViewModel =
                new ViewModelProvider(this).get(UsuarioViewModel.class);

        binding = FragmentUsuarioBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        signOutButton=binding.btnUsuarioSignOut;
        signOutButton.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
        });

        usuario=FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("Users");
        userId=usuario.getUid();

        saludoTV= binding.tvUsuarioSaludo;
        emailTV= binding.tvUsuarioEmailUsuario;
        nombreTV= binding.tvUsuarioNombreUsuario;

        getUserData();

        return root;
    }

    public void updateTVs(String name, String email){
        saludoTV.setText("¡Bienvenido, "+name+"!");
        nombreTV.setText(name);
        emailTV.setText(email);


    }

    public void getUserData(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();

            updateTVs(name, email);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}