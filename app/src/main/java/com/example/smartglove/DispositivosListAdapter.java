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
    private ArrayList<BluetoothDevice> ArrayBTDispositivos;
    private int mViewResourceId;

    public DispositivosListAdapter(Context context, int tvResourceId, ArrayList<BluetoothDevice> dispositivos){
        super(context, tvResourceId,dispositivos);
        this.ArrayBTDispositivos = dispositivos;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mViewResourceId = tvResourceId;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = mLayoutInflater.inflate(mViewResourceId, null);

        BluetoothDevice dispositivoBT = ArrayBTDispositivos.get(position);
        if (dispositivoBT != null) {
            TextView nomeDispositivo = (TextView) convertView.findViewById(R.id.txtNomeDispositivo);
            TextView enderecoDispositivo = (TextView) convertView.findViewById(R.id.txtEnderecoDispositivo);

            if (nomeDispositivo != null) {
                nomeDispositivo.setText(dispositivoBT.getName());
            }
            if (enderecoDispositivo != null) {
                enderecoDispositivo.setText(dispositivoBT.getAddress());
            }
        }

        return convertView;
    }

}
