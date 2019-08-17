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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class Bluetooth_Activity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private Button btnLigar, btnVisibilidadeBT, btnProcurarDispositivos;
    private TextView txtReceberBroadcast4, txtEstadoBluetooth, txtEstadoBluetoothTitulo, txtVisibilidadeBluetooh;
    private ListView ListNovosDispositivos;
    private BluetoothAdapter mBluetoothAdapter;
    private ArrayList<BluetoothDevice> ArrayBTDispositvos = new ArrayList<>();
    private DispositivosListAdapter mDispositivosListAdapter;

    //BT = bluetooth

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetooth_layout);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        btnProcurarDispositivos = (Button) findViewById(R.id.btnProcurarDispositivos);
        btnLigar = (Button) findViewById(R.id.btnLigar);
        btnVisibilidadeBT = (Button) findViewById(R.id.btnVisibilidadeBT);
        txtEstadoBluetoothTitulo = (TextView) findViewById(R.id.txtEstadoBluetoohTitulo);
        txtEstadoBluetooth = (TextView) findViewById(R.id.txtEstadoBluetooh);
        txtVisibilidadeBluetooh = (TextView) findViewById(R.id.txtVisibilidadeBluetooh);
        ListNovosDispositivos = (ListView) findViewById(R.id.listNovosDispositivos);
        txtReceberBroadcast4 = (TextView) findViewById(R.id.txtReceberBroadcast4);
        ArrayBTDispositvos = new ArrayList<>();

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(mBroadcastReceiver4, filter);

        ListNovosDispositivos.setOnItemClickListener(Bluetooth_Activity.this);

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
                        txtVisibilidadeBluetooh.setVisibility(View.GONE);
                        txtEstadoBluetooth.setText("Desligado");
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        txtEstadoBluetooth.setText("Desligando...");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        txtEstadoBluetooth.setText("Ligado");
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        txtVisibilidadeBluetooh.setVisibility(View.VISIBLE);
                        txtEstadoBluetooth.setText("Ligando...");
                        txtEstadoBluetoothTitulo.setVisibility(View.VISIBLE);
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
                        txtVisibilidadeBluetooh.setText("Visivel");
                        break;
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE:
                        txtVisibilidadeBluetooh.setText("Invisivel, e aberto a conexões");
                        break;
                    case BluetoothAdapter.SCAN_MODE_NONE:
                        txtVisibilidadeBluetooh.setText("Invisivel, fechado a conexão");
                        break;
                    case BluetoothAdapter.STATE_CONNECTING:
                        txtVisibilidadeBluetooh.setText("Conectando...");
                        break;
                    case BluetoothAdapter.STATE_CONNECTED:
                        txtVisibilidadeBluetooh.setText("Conectado");
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
                BluetoothDevice dispositivo = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                ArrayBTDispositvos.add(dispositivo);
                mDispositivosListAdapter = new DispositivosListAdapter(context, R.layout.dispositivo_adapter_view, ArrayBTDispositvos);
                ListNovosDispositivos.setAdapter(mDispositivosListAdapter);
            }

        }
    };

    private BroadcastReceiver mBroadcastReceiver4 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)) {
                BluetoothDevice mDispositivo = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //3 casos:
                //caso 1: Já há uma ligação
                if (mDispositivo.getBondState() == BluetoothDevice.BOND_BONDED) {
                    txtReceberBroadcast4.setText("Já está pareado");
                }
                //caso 2: Criando uma ligação
                if (mDispositivo.getBondState() == BluetoothDevice.BOND_BONDING) {
                    txtReceberBroadcast4.setText("Criando pareamento");
                }
                //caso 3: ligação quebrada
                if (mDispositivo.getBondState() == BluetoothDevice.BOND_NONE) {
                    txtReceberBroadcast4.setText("não foi possivel parear");
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
            Toast.makeText(this, "Aguarde 60 segundos, para fazer uma nova busca", Toast.LENGTH_SHORT).show();

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

            //serve para trocar o texto do botão após 60 segundos 'se a busca não for cancelada'
            btnProcurarDispositivos.setText("Cancelar");
            new CountDownTimer(60000, 10) {
                public void onTick(long millisUntilFinished) {
                }

                @Override
                public void onFinish() {
                    btnProcurarDispositivos.setText("Procurar");
                }
            }.start();

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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //primeiro cancelar a descoberta porque é muito intensivo na memória.
        mBluetoothAdapter.cancelDiscovery();

        String nomeDispositivo = ArrayBTDispositvos.get(i).getName();

        //criando uma ligação.
        //NOTA: Requer API 17+ (JellyBean)
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2) {
            Toast.makeText(this, "Tentando emparelhar com " + nomeDispositivo, Toast.LENGTH_SHORT).show();
            ArrayBTDispositvos.get(i).createBond();
        }
    }
}