package com.example.clinicadental.ui.agregarplatillo;

import static android.app.Activity.RESULT_OK;
import static com.example.clinicadental.CrearUActivity.firestore;
import static com.example.clinicadental.CrearUActivity.storage;

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

import com.example.clinicadental.R;
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

public class AgregarPlatilloFragment extends Fragment implements View.OnClickListener{

    Button btnAgregarPlatillo;
    Button btnSubirFoto;

    EditText nombre;
    EditText tipo;
    EditText precio;
    EditText descripcion;

    String snombre;
    String stipo;
    String sprecio;
    String sdescripcion;

    String slink;
    String urluri;

    ImageView imagen;
    boolean imagenagregada=false;

    public static AgregarPlatilloFragment newInstance() {
        return new AgregarPlatilloFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_agregarp, container, false);
        Componentes(root);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
    }

    private void Componentes(View root){
        ButtonComponentes(root);
        EditTextComponentes(root);
        ImageViewComponentes(root);
    }

    private void ButtonComponentes(View root){
        btnAgregarPlatillo = root.findViewById(R.id.btnAPlatillo);
        btnAgregarPlatillo.setOnClickListener(this);

        btnSubirFoto = root.findViewById(R.id.btnAPSubirFoto);
        btnSubirFoto.setOnClickListener(this);
    }

    private void EditTextComponentes(View root){
        nombre = root.findViewById(R.id.etAPNombre);
        tipo = root.findViewById(R.id.etAPTipo);
        precio = root.findViewById(R.id.etAPPrecio);
        descripcion = root.findViewById(R.id.etAPDescripcion);
    }

    private void ImageViewComponentes(View root){
        imagen = root.findViewById(R.id.ivAPImagen);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAPlatillo:
                snombre = nombre.getText().toString().toUpperCase().charAt(0) + nombre.getText().toString().substring(1, nombre.length()).toLowerCase();
                snombre = snombre.trim();
                stipo = tipo.getText().toString().trim();
                sprecio = precio.getText().toString().trim();
                sdescripcion = descripcion.getText().toString().trim();

                if(validaCampos()==true){
                    consultaNombreP(snombre);
                }
                break;
            case R.id.btnAPSubirFoto:
                cargarImagen();
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK){
            Uri path = data.getData();
            imagen.setImageURI(path);
            imagenagregada=true;
        }

    }

    public void cargarImagen(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/jpeg");
        startActivityForResult(intent.createChooser(intent,"Selecciona la aplicacion"),10);
    }

    public void subirFoto(){

        StorageReference storageRef = storage.getReference();
        StorageReference usuarioRef = storageRef.child("Tratamiento/" + snombre);

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
                Toast.makeText(getContext(), "Error al subir imagen", Toast.LENGTH_SHORT).show();
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
                        agregarUsuario(snombre,stipo,sprecio,sdescripcion,slink);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }
        });
    }

    public void consultaNombreP(String nombre){

        DocumentReference docref = firestore.collection("platillo").document(nombre);
        docref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        Toast.makeText(getActivity(), "Este platillo ya ha sido registrado previamente", Toast.LENGTH_SHORT).show();
                    }else{
                        subirFoto();
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

    public void agregarUsuario(String nombre,
                               String tipo,
                               String precio,
                               String descripcion,
                               String linkimagen){

        Map<String,Object> usuario = new HashMap<>();

        usuario.put("nombre",nombre);
        usuario.put("tipo",tipo);
        usuario.put("precio",precio);
        usuario.put("descripcion",descripcion);
        usuario.put("link_imagen",linkimagen);

        firestore.collection("tratamiento").document(nombre).set(usuario).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getActivity(), "Tratamiento añadido al menú", Toast.LENGTH_SHORT).show();
                limpiaCampos();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Error al crear tratamiento", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean validaCampos(){

        if(nombre.getText().toString().trim().isEmpty()) {
            nombre.setHint("Nombre obligatorio");
            nombre.setHintTextColor(getResources().getColor(R.color.red));
            return false;
        }else{
            if(tipo.getText().toString().trim().isEmpty()) {
                tipo.setHint("Tipo de tratamiento obligatorio");
                tipo.setHintTextColor(getResources().getColor(R.color.red));
                return false;
            }else{
                if(precio.getText().toString().trim().isEmpty()) {
                    precio.setHint("Precio obligatorio");
                    precio.setHintTextColor(getResources().getColor(R.color.red));
                    return false;
                }else{
                    if(descripcion.getText().toString().trim().isEmpty()) {
                        descripcion.setHint("Descripcion obligatoria");
                        descripcion.setHintTextColor(getResources().getColor(R.color.red));
                        return false;
                    }else{
                        if(imagenagregada == false){
                            Toast.makeText(getActivity(), "Debe agregar una imagen", Toast.LENGTH_SHORT).show();
                            return false;
                        }else {
                            return true;
                        }
                    }
                }
            }
        }
    }

    public void limpiaCampos(){
        nombre.setText("");
        tipo.setText("");
        descripcion.setText("");
        precio.setText("");
        imagen.setImageResource(R.drawable.icono_platillo);
    }

}