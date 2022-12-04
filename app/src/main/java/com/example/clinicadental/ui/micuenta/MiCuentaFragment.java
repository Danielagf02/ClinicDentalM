package com.example.clinicadental.ui.micuenta;

import static android.app.Activity.RESULT_OK;
import static com.example.clinicadental.CrearUActivity.firestore;
import static com.example.clinicadental.CrearUActivity.storage;
import static com.example.clinicadental.LoginActivity.correorecuperado;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.clinicadental.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class MiCuentaFragment extends Fragment implements View.OnClickListener{

    ImageView imagen;

    EditText nombre;
    EditText apellidop;
    EditText apellidom;
    EditText telefono;
    EditText direccion;
    EditText contrasena;

    String snombre;
    String sapellidop;
    String sapellidom;
    String stelefono;
    String sdireccion;
    String scontrasena;
    String scorreo;

    Button btnEliminarC;
    Button btnActualizar;
    Button btnSubirFoto;
    Button btnCerrarSesion;

    boolean encontrado=false;
    boolean imagenagregada=false;

    Bundle datos;

    public static MiCuentaFragment newInstance() {return new MiCuentaFragment();}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.fragment_micuenta, container, false);
        View root = inflater.inflate(R.layout.fragment_micuenta, container, false);
        Componentes(root);
        return root;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        consultaCorreo(correorecuperado);
        encontrado = true;
    }

    private void Componentes(View root){
        ButtonComponentes(root);
        EditTextComponentes(root);
        ImageViewComponentes(root);
    }

    private void ButtonComponentes(View root){
        btnEliminarC = root.findViewById(R.id.btnMCEliminarC);
        btnEliminarC.setOnClickListener(this);

        btnSubirFoto = root.findViewById(R.id.btnMCSubirFoto);
        btnSubirFoto.setOnClickListener(this);

        btnActualizar = root.findViewById(R.id.btnMCModificarC);
        btnActualizar.setOnClickListener(this);

        btnCerrarSesion = root.findViewById(R.id.btnCerrarSesion);
        btnCerrarSesion.setOnClickListener(this);
    }

    private void EditTextComponentes(View root){
        nombre = root.findViewById(R.id.etMCNombre);
        apellidop = root.findViewById(R.id.etMCApellidoP);
        apellidom = root.findViewById(R.id.etMCApellidoM);
        telefono = root.findViewById(R.id.etMCTelefono);
        direccion = root.findViewById(R.id.etMCDireccion);
        contrasena = root.findViewById(R.id.etMCContra);
    }

    private void ImageViewComponentes(View root){
        imagen = root.findViewById(R.id.ivMCImagen);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnMCModificarC:
                if(validaCampos()==true){
                    if(imagenagregada==true){
                        actualizaUsuario();
                        subirFoto();
                    }else{
                        actualizaUsuario();
                    }
                }
                break;
            case R.id.btnMCSubirFoto:
                seleccionaImagen();
                break;
            case R.id.btnMCEliminarC:
                new AlertDialog.Builder(getActivity())
                        .setTitle("Eliminar cuenta")
                        .setMessage("¿Esta segudo que desea eliminar su cuenta?")
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                eliminaUsuario(correorecuperado);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                System.out.println("No elimino la cuenta");
                            }
                        }).show();
                break;
            case R.id.btnCerrarSesion:
                getActivity().finish();
                break;
            default:
                break;
        }
    }

    public void consultaCorreo(String correob){

        DocumentReference docref = firestore.collection("usuario").document(correob);
        docref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        //System.out.println(document.getData().get("nombre"));
                        snombre = (String) document.getData().get("nombre");
                        sapellidop = (String) document.getData().get("apellido_paterno");
                        sapellidom = (String) document.getData().get("apellido_materno");
                        stelefono = (String) document.getData().get("numero_telefono");
                        sdireccion = (String) document.getData().get("direccion");
                        scontrasena = (String) document.getData().get("contrasena");

                        scorreo = (String) document.getData().get("correo");

                        nombre.setText(snombre);
                        apellidop.setText(sapellidop);
                        apellidom.setText(sapellidom);
                        telefono.setText(stelefono);
                        direccion.setText(sdireccion);
                        contrasena.setText(scontrasena);

                        cargarImagen(scorreo);

                        //encontrado=true;
                        btnEliminarC.setEnabled(true);
                        btnActualizar.setEnabled(true);
                    }else{
                        Toast.makeText(getActivity(), "No se encontro el usuario", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    System.out.println("Error al consultar: " + task.getException());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Error al ingresar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void actualizaUsuario(){

        if(encontrado ==true){
            DocumentReference docref = firestore.collection("usuario").document(scorreo);

            docref.update("nombre",nombre.getText().toString().trim(),
                            "apellido_paterno",apellidop.getText().toString().trim(),
                            "apellido_materno",apellidom.getText().toString().trim(),
                            "numero_telefono",telefono.getText().toString().trim(),
                            "direccion",direccion.getText().toString().trim(),
                            "contrasena",contrasena.getText().toString().trim())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getActivity(), "Se actualizaron los datos correctamente. La proxima vez que entre a su perfil, vera los cambios reflejados", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "Error al actualizar el usuario", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    public void cargarImagen(String correo){
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        storageRef.child("usuario/" + scorreo).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String url = uri.toString();
                System.out.println("-------------------URL IMAGEN:" + url);
                Glide.with(getContext())
                        .load(url)
                        .fitCenter()
                        .centerCrop()
                        .into(imagen);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    public void seleccionaImagen(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/jpeg");
        startActivityForResult(intent.createChooser(intent,"Selecciona la aplicacion"),10);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            Uri path = data.getData();
            imagen.setImageURI(path);
            imagenagregada=true;
        }
    }

    public void subirFoto(){

        StorageReference storageRef = storage.getInstance().getReference();
        StorageReference usuarioRef = storageRef.child("usuario/" + scorreo);

        System.out.println("-----------------------USUARIO REF " + usuarioRef);

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
                Toast.makeText(getActivity(), "Error al subir imagen", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //Toast.makeText(CrearUActivity.this, "IMAGEN SUBIDA CON EXITO", Toast.LENGTH_SHORT).show();
                System.out.println("IMAGEN GUARDADA CON EXITO");
                System.out.println("--------------------LINK IMAGEN: " + usuarioRef);
            }
        });
    }

    /*ELIMINACION*/

    public void eliminaUsuario(String correo){

        if(encontrado == true){
            eliminaImagen(correo);
            firestore.collection("usuario").document(correo).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(getActivity(), "El usuario se elimino correctamente", Toast.LENGTH_SHORT).show();
                    //Intent intent = new Intent(getContext(), LoginActivity.class);
                    //startActivity(intent);
                    getActivity().finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), "Error al eliminar usuario", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void eliminaImagen(String correo){

        StorageReference storageRef = storage.getInstance().getReference();

        StorageReference desertRef = storageRef.child("usuario/"+correo);

        desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                System.out.println("Imagen eliminada correctamente");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                System.out.println("Imagen eliminada correctamente");
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
                    if(telefono.getText().toString().trim().isEmpty()) {
                        telefono.setHint("Numero de telefono obligatorio");
                        telefono.setHintTextColor(getResources().getColor(R.color.red));
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
                                if(contrasena.getText().toString().length()<6){
                                    Toast.makeText(getActivity(), "La contraseña debe ser mayor a 6 digitos", Toast.LENGTH_SHORT).show();
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