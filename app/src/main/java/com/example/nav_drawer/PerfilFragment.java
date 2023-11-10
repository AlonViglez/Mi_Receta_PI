package com.example.nav_drawer;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
//PERFIL
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PerfilFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PerfilFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // Variables para TextView
    TextView titleName;
    TextView profileName;
    TextView profileEmail;
    TextView profilegenero;
    TextView profilefecha;
    TextView profileespeciality;

    FirebaseAuth mAuth;


    // Variables para EditText
    EditText editText;


    // Variable para Button
    Button editButton;

    public PerfilFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PerfilFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PerfilFragment newInstance(String param1, String param2) {
        PerfilFragment fragment = new PerfilFragment();
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
        View view =  inflater.inflate(R.layout.fragment_perfil, container, false);
        titleName = view.findViewById(R.id.titleName);
        profileName = view.findViewById(R.id.profileName);
        profileEmail = view.findViewById(R.id.profileEmail);
        profilegenero = view.findViewById(R.id.profilegenero);
        profilefecha = view.findViewById(R.id.profilefecha);
        profileespeciality = view.findViewById(R.id.profileespeciality);


        // Inicialización de variables para EditText
        editText = view.findViewById(R.id.editText);


        // Inicialización de variable para Button
        editButton = view.findViewById(R.id.editButton);

        showDoctorData();
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //passUserData();
            }
        });
        return view;
    }

    public void showDoctorData(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser(); //Correo de usuario logueado
        if (currentUser != null) {
            String userEmail = currentUser.getEmail();
            // Realizar una consulta para obtener los datos de los doctores
            db.collection("altadoctores")
                    .whereEqualTo("correo", userEmail)//buscar el usuario mediante el correo
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String email = document.getString("correo");
                                String nombreDoctor = document.getString("nombre");
                                String sexo = document.getString("sexo");
                                String telefono = document.getString("telefono");
                                String password = document.getString("password");

                                // Agrega el botón de detalles
                                // Configurar los elementos de la tarjeta
                                profileEmail.setText(email);
                                titleName.setText(nombreDoctor);
                                profileName.setText(nombreDoctor);
                                profiletipo.setText(sexo);
                                profiletelefono.setText(telefono);
                                profilePassword.setText(password);

                                // Establecer un OnClickListener para el botón de "Detalles"

                            /*ver.setOnClickListener(v -> {
                                // Ocultar el fragmento actual (AdminPeticiones)
                                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.hide(this);  // Ocultar el fragmento actual
                                fragmentTransaction.addToBackStack(null);

                                // Reemplazar el fragmento con el nuevo fragmento de detalles (FragmentDetails)
                                fragmentTransaction.replace(R.id.fragmentContainerDetails, new FragmentDetails());
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();
                            });*/

                            }
                        } else {
                            // Si hubiera un error
                        }
                    });
        }

    }
}