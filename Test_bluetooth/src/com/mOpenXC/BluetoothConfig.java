package com.mOpenXC;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import android.widget.ToggleButton;

public class BluetoothConfig {
	
	public static final int START_BT_CODE = 0;
	Context mContext;
	Activity mMainActivity;
	//private static final String TAG = "MainBluetooth";
	private BluetoothAdapter mBluetoothAdapter;
	/**
	 * Constructor principal.
	 * Obtiene el objeto BluetoothAdapter.
	 */
	public BluetoothConfig(Context aContext, Activity aActivity) {
		mContext = aContext;
		mMainActivity = aActivity;
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	}
	public BluetoothAdapter getBluetoothAdapter(){
		//comprueba si no dio error la peticion de bluetooth
		if (mBluetoothAdapter != null) {
			return mBluetoothAdapter;
		} else {
			Toast.makeText(mContext, "Bluetooth is not available", Toast.LENGTH_LONG).show();
			return null;
		}
	}
	
	
	//comprueba bluetooth para sincronizar con android
	public void CheckBluetooth (ToggleButton tg_button){
		//deja el boton en el estado del bluetooth
		//si esta encendido
		if (mBluetoothAdapter.isEnabled()){
		//lo enciende
			tg_button.setChecked(true); 
		}
		//si no 
		else{
		//lo apaga
			tg_button.setChecked(false);
	    }
	}
	
	
	
	//enciende bluetooth
	public void BluetoothOn () {
		//si no esta encendido enciende bluetooth
		if (!mBluetoothAdapter.isEnabled()) {
	         Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
	         mMainActivity.startActivityForResult(turnOn, START_BT_CODE);
	      }
	     else{
	         Toast.makeText(mContext,"Already on",  Toast.LENGTH_LONG).show();
	      }
	}
	
	public void BluetoothOff (){
		mBluetoothAdapter.disable();
		Toast.makeText(mContext,"Turned off" , Toast.LENGTH_LONG).show();
	}

}
