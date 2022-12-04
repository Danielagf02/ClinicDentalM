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

public class PlatilloAdapter extends FirestoreRecyclerAdapter<Platillo, PlatilloAdapter.ViewHolder> {

    public PlatilloAdapter(@NonNull FirestoreRecyclerOptions<Platillo> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Platillo platillo) {
        holder.tvLNombreP.setText(platillo.getNombre());
        holder.tvLDescPlatillo.setText(platillo.getDescripcion());
        holder.tvLPrecio.setText("$"+platillo.getPrecio());

        Glide.with(holder.ivLImagenP)
                .load(platillo.getLink_imagen())
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(holder.ivLImagenP);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_platillo,viewGroup,false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvLNombreP;
        TextView tvLDescPlatillo;
        TextView tvLPrecio;
        ImageView ivLImagenP;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLNombreP = itemView.findViewById(R.id.tvLNombreP);
            tvLDescPlatillo = itemView.findViewById(R.id.tvLDescPlatillo);
            tvLPrecio = itemView.findViewById(R.id.tvLPrecio);
            ivLImagenP = itemView.findViewById(R.id.ivLImagenP);
        }
    }
}
