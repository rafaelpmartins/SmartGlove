package com.example.smartglove;

import android.bluetooth.BluetoothAdapter;
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

    Button btnLigar;
    Button btnVisibilidadeBT;
    BluetoothAdapter mBluetoothAdapter;
    TextView estadoBluetooth;
    TextView estadoBluetoothTitulo;
    TextView VisibilidadeBluetooh;

    //BT = bluetooth

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetooth_layout);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        estadoBluetoothTitulo = (TextView) findViewById(R.id.estadoBluetoohTitulo);
        estadoBluetooth = (TextView) findViewById(R.id.estadoBluetooh);
        btnLigar = (Button) findViewById(R.id.btnLigar);
        btnVisibilidadeBT = (Button) findViewById(R.id.btnVisibilidadeBT);
        VisibilidadeBluetooh = (TextView) findViewById(R.id.VisibilidadeBluetooh);

        btnLigar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ligarDesligarBluetooth();
                btnVisibilidadeBT.setVisibility(View.VISIBLE);
            }
        });

        btnVisibilidadeBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VisivelBT();
            }
        });
    }

    //serve para checar o estado do bluetooh
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
    //serve para checar a visibilidade do bluetooth
    private final BroadcastReceiver mBroadcastReceiver2 = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(mBluetoothAdapter.ACTION_SCAN_MODE_CHANGED)) {
                int modo = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, BluetoothAdapter.ERROR);

                switch (modo) {
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE:
                        VisibilidadeBluetooh.setText("Visivel");
                        break;
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE:
                        VisibilidadeBluetooh.setText("Invisivel, e aberto a conexões");
                        break;
                    case BluetoothAdapter.SCAN_MODE_NONE:
                        VisibilidadeBluetooh.setText("Invisivel, fechado a conexão");
                        break;
                    case BluetoothAdapter.STATE_CONNECTING:
                        VisibilidadeBluetooh.setText("Conectando...");
                        break;
                    case BluetoothAdapter.STATE_CONNECTED:
                        VisibilidadeBluetooh.setText("Conectado");
                        break;
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        mBluetoothAdapter.disable();
        Toast.makeText(this, "A Conexão se perdeu!", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }

    private void ligarDesligarBluetooth() {
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth não funciona no seu aparelho", Toast.LENGTH_SHORT).show();
        }
        if (!mBluetoothAdapter.isEnabled()) {
            btnLigar.setText("Desligar");

            Intent ligarBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(ligarBtIntent);

            IntentFilter BluetoothIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mBroadcastReceiver1, BluetoothIntent);
        }
        if (mBluetoothAdapter.isEnabled()) {
            btnLigar.setText("Ligar");

            mBluetoothAdapter.disable();

            IntentFilter BluetoothIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mBroadcastReceiver1, BluetoothIntent);
        }
    }

    private void VisivelBT() {
        VisibilidadeBluetooh.setText("Visivel por 300 segundos...");

        Intent VisibilidadeIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        VisibilidadeIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivity(VisibilidadeIntent);

        IntentFilter VisibilidadeIntentFilter = new IntentFilter(mBluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        registerReceiver(mBroadcastReceiver2, VisibilidadeIntentFilter);
    }
}
