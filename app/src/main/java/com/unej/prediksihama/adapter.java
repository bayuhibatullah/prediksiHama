package com.unej.prediksihama;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Map;

public class adapter extends RecyclerView.Adapter<adapter.ViewHolder> {

    Context context;
    Map<String, String> MapPencegahan;

    public adapter(Context context, Map<String, String> MapPencegahan) {
        this.context = context;
        this.MapPencegahan = MapPencegahan;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_pencegahan, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.nomor.setText(Integer.toString(position+1));
        holder.pesan.setText(MapPencegahan.get(Integer.toString(position+1)));
    }

    @Override
    public int getItemCount() {
        return MapPencegahan.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView nomor, pesan;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nomor = itemView.findViewById(R.id.nomor);
            pesan = itemView.findViewById(R.id.pesan);
        }
    }
}
