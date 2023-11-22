package com.example.nav_drawer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.nav_drawer.viewdoc.FragmentChats;
import com.example.nav_drawer.viewdoc.FragmentNotificaciones;
import com.example.nav_drawer.viewdoc.FragmentRecomendacionesDoctor;
import com.example.nav_drawer.viewdoc.PreguntaDoctor;
import com.example.nav_drawer.viewpaciente.PreguntaPaciente;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarItemView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    //francisco javier
    DrawerLayout drawerLayout;
    BottomNavigationView bottomNavigationView;

    FragmentManager fragmentManager;
    Toolbar toolbar;
    FloatingActionButton fab;
    FirebaseAuth auth;
    Button button;
    TextView textView;
    FirebaseUser user;
   static final int MAX_NUM_CLICS = 2; // Puedes ajustar este valor según tus necesidades
    int numClics = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /* fab = findViewById(R.id.recetar);*/
        auth = FirebaseAuth.getInstance();
        user = auth .getCurrentUser();
        if (user == null){
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }
        else{
            toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            drawerLayout = findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawerLayout.addDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = findViewById(R.id.navigation_drawer);
            navigationView.setNavigationItemSelectedListener(this);

            bottomNavigationView = findViewById(R.id.bottom_navigation);
            bottomNavigationView.setBackground(null);

            bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    int itemId = item.getItemId();
                    if (itemId == R.id.bottom_notify) {
                        //handleNavigationClick(new FragmentNotificaciones());
                        handleNavigationClick(new PreguntaDoctor());
                        return true;
                    } else if (itemId == R.id.bottom_chats) {
                        //handleNavigationClick(new FragmentChats());
                        handleNavigationClick(new FragmentRecomendacionesDoctor());
                        return true;
                    } else if (itemId == R.id.bottom_perfil) {
                        handleNavigationClick(new PerfilFragment());
                        return true;
                    }

                    return false;
                }
            });
            /* archivos que se tiene verificar para eliminar o no
            * DiagnosticarFragment()
            * AddFragment()
            * CitasFragment()
            * RecetarFragment()
            * */

            fragmentManager = getSupportFragmentManager();
           // openFragment(new HomeFragment());
        /*
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this,"Upload Videos", Toast.LENGTH_SHORT).show();
            }
        });*/
        }
    }
    private void handleNavigationClick(Fragment fragment) {
        numClics++;

        if (numClics >= MAX_NUM_CLICS) {
            // Realizar acciones adicionales después de múltiples clics
            // Por ejemplo, reiniciar el contador y hacer algo especial.
            numClics = 0;
            // Agrega aquí cualquier acción adicional que desees realizar.
        } else {
            openFragment(fragment);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }
}