package com.example.nav_drawer.viewpaciente;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.nav_drawer.DoctorModel;
import com.example.nav_drawer.FirebaseUtilDoctor;
import com.example.nav_drawer.R;
import com.example.nav_drawer.DoctorModel;
import com.example.nav_drawer.adapter.SearchDoctorRecyclerAdapter;
import com.example.nav_drawer.adapter.SearchUserRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

public class Activity_search_doctor extends AppCompatActivity {
        EditText searchInput;
        ImageButton searchButton;
        ImageButton backButton;
        RecyclerView recyclerView;

        SearchDoctorRecyclerAdapter adapter;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_search_doctor);

            searchInput = findViewById(R.id.search_username_input);
            searchButton = findViewById(R.id.search_user_btn);
            backButton = findViewById(R.id.back_btn);
            recyclerView = findViewById(R.id.search_user_recycler_view);

            searchInput.requestFocus();

            backButton.setOnClickListener(v -> onBackPressed());

            searchButton.setOnClickListener(v -> {
                String searchTerm = searchInput.getText().toString();
                if (searchTerm.isEmpty() || searchTerm.length() < 3) {
                    searchInput.setError("Doctor Incorrecto");
                    return;
                }
                setupSearchRecyclerView(searchTerm);
            });
        }

        void setupSearchRecyclerView(String searchTerm) {
            Query query = FirebaseUtilDoctor.allDoctorCollectionReference()
                    .whereEqualTo("nombre", searchTerm);

            FirestoreRecyclerOptions<DoctorModel> options = new FirestoreRecyclerOptions.Builder<DoctorModel>()
                    .setQuery(query, DoctorModel.class).build();

            adapter = new SearchDoctorRecyclerAdapter(options, getApplicationContext());
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);
            adapter.startListening();
        }

        @Override
        protected void onStart() {
            super.onStart();
            if (adapter != null)
                adapter.startListening();
        }

        @Override
        protected void onStop() {
            super.onStop();
            if (adapter != null)
                adapter.stopListening();
        }

        @Override
        protected void onResume() {
            super.onResume();
            if (adapter != null)
                adapter.startListening();
        }
}
