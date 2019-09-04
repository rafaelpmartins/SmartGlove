package com.example.smartglove;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private Context mContext;
    private List<Esportes> mData;

    public RecyclerViewAdapter(Context mContext, List<Esportes> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;

        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.cardview, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.imgEsporte.setImageResource(mData.get(position).getImagem());
        holder.tvTituloEsporte.setText(mData.get(position).getTitulo());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, StarTreino_Activity.class);
                intent.putExtra("Titulo",mData.get(position).getTitulo());
                mContext.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });
    }

    @Override
    public int getItemCount() {

        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvTituloEsporte;
        ImageView imgEsporte;

        CardView cardView;

        public MyViewHolder(@NonNull View itemView) {

            super(itemView);

            imgEsporte = (ImageView) itemView.findViewById(R.id.id_imgEsporte);
            tvTituloEsporte = (TextView) itemView.findViewById(R.id.id_txtEsporte);

            cardView = (CardView) itemView.findViewById(R.id.idCardView);
        }
    }
}
