package com.example.clinicadental.ui.agregarreservacion;

import static com.example.clinicadental.CrearUActivity.firestore;
import static com.example.clinicadental.CrearUActivity.storage;
import static com.example.clinicadental.LoginActivity.correorecuperado;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AgregarRFragment extends Fragment implements View.OnClickListener{

    TextView nombre;
    TextView apellidop;
    TextView apellidom;
    TextView telefono;
    String snombre;
    String sapellidop;
    String sapellidom;
    String stelefono;

    EditText fecha;
    EditText hora;
  //  EditText nopersonas;
   // EditText nomesas;
    String sfecha;
    String shora;
  //  String snopersonas;
  //  String snomesas;

    String scorreo;

    Button btnRealizarRes;
    Button btnSeleccionarF;
    Button btnSeleccionarH;

    public static AgregarRFragment newInstance() {
        return new AgregarRFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_realizarreservacion, container, false);
        Componentes(root);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        System.out.println("------------correo recuperado:" + correorecuperado);
        consultaCorreo(correorecuperado);
    }

    private void Componentes(View root){
        ButtonComponentes(root);
        EditTextComponentes(root);
        TextViewComponentes(root);
    }

    private void ButtonComponentes(View root){
        btnRealizarRes = root.findViewById(R.id.btnRealizarReservacion);
        btnRealizarRes.setOnClickListener(this);

        btnSeleccionarF = root.findViewById(R.id.btnSeleccionarF);
        btnSeleccionarF.setOnClickListener(this);

        btnSeleccionarH = root.findViewById(R.id.btnSeleccionarH);
        btnSeleccionarH.setOnClickListener(this);
    }

    private void EditTextComponentes(View root){
        fecha = root.findViewById(R.id.etCFechaR);
        hora = root.findViewById(R.id.etCHoraR);
    //    nopersonas = root.findViewById(R.id.etCNumeroPR);
    //    nomesas = root.findViewById(R.id.etCNumeroMR);
    }

    private void TextViewComponentes(View root){
        nombre = root.findViewById(R.id.tvCNombreR);
        apellidop = root.findViewById(R.id.tvCApellidoPR);
        apellidom = root.findViewById(R.id.tvCApellidoMR);
        telefono = root.findViewById(R.id.tvCTelefonoR);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnRealizarReservacion:
                snombre = nombre.getText().toString().trim();
                sapellidop = apellidop.getText().toString().trim();
                sapellidom = apellidom.getText().toString().trim();
                stelefono = telefono.getText().toString().trim();

                sfecha = fecha.getText().toString().trim();
                shora = hora.getText().toString().trim();
      //          snopersonas = nopersonas.getText().toString().trim();
        //        snomesas = nomesas.getText().toString().trim();

                if(validaCampos()==true){ //VALIDA QUE LOS CAMPOS NO ESTEN VACIOS

                    //VALIDA QUE LA FECHA NO SEA ANTERIOR AL DIA DE HOY
                    Date fechaHoy = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    String hoyFormateado = sdf.format(fechaHoy.getDate());

                    System.out.println("--------------HOYFORMATEADO" + hoyFormateado);

                    Date fechaReserva = null;
                    try {
                        fechaReserva = sdf.parse(sfecha);

                        if(fechaReserva.before(fechaHoy)){
                            Toast.makeText(getContext(), "La fecha de reservacion no puede ser anterior a la fecha actual", Toast.LENGTH_SHORT).show();
                        }else{
                            //AQUI DEBE IR LA VALIDACION DE QUE LA HORA NO SEA ANTERIOR A LA ACTUAL

                            //ELIMINA LOS / Y : PARA DAR NOMBRE A LA RESERVACION
                            String diagonal = "/";
                            String puntos = ":";

                            String fechahora = sfecha + shora;
                            fechahora = fechahora.replace(String.valueOf(diagonal),"");
                            fechahora = fechahora.replace(String.valueOf(puntos),"");

                            consultaFecha(fechahora);
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                break;

            case R.id.btnSeleccionarF:
                final Calendar c = Calendar.getInstance();
                int anio = c.get(Calendar.YEAR);
                int mes = c.get(Calendar.MONTH);
                int dia = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dpd = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int mYear, int mMonth, int mDay) {
                        fecha.setText(mDay + "/" + (mMonth+1) + "/" + mYear);
                    }
                },anio,mes,dia);
                dpd.show();
                break;
            case R.id.btnSeleccionarH:

                Calendar ch = Calendar.getInstance();
                int mHour = ch.get(Calendar.HOUR_OF_DAY);
                int mMinute = ch.get(Calendar.MINUTE);

                TimePickerDialog tpd = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                        hora.setText(hourOfDay + ":" + minute);
                    }
                },mHour,mMinute,false);
                tpd.show();
                break;
            default:
                break;
        }
    }


    public void consultaFecha(String fecha){

        DocumentReference docref = firestore.collection("cita").document(fecha);
        docref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        Toast.makeText(getContext(), "La fecha y hora se encuentran ocupadas", Toast.LENGTH_SHORT).show();
                        //limpiaCampos();
                    }else{
                        agregarReservacion(snombre,sapellidop,sapellidom,stelefono,sfecha,shora);
                    }
                }else{
                    System.out.println("Error al consultar: " + task.getException());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });
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

                        scorreo = (String) document.getData().get("correo");

                        nombre.setText(snombre);
                        apellidop.setText(sapellidop);
                        apellidom.setText(sapellidom);
                        telefono.setText(stelefono);
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

    public void agregarReservacion(String nombre,String apellidop, String apellidom, String numero,
                                   String fecha,String hora){

        Map<String,Object> reservacion = new HashMap<>();

        reservacion.put("nombre",nombre);
        reservacion.put("apellido_paterno",apellidop);
        reservacion.put("apellido_materno",apellidom);
        reservacion.put("numero_telefono",numero);

        reservacion.put("fecha_reservacion",fecha);
        reservacion.put("hora_reservacion",hora);
     //   reservacion.put("numero_personas",nopersonas);
     //   reservacion.put("numero_mesas",nomesas);

        String diagonal = "/";
        String puntos = ":";

        String fechahora = fecha + hora;
        fechahora = fechahora.replace(String.valueOf(diagonal),"");
        fechahora = fechahora.replace(String.valueOf(puntos),"");

        System.out.println("-----------------NOMBRE DE LA RESERVACION " + fechahora);

        firestore.collection("cita").document(fechahora).set(reservacion).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getActivity(), "Cita creada correctamente", Toast.LENGTH_SHORT).show();
                limpiaCampos();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Error al crear cita", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean validaCampos(){
        if(fecha.getText().toString().trim().isEmpty()) {
            fecha.setHint("Fecha obligatoria");
            fecha.setHintTextColor(getResources().getColor(R.color.red));
            return false;
        }else{
            if(hora.getText().toString().trim().isEmpty()) {
                hora.setHint("Hora obligatoria");
                hora.setHintTextColor(getResources().getColor(R.color.red));
                return false;

            }
        }
        return true;
        //SE HICIERON CAMBIOS AWUI, NO SE SI ESTEN BIEN ************
    }

    public void limpiaCampos(){
        fecha.setText("");
        hora.setText("");
      //  nopersonas.setText("");
       // nomesas.setText("");
    }

}