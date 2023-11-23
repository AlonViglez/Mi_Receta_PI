package com.example.nav_drawer.viewpaciente;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nav_drawer.R;
import com.example.nav_drawer.viewdoc.Actualizarperfildoc;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActualizarPerfilPaciente extends AppCompatActivity {
    EditText editnombre;
    EditText editnumero;

    String userEmail;

    EditText editdescripcion;

    TextView textViewCamp;

    Button editProfile;

    Button btnSubirfotoperfil;
    String id;

    Intent intent;

    ImageView btnregresar;
    CircleImageView imagenperfil;

    private static final int PICK_IMAGE = 1;

    private boolean subirimage = true;
    private Uri imagenperfila = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_perfil_paciente);

        editnombre = findViewById(R.id.profileName);
        editdescripcion = findViewById(R.id.profiledescr);
        editProfile = findViewById(R.id.editbutton);
        btnregresar = findViewById(R.id.btnatras);
        textViewCamp = findViewById(R.id.textViewCampos);
        imagenperfil = findViewById(R.id.fotoperfil);

        btnSubirfotoperfil = findViewById(R.id.editperfil);
        intent = getIntent();
        id = intent.getStringExtra("id"); //Recibimos el id del fragment

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            userEmail = currentUser.getEmail();
           // Toast.makeText(this, "Usuario autenticado" + userEmail, Toast.LENGTH_SHORT).show();
            // Resto del código...
        } else {
            Log.e(TAG, "Usuario no autenticado");
            Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show();
        }

        btnregresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActualizarPerfilPaciente.this, ViewPacient.class);
                startActivity(intent);
            }
        });
        //Actualizar los campos del perfil
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String valorEditText = editdescripcion.getText().toString();
                String valorEditTextnombre = editnombre.getText().toString();
                uploadTextToFirestore(userEmail, valorEditText, valorEditTextnombre, imagenperfila);

            }
        });
        btnSubirfotoperfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subirimage = true;
                seleccionarImagen();
                boolean imagenINEValida = validarImagen(imagenperfila);
                if (imagenINEValida) {
                    // IMAGENES SON VALIDAS
                    // OCULTAR MENSAJES DE ERROR
                    textViewCamp.setVisibility(View.GONE);
                    // GUARDAR EN FIRESTORE
                }else {
                    // Al menos una de las imágenes no es válida
                    textViewCamp.setText("Imagen(es) no válidas");
                    textViewCamp.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void  seleccionarImagen(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE);

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {//SUBIR IMAGEN INE Y CEDULA
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE) {
                Uri imageUri = data.getData(); //URL de la imagen
                if (subirimage) {
                    // La imagen es para la INE
                    imagenperfila = imageUri; // Guardar la Uri de la imagen en la variable
                    imagenperfil.setImageURI(imageUri);
                    imagenperfil.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private boolean validarImagen(Uri imagenperfila) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(imagenperfila);// Abre un flujo de entrada para la URI de la imagen
            BitmapFactory.Options options = new BitmapFactory.Options(); // Crea un objeto BitmapFactory.Options
            options.inJustDecodeBounds = true;// Establece la propiedad inJustDecodeBounds en true para obtener solo las dimensiones de la imagen
            BitmapFactory.decodeStream(inputStream, null, options);// Intenta decodificar las dimensiones de la imagen sin cargar completamente su contenido
            inputStream.close();// Cierra el flujo de entrada

            return (options.outWidth != -1 && options.outHeight != -1);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public void uploadTextToFirestore(String email, String valorEditText, String valorEditTextnombre, Uri imagenperfila) {
        try {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            if (currentUser != null) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                if (valorEditTextnombre.isEmpty()){
                    textViewCamp.setText("Debe de rellenar los campos vacios");
                    textViewCamp.setVisibility(View.VISIBLE);
                }else {
                    if (valorEditText.isEmpty()) {
                        textViewCamp.setText("Debe de rellenar los campos vacios");
                        textViewCamp.setVisibility(View.VISIBLE);
                    } else {

                            Map<String, Object> data = new HashMap<>();
                            data.put("nombre", valorEditTextnombre);
                            if (email != null && !email.isEmpty()) {
                                // Llamar a update aquí
                                db.collection("users")
                                        .document(id)
                                        .update("nombre", valorEditTextnombre)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d(TAG, "Campo 'descripcion' actualizado con éxito");
                                                // Puedes realizar acciones adicionales si es necesario
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w(TAG, "Error al actualizar campo 'descripcion'", e);
                                                // Puedes manejar el error aquí
                                                Toast.makeText(ActualizarPerfilPaciente.this, "Error al actualizar campo 'descripcion'", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                Map<String, Object> data2 = new HashMap<>();
                                data2.put("descripcion", valorEditText);
                                db.collection("users")
                                        .document(id)
                                        .update("descripcion", valorEditText)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d(TAG, "Campo 'descripcion' actualizado con éxito");
                                                // Puedes realizar acciones adicionales si es necesario
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w(TAG, "Error al actualizar campo 'descripcion'", e);
                                                // Puedes manejar el error aquí
                                                Toast.makeText(ActualizarPerfilPaciente.this, "Error al actualizar campo 'descripcion'", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                Map<String, Object> data4 = new HashMap<>();
                                data4.put("imagenperfilurl", imagenperfila);
                                db.collection("users")
                                        .document(id)
                                        .update("imagenperfilurl", imagenperfila)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d(TAG, "Campo 'descripcion' actualizado con éxito");
                                                String imageName = UUID.randomUUID().toString(); // Nombre único para la imagen
                                                StorageReference imageRef = storageRef.child("images/perfilpaciente" + imageName);

                                                // Subir la imagen y obtener su URL
                                                imageRef.putFile(imagenperfila)
                                                        .addOnSuccessListener(taskSnapshot -> {
                                                            // Obtener la URL de la imagen subida
                                                            imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                                                // Guardar la URL en Firestore
                                                                Map<String, Object> data4 = new HashMap<>();
                                                                data4.put("imagenperfilurl", uri.toString());
                                                                db.collection("users")
                                                                        .document(id)
                                                                        .update(data4);
                                                            });
                                                        })
                                                        .addOnFailureListener(e -> {
                                                            // Manejar el error al subir la imagen
                                                            Log.e(TAG, "Error al subir imagen: " + e.getMessage());
                                                            Toast.makeText(ActualizarPerfilPaciente.this, "Error al subir imagen", Toast.LENGTH_SHORT).show();
                                                        });
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w(TAG, "Error al actualizar campo 'descripcion'", e);
                                                // Puedes manejar el error aquí
                                                Toast.makeText(ActualizarPerfilPaciente.this, "Error al actualizar campo 'descripcion'", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                Toast.makeText(ActualizarPerfilPaciente.this, "Datos actualizados correctamente", Toast.LENGTH_SHORT).show();

                            } else {
                                Log.e(TAG, "El correo electrónico es nulo o vacío");
                                Toast.makeText(ActualizarPerfilPaciente.this, "El correo electrónico es nulo o vacío", Toast.LENGTH_SHORT).show();
                            }
                        Intent i = new Intent(ActualizarPerfilPaciente.this, ViewPacient.class);
                        startActivity(i);
                    }
                }

            }
        }catch (Exception e) {
            Log.e(TAG, "Error: " + e.getMessage());
            Toast.makeText(ActualizarPerfilPaciente.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }
}