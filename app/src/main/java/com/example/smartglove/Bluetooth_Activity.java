package com.example.smartglove;

import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Bluetooth_Activity extends AppCompatActivity {

    public static int ENABLE_BLUETOOTH = 1;
    public static int SELECT_PAIRED_DEVICE = 2;
    public static int SELECT_DISCOVERED_DEVICE = 3;
    ConnectionThread connect;
    static TextView statusMessage;
    private AlertDialog alerta;

    Button button_PairedDevices;
    Button button_DiscoveredDevices;
    Button button_Visibility;
    Button button_WaitConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetooth_layout);

        findViewById();

        button_PairedDevices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchPairedDevices(v);
            }
        });
        button_DiscoveredDevices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                discoverDevices(v);
            }
        });
        button_Visibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableVisibility(v);
            }
        });
        button_WaitConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                waitConnection(v);
            }
        });

        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        if (btAdapter == null) {
            statusMessage.setText("Que pena! Hardware Bluetooth não está funcionando :(");
        } else {
            statusMessage.setText("Ótimo! Hardware Bluetooth está funcionando :)");
        }

        if (!btAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, ENABLE_BLUETOOTH);
            statusMessage.setText("Solicitando ativação do Bluetooth...");
        } else {
            statusMessage.setText("Bluetooth já ativado :)");
        }

        dialogBluetooh();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == ENABLE_BLUETOOTH) {
            if (resultCode == RESULT_OK) {
                statusMessage.setText("Bluetooth ativado :D");
            } else {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                Toast.makeText(this, "Bluetooth não ativado :(", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == SELECT_PAIRED_DEVICE || requestCode == SELECT_DISCOVERED_DEVICE) {
            if (resultCode == RESULT_OK) {
                statusMessage.setText("Você selecionou " + data.getStringExtra("btDevName") + "\n"
                        + data.getStringExtra("btDevAddress"));
                connect = new ConnectionThread(data.getStringExtra("btDevAddress"));
                connect.start();
            } else {
                statusMessage.setText("Nenhum dispositivo selecionado :(");
            }
        }
    }


    public void searchPairedDevices(View view) {
        Intent searchPairedDevicesIntent = new Intent(this, PairedDevices.class);
        startActivityForResult(searchPairedDevicesIntent, SELECT_PAIRED_DEVICE);
    }

    public void discoverDevices(View view) {
        Intent searchPairedDevicesIntent = new Intent(this, DiscoveredDevices.class);
        startActivityForResult(searchPairedDevicesIntent, SELECT_DISCOVERED_DEVICE);
    }

    public void enableVisibility(View view) {
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 30);
        startActivity(discoverableIntent);

        button_WaitConnection.setEnabled(true);
    }

    public void waitConnection(View view) {
        Toast.makeText(this, "Aguardando cliente...", Toast.LENGTH_SHORT).show();

        connect = new ConnectionThread();
        connect.start();
    }

    public static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            Bundle bundle = msg.getData();
            byte[] data = bundle.getByteArray("data");
            String dataString = new String(data);

            if (dataString.equals("---N"))
                statusMessage.setText("Ocorreu um erro durante a conexão D:");
            else if (dataString.equals("---S"))
                statusMessage.setText("Conectado :D");
        }
    };

    private void findViewById() {
        statusMessage = (TextView) findViewById(R.id.statusMessage);
        button_PairedDevices = (Button) findViewById(R.id.button_PairedDevices);
        button_DiscoveredDevices = (Button) findViewById(R.id.button_DiscoveredDevices);
        button_Visibility = (Button) findViewById(R.id.button_Visibility);
        button_WaitConnection = (Button) findViewById(R.id.button_WaitConnection);
    }

    private void dialogBluetooh() {
        //Cria o gerador do AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //define o titulo
        builder.setTitle("Bluetooth");
        //define a mensagem
        builder.setMessage("Escolha um modo, servidor(espera a conexão de outro dispositivo) ou cliente(faz a solicitação de conexão).");
        //define um botão como positivo
        builder.setPositiveButton("Cliente", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                button_Visibility.setVisibility(View.GONE);
                button_WaitConnection.setVisibility(View.GONE);
            }
        });
        //define um botão como negativo.
        builder.setNegativeButton("Servidor", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                button_PairedDevices.setVisibility(View.GONE);
                button_DiscoveredDevices.setVisibility(View.GONE);
                button_WaitConnection.setEnabled(false);
            }
        });

        builder.setNeutralButton("Voltar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        //cria o AlertDialog
        alerta = builder.create();
        //Exibe
        alerta.show();
    }

}
