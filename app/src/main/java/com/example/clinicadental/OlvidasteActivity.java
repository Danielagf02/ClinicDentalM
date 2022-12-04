package com.example.clinicadental;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class OlvidasteActivity extends AppCompatActivity {

    EditText correo;
    Button btnEnviarCorreo;

    String scorreo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_olvidaste);

        correo = findViewById(R.id.etRCorreo);
        scorreo = correo.getText().toString().trim();

        btnEnviarCorreo = findViewById(R.id.btnEnviarCorreo);
        btnEnviarCorreo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enviarCorreo(scorreo);
            }
        });
    }

    //Metodo para enviar correo de recuperacion
    public void enviarCorreo(String correo){
        Toast.makeText(OlvidasteActivity.this, "Correo de recuperacion enviado", Toast.LENGTH_SHORT).show();
    }

}
