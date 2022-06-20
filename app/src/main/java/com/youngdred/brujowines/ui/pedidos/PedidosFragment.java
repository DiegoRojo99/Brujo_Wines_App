package com.youngdred.brujowines.ui.pedidos;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.youngdred.brujowines.MainActivity;
import com.youngdred.brujowines.R;
import com.youngdred.brujowines.Reserva;
import com.youngdred.brujowines.ReservarActivity;
import com.youngdred.brujowines.TitaniaTintoActivity;
import com.youngdred.brujowines.databinding.FragmentPedidosBinding;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class PedidosFragment extends Fragment implements View.OnClickListener {

    private FragmentPedidosBinding binding;
    private ImageView blancoIV, tintoIV, rosadoIV;
    private TextView blancoTV, tintoTV, rosadoTV, precioTV;
    private Button masBlanco, menosBlanco, masRosado, menosRosado, masTinto, menosTinto, realizarPedidoBtn;

    public int unidadesBlanco=0, unidadesTinto=0, unidadesRosado=0;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentPedidosBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        precioTV = binding.tvTiendaPrecio;

        blancoIV = binding.tiendaBlanco.ivVinoTienda;
        tintoIV = binding.tiendaTinto.ivVinoTienda;
        rosadoIV = binding.tiendaRosado.ivVinoTienda;

        blancoTV= binding.tiendaBlanco.unidadesVino;
        tintoTV= binding.tiendaTinto.unidadesVino;
        rosadoTV= binding.tiendaRosado.unidadesVino;

        menosBlanco = binding.tiendaBlanco.botonMenosVino;
        masBlanco = binding.tiendaBlanco.botonMasVino;

        menosRosado = binding.tiendaRosado.botonMenosVino;
        masRosado = binding.tiendaRosado.botonMasVino;

        menosTinto = binding.tiendaTinto.botonMenosVino;
        masTinto = binding.tiendaTinto.botonMasVino;

        realizarPedidoBtn=binding.btnRealizarPedido;

        masTinto.setOnClickListener(this);
        menosTinto.setOnClickListener(this);
        masBlanco.setOnClickListener(this);
        menosBlanco.setOnClickListener(this);
        masRosado.setOnClickListener(this);
        menosRosado.setOnClickListener(this);
        realizarPedidoBtn.setOnClickListener(this);

        actualizarVinos();

        return root;
    }

    public PedidosFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void actualizarPrecio(){
        int total=30*unidadesBlanco+40*unidadesTinto+25*unidadesRosado;
        precioTV.setText(String.valueOf(total)+"€");
    }

    public void actualizarVinos(){
        blancoIV.setImageResource(R.drawable.titania_blanco);
        rosadoIV.setImageResource(R.drawable.titania_rosado);
        tintoIV.setImageResource(R.drawable.titania_tinto);
    }
    public void actualizarUnidades(){
        if(unidadesRosado>=0){
            String ur=String.valueOf(unidadesRosado);
            rosadoTV.setText(ur);
        }else{
            unidadesRosado=0;
        }
        if(unidadesBlanco>=0){
            String ub=String.valueOf(unidadesBlanco);
            blancoTV.setText(ub);
        }else{
            unidadesBlanco=0;
        }
        if(unidadesTinto>=0){
            String ut=String.valueOf(unidadesTinto);
            tintoTV.setText(ut);
        }else{
            unidadesTinto=0;
        }
        actualizarPrecio();
    }

    public void menosVino(String vino){
        switch (vino){
            case "blanco":
                unidadesBlanco--;
                break;
            case "tinto":
                unidadesTinto--;
                break;
            case "rosado":
                unidadesRosado--;
                break;
            default:
                break;
        }
        actualizarUnidades();
    }
    public void masVino(String vino){
        switch (vino){
            case "blanco":
                unidadesBlanco++;
                break;
            case "tinto":
                unidadesTinto++;
                break;
            case "rosado":
                unidadesRosado++;
                break;
            default:
                break;
        }
        actualizarUnidades();
    }

    @Override
    public void onClick(View view) {
        if(view==menosRosado){
            menosVino("rosado");
        }else if(view==masRosado){
            masVino("rosado");
        }else if(view==menosBlanco){
            menosVino("blanco");
        }else if(view==masBlanco){
            masVino("blanco");
        }else if(view==menosTinto){
            menosVino("tinto");
        }else if(view==masTinto){
            masVino("tinto");
        }else if(view==realizarPedidoBtn){
            realizarPedido();
        }
    }

    private void realizarPedido(){

        int precioTotal=30*unidadesBlanco+40*unidadesTinto+25*unidadesRosado;

        FirebaseUser usuario= FirebaseAuth.getInstance().getCurrentUser();
        String userId=usuario.getUid();
        Map<String,Object> pedido=new HashMap<>();
        pedido.put("Pagado",false);
        pedido.put("Precio",precioTotal);
        pedido.put("UnidadesBlanco",unidadesBlanco);
        pedido.put("UnidadesRosado",unidadesRosado);
        pedido.put("UnidadesTinto",unidadesTinto);
        pedido.put("UserId",userId);

        FirebaseFirestore db= FirebaseFirestore.getInstance();
        db.collection("pedidos")
                .add(pedido)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

        getActivity().startActivity(new Intent(getActivity(), MainActivity.class));

    }
}