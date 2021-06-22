package com.pucpr.realtimefirebase.controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pucpr.realtimefirebase.R;
import com.pucpr.realtimefirebase.model.Colecao;
import com.pucpr.realtimefirebase.model.DataModel;
import com.pucpr.realtimefirebase.model.Volume;

import java.util.ArrayList;

public class VolumeActivity extends AppCompatActivity {

    RecyclerView recyclerViewVolume;
    VolumeAdapter adapter;
    TextView textViewVolume;

    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volume);

        setTitle(DataModel.getInstance().getColecoes().get(DataModel.getInstance().getPosicao()).getTitulo()+" - Volumes");

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        recyclerViewVolume = findViewById(R.id.recyclerViewVolume);

        final LayoutInflater factory = getLayoutInflater();
        final View volumeRecyclerView = factory.inflate(R.layout.volume_recyclerview, null);

        textViewVolume = volumeRecyclerView.findViewById(R.id.textViewVolume);

        DataModel.getInstance().setContext(VolumeActivity.this);
        adapter = new VolumeAdapter();

        recyclerViewVolume.setAdapter(adapter);
        recyclerViewVolume.setLayoutManager(new GridLayoutManager(VolumeActivity.this, 4));

        adapter.setClickListener(new VolumeAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                Volume v = DataModel.getInstance().getColecoes().get(DataModel.getInstance().getPosicao()).getVolumes().get(position);
                switch (v.getStatus()){
                    case "Lido":
                        v.setStatus("Não possui");
                        DataModel.getInstance().getColecoes().get(DataModel.getInstance().getPosicao()).getVolumes().set(position, v);
                        break;
                    case "Não possui":
                        v.setStatus("Comprado");
                        DataModel.getInstance().getColecoes().get(DataModel.getInstance().getPosicao()).getVolumes().set(position, v);
                        break;
                    case "Comprado":
                        v.setStatus("Lido");
                        DataModel.getInstance().getColecoes().get(DataModel.getInstance().getPosicao()).getVolumes().set(position, v);
                        break;
                    default:
                        break;
                }
                adapter.notifyItemChanged(position);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Colecao c = DataModel.getInstance().getColecoes().get(DataModel.getInstance().getPosicao());
        ArrayList<Volume> volumes = c.getVolumes();

        for(Volume v : volumes){
            if(v.getStatus().equals("Lido"))
                c.setUltimoLido(v.getVolume());

            if(v.getStatus().equals("Comprado"))
                c.setUltimoComprado(v.getVolume());
        }

        if(c.getUltimoLido() == (c.getVolumes().size()))
            c.setUltimoComprado(c.getUltimoLido());

        DataModel.getInstance().getColecoes().set(DataModel.getInstance().getPosicao(), c);

        DatabaseReference rootReference = firebaseDatabase.getReference();

        rootReference.child(firebaseUser.getUid()).setValue(DataModel.getInstance().getColecoes())
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Toast.makeText(getApplicationContext(), "Item adicionado com sucesso", Toast.LENGTH_LONG).show();
                    }
                });

        Intent intent = new Intent(VolumeActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void btnAddClicked(View view){
        ArrayList<Volume> volumes = DataModel.getInstance().getColecoes().get(DataModel.getInstance().getPosicao()).getVolumes();
        DataModel.getInstance().getColecoes().get(DataModel.getInstance().getPosicao()).getVolumes().add(new Volume(volumes.size()+1, "Comprado"));
        adapter.notifyItemInserted(volumes.size());
    }

    public void btnARemoveClicked(View view){
        ArrayList<Volume> volumes = DataModel.getInstance().getColecoes().get(DataModel.getInstance().getPosicao()).getVolumes();
        int p = volumes.size() - 1;
        DataModel.getInstance().getColecoes().get(DataModel.getInstance().getPosicao()).getVolumes().remove(p);
        adapter.notifyItemRemoved(p);
    }
}