package com.example.nav_drawer;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

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

import com.example.nav_drawer.viewdoc.Actualizarperfildoc;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;
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

    TextView profilenum;
    TextView profileespeciality;

    TextView aboutme;
    SharedPreferences sharedPreferences;

    // Variable para Button
    Button editButton;

    String userEmail;
    FirebaseAuth mAuth;
    String id;

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
        aboutme = view.findViewById(R.id.aboutme);
        titleName = view.findViewById(R.id.titleName);
        profileName = view.findViewById(R.id.profileName);
        profileEmail = view.findViewById(R.id.profileEmail);
        profilegenero = view.findViewById(R.id.profilegenero);
        profilefecha = view.findViewById(R.id.profilefecha);
        profileespeciality = view.findViewById(R.id.profileespeciality);
        profilenum = view.findViewById(R.id.profilenum);

        sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);

        mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Inicialización de variable para Button
        editButton = view.findViewById(R.id.editButton);
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
        showDoctorData(db, currentUser);

        //mostrar pantalla emergente
        showdialog();

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent i = new Intent(getActivity(), Actualizarperfildoc.class);
                startActivity(i);
            }
        });
        return view;
    }

    public void showDoctorData(FirebaseFirestore db, FirebaseUser currentUser) {
        //Correo de usuario logueadote
        try {
                // Realizar una consulta para obtener los datos de los doctores
                db.collection("altadoctores")
                        .whereEqualTo("correo", userEmail)//buscar el usuario mediante el correo
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    id = document.getString("id");
                                    String nombreDoctor = document.getString("nombre");
                                    String telefono = document.getString("telefono");
                                    String email = document.getString("correo");
                                    String sexo = document.getString("sexo");
                                    String fechanac = document.getString("fecha");
                                    String especialidad = document.getString("especialidad");
                                    String descricion = document.getString("descripcion");

                                    // Agrega el botón de detalles
                                    // Configurar los elementos de la tarjeta

                                    titleName.setText(nombreDoctor);
                                    profileName.setText(nombreDoctor);
                                    profilenum.setText(telefono);
                                    profileEmail.setText(email);
                                    profilegenero.setText(sexo);
                                    profilefecha.setText(fechanac);
                                    profileespeciality.setText(especialidad);
                                    aboutme.setText(descricion);
                              /*  profiletipo.setText(sexo);
                                profiletelefono.setText(telefono);
                                profilePassword.setText(password);*/

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
                                Toast.makeText(getActivity(), "Valor no valido", Toast.LENGTH_SHORT).show();
                            }
                        });
        }catch (Exception e) {
            Toast.makeText(getActivity(), "Valor no valido", Toast.LENGTH_SHORT).show();
        }

    }


    //parte mostrar la pantalla emergente para about me
    public void showdialog(){
        boolean dialogShown = sharedPreferences.getBoolean("dialogShown", false);
        if (!dialogShown) {
            // Mostrar el diálogo solo si no se ha mostrado antes
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Ingrese texto");

            // Crear un EditText para la entrada de texto
            final EditText input = new EditText(getActivity());
            builder.setView(input);
            // Mostrar el diálogo
            builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Código para el botón Aceptar
                    String userInput = input.getText().toString();

                    // Guardar el texto en Firebase Firestore
                    uploadTextToFirestore(userInput, userEmail);

                    // Guardar un indicador para que no se muestre el diálogo nuevamente
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("dialogShown", true);
                    editor.apply();
                }
            });

            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Código para el botón Cancelar
                    dialog.cancel();
                }
            });

            // Mostrar el diálogo
            builder.show();
            showdialog();
        }else {
            Toast.makeText(getActivity(), "Ya has ingresado sobre tí", Toast.LENGTH_SHORT).show();
        }

    }
    public void uploadTextToFirestore(String text, String email) {
        try {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

            if (currentUser != null) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();

                Map<String, Object> data = new HashMap<>();
                data.put("descripcion", text);

                if (email != null && !email.isEmpty()) {
                    // Llamar a update aquí
                    db.collection("altadoctores")
                            .document(id)
                            .update("descripcion", text)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "Campo 'descripcion' actualizado con éxito");
                                    // Puedes realizar acciones adicionales si es necesario
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error al actualizar campo 'descripcion'", e);
                                    // Puedes manejar el error aquí
                                    Toast.makeText(getActivity(), "Error al actualizar campo 'descripcion'", Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Log.e(TAG, "El correo electrónico es nulo o vacío");
                    Toast.makeText(getActivity(), "El correo electrónico es nulo o vacío", Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.e(TAG, "Usuario no autenticado");
                Toast.makeText(getActivity(), "Usuario no autenticado", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error: " + e.getMessage());
            Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


}