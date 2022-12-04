package com.example.clinicadental;

import static com.example.clinicadental.CrearUActivity.firestore;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    Button btnAcceso;

    EditText correo;
    EditText contrasena;

    TextView olvidaste;
    TextView crearcuenta;

    //Almacena lo que tienen los edittext
    public static String scorreo;
    public static String scontrasena;

    //Almacena lo que recupera la base de datos
    public static String correorecuperado;
    String contrarecuperada;
    public static String tiporecuperado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firestore = FirebaseFirestore.getInstance();

        correo = findViewById(R.id.etLCorreo);
        contrasena = findViewById(R.id.etLContrasena);

        btnAcceso = findViewById(R.id.btnAcceder);
        btnAcceso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                scorreo = correo.getText().toString().trim();
                scontrasena = contrasena.getText().toString().trim();

                if(validaCampos()==true){
                    login(scorreo,scontrasena);
                }

            }
        });

        /*olvidaste = findViewById(R.id.tvOlvidaste);
        olvidaste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, OlvidasteActivity.class);
                startActivity(intent);
            }
        });*/


        crearcuenta = findViewById(R.id.tvCrear);
        crearcuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, CrearUActivity.class);
                startActivity(intent);
            }
        });


    }

    // Funciones propias
    public void login(String correo,String contrasena){

        DocumentReference docref = firestore.collection("usuario").document(correo);
        docref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        correorecuperado = (String) document.getData().get("correo");
                        contrarecuperada = (String) document.getData().get("contrasena");
                        tiporecuperado = (String) document.getData().get("tipo");

                        if(correo.equals(correorecuperado) && contrasena.equals(contrarecuperada)){

                            switch (tiporecuperado){
                                case "usuario":
                                    Bundle bundle = new Bundle();
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    //intent.putExtra("correologin",correorecuperado);
                                    bundle.putString("correologin",correorecuperado);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                    break;
                                case "admin":
                                    Bundle bundlea = new Bundle();
                                    Intent intenta = new Intent(LoginActivity.this, MainActivity.class);
                                    bundlea.putString("correologin",correorecuperado);
                                    intenta.putExtras(bundlea);
                                    startActivity(intenta);
                                    break;
                                default:
                                    Toast.makeText(LoginActivity.this, "Tipo de usuario no reconocido", Toast.LENGTH_SHORT).show();
                                    break;
                            }

                        }else{
                            Toast.makeText(LoginActivity.this, "Correo o contraseña incorrectas", Toast.LENGTH_SHORT).show();
                            limpiaCampos();
                        }
                    }else{
                        //Esta parte se deberia quitar por seguridad
                        Toast.makeText(LoginActivity.this, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    System.out.println("Error al ingresar: " + task.getException());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this, "Error al ingresar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean validaCampos(){
        if(correo.getText().toString().trim().isEmpty()) {
            correo.setHint("Correo obligatorio");
            correo.setHintTextColor(getResources().getColor(R.color.red));
            return false;
        }else{
            if(contrasena.getText().toString().trim().isEmpty()) {
                contrasena.setHint("Contraseña obligatoria");
                contrasena.setHintTextColor(getResources().getColor(R.color.red));
                return false;
            }else{
                return true;
            }
        }
    }

    public void limpiaCampos(){
        correo.setText("");
        contrasena.setText("");
    }
}
