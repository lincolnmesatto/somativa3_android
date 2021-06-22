package com.pucpr.realtimefirebase.controller;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.pucpr.realtimefirebase.R;
import com.pucpr.realtimefirebase.model.DataModel;
import com.pucpr.realtimefirebase.model.Volume;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class VolumeAdapter extends RecyclerView.Adapter<VolumeAdapter.ViewHolder> {
    private static ClickListener clickListener;

    public void setClickListener(ClickListener clickListener) {
        VolumeAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int id, View view);
    }

    @NonNull
    @Override
    public VolumeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate( R.layout.volume_recyclerview,
                        parent, false
                );

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VolumeAdapter.ViewHolder holder, int position) {
        Volume volume = DataModel.getInstance().getColecoes().get(DataModel.getInstance().getPosicao()).getVolumes().get(position);

        if(volume.getStatus().equals("Lido")) {
            holder.textViewVolume.setBackgroundColor(ContextCompat.getColor(DataModel.getInstance().getContext(), R.color.verde));
        }else if(volume.getStatus().equals("Comprado")){
            holder.textViewVolume.setBackgroundColor(ContextCompat.getColor(DataModel.getInstance().getContext(), R.color.azul));
        }else{
            holder.textViewVolume.setBackgroundColor(ContextCompat.getColor(DataModel.getInstance().getContext(), R.color.vermelho));
        }

        holder.textViewVolume.setText(String.valueOf(volume.getVolume()));
    }

    @Override
    public int getItemCount() {
        return DataModel.getInstance().getColecoes().get(DataModel.getInstance().getPosicao()).getVolumes().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewVolume;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewVolume = itemView.findViewById(R.id.textViewVolume);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onItemClick(getAdapterPosition(), view);
                }
            });
        }
    }
}
