package com.example.smartglove;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Bluetooth_Activity extends AppCompatActivity {

    public static int ENABLE_BLUETOOTH = 1;
    public static int SELECT_PAIRED_DEVICE = 2;
    public static int SELECT_DISCOVERED_DEVICE = 3;

    static TextView statusMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetooth_layout);

        statusMessage = (TextView) findViewById(R.id.statusMessage);

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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == ENABLE_BLUETOOTH) {
            if (resultCode == RESULT_OK) {
                statusMessage.setText("Bluetooth ativado :D");
            } else {
                statusMessage.setText("Bluetooth não ativado :(");
            }
        }
        else if(requestCode == SELECT_PAIRED_DEVICE) {
            if(resultCode == RESULT_OK) {
                statusMessage.setText("Você selecionou " + data.getStringExtra("btDevName") + "\n"
                        + data.getStringExtra("btDevAddress"));
            }
            else {
                statusMessage.setText("Nenhum dispositivo selecionado :(");
            }
        }
    }

    public void searchPairedDevices(View view) {
        Intent searchPairedDevicesIntent = new Intent(this, PairedDevices.class);
        startActivityForResult(searchPairedDevicesIntent, SELECT_PAIRED_DEVICE);
    }
}
