package com.example.smartglove;

import android.Manifest;
import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Bluetooth_Activity extends AppCompatActivity {

    private Button btnLigar;
    private Button btnVisibilidadeBT;
    private Button btnProcurarDispositivos;
    private BluetoothAdapter mBluetoothAdapter;
    private TextView estadoBluetooth;
    private TextView estadoBluetoothTitulo;
    private TextView VisibilidadeBluetooh;
    private ArrayList<BluetoothDevice> mBTDispositvos = new ArrayList<>();
    private DispositivosListAdapter mDispositivosListAdapter;
    private ListView ListNovosDispositivos;

    //BT = bluetooth

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetooth_layout);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        btnProcurarDispositivos = (Button) findViewById(R.id.btnProcurarDispositivos);
        btnLigar = (Button) findViewById(R.id.btnLigar);
        btnVisibilidadeBT = (Button) findViewById(R.id.btnVisibilidadeBT);
        estadoBluetoothTitulo = (TextView) findViewById(R.id.estadoBluetoohTitulo);
        estadoBluetooth = (TextView) findViewById(R.id.estadoBluetooh);
        VisibilidadeBluetooh = (TextView) findViewById(R.id.VisibilidadeBluetooh);
        ListNovosDispositivos = (ListView) findViewById(R.id.ListNovosDispositivos);
        mBTDispositvos = new ArrayList<>();

        btnLigar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ligarDesligarBluetooth();
                btnVisibilidadeBT.setVisibility(View.VISIBLE);
                btnProcurarDispositivos.setVisibility(View.VISIBLE);
            }
        });

        btnVisibilidadeBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VisivelBT();
            }
        });

        btnProcurarDispositivos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProcurarBt();
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
                        btnVisibilidadeBT.setVisibility(View.GONE);
                        btnProcurarDispositivos.setVisibility(View.GONE);
                        VisibilidadeBluetooh.setVisibility(View.GONE);
                        estadoBluetooth.setText("Desligado");
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        estadoBluetooth.setText("Desligando...");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        estadoBluetooth.setText("Ligado");
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        VisibilidadeBluetooh.setVisibility(View.VISIBLE);
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

    //Busca novos Dispositivos e executa o metodo ProcurarBt
    private BroadcastReceiver mBroadcastReceiver3 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                mBTDispositvos.add(device);
                mDispositivosListAdapter = new DispositivosListAdapter(context, R.layout.dispositivo_adapter_view, mBTDispositvos);
                ListNovosDispositivos.setAdapter(mDispositivosListAdapter);
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

    private void ProcurarBt() {
        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
            btnProcurarDispositivos.setText("Procurar");
            Toast.makeText(this, "Cancelando Busca...", Toast.LENGTH_SHORT).show();

            //desativar o botao por 60 segundos
            btnProcurarDispositivos.setEnabled(false);
            new CountDownTimer(60000, 10) {
                public void onTick(long millisUntilFinished) {
                }

                @Override
                public void onFinish() {
                    btnProcurarDispositivos.setEnabled(true);
                }
            }.start();

            checarPermissoesBT();

            mBluetoothAdapter.startDiscovery();
            IntentFilter buscarDispositivosIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(mBroadcastReceiver3, buscarDispositivosIntent);
        }
        if (!mBluetoothAdapter.isDiscovering()) {
            Toast.makeText(this, "Procurando novos dispositivos...", Toast.LENGTH_SHORT).show();
            btnProcurarDispositivos.setText("Cancelar");

            checarPermissoesBT();

            mBluetoothAdapter.startDiscovery();
            IntentFilter buscarDispositivosIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(mBroadcastReceiver3, buscarDispositivosIntent);
        }
    }

    /**
     * Este método é obrigatório para todos os dispositivos que executam a API23 +
     * O Android deve verificar programaticamente as permissões para o bluetooth. Colocar as permissões adequadas no manifest não é suficiente.
     * NOTA: Isso só será executado em versões > LOLLIPOP porque não é necessário de outra forma.
     */

    @TargetApi(Build.VERSION_CODES.M)
    private void checarPermissoesBT() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            int permissionCheck = this.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
            permissionCheck += this.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
            if (permissionCheck != 0) {
                this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001); //Qualquer número
            }
        }
    }
}