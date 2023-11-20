package com.example.nav_drawer.viewpaciente;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.nav_drawer.AndroidUtilDoctor;
import com.example.nav_drawer.ChatMessageModel;
import com.example.nav_drawer.ChatroomModelDoctor;
import com.example.nav_drawer.DoctorModel;
import com.example.nav_drawer.FirebaseUtilDoctor;
import com.example.nav_drawer.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

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

        messageInput = findViewById(R.id.chat_message_inputs);
        sendMessageBtn = findViewById(R.id.message_send_btns);
        backBtn = findViewById(R.id.back_btns);
        otherDoctorname = findViewById(R.id.other_usernames);
        recyclerView = findViewById(R.id.chat_recycler_views);

        backBtn.setOnClickListener((v)->{
            onBackPressed();
        });

        otherDoctorname.setText("Medico "+otherDoctor.getNombre());


        sendMessageBtn.setOnClickListener(v -> {
            String message = messageInput.getText().toString().trim();
            if (message.isEmpty()) {
                return;
            }
            sendMessageToDoctor(message);
        });

        getOrCreateChatroomModelDoctor();

    }

    void getOrCreateChatroomModelDoctor() {
        FirebaseUtilDoctor.getChatroomReference(chatroomId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document != null && document.exists()) {
                    chatroomModelDoctor = document.toObject(ChatroomModelDoctor.class);
                } else {
                    chatroomModelDoctor = new ChatroomModelDoctor(
                            chatroomId,
                            Arrays.asList(FirebaseUtilDoctor.currentDoctorId(), otherDoctor.getId()),
                            Timestamp.now(),
                            ""
                    );
                    FirebaseUtilDoctor.getChatroomReference(chatroomId).set(chatroomModelDoctor);
                }
            } else {
                // Manejar la excepción si la operación no fue exitosa
                Exception exception = task.getException();
                if (exception != null) {
                    // Mostrar la traza de la excepción
                    exception.printStackTrace();
                }
            }
        });
    }


    void sendMessageToDoctor(String message){

        chatroomModelDoctor.setLastMessageTimestamp(Timestamp.now());
        chatroomModelDoctor.setLastMessageSenderId(FirebaseUtilDoctor.currentDoctorId());

        FirebaseUtilDoctor.getChatroomReference(chatroomId).set(chatroomModelDoctor);

        ChatMessageModel chatMessageModel = new ChatMessageModel(message,FirebaseUtilDoctor.currentDoctorId(),Timestamp.now());
        FirebaseUtilDoctor.getChatroomMessageReference(chatroomId).add(chatMessageModel)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if(task.isSuccessful()){
                            messageInput.setText("");
                        }
                    }
                });
    }


}