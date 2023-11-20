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
    boolean showDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pacient);
        Intent intentdialog = getIntent();
        showDialog = intentdialog.getBooleanExtra("SHOW_DIALOG", false);
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
                    if(itemId == R.id.bottom_chats_paciente) {
                        openFragment(new FragmentChatPaciente());
                        return true;
                    }else if(itemId == R.id.bottom_doctores_paciente){
                        openFragment(new FragmentDoctoresPaciente());
                        return true;
                    }else if(itemId == R.id.bottom_tratamiento_paciente){
                        openFragment(new FragmentTratamientoPaciente());
                        return true;
                    }else if(itemId == R.id.bottom_historial_paciente){
                        openFragment(new HistorialPaciente());
                        return true;
                    }else if(itemId == R.id.bottom_perfil_paciente){
                        openFragment(new PerfilPaciente());
                        return true;
                    }
                    return false;
                    /*verificar si se deben elimninar:
                    *CitasPacientes()
                    * DiagnosticarPaciente()
                    * RecetaPaciente()
                     */
                }
            });
            fragmentManager = getSupportFragmentManager();
            //al entrar te mandara a esta primero
            if (showDialog == false) {
                openFragment(new FragmentTratamientoPaciente());
            }else if (showDialog == true){
                // Crear una instancia de MiFragmento
                FragmentTratamientoPaciente miFragmento = new FragmentTratamientoPaciente();
                // Crear un Bundle para los argumentos
                Bundle args = new Bundle();
                args.putBoolean("SHOW_DIALOG", showDialog);
                // Establecer los argumentos en el fragmento
                miFragmento.setArguments(args);
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragment_container_pacient, miFragmento);
                transaction.commit();
            }
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
            openFragment(new HistorialPaciente());
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
