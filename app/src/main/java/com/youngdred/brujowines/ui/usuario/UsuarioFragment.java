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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.youngdred.brujowines.DetallesReservaActivity;
import com.youngdred.brujowines.LoginActivity;
import com.youngdred.brujowines.Pedido;
import com.youngdred.brujowines.Reserva;
import com.youngdred.brujowines.databinding.FragmentUsuarioBinding;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class UsuarioFragment extends Fragment {

    private UsuarioViewModel usuarioViewModel;
    private FragmentUsuarioBinding binding;

    private FirebaseUser usuario;
    private DatabaseReference reference;
    private String userId;
    private Button signOutButton;

    private View includeRes, includePed;
    private TextView fechaTV, numeroTV, tipoTV;
    private Button otraReservaBtn, editarReservaBtn;
    private int indexReserva=0, indexPedido=0;
    private String reservaActual="", pedidoActual="";

    public TextView blancoTV, tintoTV, rosadoTV;
    private Button otroPedidoBtn, editarPedidoBtn;

    private TextView saludoTV;

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

        fechaTV = binding.includeUsuarioReserva.tvReservaCartaFechaInfo;
        numeroTV = binding.includeUsuarioReserva.tvReservaCartaPersonasInfo;
        tipoTV = binding.includeUsuarioReserva.tvReservaCartaTipoInfo;

        otraReservaBtn = binding.includeUsuarioReserva.verOtraReservaButton;
        otraReservaBtn.setOnClickListener(view -> {
            cambiarReserva();
        });
        editarReservaBtn = binding.includeUsuarioReserva.cancelarReservaButton;
        editarReservaBtn.setOnClickListener(view -> {
            detallesReserva();
        });

        //Pedidos
        blancoTV = binding.includeUsuarioPedido.cartaUnidadesBlanco;
        tintoTV = binding.includeUsuarioPedido.cartaUnidadesTinto;
        rosadoTV = binding.includeUsuarioPedido.cartaUnidadesRosado;

        otroPedidoBtn = binding.includeUsuarioPedido.verOtraReservaButton;
        otroPedidoBtn.setOnClickListener(view -> {
            cambiarPedido();
        });
        editarPedidoBtn = binding.includeUsuarioPedido.cancelarReservaButton;
        editarPedidoBtn.setOnClickListener(view -> {
            //detallesPedido();
        });


        getUserData();

        return root;
    }

    public void updateTVs(String name, String email){
        saludoTV.setText("¡Bienvenido, "+name+"!");


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

            conseguirReservasyPedidos(uid);

            updateTVs(name, email);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void conseguirReservasyPedidos(String uid){
        cargarReservas();
        cargarPedidos();
    }

    public void cargarPedidos(){
        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        db.collection("pedidos")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(firebaseUser.getUid().equals(document.get("UserId"))){
                                    int blanco=Integer.parseInt(String.valueOf(document.get("UnidadesBlanco")));
                                    int tinto=Integer.parseInt(String.valueOf(document.get("UnidadesTinto")));
                                    int rosado=Integer.parseInt(String.valueOf(document.get("UnidadesRosado")));
                                    int precio=Integer.parseInt(String.valueOf(document.get("Precio")));
                                    Pedido ped=new Pedido(blanco,tinto,rosado,precio);
                                    actualizarPedido(document.getId(),ped);
                                }

                            }
                        }
                    }
                });
    }

    public void actualizarPedido(String docID,Pedido pedido){

        blancoTV.setText(String.valueOf(pedido.blanco));
        tintoTV.setText(String.valueOf(pedido.tinto));
        rosadoTV.setText(String.valueOf(pedido.rosado));

        pedidoActual=docID;

    }
    public void cambiarPedido(){

        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        db.collection("pedidos")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int total=0; boolean found=false;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(firebaseUser.getUid().equals(document.get("UserId"))){
                                    if(total == indexPedido&&!found){
                                        int blanco=Integer.parseInt(String.valueOf(document.get("UnidadesBlanco")));
                                        int tinto=Integer.parseInt(String.valueOf(document.get("UnidadesTinto")));
                                        int rosado=Integer.parseInt(String.valueOf(document.get("UnidadesRosado")));
                                        int precio=Integer.parseInt(String.valueOf(document.get("Precio")));
                                        Pedido ped=new Pedido(blanco,tinto,rosado,precio);
                                        actualizarPedido(document.getId(),ped);
                                        indexPedido++;
                                        total++;
                                        found=true;
                                    }else{
                                        total++;
                                    }
                                }

                            }

                            if(total==indexPedido){
                                indexPedido=0;
                            }
                        }
                    }
                });

    }

    public void detallesPedido2(){

        Intent detallesIntent = new Intent(getActivity(), DetallesReservaActivity.class);
        detallesIntent.putExtra("ReservaID",reservaActual);
        Objects.requireNonNull(getActivity()).startActivity(detallesIntent);
    }


    public void cargarReservas(){
        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        db.collection("reservas")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(firebaseUser.getUid().equals(document.get("UserId"))){
                                    boolean tipo=Boolean.parseBoolean(String.valueOf(document.get("Tipo")));
                                    int numeroPersonas=Integer.parseInt(String.valueOf(document.get("NumeroPersonas")));
                                    Date fechaReserva=document.getDate("FechaReserva");
                                    Reserva res=new Reserva(fechaReserva,numeroPersonas,tipo);
                                    actualizarReserva(document.getId(),res);
                                }

                            }
                        }
                    }
                });
    }

    public void actualizarReserva(String docID,Reserva reserva){

        //new Locale("ES") si el default no funciona
        Format format = new SimpleDateFormat("E dd-LL-yyyy HH:mm zzz", Locale.getDefault());
        String fechaString1 = format.format(reserva.fechaReserva);
        fechaTV.setText(fechaString1);
        String numeroPersonas1 = String.valueOf(reserva.numeroPersonas);
        numeroTV.setText(numeroPersonas1);
        if(reserva.tipo){
            tipoTV.setText("Cata de vinos");
        }else{
            tipoTV.setText("Visita a la bodega");
        }
        reservaActual=docID;

    }
    public void cambiarReserva(){

        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        db.collection("reservas")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int total=0; boolean found=false;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                boolean tipo=Boolean.parseBoolean(String.valueOf(document.get("Tipo")));

                                if(firebaseUser.getUid().equals(document.get("UserId"))){
                                    if(total == indexReserva&&!found){
                                        int numeroPersonas=Integer.parseInt(String.valueOf(document.get("NumeroPersonas")));
                                        Date fechaReserva=document.getDate("FechaReserva");
                                        Reserva res=new Reserva(fechaReserva,numeroPersonas,tipo);
                                        actualizarReserva(document.getId(),res);
                                        indexReserva++;
                                        total++;
                                        found=true;
                                    }else{
                                        total++;
                                    }
                                }

                            }

                            if(total==indexReserva){
                                indexReserva=0;
                            }
                        }
                    }
                });

    }

    public void detallesReserva(){

        Intent detallesIntent = new Intent(getActivity(), DetallesReservaActivity.class);
        detallesIntent.putExtra("ReservaID",reservaActual);
        Objects.requireNonNull(getActivity()).startActivity(detallesIntent);
    }


}