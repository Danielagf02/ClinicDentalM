package com.example.clinicadental.ui.eliminarusuarios;

import static com.example.restauranteeltapanco.CrearUActivity.firestore;
import static com.example.restauranteeltapanco.CrearUActivity.storage;

import android.net.Uri;
import android.os.Bundle;
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

public class EliminarUFragment extends Fragment implements View.OnClickListener{

    EditText etcorreob;

    String correobuscar;

    String snombre;
    String sapellidop;
    String sapellidom;
    String stelefono;
    String scorreo;
    String sdireccion;

    EditText nombre;
    EditText apellidop;
    EditText apellidom;
    EditText telefono;
    EditText direccion;

    Button btnEliminarU;
    Button btnBuscarU;

    ImageView imagen;

    boolean encontrado=false;

    public static EliminarUFragment newInstance() {
        return new EliminarUFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_eliminau, container, false);
        Componentes(root);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        btnEliminarU.setEnabled(false);
        nombre.setEnabled(false);
        apellidop.setEnabled(false);
        apellidom.setEnabled(false);
        telefono.setEnabled(false);
        direccion.setEnabled(false);
    }

    private void Componentes(View root){
        ButtonComponentes(root);
        EditTextComponentes(root);
        ImageViewComponentes(root);
    }

    private void EditTextComponentes(View root){
        etcorreob = root.findViewById(R.id.etECorreo);

        nombre = root.findViewById(R.id.etENombre);
        apellidop = root.findViewById(R.id.etEApellidoP);
        apellidom = root.findViewById(R.id.etEApellidoM);
        telefono = root.findViewById(R.id.eteTelefono);
        direccion = root.findViewById(R.id.etEDireccion);
    }

    private void ButtonComponentes(View root){
        btnEliminarU = root.findViewById(R.id.btnEEliminarU);
        btnBuscarU = root.findViewById(R.id.btnEBuscarCorreo);

        btnEliminarU.setOnClickListener(this);
        btnBuscarU.setOnClickListener(this);
    }

    private void ImageViewComponentes(View root){
        imagen = root.findViewById(R.id.ivEImagen);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnEBuscarCorreo:

                if(etcorreob.getText().toString().trim().isEmpty()) {
                    etcorreob.setHint("Ingrese una correo");
                    etcorreob.setHintTextColor(getResources().getColor(R.color.red));
                }else{
                    correobuscar = etcorreob.getText().toString().trim();
                    consultaCorreo(correobuscar);
                }
                break;
            case R.id.btnEEliminarU:
                eliminaUsuario();
                break;
            default:
                break;
        }
    }

    /*Busca el usuario a eliminar*/
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
                        scorreo = (String) document.getData().get("correo");
                        sdireccion = (String) document.getData().get("direccion");

                        nombre.setText(snombre);
                        apellidop.setText(sapellidop);
                        apellidom.setText(sapellidom);
                        telefono.setText(stelefono);
                        direccion.setText(sdireccion);

                        cargarImagen(scorreo);

                        encontrado = true;
                        btnEliminarU.setEnabled(true);
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

    public void eliminaUsuario(){

        if(encontrado == true){
            eliminaImagen(scorreo);
            firestore.collection("usuario").document(scorreo).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(getActivity(), "El usuario se elimino correctamente", Toast.LENGTH_SHORT).show();
                    limpiaCampos();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), "Error al eliminar usuario", Toast.LENGTH_SHORT).show();
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

    public void limpiaCampos(){
        etcorreob.setText("");
        nombre.setText("");
        apellidop.setText("");
        apellidom.setText("");
        telefono.setText("");
        direccion.setText("");
        imagen.setImageResource(R.drawable.icono_usuario);
    }

}