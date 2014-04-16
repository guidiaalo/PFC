package com.mOpenXC;

	import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

	import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

	public class BluetoothManageSocket extends Thread{
		
		private BluetoothSocket mmSocket;
	    private InputStream mmInStream;
	    private OutputStream mmOutStream;
	    private Handler mHandler;
		private int MESSAGE_READ;
		private String json = "";
	    public BluetoothManageSocket(BluetoothSocket socket) {
	    	
	        mmSocket = socket;
	        InputStream tmpIn = null;
	        OutputStream tmpOut = null;
	 
	        // Get the input and output streams, using temp objects because
	        // member streams are final
	        try {
	            tmpIn = socket.getInputStream();
	            tmpOut = socket.getOutputStream();
	        } catch (IOException e) { }
	 
	        mmInStream = tmpIn;
	        mmOutStream = tmpOut;
	    }
	 
	    public void run() {
	        byte[] buffer = new byte[1024];  // buffer store for the stream
	        int bytes; // bytes returned from read()
	 
	        // Keep listening to the InputStream until an exception occurs
	        while (true) {
	            try {
	                // Read from the InputStream
	            	mmOutStream.write("HELLO".getBytes());
	                bytes = mmInStream.read(buffer);
	                try {
	                    BufferedReader reader = new BufferedReader(new InputStreamReader(mmInStream, "iso-8859-1"), 8);
	                    StringBuilder sb = new StringBuilder();
	                    String line = null;
	                    while ((line = reader.readLine()) != null) {
	                        sb.append(line + "\n");
	                    }
	                    mmInStream.close();
	                    json = sb.toString();
	                } catch (Exception e) {
	                    Log.e("Buffer Error", "Error converting result " + e.toString());
	                }
	                // Send the obtained bytes to the UI activity
	                mHandler.obtainMessage(MESSAGE_READ , bytes, -1, buffer).sendToTarget();
	              
	            } catch (IOException e) {
	                break;
	            }
	        }
	    }
	 
	    /* Call this from the main activity to send data to the remote device */
	    public void write(byte[] bytes) {
	        try {
	            mmOutStream.write(bytes);
	        } catch (IOException e) { }
	    }
	 
	    /* Call this from the main activity to shutdown the connection */
	    public void cancel() {
	        try {
	            mmSocket.close();
	        } catch (IOException e) { }
	    }
	    
	}


