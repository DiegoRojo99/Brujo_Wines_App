package com.youngdred.brujowines;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.youngdred.brujowines.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ImageButton titaniaBlancoButton=findViewById(R.id.ib_inicio_titania_blanco);
        ImageButton titaniaRosadoButton=findViewById(R.id.ib_inicio_titania_rosado);
        ImageButton titaniaTintoButton=findViewById(R.id.ib_inicio_titania_tinto);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_inicio, R.id.navigation_vinos, R.id.navigation_usuario)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        titaniaBlancoButton.setOnClickListener((View v) ->
            cambiarTitania(TitaniaBlancoActivity.class)
        );

        titaniaRosadoButton.setOnClickListener((View v) ->
            cambiarTitania(TitaniaBlancoActivity.class)
        );
    }

    public void cambiarTitania(Class titania){
            Intent titaniaBlancoIntent = new Intent(MainActivity.this, titania);
            startActivity(titaniaBlancoIntent);
    }

}