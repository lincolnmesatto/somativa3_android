package com.pucpr.realtimefirebase.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pucpr.realtimefirebase.R;
import com.pucpr.realtimefirebase.model.Colecao;
import com.pucpr.realtimefirebase.model.DataModel;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ColecaoAdapter adapter;

    FirebaseDatabase firebaseDatabase;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setTitle("Coleção");

        recyclerView = findViewById(R.id.recyclerView);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference ref = firebaseDatabase.getReference().child(firebaseUser.getUid());

        DataModel.getInstance().setContext(HomeActivity.this);
        adapter = new ColecaoAdapter();

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter.setClickListener((position, view) -> {
            Intent intent = new Intent(HomeActivity.this, FormActivity.class);
            intent.putExtra("position", position);
            startActivity(intent);
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP|ItemTouchHelper.DOWN,
                        ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                        int fromPos = viewHolder.getAdapterPosition();
                        int toPos = target.getAdapterPosition();
                        Colecao from = DataModel.getInstance().getColecoes().get(fromPos);
                        Colecao to = DataModel.getInstance().getColecoes().get(toPos);

                        DataModel.getInstance().getColecoes().set(fromPos, to);
                        DataModel.getInstance().getColecoes().set(toPos, from);
                        adapter.notifyItemMoved(fromPos,toPos);
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                        int position = viewHolder.getAdapterPosition();
                        Colecao c = DataModel.getInstance().getColecoes().get(position);

                        DataModel.getInstance().getColecoes().remove(c);

                        ref.setValue(DataModel.getInstance().getColecoes())
                                .addOnCompleteListener(task -> {
                                    if(task.isSuccessful()){
                                        Toast.makeText(getApplicationContext(), "Item removido com sucesso", Toast.LENGTH_LONG).show();
                                    }
                                });

                        adapter.notifyItemRemoved(position);
                    }
                }
        );
        itemTouchHelper.attachToRecyclerView(recyclerView);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataModel.getInstance().getColecoes().clear();

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Colecao colecao = dataSnapshot.getValue(Colecao.class);
                    DataModel.getInstance().getColecoes().add(colecao);
                    DataModel.getInstance().getColecoesOriginal().add(colecao);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void btnAddClicked(View view){
        Intent intent = new Intent(HomeActivity.this, FormActivity.class);
        startActivity(intent);
    }

    public void btnFilterClicked(View view){
        EditText editTextFiltro = findViewById(R.id.editTextFiltro);
        String filtro = editTextFiltro.getText().toString();

        if(!filtro.equals("") && filtro != null){
            ArrayList<Colecao> colecoes = new ArrayList<>();

            for(Colecao c : DataModel.getInstance().getColecoes()){
                if(!c.getTitulo().startsWith(filtro))
                    colecoes.add(c);
            }

            DataModel.getInstance().getColecoes().removeAll(colecoes);
        }else{
            ArrayList<Colecao> colecoes = DataModel.getInstance().getColecoesOriginal();
            ArrayList<Colecao> cs = DataModel.getInstance().getColecoes();

            DataModel.getInstance().getColecoes().removeAll(cs);
            DataModel.getInstance().getColecoes().addAll(colecoes);
        }

        adapter.notifyDataSetChanged();
    }

    public void btnInfoClicked(View view){
        Intent intent = new Intent(HomeActivity.this, ChartActivity.class);
        startActivity(intent);
    }

}