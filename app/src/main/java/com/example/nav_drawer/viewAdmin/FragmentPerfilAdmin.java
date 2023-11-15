package com.example.nav_drawer.viewAdmin;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.nav_drawer.PerfilFragment;
import com.example.nav_drawer.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentPerfilAdmin#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentPerfilAdmin extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView profileName, profileEmail, profiletelefono, profilePassword, profiletipo;
    TextView titleName;

    SharedPreferences sharedPreferences;

    FirebaseAuth mAuth;

    String userEmail;

    String id;


    public FragmentPerfilAdmin() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentPerfilAdmin.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentPerfilAdmin newInstance(String param1, String param2) {
        FragmentPerfilAdmin fragment = new FragmentPerfilAdmin();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_perfil_admin, container, false);

        //agregamos el view para que pueda llamar los id
        titleName = view.findViewById(R.id.titleName);
        profileName = view.findViewById(R.id.profileName);
        profileEmail = view.findViewById(R.id.profileEmail);

        sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);

        mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Inicialización de variable para Button
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            userEmail = currentUser.getEmail();
            Toast.makeText(getActivity(), "Usuario autenticado" + userEmail, Toast.LENGTH_SHORT).show();
            // Resto del código...
        } else {
            Log.e(TAG, "Usuario no autenticado");
            Toast.makeText(getActivity(), "Usuario no autenticado", Toast.LENGTH_SHORT).show();
        }
        //mostrar datos el del perfil
        showAdminData(db, currentUser);
        return view;
    }

    public void showAdminData(FirebaseFirestore db, FirebaseUser currentUser) {
        try {
            // Realizar una consulta para obtener los datos de los doctores
            db.collection("administrador")
                    .whereEqualTo("correo", userEmail)//buscar el usuario mediante el correo
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                id = String.valueOf(document.getLong("id"));;
                                String nombreDoctor = document.getString("nombre");
                                String email = document.getString("correo");
                                // Agrega el botón de detalles
                                // Configurar los elementos de la tarjeta

                                titleName.setText("¡Hola admin! "+nombreDoctor);
                                profileName.setText(nombreDoctor);
                                profileEmail.setText(email);
                            }
                        } else {
                            Toast.makeText(getActivity(), "Valor no valido", Toast.LENGTH_SHORT).show();
                        }
                    });
        }catch (Exception e) {
            Toast.makeText(getActivity(), "Valor no valido", Toast.LENGTH_SHORT).show();
        }
    }

}