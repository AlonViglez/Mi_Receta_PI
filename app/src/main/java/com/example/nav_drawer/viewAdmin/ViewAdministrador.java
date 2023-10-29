package com.example.nav_drawer.viewAdmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.example.nav_drawer.ContactarFragment;
import com.example.nav_drawer.GuiaFragment;
import com.example.nav_drawer.HistoryFragment;
import com.example.nav_drawer.Login;
import com.example.nav_drawer.R;
import com.example.nav_drawer.viewpaciente.CitasPacientes;
import com.example.nav_drawer.viewpaciente.DiagnosticarPaciente;
import com.example.nav_drawer.viewpaciente.HistorialPaciente;
import com.example.nav_drawer.viewpaciente.HomePaciente;
import com.example.nav_drawer.viewpaciente.PerfilPaciente;
import com.example.nav_drawer.viewpaciente.RecetaPaciente;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ViewAdministrador extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawerLayout;
    BottomNavigationView bottomNavigationView;

    FragmentManager fragmentManager;
    Toolbar toolbar;
    FloatingActionButton fab;
    FirebaseAuth auth;
    Button button;
    TextView textView;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_administrador);
        auth = FirebaseAuth.getInstance();
        user = auth .getCurrentUser();
        if (user == null){
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }else{
            toolbar = findViewById(R.id.toolbaradmin);
            setSupportActionBar(toolbar);
            drawerLayout = findViewById(R.id.drawer_layout_admin);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawerLayout.addDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = findViewById(R.id.navigation_drawer_admin);
            navigationView.setNavigationItemSelectedListener(this);

            bottomNavigationView = findViewById(R.id.bottom_navigation_admin);
            bottomNavigationView.setBackground(null);

            bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    //fragmentos del nav
                    int itemId = item.getItemId();
                    if(itemId == R.id.bottom_solicitud) {
                        openFragment(new FragmentSolicitud());
                        return true;
                    }else if(itemId == R.id.bottom_doctores){
                        openFragment(new FragmentDoctores());
                        return true;
                    }else if(itemId == R.id.bottom_perfil_administrador){
                        openFragment(new FragmentPerfilAdmin());
                        return true;
                    }
                    return false;
                }
            });
            //al entrar te mandara a esta primero
            fragmentManager = getSupportFragmentManager();
            openFragment(new HomePaciente());
        /*
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this,"Upload Videos", Toast.LENGTH_SHORT).show();
            }
        });*/
        }
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        //menu
        int itemId = item.getItemId();
        if (itemId == R.id.nav_logout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void openFragment(Fragment fragment){
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container_admin, fragment);
        transaction.commit();
    }
}