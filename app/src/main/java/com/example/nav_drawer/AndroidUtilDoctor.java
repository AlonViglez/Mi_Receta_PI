package com.example.nav_drawer;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AndroidUtilDoctor {
        public static void showToast(Context context, String message) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }

        public static void passDoctorModelAsIntent(Intent intent, DoctorModel model){
            intent.putExtra("nombre",model.getNombre());
            intent.putExtra("id",model.getId());

        }

        public static DoctorModel getDoctorModelFromIntent(Intent intent) {
            DoctorModel doctorModel= new DoctorModel();
            doctorModel.setNombre(intent.getStringExtra("nombre"));
            doctorModel.setId(intent.getStringExtra("id"));
            return doctorModel;
        }
}
