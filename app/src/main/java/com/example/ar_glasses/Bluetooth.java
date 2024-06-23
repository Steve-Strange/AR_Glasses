package com.example.ar_glasses;

import android.bluetooth.*;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class Bluetooth extends AppCompatActivity {
    private static final int REQUEST_ENABLE_BT = 1;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothDevice pairedDevice;
    private BluetoothSocket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Bluetooth adapter
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            // Device doesn't support Bluetooth
            return;
        }

        // Enable Bluetooth if it's not enabled
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        // Discover and pair with the device
        discoverAndPairDevice();
    }

    private void discoverAndPairDevice() {
        // Start discovery
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        bluetoothAdapter.startDiscovery();

        // Register a BroadcastReceiver for ACTION_FOUND
        // When a device is found, attempt to pair with it
        // Once paired, call connectToDevice()
    }

    private void connectToDevice() {
        try {
            // Create a BluetoothSocket for connection
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            socket = pairedDevice.createRfcommSocketToServiceRecord(MY_UUID);

            // Connect to the remote device through the socket. This call blocks
            // until it succeeds or throws an exception
            socket.connect();

            // Connection succeeded, now you can send/receive data
            sendData("Hello from Android!");
            receiveData();
        } catch (IOException e) {
            // Unable to connect; close the socket and return
        }
    }

    private void sendData(String message) {
        try {
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(message.getBytes());
        } catch (IOException e) {
            // Error occurred when sending data
        }
    }

    private void receiveData() {
        InputStream inputStream;
        try {
            inputStream = socket.getInputStream();
            // Read incoming data (this should be done in a separate thread)
            byte[] buffer = new byte[1024];
            int bytes;
            while (true) {
                bytes = inputStream.read(buffer);
                // Process received data
            }
        } catch (IOException e) {
            // Error occurred when receiving data
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                // Error closing socket
            }
        }
    }
}