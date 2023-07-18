package com.grupom.pm1examen2grupo2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.grupom.pm1examen2grupo2.Procesos.InfoFirma;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {
    ArrayList<InfoFirma> DatosFirmaLista;


    public Adapter(ArrayList<InfoFirma> firms){
        this.DatosFirmaLista = firms;
    }

    @NonNull
    @Override
    public Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent , int viewType){

        View view = LayoutInflater.from(
                parent.getContext()).inflate(R.layout.activity_diseno, parent,
                false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.MyViewHolder holder, final int position) {

        holder.setData(DatosFirmaLista.get(position));
    }

    @Override
    public int getItemCount() {
        return DatosFirmaLista.size();
    }

    private static Bitmap getSignaturessImage(String encodedImage){
        byte[] bytes = android.util.Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        ImageView imageViewIcon;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textViewName = (TextView) itemView.findViewById(R.id.textViewName);
            this.imageViewIcon = (ImageView) itemView.findViewById(R.id.imageView);

        }

        void setData(InfoFirma firm){
            textViewName.setText(firm.getInformacion());
            imageViewIcon.setImageBitmap(getSignaturessImage(firm.getImg()));
        }
    }
}

