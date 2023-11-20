package com.example.nav_drawer;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.annotation.Documented;

public class FirebaseUtilDoctor {


        public static String currentDoctorId(){
            return FirebaseAuth.getInstance().getUid();
        }
        public static DocumentReference currentDoctorDetails(){
            return FirebaseFirestore.getInstance().collection("altadoctores").document(currentDoctorId());
        }

        public static CollectionReference allDoctorCollectionReference(){
            return FirebaseFirestore.getInstance().collection("altadoctores");
        }

        public  static DocumentReference getChatroomReference(String chatroomId){
            return FirebaseFirestore.getInstance().collection("chatrooms").document(chatroomId);
        }

        public static CollectionReference getChatroomMessageReference(String chatroomId){
            return getChatroomReference(chatroomId).collection("chats");
        }

        public  static String getChatroomId(String doctorId1,String doctorId2){
            if (doctorId1.hashCode()<doctorId2.hashCode()){
                return doctorId1+"_"+doctorId2;
            }else {
                return doctorId2+"_"+doctorId1;
            }
        }

    }
