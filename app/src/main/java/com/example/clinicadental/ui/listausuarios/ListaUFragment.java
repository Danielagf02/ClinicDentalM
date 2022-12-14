package com.example.clinicadental.ui.listausuarios;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clinicadental.R;
import com.example.clinicadental.Usuario;
import com.example.clinicadental.UsuarioAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ListaUFragment extends Fragment {

    RecyclerView recyclerListaU;
    UsuarioAdapter mAdapter;
    FirebaseFirestore mFirestore;

    public static ListaUFragment newInstance() {
        return new ListaUFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View vista = inflater.inflate(R.layout.fragment_listaru,container,false);
        recyclerListaU = vista.findViewById(R.id.recyclerListaU);
        recyclerListaU.setLayoutManager(new LinearLayoutManager(getContext()));
        mFirestore = FirebaseFirestore.getInstance();

        Query query = mFirestore.collection("usuario");

        FirestoreRecyclerOptions<Usuario> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Usuario>()
                .setQuery(query,Usuario.class).build();

        mAdapter = new UsuarioAdapter(firestoreRecyclerOptions);
        mAdapter.notifyDataSetChanged();
        recyclerListaU.setAdapter(mAdapter);

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