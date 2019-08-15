package com.example.smartglove;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

//serve para achar nome e endereço de um dispositivo
public class DispositivosListAdapter extends ArrayAdapter<BluetoothDevice> {
    private LayoutInflater mLayoutInflater;
    private ArrayList<BluetoothDevice> mDispositivos;
    private int  mViewResourceId;

    public DispositivosListAdapter(Context context, int tvResourceId, ArrayList<BluetoothDevice> dispositivos){
        super(context, tvResourceId,dispositivos);
        this.mDispositivos = dispositivos;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mViewResourceId = tvResourceId;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = mLayoutInflater.inflate(mViewResourceId, null);

        BluetoothDevice dispositivo = mDispositivos.get(position);
        if (dispositivo != null) {
            TextView nomeDispositivo = (TextView) convertView.findViewById(R.id.txtNomeDispositivo);
            TextView enderecoDispositivo = (TextView) convertView.findViewById(R.id.txtEnderecoDispositivo);

            if (nomeDispositivo != null) {
                nomeDispositivo.setText(dispositivo.getName());
            }
            if (enderecoDispositivo != null) {
                enderecoDispositivo.setText(dispositivo.getAddress());
            }
        }

        return convertView;
    }

}
