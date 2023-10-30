package com.example.nav_drawer.viewAdmin;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nav_drawer.R;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentDetails#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentDetails extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    //BASE DE DATOS
    private String id;

    public FragmentDetails() {
        // Required empty public constructor
    }
    public static FragmentDetails newInstance(String id) {
        FragmentDetails fragment = new FragmentDetails();
        Bundle args = new Bundle();
        args.putString("id", id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getString("id");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        // Accede a Firebase Firestore y consulta los detalles del doctor utilizando el doctorId

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("doctor")
                .document(id)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String nombre = documentSnapshot.getString("nombre");
                        String especialidad = documentSnapshot.getString("especialidad");

                        // Configura los detalles en los elementos de la interfaz de usuario
                        TextView textViewNombre = view.findViewById(R.id.textViewNombre);
                        TextView textViewEspecialidad = view.findViewById(R.id.textViewEspecialidad);

                        textViewNombre.setText(nombre);
                        textViewEspecialidad.setText(especialidad);
                    }
                })
                .addOnFailureListener(e -> {
                    // Maneja el caso de error al obtener datos de Firestore
                });
        return view;
    }
}