package com.example.clinicadental;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class UsuarioAdapter extends FirestoreRecyclerAdapter<Usuario,UsuarioAdapter.ViewHolder> {

    public UsuarioAdapter(@NonNull FirestoreRecyclerOptions<Usuario> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Usuario usuario) {
        holder.tvLNombre.setText(usuario.getNombre());
        holder.tvLApellidoP.setText(usuario.getApellido_paterno());
        holder.tvLCorreo.setText(usuario.getCorreo());

        Glide.with(holder.ivLImagen)
                .load(usuario.getLink_imagen())
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(holder.ivLImagen);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_usuario,viewGroup,false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvLNombre;
        TextView tvLApellidoP;
        TextView tvLCorreo;
        ImageView ivLImagen;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvLNombre = itemView.findViewById(R.id.tvLNombre);
            tvLApellidoP = itemView.findViewById(R.id.tvLApellidoP);
            tvLCorreo = itemView.findViewById(R.id.tvLCorreo);
            ivLImagen = itemView.findViewById(R.id.ivLImagen);
        }
    }
}
