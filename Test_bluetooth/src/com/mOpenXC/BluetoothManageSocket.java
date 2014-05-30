package com.mOpenXC;

	import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import org.json.JSONException;
import org.json.JSONObject;

import com.mOpenXC.MainBluetooth.Atualiza;

import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

	public class BluetoothManageSocket extends Thread{
		
		private BluetoothSocket mmSocket;
	    private InputStream mmInStream;
	    private OutputStream mmOutStream;
	    private Handler mHandler;
		private int MESSAGE_READ;
		private String json = "";
		private JSONObject jObj;
		private Message mMessage;
		private Atualiza	mAtualiza;
		private HandlerThread mAtualizadorThread;
		private MainBluetooth mMainBluetooth;
		
		
	    public BluetoothManageSocket(BluetoothSocket socket, Atualiza aAtualiza, HandlerThread aAtualizadorThread) {
	    	
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
	        mAtualiza = aAtualiza;
	        mAtualizadorThread = aAtualizadorThread;
	       
	        
	    }
	 
	    public void run() {
	    	
	        byte[] buffer = new byte[1024];  // buffer store for the stream
	        int bytes; // bytes returned from read()
	       // String msg = "iniciando...";
	        BufferedReader reader;
            reader = new BufferedReader(new InputStreamReader(mmInStream));
            String line = null;
	        StringBuilder sb = new StringBuilder();
	        // Keep listening to the InputStream until an exception occurs
	        while (true) {
	            try {
	                // Read from the InputStream
	            	
	            	line = reader.readLine();
	            	Log.i("tag",line);
	                sb.append(line + "\n");
	                json = sb.toString();
	                jObj = new JSONObject(json);
	                Log.i("tag", jObj.getString("name"));
	                
                	
	                if (jObj.getString("name").equals("steering_wheel_angle")) {
	                		
	                	mMessage = mAtualiza.obtainMessage();
	                	mMessage.obj = jObj.get("value");
	                	mAtualiza.sendMessage(mMessage);
	            		//mHandler.post(new MainBluetooth().new Atualiza(jObj.getString("value")));
	            		Log.i("TAG", String.valueOf(jObj.getDouble("value")));	
	            			        
	                }
	            } catch (IOException e) {
	            	try {
	            		mmInStream.close();
						mmSocket.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
	                break;
	            } catch (JSONException e) {
					// TODO Auto-generated catch block
	            	try {
	            		mmInStream.close();
						mmSocket.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					e.printStackTrace();
				}
	        }
	    }
	 
	 
	    /* Call this from the main activity to shutdown the connection */
	    public void cancel() {
	        try {
	        	mmInStream.close();
	            mmSocket.close();
	        } catch (IOException e) { }
	    }   
	}