package com.example.clinicadental.ui.eliminarreservaciones;

import static com.example.clinicadental.CrearUActivity.firestore;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
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

import java.util.Calendar;

public class EliminarRFragment extends Fragment implements View.OnClickListener{

    EditText etfechab;
    EditText ethorab;

    String fechabuscar;
    String horabuscar;
    String fechahora;

    EditText nombre;
    EditText apellidop;
    EditText apellidom;
    EditText fecha;
    EditText hora;
   // EditText nopersonas;
   // EditText nomesas;

    String snombre;
    String sapellidop;
    String sapellidom;
    String sfecha;
    String shora;
   // String snopersonas;
    //String snomesas;

    Button btnEliminarR;
    Button btnBuscarR;
    Button btnSeleccionarFB;
    Button btnSeleccionarHB;

    boolean encontrado=false;

    public static EliminarRFragment newInstance() {
        return new EliminarRFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_eliminarr, container, false);
        Componentes(root);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        btnEliminarR.setEnabled(false);
        nombre.setEnabled(false);
        apellidop.setEnabled(false);
        apellidom.setEnabled(false);
        fecha.setEnabled(false);
        hora.setEnabled(false);
      //  nopersonas.setEnabled(false);
       // nomesas.setEnabled(false);
    }

    private void Componentes(View root){
        ButtonComponentes(root);
        EditTextComponentes(root);
    }

    private void EditTextComponentes(View root){
        etfechab = root.findViewById(R.id.etBFechaR);
        ethorab = root.findViewById(R.id.etBHoraR);

        nombre = root.findViewById(R.id.etENombreR);
        apellidop = root.findViewById(R.id.etEApellidoPR);
        apellidom = root.findViewById(R.id.etEApellidoMR);
        fecha = root.findViewById(R.id.etEFechaR);
        hora = root.findViewById(R.id.etEHoraR);
        //nopersonas = root.findViewById(R.id.etENoPersonasR);
        //nomesas = root.findViewById(R.id.etNoMesasR);
    }

    private void ButtonComponentes(View root){
        btnEliminarR = root.findViewById(R.id.btnEEliminarR);
        btnEliminarR.setOnClickListener(this);

        btnBuscarR = root.findViewById(R.id.btnEBuscarR);
        btnBuscarR.setOnClickListener(this);

        btnSeleccionarFB = root.findViewById(R.id.btnSeleccionarFB);
        btnSeleccionarFB.setOnClickListener(this);

        btnSeleccionarHB = root.findViewById(R.id.btnSeleccionarHB);
        btnSeleccionarHB.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnEBuscarR:

                if(etfechab.getText().toString().trim().isEmpty()) {
                    etfechab.setHint("Ingrese una fecha");
                    etfechab.setHintTextColor(getResources().getColor(R.color.red));
                }else{
                    if(ethorab.getText().toString().trim().isEmpty()) {
                        ethorab.setHint("Ingrese una hora");
                        ethorab.setHintTextColor(getResources().getColor(R.color.red));
                    }else{
                        fechabuscar = etfechab.getText().toString().trim();
                        horabuscar = ethorab.getText().toString().trim();

                        String diagonal = "/";
                        String puntos = ":";

                        fechahora = fechabuscar + horabuscar;
                        fechahora = fechahora.replace(String.valueOf(diagonal),"");
                        fechahora = fechahora.replace(String.valueOf(puntos),"");

                        consultaReservacion(fechahora);
                    }
                }
                break;
            case R.id.btnEEliminarR:
                eliminaUsuario();
                break;

            case R.id.btnSeleccionarFB:
                final Calendar c = Calendar.getInstance();
                int anio = c.get(Calendar.YEAR);
                int mes = c.get(Calendar.MONTH);
                int dia = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dpd = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int mYear, int mMonth, int mDay) {
                        etfechab.setText(mDay + "/" + (mMonth+1) + "/" + mYear);
                    }
                },anio,mes,dia);
                dpd.show();
                break;
            case R.id.btnSeleccionarHB:
                Calendar ch = Calendar.getInstance();
                int mHour = ch.get(Calendar.HOUR_OF_DAY);
                int mMinute = ch.get(Calendar.MINUTE);

                TimePickerDialog tpd = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                        ethorab.setText(hourOfDay + ":" + minute);
                    }
                },mHour,mMinute,false);
                tpd.show();
                break;
            default:
                break;
        }
    }

    public void consultaReservacion(String fechahora){

        DocumentReference docref = firestore.collection("cita").document(fechahora);
        docref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        snombre = (String) document.getData().get("nombre");
                        sapellidop = (String) document.getData().get("apellido_paterno");
                        sapellidom = (String) document.getData().get("apellido_materno");
                        sfecha = (String) document.getData().get("fecha_reservacion");
                        shora = (String) document.getData().get("hora_reservacion");
                      //  snopersonas = (String) document.getData().get("numero_personas");
                       // snomesas = (String) document.getData().get("numero_mesas");

                        nombre.setText(snombre);
                        apellidop.setText(sapellidop);
                        apellidom.setText(sapellidom);
                        fecha.setText(sfecha);
                        hora.setText(shora);
                      //  nopersonas.setText(snopersonas);
                       // nomesas.setText(snomesas);

                        encontrado = true;
                        btnEliminarR.setEnabled(true);
                    }else{
                        Toast.makeText(getActivity(), "No se encontro la cita", Toast.LENGTH_SHORT).show();
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
            firestore.collection("cita").document(fechahora).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(getActivity(), "La cita se elimino correctamente", Toast.LENGTH_SHORT).show();
                    limpiaCampos();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), "Error al eliminar cita", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void limpiaCampos(){
        etfechab.setText("");
        ethorab.setText("");
        nombre.setText("");
        apellidop.setText("");
        apellidom.setText("");
        fecha.setText("");
        hora.setText("");
     //   nopersonas.setText("");
     //   nomesas.setText("");
    }

}