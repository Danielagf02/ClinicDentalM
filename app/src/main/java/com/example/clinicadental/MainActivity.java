package com.example.clinicadental;

import static com.example.clinicadental.LoginActivity.tiporecuperado;

import android.os.Bundle;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.clinicadental.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        //Toast.makeText(MainActivity.this, "Accediste como: "+tiporecuperado, Toast.LENGTH_SHORT).show();

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home,
                R.id.nav_micuenta,
                R.id.nav_agregarusuarios,
                R.id.nav_eliminausuarios,
                R.id.nav_menu,
                R.id.nav_modificausuarios,
                R.id.nav_ubicacion,
                R.id.nav_listausuarios,
                R.id.nav_agregarplatillo,
                R.id.nav_eliminarplatillo,
                R.id.nav_modificarplatillo,
                R.id.nav_agregarreservacion,
                R.id.nav_listareservaciones,
                R.id.nav_eliminarreservaciones)
                .setOpenableLayout(drawer)
                .build();

        if(tiporecuperado.equals("admin")){
            navigationView.getMenu().findItem(R.id.nav_ubicacion).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_agregarreservacion).setVisible(false);

        }else{
            navigationView.getMenu().findItem(R.id.nav_agregarusuarios).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_eliminausuarios).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_modificausuarios).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_listausuarios).setVisible(false);

            navigationView.getMenu().findItem(R.id.nav_agregarplatillo).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_eliminarplatillo).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_modificarplatillo).setVisible(false);

            navigationView.getMenu().findItem(R.id.nav_eliminarreservaciones).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_listareservaciones).setVisible(false);
        }

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setItemIconTintList(null);
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}