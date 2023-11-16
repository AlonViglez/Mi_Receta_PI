package com.example.nav_drawer.viewpaciente;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.nav_drawer.PerfilFragment;
import com.example.nav_drawer.R;
import com.example.nav_drawer.viewdoc.Actualizarperfildoc;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PerfilPaciente#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PerfilPaciente extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView titleName;
    TextView profileName;
    TextView profileEmail;
    TextView profilegenero;
    TextView profilefecha;

    TextView aboutme;

    SharedPreferences sharedPreferences;

    // Variable para Button
    Button editButton;

    String userEmail;

    String fechanac;
    FirebaseAuth mAuth;
    String id;

    String fechaFormateada;

    Date fecha;

    long timestamp;

    ImageView fotoperfil;

    SimpleDateFormat sdf;

    public PerfilPaciente() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PerfilPaciente.
     */
    // TODO: Rename and change types and number of parameters
    public static PerfilPaciente newInstance(String param1, String param2) {
        PerfilPaciente fragment = new PerfilPaciente();
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
       View view =  inflater.inflate(R.layout.fragment_perfil_paciente, container, false);
        aboutme = view.findViewById(R.id.aboutme);
        titleName = view.findViewById(R.id.titleName);
        profileName = view.findViewById(R.id.profileName);
        profileEmail = view.findViewById(R.id.profileEmail);
        profilegenero = view.findViewById(R.id.profilegenero);
        profilefecha = view.findViewById(R.id.profilefecha);
        editButton = view.findViewById(R.id.editButton);
        fotoperfil = view.findViewById(R.id.fotoperfil);

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

        showDoctorData(db, currentUser);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), ActualizarPerfilPaciente.class);
                i.putExtra("id",id);
                startActivity(i);
            }
        });
        return view;
    }

    public void showDoctorData(FirebaseFirestore db, FirebaseUser currentUser) {
        //Correo de usuario logueadote
        try {
            // Realizar una consulta para obtener los datos de los doctores
            db.collection("users")
                    .whereEqualTo("correo", userEmail)//buscar el usuario mediante el correo
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                id = document.getString("id");
                                String nombrepaciente = document.getString("nombre");
                                String email = document.getString("correo");
                                String sexo = document.getString("sexo");
                                fechanac =  String.valueOf(document.getLong("fechanac"));
                                String descricion = document.getString("descripcion");
                                String cedulaImageUrl = document.getString("imagenperfilurl");
                                if (cedulaImageUrl != null) {
                                    Glide.with(PerfilPaciente.this).load(cedulaImageUrl).into(fotoperfil);
                                }
                                timestamp = Long.parseLong(fechanac);
                                fecha = new Date(timestamp);
                                sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                                fechaFormateada = sdf.format(fecha);
                                // Agrega el botón de detalles
                                // Configurar los elementos de la tarjeta

                                titleName.setText("¡Hola! "+ nombrepaciente);
                                profileName.setText(nombrepaciente);
                                profileEmail.setText(email);
                                profilegenero.setText(sexo);
                                profilefecha.setText(fechaFormateada);
                                aboutme.setText(descricion);
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