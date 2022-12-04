package com.example.clinicadental;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class CrearUActivity extends AppCompatActivity {

    Button btnCrearCuenta;
    Button btnSubirFoto;

    public static FirebaseFirestore firestore;
    public static FirebaseStorage storage;

    EditText nombre;
    EditText apellidop;
    EditText apellidom;
    EditText correo;
    EditText numerotelefono;
    EditText direccion;
    EditText contrasena;
    EditText repetircontra;

    String snombre;
    String sapellidop;
    String sapellidom;
    String scorreo;
    String stelefono;
    String sdireccion;
    String scontrasena;
    String srepetircontra;
    String stipo;
    String slink;
    String urluri;

    ImageView imagen;
    boolean imagenagregada=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crearcuenta);

        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        //Obtiene los datos de los text field
        nombre = findViewById(R.id.etCNombre);
        apellidop = findViewById(R.id.etCApellidoP);
        apellidom = findViewById(R.id.etCApellidoM);
        correo = findViewById(R.id.etCCorreo);
        numerotelefono = findViewById(R.id.etCTelefono);
        direccion = findViewById(R.id.etCDireccion);
        contrasena = findViewById(R.id.etCContra);
        repetircontra = findViewById(R.id.etCRContra);

        imagen = findViewById(R.id.ivCImagen);

        btnSubirFoto = findViewById(R.id.btnCSubirFoto);
        btnSubirFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargarImagen();
            }
        });

        btnCrearCuenta = findViewById(R.id.btnCrearCuenta);
        btnCrearCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Transforma los datos a string para poder trabajar con ellos
                snombre = nombre.getText().toString().trim();
                sapellidop = apellidop.getText().toString().trim();
                sapellidom = apellidom.getText().toString().trim();
                scorreo = correo.getText().toString().trim();
                stelefono = numerotelefono.getText().toString().trim();
                sdireccion = direccion.getText().toString().trim();
                scontrasena = contrasena.getText().toString().trim();
                srepetircontra = repetircontra.getText().toString().trim();
                stipo = "usuario";

                if(validaCampos()==true){
                    consultaCorreo(scorreo);
                }
            }
        });
    }

    public void cargarImagen(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/jpeg");
        startActivityForResult(intent.createChooser(intent,"Selecciona la aplicacion"),10);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            Uri path = data.getData();
            imagen.setImageURI(path);
            imagenagregada=true;
        }

    }

    public void subirFoto(){

        StorageReference storageRef = storage.getReference();
        StorageReference usuarioRef = storageRef.child("usuario/" + scorreo);

        imagen.setDrawingCacheEnabled(true);
        imagen.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imagen.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = usuarioRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(CrearUActivity.this, "Error al subir imagen", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //Toast.makeText(CrearUActivity.this, "IMAGEN SUBIDA CON EXITO", Toast.LENGTH_SHORT).show();
                System.out.println("IMAGEN GUARDADA CON EXITO");
                //System.out.println("--------------------LINK IMAGEN: " + usuarioRef);
                usuarioRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        urluri = uri.toString();
                        slink = urluri;
                        System.out.println("------------------LINK IMAGEN: " + slink);
                        agregarUsuario(snombre,sapellidop,sapellidom,scorreo,stelefono,sdireccion,scontrasena,stipo,slink);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }
        });
    }

    public void consultaCorreo(String correo){

        DocumentReference docref = firestore.collection("usuario").document(correo);
        docref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        Toast.makeText(CrearUActivity.this, "El correo que ingreso ha sido registrado previamente", Toast.LENGTH_SHORT).show();
                        //limpiaCampos();
                    }else{
                        subirFoto();
                        //System.out.println("-----------------SLINK: "+ slink);
                    }
                }else{
                    System.out.println("Error al consultar: " + task.getException());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CrearUActivity.this, "Error al ingresar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void agregarUsuario(String nombre,String apellidop, String apellidom, String correo,
                               String numero,String direccion,String contrasena,String tipo,String linkimagen){

        Map<String,Object> usuario = new HashMap<>();

        usuario.put("nombre",nombre);
        usuario.put("apellido_paterno",apellidop);
        usuario.put("apellido_materno",apellidom);
        usuario.put("correo",correo);
        usuario.put("numero_telefono",numero);
        usuario.put("direccion",direccion);
        usuario.put("contrasena",contrasena);
        usuario.put("tipo",tipo);
        usuario.put("link_imagen",linkimagen);

        firestore.collection("usuario").document(correo).set(usuario).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(CrearUActivity.this, "Usuario creado correctamente", Toast.LENGTH_SHORT).show();
                limpiaCampos();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CrearUActivity.this, "Error al crear usuario", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean validaCampos(){

        if(nombre.getText().toString().trim().isEmpty()) {
            nombre.setHint("Nombre obligatorio");
            nombre.setHintTextColor(getResources().getColor(R.color.red));
            return false;
        }else{
            if(apellidop.getText().toString().trim().isEmpty()) {
                apellidop.setHint("Apellido paterno obligatorio");
                apellidop.setHintTextColor(getResources().getColor(R.color.red));
                return false;
            }else{
                if(apellidom.getText().toString().trim().isEmpty()) {
                    apellidom.setHint("Apellido materno obligatorio");
                    apellidom.setHintTextColor(getResources().getColor(R.color.red));
                    return false;
                }else{
                    if(numerotelefono.getText().toString().trim().isEmpty()) {
                        numerotelefono.setHint("Numero de telefono obligatorio");
                        numerotelefono.setHintTextColor(getResources().getColor(R.color.red));
                        return false;
                    }else{
                        if(correo.getText().toString().trim().isEmpty()) {
                            correo.setHint("Correo obligatorio");
                            correo.setHintTextColor(getResources().getColor(R.color.red));
                            return false;
                        }else{
                            if(direccion.getText().toString().trim().isEmpty()) {
                                direccion.setHint("Direccion obligatoria");
                                direccion.setHintTextColor(getResources().getColor(R.color.red));
                                return false;
                            }else{
                                if(contrasena.getText().toString().trim().isEmpty()) {
                                    contrasena.setHint("Contraseña obligatoria");
                                    contrasena.setHintTextColor(getResources().getColor(R.color.red));
                                    return false;
                                }else{
                                    if(repetircontra.getText().toString().trim().isEmpty()) {
                                        repetircontra.setHint("Campo obligatorio");
                                        repetircontra.setHintTextColor(getResources().getColor(R.color.red));
                                        return false;
                                    }else{
                                        if(scontrasena.equals(srepetircontra) == false){
                                            Toast.makeText(CrearUActivity.this, "Ambas contraseñas deben coincidir", Toast.LENGTH_SHORT).show();
                                            return false;
                                        }else{
                                            if(imagenagregada == false){
                                                Toast.makeText(CrearUActivity.this, "Debe agregar una imagen", Toast.LENGTH_SHORT).show();
                                                return false;
                                            }else{
                                                if(contrasena.getText().toString().length()<6 ||
                                                        repetircontra.getText().toString().length()<6){
                                                    Toast.makeText(CrearUActivity.this, "La contraseña debe ser mayor a 6 digitos", Toast.LENGTH_SHORT).show();
                                                    return false;
                                                }else{
                                                    return true;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void limpiaCampos(){
        nombre.setText("");
        apellidop.setText("");
        apellidom.setText("");
        correo.setText("");
        numerotelefono.setText("");
        direccion.setText("");
        contrasena.setText("");
        repetircontra.setText("");
        imagen.setImageResource(R.drawable.icono_usuario);
    }
}