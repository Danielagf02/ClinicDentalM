package com.example.clinicadental.ui.eliminarplatillo;

import static com.example.clinicadental.CrearUActivity.firestore;
import static com.example.clinicadental.CrearUActivity.storage;

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
import com.example.clinicadental.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class EliminarPFragment extends Fragment implements View.OnClickListener{

    EditText etnombreb;

    String nombrebuscar;

    EditText nombre;
    EditText tipo;
    EditText precio;
    EditText descripcion;

    String snombre;
    String stipo;
    String sprecio;
    String sdescripcion;

    Button btnEliminaP;
    Button btnBuscarP;

    ImageView imagen;

    boolean encontrado=false;

    public static EliminarPFragment newInstance() {
        return new EliminarPFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_eliminap, container, false);
        Componentes(root);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        btnEliminaP.setEnabled(false);
        nombre.setEnabled(false);
        tipo.setEnabled(false);
        precio.setEnabled(false);
        descripcion.setEnabled(false);
    }

    private void Componentes(View root){
        ButtonComponentes(root);
        EditTextComponentes(root);
        ImageViewComponentes(root);
    }

    private void EditTextComponentes(View root){
        etnombreb = root.findViewById(R.id.etEPNombreBuscar);

        nombre = root.findViewById(R.id.etEPNombre);
        tipo = root.findViewById(R.id.etEPTipo);
        precio = root.findViewById(R.id.etEPPrecio);
        descripcion = root.findViewById(R.id.etEPDescripcion);
    }

    private void ButtonComponentes(View root){
        btnEliminaP = root.findViewById(R.id.btnEliminarPlatillo);
        btnEliminaP.setOnClickListener(this);

        btnBuscarP = root.findViewById(R.id.btnEPBuscaNombre);
        btnBuscarP.setOnClickListener(this);
    }

    private void ImageViewComponentes(View root){
        imagen = root.findViewById(R.id.ivEPImagen);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnEPBuscaNombre:
                if(etnombreb.getText().toString().trim().isEmpty()) {
                    etnombreb.setHint("Ingrese un nombre");
                    etnombreb.setHintTextColor(getResources().getColor(R.color.red));
                }else{
                    nombrebuscar = etnombreb.getText().toString().toUpperCase().charAt(0) + etnombreb.getText().toString().substring(1, etnombreb.length()).toLowerCase();
                    nombrebuscar = nombrebuscar.trim();
                    consultaNombre(nombrebuscar);
                }
                break;
            case R.id.btnEliminarPlatillo:
                eliminaPlatillo();
                break;
            default:
                break;
        }
    }

    /*Busca el usuario a eliminar*/
    public void consultaNombre(String nombreb){

        DocumentReference docref = firestore.collection("tratamiento").document(nombreb);
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

                        encontrado = true;
                        btnEliminaP.setEnabled(true);
                    }else{
                        Toast.makeText(getActivity(), "No se encontro el tratamiento", Toast.LENGTH_SHORT).show();
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

    public void eliminaPlatillo(){

        if(encontrado == true){
            eliminaImagen(snombre);
            firestore.collection("tratamiento").document(snombre).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(getActivity(), "El tratamiento se elimino correctamente", Toast.LENGTH_SHORT).show();
                    limpiaCampos();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), "Error al eliminar tratamiento", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void cargarImagen(String nombre){
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        storageRef.child("tratamiento/" + snombre).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
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

    public void eliminaImagen(String nombre){

        StorageReference storageRef = storage.getInstance().getReference();

        StorageReference desertRef = storageRef.child("tratamiento/"+nombre);

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
        etnombreb.setText("");
        nombre.setText("");
        tipo.setText("");
        precio.setText("");
        descripcion.setText("");
        imagen.setImageResource(R.drawable.icono_platillo);
    }

}