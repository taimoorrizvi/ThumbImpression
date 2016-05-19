package com.IDWORLD;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.util.Log;

import java.util.HashMap;

//import android.hardware.usb.UsbRequest;

public class HostUsb {

    private static final String TAG = "OpenHostUsb";
    private static final boolean D = true;

    //private static final String ACTION_USB_PERMISSION = "android.permission.USB_PERMISSION"
    private static final String ACTION_USB_PERMISSION = "com.CivilianCard.USB_PERMISSION";
    UsbEndpoint endpoint_IN;
    UsbEndpoint endpoint_OUT;
    UsbEndpoint endpoint_INT;
    UsbEndpoint curEndpoint;
    private Context context = null;
    private UsbManager mDevManager;
    private PendingIntent permissionIntent = null;
    private UsbInterface intf;
    private UsbDeviceConnection connection;
    private UsbDevice device;
    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if (device != null) {
                            if (D) Log.e(TAG, "Authorize permission " + device);
                        }
                    } else {
                        if (D) Log.e(TAG, "permission denied for device " + device);
                    }
                }
            }
        }
    };
    private String news = null;

    public HostUsb(Activity a) {
        AuthorizeDevice(a);
    }

    private boolean AuthorizeDevice(Context paramContext) {
        context = paramContext;
        mDevManager = ((UsbManager) context.getSystemService(Context.USB_SERVICE));
        permissionIntent = PendingIntent.getBroadcast(context, 0, new Intent(ACTION_USB_PERMISSION), 0);
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        context.registerReceiver(mUsbReceiver, filter);
        context.registerReceiver(mUsbReceiver, new IntentFilter(UsbManager.ACTION_USB_DEVICE_DETACHED));

        HashMap<String, UsbDevice> deviceList = mDevManager.getDeviceList();

        if (D) Log.e(TAG, "news:" + "mDevManager");
        for (UsbDevice device : deviceList.values()) {
            if (D) Log.e(TAG, "news:" + news);
            if (device.getVendorId() == 0x0483 && (device.getProductId() == 0x5720)) //
            {
                mDevManager.requestPermission(device, permissionIntent);
                return true;
            }
        }
        return false;
    }

    public int OpenDeviceInterfaces() {
        UsbDevice mDevice = device;
        Log.d(TAG, "setDevice " + mDevice);
        int fd = -1;

        if (mDevice == null) return -1;
        if (mDevice.getInterfaceCount() < 1) return -1;
        intf = mDevice.getInterface(0);

        // device should have one endpoint
        if (intf.getEndpointCount() == 0) return -1;

        for (int i = 0; i < intf.getEndpointCount(); i++) {
            if (intf.getEndpoint(i).getType() == UsbConstants.USB_ENDPOINT_XFER_BULK) {
                // 128
                if (intf.getEndpoint(i).getDirection() == UsbConstants.USB_DIR_IN) {
                    endpoint_IN = intf.getEndpoint(i);
                } // 0
                else if (intf.getEndpoint(i).getDirection() == UsbConstants.USB_DIR_OUT) {
                    endpoint_OUT = intf.getEndpoint(i);
                }
            } else if (intf.getEndpoint(i).getType() == UsbConstants.USB_ENDPOINT_XFER_INT) {
                endpoint_INT = intf.getEndpoint(i);
            } else {
                if (D) Log.e(TAG, "Not Endpoint or other Endpoint ");
            }
        }
        curEndpoint = intf.getEndpoint(0);

        connection = this.mDevManager.openDevice(mDevice);
        if ((connection != null)) {
            if (D) Log.e(TAG, "open connection success!");
            fd = connection.getFileDescriptor();
            return fd;

        } else {
            if (D) Log.e(TAG, "finger device open connection FAIL");
            return -1;
        }

    }

    public void CloseDeviceInterface() {
        if (connection != null) {
            connection.releaseInterface(intf);
            connection.close();
        }
    }

}


