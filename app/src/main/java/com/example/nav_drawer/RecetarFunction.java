package com.example.nav_drawer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import com.example.nav_drawer.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class RecetarFunction extends AppCompatActivity implements View.OnClickListener {
    LinearLayout layoutList;
    ImageButton buttonAdd;

    Button button_submit_list;
    List<String> teamList = new ArrayList<>(); // Declaración de una lista para almacenar nombres de equipos
    ArrayList<Cricketer> cricketersList = new ArrayList<>();


    // Declaración de un array de nombres de medicamentos
    //String[] medicamentos = {"Medicametos", "Ácido nalidíxico-Fenazopiridina", "Amikacina", "Amoxicilina - Ambroxol", "Amoxicilina", "Amoxicilina Ac clav", "Ampicilina", "Azitromicina", "Bencilpenicilina procaínica c/ Bencilpenicilina cristalina", "Cefaclor", "Cefalexina", "Cefalexina/Bromhexina", "Cefalexina-Ambroxol", "Ceftriaxona", "Ciprofloxacino", "Claritromicina", "Clindamicina", "Dicloxacilina", "Levofloxacino", "Lincomicina", "Metronidazol", "Metronidazol Diyodohidroxiquinoleína", "Tetraciclina", "Tetraciclina clorhidrato de Tetra Atlantis", "Trimetoprim-Sulfametoxazol", "Acemetacina", "Ácido acetil salicílico", "Clonixinato de lisina-Hioscina","Complejo B Dexametasona Lidocaína", "Diclofenaco", "Diclofenaco-Complejo B", "Diclofenaco-Paracetamol", "Ibuprofeno", "Ibuprofeno al 2%", "Ibuprofeno-Cafeína", "Indometacina", "Indometacina-Betametasona-Metocarbamol", "Ketoprofeno", "Ketoprofeno-Paracetamol", "Ketorolaco", "Ketorolaco-Tramadol", "Ketorolaco-Trometamina", "Meloxicam", "Meloxicam-Metocarbamol", "Metamizol sodico", "Metocarbamol-Fenilbutazona-Dexametasona", "Metocarbamol-Ibuprofeno", "Naproxeno", "Naproxeno-Carisooprodol", "Naproxeno-Lidocaína", "Naproxeno-Paracetamol", "Paracetamol", "Paracetamol-ácido acetil-Cafeína", "Paracetamol-Cafeína", "Paracetamol-Ibuprofeno", "Paracetamol-Naproxeno", "Sulindaco", "Tiamina-Piridoxina-Cianocobalamina", "Tiamina-Piridoxina-Cianocobalamina-Lidocaína-Diclofenaco", "Tramadol", "Tramadol-Paracetamol", "Tribedoce Dx (Vit con dexa y lidocaína)", "Tribedoce compuesto (Vit con diclofenaco)"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layoutList = findViewById(R.id.layout_list);
        buttonAdd = findViewById(R.id.button_add);
        button_submit_list = findViewById(R.id.button_submit);

        buttonAdd.setOnClickListener(this); // Configurar un listener para el botón
        button_submit_list.setOnClickListener(this);

        teamList.add("Medicametos");
        teamList.add("Ácido nalidíxico-Fenazopiridina");
        teamList.add("Amikacina");
        teamList.add("Amoxicilina - Ambroxol");
        teamList.add("Amoxicilina");
        teamList.add("Amoxicilina Ac clav");
        teamList.add("Ampicilina");
        teamList.add("Azitromicina");
        teamList.add("Bencilpenicilina procaínica c/ Bencilpenicilina cristalina");
        teamList.add("Cefaclor");
        teamList.add("Cefalexina");
        teamList.add("Cefalexina/Bromhexina");
        teamList.add("Cefalexina-Ambroxol");
        teamList.add("Ceftriaxona");
        teamList.add("Ciprofloxacino");
        teamList.add("Claritromicina");
        teamList.add("Clindamicina");
        teamList.add("Dicloxacilina");
        teamList.add("Levofloxacino");
        teamList.add("Lincomicina");
        teamList.add("Metronidazol");
        teamList.add("Metronidazol Diyodohidroxiquinoleína");
        teamList.add("Tetraciclina");
        teamList.add("Tetraciclina clorhidrato de Tetra Atlantis");
        teamList.add("Trimetoprim-Sulfametoxazol");
        teamList.add("Acemetacina");
        teamList.add("Ácido acetil salicílico");
        teamList.add("Clonixinato de lisina-Hioscina");
        teamList.add("Complejo B Dexametasona Lidocaína");
        teamList.add("Diclofenaco");
        teamList.add("Diclofenaco-Complejo B");
        teamList.add("Diclofenaco-Paracetamol");
        teamList.add("Ibuprofeno");
        teamList.add("Ibuprofeno al 2%");
        teamList.add("Ibuprofeno-Cafeína");
        teamList.add("Indometacina");
        teamList.add("Indometacina-Betametasona-Metocarbamol");
        teamList.add("Ketoprofeno");
        teamList.add("Ketoprofeno-Paracetamol");
        teamList.add("Ketorolaco");
        teamList.add("Ketorolaco-Tramadol");
        teamList.add("Ketorolaco-Trometamina");
        teamList.add("Meloxicam");
        teamList.add("Meloxicam-Metocarbamol");
        teamList.add("Metamizol sodico");
        teamList.add("Metocarbamol-Fenilbutazona-Dexametasona");
        teamList.add("Metocarbamol-Ibuprofeno");
        teamList.add("Naproxeno");
        teamList.add("Naproxeno-Carisooprodol");
        teamList.add("Naproxeno-Lidocaína");
        teamList.add("Naproxeno-Paracetamol");
        teamList.add("Paracetamol");
        teamList.add("Paracetamol-ácido acetil-Cafeína");
        teamList.add("Paracetamol-Cafeína");
        teamList.add("Paracetamol-Ibuprofeno");
        teamList.add("Paracetamol-Naproxeno");
        teamList.add("Sulindaco");
        teamList.add("Tiamina-Piridoxina-Cianocobalamina");
        teamList.add("Tiamina-Piridoxina-Cianocobalamina-Lidocaína-Diclofenaco");
        teamList.add("Tramadol");
        teamList.add("Tramadol-Paracetamol");
        teamList.add("Tribedoce Dx (Vit con dexa y lidocaína)");
        teamList.add("Tribedoce compuesto (Vit con diclofenaco)");
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_add) {
            addView();
        } else if (v.getId() == R.id.button_submit) {
            if (checkIfValidAndRead()) {
                Intent intent = new Intent(RecetarFunction.this, ActivityCricketers.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("list", cricketersList);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }
    }


    private boolean checkIfValidAndRead(){
        cricketersList.clear();
        boolean result = true;

        for (int i=0;i<layoutList.getChildCount();i++){
            View cricketerView = layoutList.getChildAt(i);

            AppCompatSpinner spinner1 = (AppCompatSpinner)cricketerView.findViewById(R.id.spinner1);
            EditText editDosis = (EditText)cricketerView.findViewById(R.id.editDosis);
            EditText editDuracion = (EditText)cricketerView.findViewById(R.id.editDuracion);
            EditText editIntervalo = (EditText)cricketerView.findViewById(R.id.editIntervalo);
            EditText editEspecificaciones = (EditText)cricketerView.findViewById(R.id.editEspecificaciones);

            Cricketer cricketer = new Cricketer();

            if(spinner1.getSelectedItemPosition()!=0){
                cricketer.setSpinner1(teamList.get(spinner1.getSelectedItemPosition()));
            }else {
                result = false;
                break;
            }

            if(!editDosis.getText().toString().equals("")){
                cricketer.setEditDosis(editDosis.getText().toString());
            }else {
                result = false;
                break;
            }

            if(!editDuracion.getText().toString().equals("")){
                cricketer.setEditDuracion(editDuracion.getText().toString());
            }else {
                result = false;
                break;
            }

            if(!editIntervalo.getText().toString().equals("")){
                cricketer.setEditIntervalo(editIntervalo.getText().toString());
            }else {
                result = false;
                break;
            }

            if(!editEspecificaciones.getText().toString().equals("")){
                cricketer.setEditEspecificaciones(editEspecificaciones.getText().toString());
            }else {
                result = false;
                break;
            }

            cricketersList.add(cricketer);

        }

        if(cricketersList.size()==0){
            result = false;
            Toast.makeText(this, "Add Cricketers First!", Toast.LENGTH_SHORT).show();
        }else if(!result){
            Toast.makeText(this, "Enter All Details Correctly!", Toast.LENGTH_SHORT).show();
        }

        return result;
    }

    private void addView() {

        View cricketerView = getLayoutInflater().inflate(R.layout.row_add_cricketer, null, false);

        // Obtener referencias a elementos de la vista cricketerView

        ImageButton imageClose = (ImageButton)cricketerView.findViewById(R.id.image_remove);
        AppCompatSpinner spinner1 = (AppCompatSpinner)cricketerView.findViewById(R.id.spinner1);
        EditText editDosis = (EditText)cricketerView.findViewById(R.id.editDosis);
        EditText editDuracion = (EditText)cricketerView.findViewById(R.id.editDuracion);
        EditText editIntervalo = (EditText)cricketerView.findViewById(R.id.editIntervalo);
        EditText editEspecificaciones = (EditText)cricketerView.findViewById(R.id.editEspecificaciones);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, teamList);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);

        imageClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Llamar al método removeView al hacer clic en el botón
                removeView(cricketerView);
            }
        });

        // Agregar la vista inflada al diseño principal
        layoutList.addView(cricketerView);
    }

    // Método para eliminar una vista
    private void removeView(View view){


        layoutList.removeView(view);
    }
}