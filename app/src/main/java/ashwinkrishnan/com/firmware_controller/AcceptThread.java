package ashwinkrishnan.com.firmware_controller;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by Ashwin on 2015-09-24.
 */
public class AcceptThread extends Thread {
    public final BluetoothServerSocket mmServerSocket;
    BluetoothAdapter mBluetoothAdapter;
    public final UUID MY_UUID = UUID.fromString("94f39d29-7d6d-437d-973b-fba39e49d4ee");

    public AcceptThread(BluetoothAdapter adapter) {
        // Use a temporary object that is later assigned to mmServerSocket,
        // because mmServerSocket is final
        mBluetoothAdapter = adapter;
        BluetoothServerSocket tmp = null;
        try {
            // MY_UUID is the app's UUID string, also used by the client code
            tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord("Firmware_Controller", MY_UUID);
        } catch (IOException e) { }
        mmServerSocket = tmp;
    }

    public void manageConnections(BluetoothSocket socket){
        ConnectedThread thread = new ConnectedThread(socket);
        thread.start();
    }

    public void run() {

        // Keep listening until exception occurs or a socket is returned
        while (true) {
            BluetoothSocket socket = null;
            try {
                socket = mmServerSocket.accept();
            } catch (IOException e) {
                break;
            }
            // If a connection was accepted
            if (socket != null) {

                // Do work to manage the connection (in a separate thread)
                manageConnections(socket);
                try {
                    mmServerSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }


    /** Will cancel the listening socket, and cause the thread to finish */
    public void cancel() {
        try {
            mmServerSocket.close();
        } catch (IOException e) { }
    }
}