package com.example.clinicadental;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class ReservacionAdapter extends FirestoreRecyclerAdapter<Reservacion,ReservacionAdapter.ViewHolder> {

    public ReservacionAdapter(@NonNull FirestoreRecyclerOptions<Reservacion> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Reservacion reservacion) {
        holder.tvLNombreR.setText(reservacion.getNombre());
        holder.tvLApellidoPR.setText(reservacion.getApellido_paterno());
        holder.tvLApellidoMR.setText(reservacion.getApellido_materno());
      //  holder.tvLNumPersonasR.setText("Numero de personas: " + reservacion.getNumero_personas());
       // holder.tvLNumMesasR.setText("Numero de mesas: " + reservacion.getNumero_mesas());
        holder.tvLFechaR.setText("Fecha: "+reservacion.getFecha_reservacion());
        holder.tvLHoraR.setText("Hora: "+reservacion.getHora_reservacion());

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_reservacion,viewGroup,false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvLNombreR;
        TextView tvLApellidoPR;
        TextView tvLApellidoMR;

        TextView tvLFechaR;
        TextView tvLHoraR;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvLNombreR = itemView.findViewById(R.id.tvLNombreR);
            tvLApellidoPR = itemView.findViewById(R.id.tvLApellidoPR);
            tvLApellidoMR = itemView.findViewById(R.id.tvLApellidoMR);
            tvLFechaR = itemView.findViewById(R.id.tvLFechaR);
            tvLHoraR = itemView.findViewById(R.id.tvLHoraR);
        }
    }

}
