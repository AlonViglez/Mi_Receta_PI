package com.example.nav_drawer.viewpaciente;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.os.Bundle;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.nav_drawer.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentTratamientoPaciente#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentTratamientoPaciente extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Button btnnuevotratamiento;
    String userEmail;
    String nombrePastilla;
    String totalPastillas;
    String pastillasTomadas;
    String dosis;
    TextView textNombrePastilla,textTipoMedicamento,textPastillasTomadas;


    public FragmentTratamientoPaciente() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentTratamientoPaciente.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentTratamientoPaciente newInstance(String param1, String param2) {
        FragmentTratamientoPaciente fragment = new FragmentTratamientoPaciente();
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
        View view = inflater.inflate(R.layout.fragment_tratamiento_paciente, container, false);
        btnnuevotratamiento = view.findViewById(R.id.btn_nuevo_medicamento);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Obtener una referencia al contenedor de tarjetas
        LinearLayout tratamientosContainer  = view.findViewById(R.id.pacienteContainerTratamientos);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            userEmail = currentUser.getEmail();
            Toast.makeText(getActivity(), "Usuario autenticado" + userEmail, Toast.LENGTH_SHORT).show();
        } else {
            Log.e(TAG, "Usuario no autenticado");
            Toast.makeText(getActivity(), "Usuario no autenticado", Toast.LENGTH_SHORT).show();
        }
        //Realizar una consulta para obtener los tratamientos del usuario específico
        db.collection("tratamientos")
                .whereEqualTo("usuario", userEmail)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            nombrePastilla = document.getString("medicamento");
                            totalPastillas = String.valueOf(document.getLong("totalPastillas"));
                            pastillasTomadas = String.valueOf(document.getLong("tomada"));
                            dosis = String.valueOf(document.getLong("dosis"));
                            int numpastillastomadas = Integer.parseInt(pastillasTomadas);
                            int numtotalpastillas = Integer.parseInt(totalPastillas);

                            // Inflar el diseño de la tarjeta personalizado
                            View cardView = getLayoutInflater().inflate(R.layout.paciente_card_tratamientos, null);

                            // Configurar los márgenes
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                            );
                            layoutParams.setMargins(16, 16, 16, 16); // Ajusta los márgenes según tus necesidades

                            // Aplicar los márgenes a la vista de la tarjeta
                            cardView.setLayoutParams(layoutParams);

                            // Encontrar las vistas dentro de la tarjeta
                            textNombrePastilla = cardView.findViewById(R.id.textNombrePastilla);
                            textTipoMedicamento = cardView.findViewById(R.id.textTipoMedicamento);
                            ProgressBar progressBar = cardView.findViewById(R.id.progressBar);
                            textPastillasTomadas = cardView.findViewById(R.id.textPastillasTomadas);

                            // Configurar los elementos de la tarjeta
                            textNombrePastilla.setText(nombrePastilla);
                            textTipoMedicamento.setText(dosis + "gm");
                            progressBar.setMax(100); // Ajusta según tus necesidades
                            int progreso = (numpastillastomadas * 100)/numtotalpastillas;
                            progressBar.setProgress(progreso);
                            textPastillasTomadas.setText(pastillasTomadas + "/" + totalPastillas);

                            // Agregar la tarjeta al contenedor
                            tratamientosContainer.addView(cardView);
                        }
                    } else {
                        // Si hubiera un error
                    }
                });
        btnnuevotratamiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FormularioRecetarPaciente.class);
                startActivity(intent);
            }
        });
        return view;
    }
}
