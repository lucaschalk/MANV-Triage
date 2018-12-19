package de.uniluebeck.imis.triage.activities;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;
import de.uniluebeck.imis.triage.R;

public class BT_erase_Activity extends Activity {

    private static final int REQUEST_ENABLE_BT = 1;
    APP_STATE app_state;
    TextView textview1;
    ListView listview_bt_devices;
    ArrayAdapter<String> adapter_bt_devices;
    ArrayList<BluetoothDevice> found_bt_devices = new ArrayList<BluetoothDevice>();
    Button ok_button;

    String display_str;
    String mac_addr;
    BluetoothAdapter btAdapter;
    int selected_devices = -1;
    // Create a BroadcastReceiver for ACTION_FOUND
    final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // Add the name and address to an array adapter to show in a
                // ListView
                int i; // avoid multiple entry
                for (i = 0; i < adapter_bt_devices.getCount(); i++) {
                    if (adapter_bt_devices.getItem(i).contains(device.getAddress()))
                        break;
                }
                if (i >= adapter_bt_devices.getCount()) {
                    adapter_bt_devices.add(device.getName() + " " + device.getAddress());
                    found_bt_devices.add(device);
                }
            }
            if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                if (app_state == APP_STATE.SCAN) {
                    display_str = "Found " + adapter_bt_devices.getCount() + " devices...";
                    textview1.setText(display_str);
                }
            }
            if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                int bond_state = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.BOND_NONE);
                if (bond_state == BluetoothDevice.BOND_NONE || bond_state == BluetoothDevice.BOND_BONDED) {
                    app_state = APP_STATE.SHOW;
                    selected_devices = -1;
                    if (btAdapter.isDiscovering())
                        btAdapter.cancelDiscovery();
                    show_paired_devices();
                }

            }

        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bt_erase);
        ok_button = (Button) findViewById(R.id.button_OK);
        System.out.println("App runnning!");
        listview_bt_devices = (ListView) findViewById(R.id.listView1);
        adapter_bt_devices = new ArrayAdapter<String>(this, R.layout.list_bt_item);
        listview_bt_devices.setAdapter(adapter_bt_devices);

        listview_bt_devices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int click_id, long l) {
                System.out.println("clicked " + click_id + " item");
                for (int i = 0; i < adapter_bt_devices.getCount(); i++) {
                    String item_text = adapter_bt_devices.getItem(i);
                    if (item_text.substring(item_text.length() - 4).equals(" (*)")) {
                        adapter_bt_devices.remove(item_text);
                        adapter_bt_devices.insert(item_text.substring(0, item_text.length() - 4), i);
                    }
                }
                String item_text = adapter_bt_devices.getItem(click_id);
                mac_addr = item_text.substring(item_text.length() - 17);
                adapter_bt_devices.remove(item_text);
                item_text = item_text + " (*)";
                adapter_bt_devices.insert(item_text, click_id);
                selected_devices = click_id;
            }
        });

        textview1 = (TextView) findViewById(R.id.textView1);
        // Getting the Bluetooth adapter
        btAdapter = BluetoothAdapter.getDefaultAdapter();

        show_paired_devices();
        app_state = APP_STATE.SHOW;

        // Register the BroadcastReceiver
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(mReceiver, filter); // Don't forget to unregister
        // during onDestroy
    }

    /**
     * Handle the tap event from the touchpad.
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // System.out.println("TEST!!!");
        // System.out.println(keyCode);
        switch (keyCode) {
            // Handle tap events.
            case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_ENTER:
                return true;
            default:
                return super.onKeyDown(keyCode, event);
        }
    }

    public void show_paired_devices() {
        adapter_bt_devices.clear();
        ok_button.setText("Unpair");
        if (btAdapter == null) {
            display_str = "Bluetooth NOT supported";
        } else {
            if (btAdapter.isEnabled()) {
                // Listing paired devices
                Set<BluetoothDevice> devices = btAdapter.getBondedDevices();
                display_str = "Showing " + devices.size() + " Paired Devices:";
                for (BluetoothDevice device : devices) {
                    adapter_bt_devices.add(device.getName() + ", " + device);
                }
            } else {
                display_str = display_str + "Bluetooth is not enabled...";
            }
        }
        textview1.setText(display_str);
    }

    public void scan_click(View view) {
        app_state = APP_STATE.SCAN;
        selected_devices = -1;
        ok_button.setText("Pair");
        if (btAdapter.isDiscovering())
            btAdapter.cancelDiscovery();
        System.out.println("Scan click");
        display_str = "Scan for devices...";
        textview1.setText(display_str);
        adapter_bt_devices.clear();

        found_bt_devices.clear();
        btAdapter.startDiscovery();
    }

    public void show_click(View view) {
        app_state = APP_STATE.SHOW;
        selected_devices = -1;
        System.out.println("Show click");
        if (btAdapter.isDiscovering())
            btAdapter.cancelDiscovery();
        show_paired_devices();
    }

    public void ok_click(View view) {
        if (selected_devices >= 0) {
            if (btAdapter.isDiscovering())
                btAdapter.cancelDiscovery();
            if (app_state == APP_STATE.SHOW) {
                Set<BluetoothDevice> devices = btAdapter.getBondedDevices();
                for (BluetoothDevice device : devices) {
                    if (mac_addr.equals(device.getAddress())) {
                        System.out.println("GOT");
                        try {
                            Method m = device.getClass().getMethod("removeBond", (Class[]) null);
                            m.invoke(device, (Object[]) null);
                        } catch (Exception e) {
                            // Log.e(TAG,e.getMessage());
                        }
                        break;
                    }
                    // System.out.println(device);
                    // adapter_bt_devices.add(device.getName() + ", " + device);
                }
                show_paired_devices();
                /*
                 *
                 */
            } else if (app_state == APP_STATE.SCAN) {
                try {
                    BluetoothDevice device = found_bt_devices.get(selected_devices);
                    Method m = device.getClass().getMethod("createBond", (Class[]) null);
                    m.invoke(device, (Object[]) null);
                } catch (Exception e) {
                }
            }
        } else {
            display_str = "!!!Select a device first!!!";
            textview1.setText(display_str);
        }

    }

    public enum APP_STATE {
        SCAN, SHOW
    }
}
