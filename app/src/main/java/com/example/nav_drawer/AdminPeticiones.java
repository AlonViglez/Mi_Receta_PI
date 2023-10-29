package com.example.nav_drawer;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.example.nav_drawer.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminPeticiones#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminPeticiones extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AdminPeticiones() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdminPeticiones.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminPeticiones newInstance(String param1, String param2) {
        AdminPeticiones fragment = new AdminPeticiones();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_peticiones, container, false);

        // Inicializa Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Obtén una referencia al contenedor de tarjetas
        LinearLayout doctorsContainer = view.findViewById(R.id.doctorsContainer);

        // Realiza una consulta para obtener los datos de los doctores
        db.collection("doctor")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String nombreDoctor = document.getString("nombre");
                            String especialidadMedica = document.getString("especialidad");

                            // Infla el diseño de la tarjeta personalizado
                            View cardView = getLayoutInflater().inflate(R.layout.doctor_card, null);

                            // Encuentra las vistas dentro de la tarjeta
                            ImageView doctorIconImageView = cardView.findViewById(R.id.doctorIcon);
                            TextView doctorNameTextView = cardView.findViewById(R.id.doctorName);
                            TextView especialidadMedicaTextView = cardView.findViewById(R.id.especialidadMedica);

                            // Configura los elementos de la tarjeta
                            doctorNameTextView.setText(nombreDoctor);
                            especialidadMedicaTextView.setText(especialidadMedica);

                            // Agrega la tarjeta al contenedor
                            doctorsContainer.addView(cardView);
                        }
                    } else {
                        // Maneja el caso de error aquí
                    }
                });

        return view;
    }
}