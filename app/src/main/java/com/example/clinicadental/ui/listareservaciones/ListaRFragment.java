package com.example.clinicadental.ui.listareservaciones;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restauranteeltapanco.R;
import com.example.restauranteeltapanco.Reservacion;
import com.example.restauranteeltapanco.ReservacionAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ListaRFragment extends Fragment{

    RecyclerView recyclerListaR;
    ReservacionAdapter mAdapter;
    FirebaseFirestore mFirestore;

    public static ListaRFragment newInstance() {
        return new ListaRFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View vista = inflater.inflate(R.layout.fragment_listarr,container,false);
        recyclerListaR = vista.findViewById(R.id.recyclerListaR);
        recyclerListaR.setLayoutManager(new LinearLayoutManager(getContext()));
        mFirestore = FirebaseFirestore.getInstance();

        Query query = mFirestore.collection("reservacion");

        FirestoreRecyclerOptions<Reservacion> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Reservacion>()
                .setQuery(query,Reservacion.class).build();

        mAdapter = new ReservacionAdapter(firestoreRecyclerOptions);
        mAdapter.notifyDataSetChanged();
        recyclerListaR.setAdapter(mAdapter);

        return vista;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }
}