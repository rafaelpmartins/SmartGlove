package com.example.smartglove;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Bluetooth_Activity extends AppCompatActivity {

    Button btnConectar;
    BluetoothAdapter mBluetoothAdapter;
    TextView estadoBluetooth;
    TextView estadoBluetoothTitulo;

    //BT = bluetooth

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetooth_layout);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        estadoBluetoothTitulo = (TextView) findViewById(R.id.estadoBluetoohTitulo);
        estadoBluetooth = (TextView) findViewById(R.id.estadoBluetooh);

        btnConectar = (Button) findViewById(R.id.btnConectar);
        btnConectar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ligarDesligarBluetooth();
            }
        });
    }

    private final BroadcastReceiver mBroadcastReceiver1 = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(mBluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int estado = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, mBluetoothAdapter.ERROR);

                switch (estado) {
                    case BluetoothAdapter.STATE_OFF:
                        estadoBluetooth.setText("Desligado");
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        estadoBluetooth.setText("Desligando...");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        estadoBluetooth.setText("Ligado");
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        estadoBluetooth.setText("Ligando...");
                        estadoBluetoothTitulo.setVisibility(View.VISIBLE);
                        break;
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        Toast.makeText(this, "A Conexão se perdeu!", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }

    private void ligarDesligarBluetooth() {
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth não funciona no seu aparelho", Toast.LENGTH_SHORT).show();
        }
        if (!mBluetoothAdapter.isEnabled()) {
            btnConectar.setText("Desligar");

            Intent ligarBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(ligarBtIntent);

            IntentFilter BluetoothIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mBroadcastReceiver1, BluetoothIntent);
        }
        if (mBluetoothAdapter.isEnabled()) {
            btnConectar.setText("Ligar");

            mBluetoothAdapter.disable();

            IntentFilter BluetoothIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mBroadcastReceiver1, BluetoothIntent);
        }
    }
}
