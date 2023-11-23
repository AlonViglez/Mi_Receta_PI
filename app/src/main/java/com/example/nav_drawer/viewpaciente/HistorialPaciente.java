package com.example.nav_drawer.viewpaciente;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 * Use the {@link HistorialPaciente#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistorialPaciente extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    FirebaseFirestore db;
    String userEmail;
    String nombrePastilla;
    String totalPastillas;
    String pastillasTomadas;
    String dosis;
    TextView textNombrePastilla,textTipoMedicamento,textPastillasTomadas;
    int numpastillastomadas,numtotalpastillas;

    public HistorialPaciente() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HistorialPaciente.
     */
    // TODO: Rename and change types and number of parameters
    public static HistorialPaciente newInstance(String param1, String param2) {
        HistorialPaciente fragment = new HistorialPaciente();
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
        View view = inflater.inflate(R.layout.fragment_historial_paciente, container, false);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            userEmail = currentUser.getEmail();
           // Toast.makeText(getActivity(), "Usuario autenticado" + userEmail, Toast.LENGTH_SHORT).show();
        } else {
            Log.e(TAG, "Usuario no autenticado");
            Toast.makeText(getActivity(), "Usuario no autenticado", Toast.LENGTH_SHORT).show();
        }
        db = FirebaseFirestore.getInstance();
        // Obtener una referencia al contenedor de tarjetas
        LinearLayout tratamientosContainer  = view.findViewById(R.id.pacienteContainerHistorial);
        //Realizar una consulta para obtener los tratamientos del usuario específico
        db.collection("tratamientoscompletados")
                .whereEqualTo("usuario", userEmail)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            nombrePastilla = document.getString("medicamento");
                            totalPastillas = String.valueOf(document.getLong("totalPastillas"));
                            pastillasTomadas = String.valueOf(document.getLong("tomada"));
                            dosis = String.valueOf(document.getLong("dosis"));
                            numpastillastomadas = Integer.parseInt(pastillasTomadas);
                            numtotalpastillas = Integer.parseInt(totalPastillas);

                            // Inflar el diseño de la tarjeta personalizado
                            View cardView = getLayoutInflater().inflate(R.layout.paciente_card_historial, null);

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
                            // Configurar los elementos de la tarjeta
                            textNombrePastilla.setText(nombrePastilla);
                            textTipoMedicamento.setText(dosis + "gm");
                            // Agregar la tarjeta al contenedor
                            tratamientosContainer.addView(cardView);
                        }
                    } else {
                        // Si hubiera un error
                    }
                });
        return view;
    }
}