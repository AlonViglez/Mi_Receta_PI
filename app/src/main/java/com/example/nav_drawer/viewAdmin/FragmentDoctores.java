package com.example.nav_drawer.viewAdmin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nav_drawer.R;
import com.example.nav_drawer.Registro;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentDoctores#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentDoctores extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;

    public FragmentDoctores() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentDoctores.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentDoctores newInstance(String param1, String param2) {
        FragmentDoctores fragment = new FragmentDoctores();
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
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doctores, container, false);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Obtener una referencia al contenedor de tarjetas
        LinearLayout doctorsContainer = view.findViewById(R.id.doctorsContainerAlta);

        // Realizar una consulta para obtener los datos de los doctores
        db.collection("altadoctores")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String nombreDoctor = document.getString("nombre");
                            String especialidadMedica = document.getString("especialidad");
                            String idDoctor = document.getString("id");
                            String email = document.getString("correo");
                            // Inflar el diseño de la tarjeta personalizado
                            View cardView = getLayoutInflater().inflate(R.layout.doctor_card_alta, null);
                            // Configurar los márgenes
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                            );
                            layoutParams.setMargins(16, 16, 16, 16); // Ajusta los márgenes según tus necesidades

                            // Aplicar los márgenes a la vista de la tarjeta
                            cardView.setLayoutParams(layoutParams);
                            // Encontrar las vistas dentro de la tarjeta
                            ImageView doctorIconImageView = cardView.findViewById(R.id.doctorIcon);
                            TextView doctorNameTextView = cardView.findViewById(R.id.doctorName);
                            TextView especialidadMedicaTextView = cardView.findViewById(R.id.especialidadMedica);
                            ImageButton btnborrar = cardView.findViewById(R.id.borrarButton); // Agregar el botón de editar
                            // Agregar el botón de eliminar
                            // Configurar los elementos de la tarjeta
                            doctorNameTextView.setText(nombreDoctor);
                            especialidadMedicaTextView.setText(especialidadMedica);

                            //BOTON BORRAR
                            btnborrar.setOnClickListener(v -> {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                View dialogView = getLayoutInflater().inflate(R.layout.dialog_confirmacion, null);
                                builder.setView(dialogView);
                                Button btnCancelar = dialogView.findViewById(R.id.btnCancelarEliminacion);
                                Button btnEliminar = dialogView.findViewById(R.id.btnEliminar);

                                AlertDialog alertDialog = builder.create();

                                btnCancelar.setOnClickListener(v1 -> {
                                    alertDialog.dismiss();
                                });
                                //Toast.makeText(getContext(), "Correo" + email, Toast.LENGTH_SHORT).show();
                                final Task<SignInMethodQueryResult> taskCopy = mAuth.fetchSignInMethodsForEmail(email);
                                btnEliminar.setOnClickListener(v2 -> {
                                    if (taskCopy.isSuccessful()) {
                                        SignInMethodQueryResult result = taskCopy.getResult();
                                        Toast.makeText(getContext(), "Correo" + result, Toast.LENGTH_SHORT).show();
                                        if (result != null && result.getSignInMethods() != null && result.getSignInMethods().size() > 0) {
                                            String uidUsuarioAEliminar = result.getSignInMethods().get(0);
                                            mAuth.getCurrentUser().delete()
                                                    .addOnSuccessListener(aVoid -> {
                                                        db.collection("altadoctores")
                                                                .document(email)
                                                                .delete()
                                                                .addOnSuccessListener(aVoid1 -> {
                                                                    Toast.makeText(getContext(), "El doctor con correo " + email + " se ha eliminado con éxito", Toast.LENGTH_SHORT).show();
                                                                })
                                                                .addOnFailureListener(e -> {
                                                                    Toast.makeText(getContext(), "Error al eliminar el doctor", Toast.LENGTH_SHORT).show();
                                                                });
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        Toast.makeText(getContext(), "Error al eliminar el usuario de Authentication", Toast.LENGTH_SHORT).show();
                                                    });
                                        } else {
                                            Log.d("EliminarUsuario", "Usuario con correo " + email + " no encontrado en Authentication.");
                                            Toast.makeText(getContext(), "Usuario no encontrado en Authentication" + email, Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Log.e("EliminarUsuario", "Error al buscar usuario por correo en Authentication", taskCopy.getException());
                                        Toast.makeText(getContext(), "Error al buscar usuario por correo en Authentication", Toast.LENGTH_SHORT).show();
                                    }
                                    alertDialog.dismiss();
                                });
                                alertDialog.show();
                            });
                            //BOTON EDITAR
                            /*NOSE SI IMPLEMENTARLO
                            btneditar.setOnClickListener(v -> {
                                Intent intent = new Intent(getActivity(), EditarDoctor.class);
                                intent.putExtra("doctorId", idDoctor); //Paso el ID del doctor
                                startActivity(intent);
                            });*/
                            // Agrega la tarjeta al contenedor
                            doctorsContainer.addView(cardView);
                        }
                    } else {
                        //si hubiera error
                    }
                });
        return view;
    }
}