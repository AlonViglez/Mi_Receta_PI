package com.example.nav_drawer.viewAdmin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.nav_drawer.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

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

    ImageView imagenadmin;
    TextView profileName, profileEmail, profiletelefono, profilePassword, profiletipo;
    TextView titleName;
    Button editProfile;


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
        imagenadmin = view.findViewById(R.id.fotoperfil);
        profileName = view.findViewById(R.id.profileName);
        profileEmail = view.findViewById(R.id.profileEmail);
        profiletelefono = view.findViewById(R.id.telefeonoadmin);
        profilePassword = view.findViewById(R.id.profilePassword);
        titleName = view.findViewById(R.id.titleName);
        editProfile = view.findViewById(R.id.editButton);
        profiletipo = view.findViewById(R.id.sexo);
        Button ver = view.findViewById(R.id.editButton);

        showUserData();
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //passUserData();
            }
        });
        return view;
    }

    public void showUserData(){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Realizar una consulta para obtener los datos de los doctores
        db.collection("doctor")
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
    /*
    public void passUserData(){
        String userUsername = profileUsername.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = reference.orderByChild("username").equalTo(userUsername);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    String nameFromDB = snapshot.child(userUsername).child("name").getValue(String.class);
                    String emailFromDB = snapshot.child(userUsername).child("email").getValue(String.class);
                    String usernameFromDB = snapshot.child(userUsername).child("username").getValue(String.class);
                    String passwordFromDB = snapshot.child(userUsername).child("password").getValue(String.class);
                    //forma de enviar a otro archivo desde un fragment
                    Intent intent = new Intent(getActivity(), ViewAdministrador.class);
                    intent.putExtra("fragmentToLoad", FragmentEditarPerfilAdmin.class.getName());
                    startActivity(intent);

                    intent.putExtra("name", nameFromDB);
                    intent.putExtra("email", emailFromDB);
                    intent.putExtra("username", usernameFromDB);
                    intent.putExtra("password", passwordFromDB);

                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

*/
}