package com.mOpenXC;

import java.io.IOException;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;


public class BluetoothConnect extends Thread {
	

	
	private static final UUID MY_UUID = null;
	private BluetoothAdapter mBluetoothAdapter;
	private BluetoothDevice mmDevice;
	private BluetoothSocket mmSocket;
	private BluetoothManageSocket mBluetoothManageSocket;

	/**
	 * 
	 * Crea socket conexion para 
	 * comenzar la comunicacion
	 * @return 
	 * @return 
	 */
    public BluetoothConnect(BluetoothDevice device) {
        // Use a temporary object that is later assigned to mmSocket,
        // because mmSocket is final
    	mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothSocket tmp = null;
        mmDevice = device;
 
        // Get a BluetoothSocket to connect with the given BluetoothDevice
        try {
            // MY_UUID is the app's UUID string, also used by the server code
            tmp = device.createRfcommSocketToServiceRecord(UUID.fromString("94f39d29-7d6d-437d-973b-fba39e49d4ee"));
        } catch (IOException e) { Log.e("socket", e.getMessage()); }
        mmSocket = tmp;
    }
 
    public void run() {
        // Cancel discovery because it will slow down the connection
        mBluetoothAdapter.cancelDiscovery();
 
        try {
            // Connect the device through the socket. This will block
            // until it succeeds or throws an exception
            mmSocket.connect();
        } catch (IOException connectException) {
        	Log.i("socketErro", connectException.getMessage());
            // Unable to connect; close the socket and get out
            try {
                mmSocket.close();
            } catch (IOException closeException) { }
            return;
        }
 
        // Do work to manage the connection (in a separate thread)
       mBluetoothManageSocket = new BluetoothManageSocket(mmSocket);
       mBluetoothManageSocket.start();
    }
 
    /** Will cancel an in-progress connection, and close the socket */
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) { }
    }
}
