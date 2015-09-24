package ashwinkrishnan.com.firmware_controller;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Set;


public class MainActivity extends ActionBarActivity {
    Set<BluetoothDevice> pairedDevices;
    BluetoothAdapter adapter;
    Button connect;
    EditText status;
    BluetoothSocket socket;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        enableBluetooth();
        final byte[] out = "A".getBytes();
        getPairedDevices();
        AcceptThread acceptThread = new AcceptThread(adapter);
        acceptThread.start();


        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPairedDevices();



            }
        });
    }

    public void initViews(){
        connect = (Button)findViewById(R.id.connect_button);
        status = (EditText)findViewById(R.id.status);

    }

    private void enableBluetooth() {
        adapter = BluetoothAdapter.getDefaultAdapter();
        if(adapter == null){
            // Device does not support Bluetooth
        }

        if(adapter.isEnabled() == false){
            // Enable the bluetooth if it is not enabled by calling intent
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, 1);
        }
    }

    public void getPairedDevices(){
        pairedDevices = adapter.getBondedDevices();
        if(pairedDevices.size() > 0){
            for(BluetoothDevice device: pairedDevices){
                status.setText(device.getName() + "\n" + device.getAddress());
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_CANCELED){
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }



        return super.onOptionsItemSelected(item);
    }
}
