package com.example.clinicadental;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clinicadental.Tratamiento;
import com.example.clinicadental.TratamientoAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MenuFragment extends Fragment {

    RecyclerView recyclerListaP;
    TratamientoAdapter mAdapter;
    FirebaseFirestore mFirestore;

    public static MenuFragment newInstance() {
        return new MenuFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View vista = inflater.inflate(R.layout.fragment_menu,container,false);
        recyclerListaP = vista.findViewById(R.id.recyclerListaP);
        recyclerListaP.setLayoutManager(new LinearLayoutManager(getContext()));
        mFirestore = FirebaseFirestore.getInstance();

        Query query = mFirestore.collection("platillo");

        FirestoreRecyclerOptions<Tratamiento> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Platillo>()
                .setQuery(query,Tratamiento.class).build();

        mAdapter = new TratamientoAdapter()(firestoreRecyclerOptions);
        mAdapter.notifyDataSetChanged();
        recyclerListaP.setAdapter(mAdapter);

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