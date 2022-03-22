package com.youngdred.brujowines.ui.usuario;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.youngdred.brujowines.LoginActivity;
import com.youngdred.brujowines.MainActivity;
import com.youngdred.brujowines.TitaniaBlancoActivity;
import com.youngdred.brujowines.Usuario;
import com.youngdred.brujowines.databinding.FragmentUsuarioBinding;

public class UsuarioFragment extends Fragment {

    private UsuarioViewModel usuarioViewModel;
    private FragmentUsuarioBinding binding;

    private FirebaseUser usuario;
    private DatabaseReference reference;

    private String userId;

    private Button logOut;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
       usuarioViewModel =
                new ViewModelProvider(this).get(UsuarioViewModel.class);

        binding = FragmentUsuarioBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Button loginButton=binding.btnUsuarioLogin;
        loginButton.setOnClickListener(view -> {
            getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
        });

        Button signOutButton=binding.btnUsuarioSignOut;
        signOutButton.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            getActivity().startActivity(new Intent(getActivity(), MainActivity.class));
        });

        usuario=FirebaseAuth.getInstance().getCurrentUser();

        reference= FirebaseDatabase.getInstance().getReference("Users");
        userId=usuario.getUid();

        final TextView saludoTV=(TextView) binding.tvUsuarioSaludo;
        final TextView emailTV=(TextView) binding.tvUsuarioEmailUsuario;
        final TextView nombreTV=(TextView) binding.tvUsuarioNombreUsuario;

        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Usuario perfilUsuario=snapshot.getValue(Usuario.class);

                if(perfilUsuario!=null){
                    String nombre=perfilUsuario.nombre;
                    String email=perfilUsuario.email;

                    saludoTV.setText("¡Bienvenido, "+nombre+"!");
                    nombreTV.setText(nombre);
                    emailTV.setText(email);
                }else{
                    saludoTV.setText("Esto no funciona");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(),"Algo ha salido mal",Toast.LENGTH_LONG).show();
            }
        });

        final TextView textView = binding.textUsuario;
        usuarioViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}