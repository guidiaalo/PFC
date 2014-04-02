package com.mOpenXC;

import java.util.ArrayList;
import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class BluetoothSearchPaired{
	
	
private static final String TAG = "BluetoothSearch";
private Set<BluetoothDevice> pairedDevices;
private ArrayList<String> mArrayListPaired;
private ArrayAdapter<String> mAdapterPaired;
Context mContext;
Activity mMainActivity;
BluetoothAdapter mBluetoothAdapter;

	/**
	 * Construtor principal
	 */
	public BluetoothSearchPaired (Context aContext, Activity aActivity,BluetoothAdapter aBluetoothAdapter){
		
		mContext = aContext;
		mMainActivity = aActivity;
		mBluetoothAdapter = aBluetoothAdapter;
	}

	public void discoverDevices2( Spinner aListPaired,ArrayList<String> aArrayListAvailable){
	
	//inicializo lista y adapter de paired devices
	mArrayListPaired = new ArrayList<String>();
	mAdapterPaired= new ArrayAdapter<String>(mMainActivity,android.R.layout.simple_spinner_item, mArrayListPaired);
		
	Toast.makeText(mContext,"escaneando dsipositivos",Toast.LENGTH_SHORT).show();
	pairedDevices = mBluetoothAdapter.getBondedDevices();
	//crea lista dispositivos vinculados
	ArrayList list = new ArrayList();
	//loop para asociar todos los dispositvos
	for(BluetoothDevice bt : pairedDevices){
		list.add(bt.getName());
	}
	
	Log.d(TAG, "List of paired devices:" + list.toString());
	//adaptador para mostrrar en una listview
	mAdapterPaired = new ArrayAdapter(mMainActivity,android.R.layout.simple_list_item_1, list);
	aListPaired.setAdapter(mAdapterPaired);
	    
	//si esta activo la busqueda la deshabilita
	if(mBluetoothAdapter.isDiscovering())
		mBluetoothAdapter.cancelDiscovery();
	 //habilita la busqueda de dispositivos bluetooth
	 mBluetoothAdapter.startDiscovery();
	 //eliminar lista para no acarrear en cada busqueda
	 aArrayListAvailable.clear();
	
	
	}
}
