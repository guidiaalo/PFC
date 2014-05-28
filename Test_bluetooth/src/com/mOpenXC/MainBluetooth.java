package com.mOpenXC;

import java.net.Socket;
import java.net.SocketImpl;
import java.util.ArrayList;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


public class MainBluetooth extends Activity {
	/**
	 * actividad principal
	 * 
	 */
	private static final String TAG = "MainBluetooth";
	private BluetoothAdapter mBluetoothAdapter;
	private Spinner mSpinnerPaired;
	private Spinner mSpinnerAvailable;
	private ToggleButton tg_button;
	private BluetoothManageSocket mBluetoothManageSocket;
	private BluetoothConfig mBluetoothConfig;
	private BluetoothSearchPaired mBluetoothSearch;
	private BluetoothConnect mBluetoothConnect;
	private ArrayList<String> mArrayListAvailable;
	private ArrayAdapter<String> madapterAvailable;
	private ArrayList<BluetoothDevice> devices;
	private ArrayList<BluetoothDevice> pairedDevicesArray;
	private TextView mTextView;
	private Handler  mHandler;
	private BluetoothSocket mSocket;

	@Override
	//inicializar variables en OnCreate
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_bluetooth);
		
		mSpinnerPaired =(Spinner)findViewById(R.id.spinnerPaired);
		mSpinnerAvailable = (Spinner)findViewById(R.id.spinnerAvailable);
		mTextView = (TextView)findViewById(R.id.tv1);

		mArrayListAvailable = new ArrayList<String>();
		devices = new ArrayList<BluetoothDevice>();
	
		madapterAvailable = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, mArrayListAvailable);
     
		tg_button = (ToggleButton)findViewById(R.id.tg_button);
		mBluetoothConfig = new BluetoothConfig(getApplicationContext(), this);
		//obtiene el adaptador bluetooth;
		mBluetoothAdapter= mBluetoothConfig.getBluetoothAdapter();
		//inicializo todas las variables, recomendable hacerlo en oncreate
		mBluetoothSearch = new BluetoothSearchPaired(getApplicationContext(),this,mBluetoothAdapter);
		
		mHandler = new Handler();


		IntentFilter lIntentFilter = new IntentFilter();
		 lIntentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
		 lIntentFilter.addAction(BluetoothDevice.ACTION_FOUND);
		 //lIntentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		 //registerReceiver(mReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
		 registerReceiver(mReceiver, lIntentFilter);
		 
		 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_bluetooth, menu);
		return true;
	}

	public void click  (View view){
		//si botno esta on
		if(tg_button.isChecked()){
			//enciende bluetooth
			mBluetoothConfig.BluetoothOn();
		}
		//si esta off lo apaga
		if(!tg_button.isChecked())
			mBluetoothConfig.BluetoothOff();

	}

	public void discoverDevices(View view){

		//llama al méotodo discoverDevices de la clase BluetoothSearch
		mBluetoothSearch.discoverDevices2( mSpinnerPaired, mArrayListAvailable);

	}

	final BroadcastReceiver mReceiver = new BroadcastReceiver(){


		public void onReceive(Context context, Intent intent) {
			final String action = intent.getAction();

			if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
				final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,BluetoothAdapter.ERROR);
				Log.d(TAG, "Bluetooth state changed, new state: " + state);
				switch (state) {
					case BluetoothAdapter.STATE_OFF:
						tg_button.setChecked(false);
						break;
					case BluetoothAdapter.STATE_TURNING_OFF:
						Toast.makeText(context, "Bluetooth turning off", Toast.LENGTH_SHORT).show();
						break;
					case BluetoothAdapter.STATE_ON:
						tg_button.setChecked(true);
						break;
					case BluetoothAdapter.STATE_TURNING_ON:
						Toast.makeText(context, "Bluetooth turning on", Toast.LENGTH_SHORT).show();
						break;
				}
			}
			//cuando encuentra un dispositivo
			if (BluetoothDevice.ACTION_FOUND.equals(action)){
				// Get the BluetoothDevice object from the Intent
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				Log.i("Dispositivo encontrado", devices.toString());
				devices.add(device);
				Log.d(TAG, "Device found: " + device.getName());
				// Add the name and address to an array adapter to show in a List
				if (device.getBondState() == BluetoothDevice.BOND_BONDED)
				    mArrayListAvailable.add(device.getName() + " (Paired)\n" + device.getAddress());
				else
					mArrayListAvailable.add(device.getName() + "\n" + device.getAddress());
				
				madapterAvailable = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item, mArrayListAvailable);
				mSpinnerAvailable.setAdapter(madapterAvailable);
			}
			

		}
	};
	
	public void AvailableConnect(View view){
		
		if (devices.size() == 0){
			Log.i("Lista","ERROR :Lista vacia ");
			return;
		}
			
		if (mBluetoothAdapter.isDiscovering()){
			mBluetoothAdapter.cancelDiscovery();
		}
		
		int posAvailable = mSpinnerAvailable.getSelectedItemPosition();
		Log.i("Lista", devices.toString());
		
		
		Log.d(TAG,devices.get(posAvailable).toString());
		mBluetoothConnect = new BluetoothConnect(devices.get(posAvailable));
		mBluetoothConnect.start();
	}
	
	public void PairedConnect(View view){
		
			
		if (mBluetoothAdapter.isDiscovering()){
			mBluetoothAdapter.cancelDiscovery();
		}
		int posPaired = mSpinnerPaired.getSelectedItemPosition();
		Log.i(TAG, String.valueOf(posPaired));
		//obtiene la lista de dispositivos pareados en forma de device para pasarselo 
		pairedDevicesArray = new ArrayList<BluetoothDevice>(mBluetoothAdapter.getBondedDevices());
		Log.i(TAG, pairedDevicesArray.toString());
		mBluetoothConnect = new BluetoothConnect(pairedDevicesArray.get(posPaired));
		mBluetoothConnect.start();
		
	}
	

	 @Override
	 protected void onResume() {
		 super.onResume();
		 
		 IntentFilter lIntentFilter = new IntentFilter();
		 lIntentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
		 lIntentFilter.addAction(BluetoothDevice.ACTION_FOUND);
		 //registerReceiver(mReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
		 registerReceiver(mReceiver,lIntentFilter);
		 mBluetoothConfig.CheckBluetooth(tg_button);

	 }
	 @Override
	 protected void onPause() {
		 super.onPause();
		unregisterReceiver(mReceiver);
    }
	 
	 public class Atualiza implements Runnable{
			
		 private  String mvalue;
			
		 public Atualiza (String value){
				
			 mvalue = value;			
				
		}

		@Override
		public void run() {
			mTextView.setText(mvalue);
				
		}		
	}
	 
	/*protected void onStop(){
		super.onStop();
		mBluetoothManageSocket = new BluetoothManageSocket(mSocket) ;
	}*/
}
