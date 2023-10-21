package com.example.nav_drawer.viewpaciente;

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

import com.example.nav_drawer.AddFragment;
import com.example.nav_drawer.CitasFragment;
import com.example.nav_drawer.ContactarFragment;
import com.example.nav_drawer.DiagnosticarFragment;
import com.example.nav_drawer.GuiaFragment;
import com.example.nav_drawer.HistoryFragment;
import com.example.nav_drawer.HomeFragment;
import com.example.nav_drawer.Login;
import com.example.nav_drawer.PerfilFragment;
import com.example.nav_drawer.R;
import com.example.nav_drawer.RecetarFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ViewPacient extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener {

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
        setContentView(R.layout.activity_view_pacient);
        auth = FirebaseAuth.getInstance();
        user = auth .getCurrentUser();
        if (user == null){
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }
        else{
            toolbar = findViewById(R.id.toolbarpacient);
            setSupportActionBar(toolbar);
            drawerLayout = findViewById(R.id.drawer_layout_pacient);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawerLayout.addDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = findViewById(R.id.navigation_drawer_pacient);
            navigationView.setNavigationItemSelectedListener(this);

            bottomNavigationView = findViewById(R.id.bottom_navigation_pacient);
            bottomNavigationView.setBackground(null);

            bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    //fragmentos del nav
                    int itemId = item.getItemId();
                    if(itemId == R.id.bottom_citas_pacientes) {
                        openFragment(new CitasPacientes());
                        return true;
                    }else if(itemId == R.id.bottom_diagnosticar_pacientes){
                        openFragment(new DiagnosticarPaciente());
                        return true;
                    }else if(itemId == R.id.bottom_recetas_paciente){
                        openFragment(new RecetaPaciente());
                        return true;
                    }else if(itemId == R.id.bottom_historial_pacientes){
                        openFragment(new HistorialPaciente());
                        return true;
                    }else if(itemId == R.id.bottom_perfil_paciente){
                        openFragment(new PerfilPaciente());
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
        if (itemId == R.id.nav_guia) {
            openFragment(new GuiaFragment());
        }else if (itemId == R.id.nav_contactar) {
            openFragment(new ContactarFragment());
        }else if (itemId == R.id.nav_history) {
            openFragment(new HistoryFragment());
        }else if (itemId == R.id.nav_logout) {
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
        transaction.replace(R.id.fragment_container_pacient, fragment);
        transaction.commit();
    }
}
