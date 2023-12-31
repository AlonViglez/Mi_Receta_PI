package com.example.nav_drawer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CricketerAdapter extends RecyclerView.Adapter<CricketerAdapter.CricketerView> {
    //Hola soy messi
    ArrayList<Cricketer> cricketersList = new ArrayList<>();

    public CricketerAdapter(ArrayList<Cricketer> cricketersList) {
        this.cricketersList = cricketersList;
    }

    @NonNull
    @Override
    public CricketerView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_cricketer,parent,false);

        return new CricketerView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CricketerView holder, int position) {

        Cricketer cricketer = cricketersList.get(position);
        holder.spinner1.setText(cricketer.getSpinner1());
        holder.txtDosis.setText(cricketer.getEditDosis());
        holder.txtDuracion.setText(cricketer.getEditDuracion());
        holder.txtIntervalo.setText(cricketer.getEditIntervalo());
        holder.txtEspecificaciones.setText(cricketer.getEditEspecificaciones());


    }

    @Override
    public int getItemCount() {
        return cricketersList.size();
    }

    public class CricketerView extends RecyclerView.ViewHolder{

        TextView spinner1, txtDosis, txtDuracion, txtIntervalo, txtEspecificaciones;
        public CricketerView(@NonNull View itemView) {
            super(itemView);

            spinner1 = (TextView)itemView.findViewById(R.id.spinner1);
            txtDosis = (TextView)itemView.findViewById(R.id.text_dosis);
            txtDuracion = (TextView)itemView.findViewById(R.id.editDuracion);
            txtIntervalo = (TextView)itemView.findViewById(R.id.editIntervalo);
            txtEspecificaciones = (TextView)itemView.findViewById(R.id.editEspecificaciones);
        }
    }

}