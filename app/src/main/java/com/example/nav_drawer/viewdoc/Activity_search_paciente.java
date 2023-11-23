package com.example.nav_drawer.viewdoc;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nav_drawer.FirebaseUtil;
import com.example.nav_drawer.R;
import com.example.nav_drawer.UserModel;
import com.example.nav_drawer.ChatroomModel;
import com.example.nav_drawer.adapter.SearchUserRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

public class Activity_search_paciente extends AppCompatActivity {

    EditText searchInput;
    ImageButton searchButton;
    ImageButton backButton;
    RecyclerView recyclerView;

    SearchUserRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_paciente);

        searchInput = findViewById(R.id.search_username_input);
        searchButton = findViewById(R.id.search_user_btn);
        backButton = findViewById(R.id.back_btn);
        recyclerView = findViewById(R.id.search_user_recycler_view);

        searchInput.requestFocus();

        backButton.setOnClickListener(v -> onBackPressed());

        searchButton.setOnClickListener(v -> {
            String searchTerm = searchInput.getText().toString();
            if (searchTerm.isEmpty() || searchTerm.length() < 3) {
                searchInput.setError("Paciente Incorrecto");
                return;
            }
            setupSearchRecyclerView(searchTerm);
        });
    }

    void setupSearchRecyclerView(String searchTerm) {
        // Realizar la búsqueda de usuarios según el término de búsqueda
        Query userQuery = FirebaseUtil.allUserCollectionReference()
                .whereEqualTo("nombre", searchTerm);

        FirestoreRecyclerOptions<UserModel> userOptions = new FirestoreRecyclerOptions.Builder<UserModel>()
                .setQuery(userQuery, UserModel.class).build();

        // Realizar la búsqueda de chatrooms que incluyan a usuarios con el término de búsqueda
        Query chatroomQuery = FirebaseUtil.allChatroomsCollectionReference()
                .whereArrayContains("participantIds", searchTerm);

        FirestoreRecyclerOptions<ChatroomModel> chatroomOptions = new FirestoreRecyclerOptions.Builder<ChatroomModel>()
                .setQuery(chatroomQuery, ChatroomModel.class).build();

        // Iniciar el adaptador con las opciones de búsqueda de usuarios
        adapter = new SearchUserRecyclerAdapter(userOptions, chatroomOptions, getApplicationContext());
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
