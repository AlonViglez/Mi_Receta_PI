package com.example.nav_drawer.viewpaciente;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.nav_drawer.AndroidUtilDoctor;
import com.example.nav_drawer.ChatroomModelDoctor;
import com.example.nav_drawer.DoctorModel;
import com.example.nav_drawer.FirebaseUtilDoctor;
import com.example.nav_drawer.R;
import com.google.firebase.Timestamp;

import java.util.Arrays;

public class Activity_Chat_Paciente extends AppCompatActivity {

    DoctorModel otherDoctor;
    String chatroomId;
    ChatroomModelDoctor chatroomModelDoctor;
    EditText messageInput;
    ImageButton sendMessageBtn;
    ImageButton backBtn;
    TextView otherDoctorname;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_paciente);

        otherDoctor = AndroidUtilDoctor.getDoctorModelFromIntent(getIntent());
        chatroomId = FirebaseUtilDoctor.getChatroomId(FirebaseUtilDoctor.currentDoctorId(),otherDoctor.getId());

        messageInput = findViewById(R.id.chat_message_input);
        sendMessageBtn = findViewById(R.id.message_send_btn);
        backBtn = findViewById(R.id.back_btn);
        otherDoctorname = findViewById(R.id.other_username);
        recyclerView = findViewById(R.id.chat_recycler_view);

        backBtn.setOnClickListener((v)->{
            onBackPressed();
        });

        otherDoctorname.setText("Medico "+otherDoctor.getNombre());

        getOrCreateChatroomModelDoctor();

    }

    void getOrCreateChatroomModelDoctor(){
        FirebaseUtilDoctor.getChatroomReference(chatroomId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                chatroomModelDoctor = task.getResult().toObject(chatroomModelDoctor.getClass());
                if(chatroomModelDoctor==null){
                    chatroomModelDoctor = new ChatroomModelDoctor(
                            chatroomId,
                            Arrays.asList(FirebaseUtilDoctor.currentDoctorId(),otherDoctor.getId()),
                            Timestamp.now(),
                            ""
                    );
                    FirebaseUtilDoctor.getChatroomReference(chatroomId).set(chatroomModelDoctor);
                }
            }
        });
    }
}