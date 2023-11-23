package com.example.nav_drawer;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseUtilDoctor {

    public static String currentDoctorId() {
        return FirebaseAuth.getInstance().getUid();
    }

    public static DocumentReference currentDoctorDetails() {
        return FirebaseFirestore.getInstance().collection("altadoctores").document(currentDoctorId());
    }

    // Cambios aquí: Utilizar una colección general para los chatrooms
    public static CollectionReference allDoctorCollectionReference() {
        return FirebaseFirestore.getInstance().collection("chatrooms");
    }

    public static DocumentReference getChatroomReference(String chatroomId) {
        return allDoctorCollectionReference().document(chatroomId);
    }

    // Cambios aquí: Utilizar una subcolección para los mensajes dentro de cada chatroom
    public static CollectionReference getChatroomMessageReference(String chatroomId) {
        return getChatroomReference(chatroomId).collection("messages");
    }

    // Cambios aquí: Crear un chatroomId basado en los IDs de los participantes
    public static String getChatroomId(String participantId1, String participantId2) {
        if (participantId1.hashCode() < participantId2.hashCode()) {
            return participantId1 + "_" + participantId2;
        } else {
            return participantId2 + "_" + participantId1;
        }
    }
}
