package com.example.clinicadental.ui.modificarplatillo;

import static android.app.Activity.RESULT_OK;
import static com.example.restauranteeltapanco.CrearUActivity.firestore;
import static com.example.restauranteeltapanco.CrearUActivity.storage;

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
import com.example.restauranteeltapanco.R;
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

public class ModificarPFragment extends Fragment implements View.OnClickListener{

    EditText etnombrebuscar;
    String nombrebuscar;

    EditText nombre;
    EditText tipo;
    EditText precio;
    EditText descripcion;

    String snombre;
    String stipo;
    String sprecio;
    String sdescripcion;

    Button btnModificarP;
    Button btnBuscarP;
    Button btnAgregarImagen;

    ImageView imagen;

    boolean encontrado=false;

    boolean imagenagregada=false;

    public static ModificarPFragment newInstance() {
        return new ModificarPFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.fragment_modificaru, container, false);
        View root = inflater.inflate(R.layout.fragment_modificarp, container, false);
        Componentes(root);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //mViewModel = new ViewModelProvider(this).get(MenuViewModel.class);
        // TODO: Use the ViewModel
        btnModificarP.setEnabled(false);
        btnAgregarImagen.setEnabled(false);
    }

    private void Componentes(View root){
        ButtonComponentes(root);
        EditTextComponentes(root);
        ImageViewComponentes(root);
    }

    private void ButtonComponentes(View root){
        btnModificarP = root.findViewById(R.id.btnModifPlatillo);
        btnBuscarP = root.findViewById(R.id.btnMBuscarNombrePlatillo);
        btnAgregarImagen = root.findViewById(R.id.btnMPSubirFoto);

        btnModificarP.setOnClickListener(this);
        btnBuscarP.setOnClickListener(this);
        btnAgregarImagen.setOnClickListener(this);
    }

    private void EditTextComponentes(View root){
        etnombrebuscar = root.findViewById(R.id.etMNombreBuscar);
        nombre = root.findViewById(R.id.etMPNombre);
        tipo = root.findViewById(R.id.etMPTipo);
        precio = root.findViewById(R.id.etMPPrecio);
        descripcion = root.findViewById(R.id.etMPDescripcion);
    }

    private void ImageViewComponentes(View root){
        imagen = root.findViewById(R.id.ivMImagen);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnMBuscarNombrePlatillo:
                if(etnombrebuscar.getText().toString().trim().isEmpty()) {
                    etnombrebuscar.setHint("Ingrese un nombre");
                    etnombrebuscar.setHintTextColor(getResources().getColor(R.color.red));
                }else{
                    nombrebuscar = etnombrebuscar.getText().toString().toUpperCase().charAt(0) + etnombrebuscar.getText().toString().substring(1, etnombrebuscar.length()).toLowerCase();
                    consultaPlatillo(nombrebuscar);
                }
                break;
            case R.id.btnModifPlatillo:
                if(imagenagregada==true){
                    actualizaPlatillo();
                    subirFoto();
                }else{
                    actualizaPlatillo();
                }
                break;
            case R.id.btnMPSubirFoto:
                seleccionaImagen();
                break;
            default:
                break;
        }
    }

    /*Busca el usuario y pone sus datos en los campos*/
    public void consultaPlatillo(String nombrep){

        DocumentReference docref = firestore.collection("platillo").document(nombrep);
        docref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        snombre = (String) document.getData().get("nombre");
                        stipo = (String) document.getData().get("tipo");
                        sprecio = (String) document.getData().get("precio");
                        sdescripcion = (String) document.getData().get("descripcion");

                        nombre.setText(snombre);
                        tipo.setText(stipo);
                        precio.setText(sprecio);
                        descripcion.setText(sdescripcion);

                        cargarImagen(snombre);

                        encontrado=true;
                        btnModificarP.setEnabled(true);
                        btnAgregarImagen.setEnabled(true);
                    }else{
                        Toast.makeText(getActivity(), "No se encontro el platillo", Toast.LENGTH_SHORT).show();
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

    public void actualizaPlatillo(){

        if(encontrado ==true){
            DocumentReference docref = firestore.collection("platillo").document(snombre);

            docref.update("nombre",nombre.getText().toString().trim(),
                            "tipo",tipo.getText().toString().trim(),
                            "precio",precio.getText().toString().trim(),
                            "descripcion",descripcion.getText().toString().trim())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(getActivity(), "Se actualizaron los datos correctamente", Toast.LENGTH_SHORT).show();
                    limpiaCampos();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), "Error al actualizar el platillo", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void cargarImagen(String nombre){
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        storageRef.child("platillo/" + snombre).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
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
        StorageReference usuarioRef = storageRef.child("platillo/" + snombre);

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

    public void limpiaCampos(){
        etnombrebuscar.setText("");
        nombre.setText("");
        tipo.setText("");
        precio.setText("");
        descripcion.setText("");
        imagen.setImageResource(R.drawable.icono_platillo);
    }

}