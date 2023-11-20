package com.example.nav_drawer.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nav_drawer.AndroidUtilDoctor;
import com.example.nav_drawer.DoctorModel;
import com.example.nav_drawer.R;
import com.example.nav_drawer.viewdoc.Activity_Chat;
import com.example.nav_drawer.viewpaciente.Activity_Chat_Paciente;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class SearchDoctorRecyclerAdapter extends FirestoreRecyclerAdapter<DoctorModel, SearchDoctorRecyclerAdapter.DoctorModelViewHolder> {

    Context context;

    public SearchDoctorRecyclerAdapter(@NonNull FirestoreRecyclerOptions<DoctorModel> options,Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull DoctorModelViewHolder holder, int position, @NonNull DoctorModel model) {
        holder.doctornameText.setText(model.getNombre());

        holder.itemView.setOnClickListener(v ->{
            Intent intent = new Intent(context, Activity_Chat_Paciente.class);
            AndroidUtilDoctor.passDoctorModelAsIntent(intent,model);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
    }

    @NonNull
    @Override
    public DoctorModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_user_recycler_row,parent,false);
        return new DoctorModelViewHolder(view);
    }

    class DoctorModelViewHolder extends RecyclerView.ViewHolder{
        TextView doctornameText;
        ImageView profilepic;
        public DoctorModelViewHolder(@NonNull View itemView) {
            super(itemView);
            doctornameText = itemView.findViewById(R.id.user_name_text);
            profilepic = itemView.findViewById(R.id.profile_pic_image_view);
        }
    }
}
