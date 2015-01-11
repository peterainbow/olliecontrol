// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.orbotix.le;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import com.orbotix.common.DLog;
import com.orbotix.common.ResponseListener;
import com.orbotix.common.Robot;
import com.orbotix.common.internal.AdHocCommand;
import com.orbotix.common.internal.AsyncMessage;
import com.orbotix.common.internal.DeviceCommand;
import com.orbotix.common.internal.DeviceResponse;
import com.orbotix.common.internal.MainProcessorSession;
import com.orbotix.common.internal.MainProcessorState;
import com.orbotix.common.stat.StatForPacketFactory;
import com.orbotix.common.stat.StatRecorder;
import com.orbotix.utilities.binary.ByteUtil;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// Referenced classes of package com.orbotix.le:
//            LEConnectionState, LeRadioInfo, LeLinkDelegate, DiscoveryAgentLE, 
//            RobotLE

public class LeLink extends BluetoothGattCallback
    implements ResponseListener
{


handle: 0x0001, uuid: 00002800-0000-1000-8000-00805f9b34fb
handle: 0x0002, uuid: 00002803-0000-1000-8000-00805f9b34fb
handle: 0x0003, uuid: 00002a00-0000-1000-8000-00805f9b34fb
handle: 0x0004, uuid: 00002803-0000-1000-8000-00805f9b34fb
handle: 0x0005, uuid: 00002a01-0000-1000-8000-00805f9b34fb
handle: 0x0006, uuid: 00002803-0000-1000-8000-00805f9b34fb
handle: 0x0007, uuid: 00002a04-0000-1000-8000-00805f9b34fb
handle: 0x0008, uuid: 00002800-0000-1000-8000-00805f9b34fb
handle: 0x0009, uuid: 00002803-0000-1000-8000-00805f9b34fb
handle: 0x000a, uuid: 00002a05-0000-1000-8000-00805f9b34fb
handle: 0x000b, uuid: 00002902-0000-1000-8000-00805f9b34fb
handle: 0x000c, uuid: 00002800-0000-1000-8000-00805f9b34fb
handle: 0x000d, uuid: 00002803-0000-1000-8000-00805f9b34fb
handle: 0x000e, uuid: 22bb746f-2ba1-7554-2d6f-726568705327
handle: 0x000f, uuid: 00002803-0000-1000-8000-00805f9b34fb
handle: 0x0010, uuid: 22bb746f-2ba6-7554-2d6f-726568705327
handle: 0x0011, uuid: 00002902-0000-1000-8000-00805f9b34fb
handle: 0x0012, uuid: 00002800-0000-1000-8000-00805f9b34fb
handle: 0x0013, uuid: 00002803-0000-1000-8000-00805f9b34fb
handle: 0x0014, uuid: 22bb746f-2bb1-7554-2d6f-726568705327
handle: 0x0015, uuid: 00002904-0000-1000-8000-00805f9b34fb
handle: 0x0016, uuid: 00002803-0000-1000-8000-00805f9b34fb
handle: 0x0017, uuid: 22bb746f-2bb2-7554-2d6f-726568705327
handle: 0x0018, uuid: 00002904-0000-1000-8000-00805f9b34fb
handle: 0x0019, uuid: 00002803-0000-1000-8000-00805f9b34fb
handle: 0x001a, uuid: 22bb746f-2bb3-7554-2d6f-726568705327
handle: 0x001b, uuid: 00002904-0000-1000-8000-00805f9b34fb
handle: 0x001c, uuid: 00002803-0000-1000-8000-00805f9b34fb
handle: 0x001d, uuid: 22bb746f-2bb6-7554-2d6f-726568705327
handle: 0x001e, uuid: 00002904-0000-1000-8000-00805f9b34fb
handle: 0x001f, uuid: 00002902-0000-1000-8000-00805f9b34fb
handle: 0x0020, uuid: 00002803-0000-1000-8000-00805f9b34fb
handle: 0x0021, uuid: 22bb746f-2bb7-7554-2d6f-726568705327
handle: 0x0022, uuid: 00002904-0000-1000-8000-00805f9b34fb
handle: 0x0023, uuid: 00002803-0000-1000-8000-00805f9b34fb
handle: 0x0024, uuid: 22bb746f-2bb8-7554-2d6f-726568705327
handle: 0x0025, uuid: 00002904-0000-1000-8000-00805f9b34fb
handle: 0x0026, uuid: 00002803-0000-1000-8000-00805f9b34fb
handle: 0x0027, uuid: 22bb746f-2bb9-7554-2d6f-726568705327
handle: 0x0028, uuid: 00002904-0000-1000-8000-00805f9b34fb
handle: 0x0029, uuid: 00002803-0000-1000-8000-00805f9b34fb
handle: 0x002a, uuid: 22bb746f-2bba-7554-2d6f-726568705327
handle: 0x002b, uuid: 00002904-0000-1000-8000-00805f9b34fb
handle: 0x002c, uuid: 00002803-0000-1000-8000-00805f9b34fb
handle: 0x002d, uuid: 22bb746f-2bbd-7554-2d6f-726568705327
handle: 0x002e, uuid: 00002803-0000-1000-8000-00805f9b34fb
handle: 0x002f, uuid: 22bb746f-2bbe-7554-2d6f-726568705327
handle: 0x0030, uuid: 00002904-0000-1000-8000-00805f9b34fb
handle: 0x0031, uuid: 00002803-0000-1000-8000-00805f9b34fb
handle: 0x0032, uuid: 22bb746f-2bbf-7554-2d6f-726568705327
handle: 0x0033, uuid: 00002904-0000-1000-8000-00805f9b34fb
handle: 0x0034, uuid: 00002800-0000-1000-8000-00805f9b34fb
handle: 0x0035, uuid: 00002803-0000-1000-8000-00805f9b34fb
handle: 0x0036, uuid: 22bb746f-2bc1-7554-2d6f-726568705327
handle: 0x0037, uuid: 00002901-0000-1000-8000-00805f9b34fb
handle: 0x0038, uuid: 00002904-0000-1000-8000-00805f9b34fb
handle: 0x0039, uuid: 00002902-0000-1000-8000-00805f9b34fb
handle: 0x003a, uuid: 00002803-0000-1000-8000-00805f9b34fb
handle: 0x003b, uuid: 22bb746f-2bc2-7554-2d6f-726568705327
handle: 0x003c, uuid: 00002904-0000-1000-8000-00805f9b34fb
handle: 0x003d, uuid: 00002803-0000-1000-8000-00805f9b34fb
handle: 0x003e, uuid: 22bb746f-2bc3-7554-2d6f-726568705327
handle: 0x003f, uuid: 00002904-0000-1000-8000-00805f9b34fb
handle: 0x0040, uuid: 00002800-0000-1000-8000-00805f9b34fb
handle: 0x0041, uuid: 00002803-0000-1000-8000-00805f9b34fb
handle: 0x0042, uuid: 00001013-d102-11e1-9b23-00025b00a5a5
handle: 0x0043, uuid: 00002803-0000-1000-8000-00805f9b34fb
handle: 0x0044, uuid: 00001017-d102-11e1-9b23-00025b00a5a5
handle: 0x0045, uuid: 00002803-0000-1000-8000-00805f9b34fb
handle: 0x0046, uuid: 00001014-d102-11e1-9b23-00025b00a5a5
handle: 0x0047, uuid: 00002902-0000-1000-8000-00805f9b34fb
handle: 0x0048, uuid: 00002800-0000-1000-8000-00805f9b34fb
handle: 0x0049, uuid: 00002803-0000-1000-8000-00805f9b34fb
handle: 0x004a, uuid: 00002a27-0000-1000-8000-00805f9b34fb
handle: 0x004b, uuid: 00002803-0000-1000-8000-00805f9b34fb
handle: 0x004c, uuid: 00002a25-0000-1000-8000-00805f9b34fb
handle: 0x004d, uuid: 00002904-0000-1000-8000-00805f9b34fb
handle: 0x004e, uuid: 00002803-0000-1000-8000-00805f9b34fb
handle: 0x004f, uuid: 00002a24-0000-1000-8000-00805f9b34fb
handle: 0x0050, uuid: 00002803-0000-1000-8000-00805f9b34fb
handle: 0x0051, uuid: 00002a29-0000-1000-8000-00805f9b34fb
handle: 0x0052, uuid: 00002803-0000-1000-8000-00805f9b34fb
handle: 0x0053, uuid: 00002a26-0000-1000-8000-00805f9b34fb
handle: 0x0054, uuid: 00002904-0000-1000-8000-00805f9b34fb
handle: 0x0055, uuid: 00002803-0000-1000-8000-00805f9b34fb
handle: 0x0056, uuid: 00002a50-0000-1000-8000-00805f9b34fb
handle: 0x0057, uuid: 00002800-0000-1000-8000-00805f9b34fb
handle: 0x0058, uuid: 00002803-0000-1000-8000-00805f9b34fb
handle: 0x0059, uuid: 00002a19-0000-1000-8000-00805f9b34fb
handle: 0x005a, uuid: 00002902-0000-1000-8000-00805f9b34fb

handle: 0x0002, char properties: 0x0a, char value handle: 0x0003, uuid: 00002a00-0000-1000-8000-00805f9b34fb
handle: 0x0004, char properties: 0x02, char value handle: 0x0005, uuid: 00002a01-0000-1000-8000-00805f9b34fb
handle: 0x0006, char properties: 0x02, char value handle: 0x0007, uuid: 00002a04-0000-1000-8000-00805f9b34fb
handle: 0x0009, char properties: 0x22, char value handle: 0x000a, uuid: 00002a05-0000-1000-8000-00805f9b34fb
handle: 0x000d, char properties: 0x0c, char value handle: 0x000e, uuid: 22bb746f-2ba1-7554-2d6f-726568705327
handle: 0x000f, char properties: 0x10, char value handle: 0x0010, uuid: 22bb746f-2ba6-7554-2d6f-726568705327
handle: 0x0013, char properties: 0x0a, char value handle: 0x0014, uuid: 22bb746f-2bb1-7554-2d6f-726568705327
handle: 0x0016, char properties: 0x08, char value handle: 0x0017, uuid: 22bb746f-2bb2-7554-2d6f-726568705327
handle: 0x0019, char properties: 0x08, char value handle: 0x001a, uuid: 22bb746f-2bb3-7554-2d6f-726568705327
handle: 0x001c, char properties: 0x10, char value handle: 0x001d, uuid: 22bb746f-2bb6-7554-2d6f-726568705327
handle: 0x0020, char properties: 0x0e, char value handle: 0x0021, uuid: 22bb746f-2bb7-7554-2d6f-726568705327
handle: 0x0023, char properties: 0x02, char value handle: 0x0024, uuid: 22bb746f-2bb8-7554-2d6f-726568705327
handle: 0x0026, char properties: 0x02, char value handle: 0x0027, uuid: 22bb746f-2bb9-7554-2d6f-726568705327
handle: 0x0029, char properties: 0x02, char value handle: 0x002a, uuid: 22bb746f-2bba-7554-2d6f-726568705327
handle: 0x002c, char properties: 0x0c, char value handle: 0x002d, uuid: 22bb746f-2bbd-7554-2d6f-726568705327
handle: 0x002e, char properties: 0x0a, char value handle: 0x002f, uuid: 22bb746f-2bbe-7554-2d6f-726568705327
handle: 0x0031, char properties: 0x0e, char value handle: 0x0032, uuid: 22bb746f-2bbf-7554-2d6f-726568705327
handle: 0x0035, char properties: 0x10, char value handle: 0x0036, uuid: 22bb746f-2bc1-7554-2d6f-726568705327
handle: 0x003a, char properties: 0x0a, char value handle: 0x003b, uuid: 22bb746f-2bc2-7554-2d6f-726568705327
handle: 0x003d, char properties: 0x0a, char value handle: 0x003e, uuid: 22bb746f-2bc3-7554-2d6f-726568705327
handle: 0x0041, char properties: 0x0a, char value handle: 0x0042, uuid: 00001013-d102-11e1-9b23-00025b00a5a5
handle: 0x0043, char properties: 0x08, char value handle: 0x0044, uuid: 00001017-d102-11e1-9b23-00025b00a5a5
handle: 0x0045, char properties: 0x12, char value handle: 0x0046, uuid: 00001014-d102-11e1-9b23-00025b00a5a5
handle: 0x0049, char properties: 0x02, char value handle: 0x004a, uuid: 00002a27-0000-1000-8000-00805f9b34fb
handle: 0x004b, char properties: 0x02, char value handle: 0x004c, uuid: 00002a25-0000-1000-8000-00805f9b34fb
handle: 0x004e, char properties: 0x02, char value handle: 0x004f, uuid: 00002a24-0000-1000-8000-00805f9b34fb
handle: 0x0050, char properties: 0x02, char value handle: 0x0051, uuid: 00002a29-0000-1000-8000-00805f9b34fb
handle: 0x0052, char properties: 0x02, char value handle: 0x0053, uuid: 00002a26-0000-1000-8000-00805f9b34fb
handle: 0x0055, char properties: 0x02, char value handle: 0x0056, uuid: 00002a50-0000-1000-8000-00805f9b34fb
handle: 0x0058, char properties: 0x12, char value handle: 0x0059, uuid: 00002a19-0000-1000-8000-00805f9b34fb

attr handle: 0x0001, end grp handle: 0x0007 uuid: 00001800-0000-1000-8000-00805F9B34FB
attr handle: 0x0008, end grp handle: 0x000b uuid: 00001801-0000-1000-8000-00805F9B34FB
attr handle: 0x000c, end grp handle: 0x0011 uuid: 22bb746f-2ba0-7554-2d6f-726568705327
attr handle: 0x0012, end grp handle: 0x0033 uuid: 22bb746f-2bb0-7554-2d6f-726568705327
attr handle: 0x0034, end grp handle: 0x003f uuid: 22bb746f-2bc0-7554-2d6f-726568705327
attr handle: 0x0040, end grp handle: 0x0047 uuid: 00001016-d102-11e1-9b23-00025b00a5a5
attr handle: 0x0048, end grp handle: 0x0056 uuid: 0000180a-0000-1000-8000-00805f9b34fb
attr handle: 0x0057, end grp handle: 0xffff uuid: 0000180f-0000-1000-8000-00805f9b34fb

    static final boolean $assertionsDisabled = false;
    private static final int BLE_MAX_PACKET_SIZE = 20;
    private static final UUID UUID_AntiDOSCharacteristic = UUID.fromString("22bb746f-2bbd-7554-2D6F-726568705327");
    private static final UUID UUID_AntiDOSTimeoutCharacteristic = UUID.fromString("22bb746f-2bbe-7554-2D6F-726568705327");
    private static final UUID UUID_BleRadioService = UUID.fromString("22bb746f-2bb0-7554-2D6F-726568705327");
    private static final UUID UUID_CommandCharacteristic = UUID.fromString("22bb746F-2ba1-7554-2D6F-726568705327");
    private static final UUID UUID_ConnectionIntervalCharacterisic = UUID.fromString("22BB746F-2BB9-7554-2D6F-726568705327");
    private static final UUID UUID_CrystalTrimCharacteristic = UUID.fromString("22BB746F-2BB8-7554-2D6F-726568705327");
    private static final UUID UUID_DeepSleepCharacteristic = UUID.fromString("22BB746F-2BB7-7554-2D6F-726568705327");
    private static final UUID UUID_DeviceInfoService = UUID.fromString("0000180a-0000-1000-8000-00805f9b34fb");
    private static final UUID UUID_EnableAdvertiseCharacteristic = UUID.fromString("22BB746F-2BB3-7554-2D6F-726568705327");
    private static final UUID UUID_EnableHandshakeCharacteristic = UUID.fromString("22BB746F-2BB1-7554-2D6F-726568705327");
    private static final UUID UUID_RSSINotifyCharacteristic = UUID.fromString("22BB746F-2BB6-7554-2D6F-726568705327");
    private static final UUID UUID_ResponseCharacteristic = UUID.fromString("22bb746F-2ba6-7554-2D6F-726568705327");
    private static final UUID UUID_RobotControlService = UUID.fromString("22bb746f-2ba0-7554-2d6f-726568705327");
    private static final UUID UUID_SISModelNumberCharacteristic = UUID.fromString("00002A24-0000-1000-8000-00805f9b34fb");
    private static final UUID UUID_SISRadioFirmwareVersion = UUID.fromString("00002A26-0000-1000-8000-00805f9b34fb");
    private static final UUID UUID_SerialNumCharacteristic = UUID.fromString("00002A25-0000-1000-8000-00805f9b34fb");
    private static final UUID UUID_TxPowerCharacteristic = UUID.fromString("22bb746f-2bb2-7554-2D6F-726568705327");
    private static final UUID UUID_WakeCharacteristic = UUID.fromString("22bb746f-2bbf-7554-2D6F-726568705327");
    private Integer _RSSI;
    private BluetoothGattCharacteristic _antiDosCharacteristic;
    private boolean _commandLock;
    private BluetoothGattCharacteristic _controlCharacteristic;
    private BluetoothGattCharacteristic _deepSleepCharacteristic;
    private BluetoothGattCharacteristic _dosTimeoutInSecondsCharacteristic;
    private Long _driveAckLatency;
    private int _driveMode;
    private BluetoothGatt _gatt;
    private byte _lastPacket[];
    private Date _latencyCommandStart;
    private List _latencyList;
    private LeLinkDelegate _linkDelegate;
    BluetoothGattService _mpService;
    private MainProcessorState _mpState;
    private final Vector _packetChunkQueue = new Vector();
    private BluetoothDevice _peripheral;
    private int _prevRssi;
    private LeRadioInfo _radioInfo;
    private BluetoothGattService _radioService;
    private BluetoothGattCharacteristic _responseCharacteristic;
    private LEConnectionState _rfState;
    private double _rssiUpdateDelay;
    private MainProcessorSession _session;
    private ResponseListener _sessionDelegate;
    private BluetoothGattCharacteristic _txPowerCharacteristic;
    private BluetoothGattCharacteristic _wakeCharacteristic;
    private boolean _writingPackets;
    private ExecutorService sendThread;

    public LeLink(BluetoothDevice bluetoothdevice, RobotLE robotle)
    {
        _latencyCommandStart = new Date();
        _mpState = MainProcessorState.Offline;
        _rfState = LEConnectionState.Offline;
        _RSSI = Integer.valueOf(-98);
        _radioInfo = new LeRadioInfo();
        sendThread = Executors.newSingleThreadExecutor();
        _writingPackets = false;
        _peripheral = bluetoothdevice;
        _sessionDelegate = robotle;
        _linkDelegate = robotle;
        _latencyList = new LinkedList();
        _driveAckLatency = Long.valueOf(0L);
        _rfState = LEConnectionState.Offline;
        _mpState = MainProcessorState.Offline;
        _driveMode = 0;
        _commandLock = false;
        _rssiUpdateDelay = 0.29999999999999999D;
        Object aobj[] = new Object[1];
        aobj[0] = bluetoothdevice.getAddress();
        DLog.i(String.format("Creating LeLink for %s", aobj));
    }

    private void StartWritingPackets()
    {
        if (_writingPackets)
        {
            DLog.e("StartWritingPackets() called while already writing packets!");
            return;
        } else
        {
            _writingPackets = true;
            byte abyte0[] = objectByteArrayToNativeByteArray((Byte[])((Vector)_packetChunkQueue.firstElement()).firstElement());
            write(_controlCharacteristic, 0, abyte0);
            return;
        }
    }

    private void executeAntiDos()
    {
        _antiDosCharacteristic.setValue("011i3");
        _antiDosCharacteristic.setWriteType(1);
        sendThread.execute(new Runnable() {

            final LeLink this$0;

            public void run()
            {
                DLog.v("Write save my ollie characteristic");
                _gatt.writeCharacteristic(_antiDosCharacteristic);
            }

            
            {
                this$0 = LeLink.this;
                super();
            }
        });
    }

    private void executeJumpToMain()
    {
        _mpState = MainProcessorState.PoweredOn;
        sendThread.execute(new Runnable() {

            final LeLink this$0;

            public void run()
            {
                try
                {
                    DLog.v((new StringBuilder()).append("Main Processor ON ").append(_sessionDelegate.toString()).toString());
                    Thread.sleep(100L);
                }
                catch (InterruptedException interruptedexception)
                {
                    interruptedexception.printStackTrace();
                }
                sendCommand(new AdHocCommand(1, 4));
            }

            
            {
                this$0 = LeLink.this;
                super();
            }
        });
    }

    private void executeResponseSetup()
    {
        sendThread.execute(new Runnable() {

            final LeLink this$0;

            public void run()
            {
                DLog.v("Enable Notify on Response Characteristic");
                if (_gatt.setCharacteristicNotification(_responseCharacteristic, true))
                {
                    readDeviceInfoCharacteristics(_gatt);
                }
            }

            
            {
                this$0 = LeLink.this;
                super();
            }
        });
    }

    private void executeTxPower(final int pow)
    {
        sendThread.execute(new Runnable() {

            final LeLink this$0;
            final int val$pow;

            public void run()
            {
                DLog.v("Found tx power characteristic");
                writeRobotTxPower((short)pow);
            }

            
            {
                this$0 = LeLink.this;
                pow = i;
                super();
            }
        });
    }

    private void executeWake()
    {
        byte abyte0[] = {
            1
        };
        _wakeCharacteristic.setValue(abyte0);
        _wakeCharacteristic.setWriteType(1);
        _mpState = MainProcessorState.PowerOnRequested;
        sendThread.execute(new Runnable() {

            final LeLink this$0;

            public void run()
            {
                DLog.v((new StringBuilder()).append("Sending Wake Main Processor ").append(_sessionDelegate.toString()).toString());
                _gatt.writeCharacteristic(_wakeCharacteristic);
            }

            
            {
                this$0 = LeLink.this;
                super();
            }
        });
    }

    public static UUID[] getRequiredUUIDS()
    {
        return null;
    }

    private Byte[] nativeByteArrayToObjectByteArray(byte abyte0[])
    {
        Byte abyte[] = new Byte[abyte0.length];
        for (int i = 0; i < abyte.length; i++)
        {
            abyte[i] = Byte.valueOf(abyte0[i]);
        }

        return abyte;
    }

    private byte[] objectByteArrayToNativeByteArray(Byte abyte[])
    {
        byte abyte0[] = new byte[abyte.length];
        for (int i = 0; i < abyte0.length; i++)
        {
            abyte0[i] = abyte[i].byteValue();
        }

        return abyte0;
    }

    private void readDeviceInfoCharacteristics(BluetoothGatt bluetoothgatt)
    {
        bluetoothgatt.readCharacteristic(bluetoothgatt.getService(UUID_DeviceInfoService).getCharacteristic(UUID_SISModelNumberCharacteristic));
    }

    private void sendRaw(byte abyte0[], boolean flag)
    {
        this;
        JVM INSTR monitorenter ;
        Vector vector = new Vector();
        int i = 0;
_L2:
        if (i >= abyte0.length)
        {
            break; /* Loop/switch isn't completed */
        }
        int j = 20;
        if (i + j > abyte0.length)
        {
            j = abyte0.length - i;
        }
        vector.add(nativeByteArrayToObjectByteArray(Arrays.copyOfRange(abyte0, i, i + j)));
        i += 20;
        if (true) goto _L2; else goto _L1
_L1:
        if (vector.size() != 0) goto _L4; else goto _L3
_L3:
        DLog.w((new StringBuilder()).append("No chunks generated for packet: ").append(ByteUtil.byteArrayToHex(abyte0)).toString());
_L6:
        this;
        JVM INSTR monitorexit ;
        return;
_L4:
        synchronized (_packetChunkQueue)
        {
            _packetChunkQueue.add(vector);
            if (_packetChunkQueue.size() == 1)
            {
                StartWritingPackets();
            }
        }
        if (true) goto _L6; else goto _L5
_L5:
        exception1;
        vector1;
        JVM INSTR monitorexit ;
        throw exception1;
        Exception exception;
        exception;
        this;
        JVM INSTR monitorexit ;
        throw exception;
    }

    private void write(final BluetoothGattCharacteristic btc, int i, final byte data[])
    {
        sendThread.execute(new Runnable() {

            final LeLink this$0;
            final BluetoothGattCharacteristic val$btc;
            final byte val$data[];

            public void run()
            {
                if (_gatt == null || btc == null)
                {
                    DLog.w((new StringBuilder()).append(_rfState).append(" ?  skipping write ").toString());
                } else
                {
                    btc.setWriteType(1);
                    DLog.v((new StringBuilder()).append(_peripheral.getName()).append(" write: ").append(ByteUtil.byteArrayToHex(data)).toString());
                    btc.setValue(data);
                    if (!_gatt.writeCharacteristic(btc))
                    {
                        DLog.e((new StringBuilder()).append("--------   WRITE FAILED FOR command ").append(ByteUtil.byteArrayToHex(data)).toString());
                        return;
                    }
                }
            }

            
            {
                this$0 = LeLink.this;
                btc = bluetoothgattcharacteristic;
                data = abyte0;
                super();
            }
        });
    }

    protected void close()
    {
        writeRobotTxPower((short)2);
        _linkDelegate.didDisconnect();
        _rfState = LEConnectionState.Offline;
        if (_gatt != null)
        {
            DLog.i("gatt.disconnect() requested");
            _gatt.disconnect();
            _gatt.close();
            _gatt = null;
        }
    }

    public Long getAckLatency()
    {
        return _driveAckLatency;
    }

    public String getAddress()
    {
        return _peripheral.getAddress();
    }

    public String getName()
    {
        return _peripheral.getName();
    }

    BluetoothDevice getPeripheral()
    {
        return _peripheral;
    }

    protected Integer getRSSI()
    {
        return _RSSI;
    }

    public LeRadioInfo getRadioInfo()
    {
        return _radioInfo;
    }

    public void handleAsyncMessage(final AsyncMessage asyncMessage, Robot robot)
    {
        if (_mpState.getValue() >= MainProcessorState.InBootloader.getValue() && _mpState.getValue() <= MainProcessorState.InMainApp.getValue())
        {
            ((Activity)DiscoveryAgentLE.getInstance().getContext()).runOnUiThread(new Runnable() {

                final LeLink this$0;
                final AsyncMessage val$asyncMessage;

                public void run()
                {
                    _sessionDelegate.handleAsyncMessage(asyncMessage, (Robot)_sessionDelegate);
                }

            
            {
                this$0 = LeLink.this;
                asyncMessage = asyncmessage;
                super();
            }
            });
        }
    }

    public void handleResponse(final DeviceResponse response, Robot robot)
    {
        DLog.v((new StringBuilder()).append(_sessionDelegate).append(" handleResponse ").append(response).toString());
        if (1 == response.getDeviceId() && 4 == response.getCommandId())
        {
            DLog.v("Main App responding");
            _mpState = MainProcessorState.InMainApp;
            _linkDelegate.didConnect();
        }
        ((Activity)DiscoveryAgentLE.getInstance().getContext()).runOnUiThread(new Runnable() {

            final LeLink this$0;
            final DeviceResponse val$response;

            public void run()
            {
                _sessionDelegate.handleResponse(response, (Robot)_sessionDelegate);
            }

            
            {
                this$0 = LeLink.this;
                response = deviceresponse;
                super();
            }
        });
    }

    public void handleStringResponse(final String stringResponse, Robot robot)
    {
        ((Activity)DiscoveryAgentLE.getInstance().getContext()).runOnUiThread(new Runnable() {

            final LeLink this$0;
            final String val$stringResponse;

            public void run()
            {
                _sessionDelegate.handleStringResponse(stringResponse, (Robot)_sessionDelegate);
            }

            
            {
                this$0 = LeLink.this;
                stringResponse = s;
                super();
            }
        });
    }

    public boolean isConnected()
    {
        return _rfState.ordinal() >= LEConnectionState.Connected.ordinal() && _mpState == MainProcessorState.PoweredOn;
    }

    public boolean isConnecting()
    {
        return _rfState == LEConnectionState.Connecting;
    }

    public boolean isReadyForCommand()
    {
        return !_commandLock;
    }

    public void onCharacteristicChanged(BluetoothGatt bluetoothgatt, BluetoothGattCharacteristic bluetoothgattcharacteristic)
    {
        byte abyte0[];
        super.onCharacteristicChanged(bluetoothgatt, bluetoothgattcharacteristic);
        if (!_responseCharacteristic.equals(bluetoothgattcharacteristic))
        {
            break MISSING_BLOCK_LABEL_125;
        }
        abyte0 = bluetoothgattcharacteristic.getValue();
        DLog.v((new StringBuilder()).append(Thread.currentThread().getName()).append(" says     ").append(ByteUtil.byteArrayToHex(abyte0)).toString());
        if (abyte0.length > 3 && abyte0[1] == -2 && abyte0[2] == 20 && _mpState == MainProcessorState.OfflineRequested)
        {
            DLog.v("Found Sleep asyncMessage - disconnect");
            ((RobotLE)_sessionDelegate).disconnect();
        }
        this;
        JVM INSTR monitorenter ;
        _session.processRawData(abyte0);
        this;
        JVM INSTR monitorexit ;
        return;
        Exception exception;
        exception;
        this;
        JVM INSTR monitorexit ;
        throw exception;
    }

    public void onCharacteristicRead(BluetoothGatt bluetoothgatt, BluetoothGattCharacteristic bluetoothgattcharacteristic, int i)
    {
        super.onCharacteristicRead(bluetoothgatt, bluetoothgattcharacteristic, i);
        if (bluetoothgattcharacteristic.getUuid().equals(UUID_SISModelNumberCharacteristic))
        {
            _radioInfo.setModelNumber(bluetoothgattcharacteristic.getStringValue(0));
            bluetoothgatt.readCharacteristic(bluetoothgattcharacteristic.getService().getCharacteristic(UUID_SISRadioFirmwareVersion));
            return;
        }
        if (bluetoothgattcharacteristic.getUuid().equals(UUID_SISRadioFirmwareVersion))
        {
            _radioInfo.setFirmwareRevision(bluetoothgattcharacteristic.getStringValue(0));
            bluetoothgatt.readCharacteristic(bluetoothgattcharacteristic.getService().getCharacteristic(UUID_SerialNumCharacteristic));
            return;
        }
        if (bluetoothgattcharacteristic.getUuid().equals(UUID_SerialNumCharacteristic))
        {
            _radioInfo.setSerialNumber(bluetoothgattcharacteristic.getStringValue(0));
            DLog.v("Read all aux characteristics, jumping to main");
            executeJumpToMain();
            return;
        } else
        {
            DLog.w((new StringBuilder()).append("Unhandled characteristic read: ").append(bluetoothgattcharacteristic.getUuid().toString()).toString());
            return;
        }
    }

    public void onCharacteristicWrite(BluetoothGatt bluetoothgatt, BluetoothGattCharacteristic bluetoothgattcharacteristic, int i)
    {
        super.onCharacteristicWrite(bluetoothgatt, bluetoothgattcharacteristic, i);
        if (!_controlCharacteristic.equals(bluetoothgattcharacteristic)) goto _L2; else goto _L1
_L1:
        Vector vector = _packetChunkQueue;
        vector;
        JVM INSTR monitorenter ;
        Vector vector1;
        vector1 = (Vector)_packetChunkQueue.firstElement();
        vector1.removeElementAt(0);
        if (vector1.size() != 0)
        {
            break MISSING_BLOCK_LABEL_245;
        }
        _linkDelegate.didRadioACK();
        _packetChunkQueue.removeElementAt(0);
        if (_packetChunkQueue.size() != 0) goto _L4; else goto _L3
_L3:
        _writingPackets = false;
_L6:
        this;
        JVM INSTR monitorenter ;
        _commandLock = false;
        if (_latencyList.size() > 20)
        {
            _latencyList.remove(0);
        }
        Long long1 = Long.valueOf((System.currentTimeMillis() - _latencyCommandStart.getTime()) / 2L);
        _latencyList.add(long1);
        long l = 0L;
        Iterator iterator = _latencyList.iterator();
_L5:
        long l1;
        if (!iterator.hasNext())
        {
            break MISSING_BLOCK_LABEL_273;
        }
        l1 = ((Long)iterator.next()).longValue();
        l += l1;
          goto _L5
_L4:
        byte abyte1[] = objectByteArrayToNativeByteArray((Byte[])((Vector)_packetChunkQueue.firstElement()).firstElement());
        write(_controlCharacteristic, 0, abyte1);
          goto _L6
        Exception exception;
        exception;
        vector;
        JVM INSTR monitorexit ;
        throw exception;
        byte abyte0[] = objectByteArrayToNativeByteArray((Byte[])vector1.firstElement());
        write(_controlCharacteristic, 0, abyte0);
          goto _L6
        _driveAckLatency = Long.valueOf(l / (long)_latencyList.size());
        this;
        JVM INSTR monitorexit ;
_L2:
        if (_wakeCharacteristic.equals(bluetoothgattcharacteristic))
        {
            executeResponseSetup();
        }
        if (_txPowerCharacteristic.equals(bluetoothgattcharacteristic))
        {
            DLog.v("TX Power Set");
            executeWake();
        }
        if (_antiDosCharacteristic.equals(bluetoothgattcharacteristic))
        {
            DLog.v("DOS ACK OK");
            executeTxPower(7);
        }
        return;
        Exception exception1;
        exception1;
        this;
        JVM INSTR monitorexit ;
        throw exception1;
    }

    public void onConnectionStateChange(BluetoothGatt bluetoothgatt, int i, int j)
    {
        DLog.v((new StringBuilder()).append("onConnectStateChange ").append(i).append(" ").append(j).toString());
        if (j == 2)
        {
            if (_rfState != LEConnectionState.Connected)
            {
                _rfState = LEConnectionState.Connected;
                _gatt = bluetoothgatt;
                _session = new MainProcessorSession(DiscoveryAgentLE.getInstance().getContext(), this);
                _gatt.discoverServices();
            } else
            {
                DLog.v("Skipping connection notification because this link is already connected");
            }
        }
        if (j == 0 && _rfState == LEConnectionState.Connected)
        {
            _rfState = LEConnectionState.Connecting;
            DLog.v((new StringBuilder()).append("Connection Dropped - reconnecting ").append(_linkDelegate).toString());
            _linkDelegate.didDisconnect();
            DiscoveryAgentLE.getInstance().internalConnect((RobotLE)_linkDelegate, Boolean.valueOf(true));
        }
    }

    public void onServicesDiscovered(BluetoothGatt bluetoothgatt, int i)
    {
        if (!_gatt.equals(bluetoothgatt))
        {
            DLog.e("Mismatch GATT - see vves");
            return;
        }
        Object aobj[] = new Object[2];
        aobj[0] = Integer.valueOf(bluetoothgatt.getServices().size());
        aobj[1] = Integer.valueOf(i);
        DLog.v(String.format("onServicesDiscovered count=%d status=%d", aobj));
        _radioService = bluetoothgatt.getService(UUID_BleRadioService);
        if (!$assertionsDisabled && _radioService == null)
        {
            throw new AssertionError();
        }
        _txPowerCharacteristic = _radioService.getCharacteristic(UUID_TxPowerCharacteristic);
        _antiDosCharacteristic = _radioService.getCharacteristic(UUID_AntiDOSCharacteristic);
        _mpService = bluetoothgatt.getService(UUID_RobotControlService);
        if (!$assertionsDisabled && _mpService == null)
        {
            throw new AssertionError();
        } else
        {
            _responseCharacteristic = _mpService.getCharacteristic(UUID_ResponseCharacteristic);
            _wakeCharacteristic = _radioService.getCharacteristic(UUID_WakeCharacteristic);
            _controlCharacteristic = _mpService.getCharacteristic(UUID_CommandCharacteristic);
            executeAntiDos();
            return;
        }
    }

    public void sendCommand(DeviceCommand devicecommand)
    {
        sendCommand(devicecommand, false);
    }

    public void sendCommand(DeviceCommand devicecommand, boolean flag)
    {
        byte abyte0[];
        byte byte0;
        abyte0 = _session.packetForCommand(devicecommand);
        byte0 = devicecommand.getCommandId();
        byte byte1 = devicecommand.getDeviceId();
        this;
        JVM INSTR monitorenter ;
        if (byte1 != 2)
        {
            break MISSING_BLOCK_LABEL_167;
        }
        if (byte0 != 48) goto _L2; else goto _L1
_L1:
        if (abyte0[9] != 0) goto _L4; else goto _L3
_L3:
        DLog.v((new StringBuilder()).append("Forcing Stop command ").append(ByteUtil.byteArrayToHex(devicecommand.getPacket())).toString());
_L2:
        if (byte0 != 32)
        {
            break MISSING_BLOCK_LABEL_151;
        }
        if (!_commandLock)
        {
            break MISSING_BLOCK_LABEL_151;
        }
        this;
        JVM INSTR monitorexit ;
        return;
_L4:
        if (!_commandLock) goto _L2; else goto _L5
_L5:
        DLog.v((new StringBuilder()).append("---------------- Lock skip for ").append(devicecommand.toString()).toString());
        if (devicecommand.isResponseRequested())
        {
            _session.popCommandForSequence(devicecommand.getSequenceNumber());
        }
        this;
        JVM INSTR monitorexit ;
        return;
        Exception exception;
        exception;
        this;
        JVM INSTR monitorexit ;
        throw exception;
        _commandLock = true;
        _latencyCommandStart = new Date();
        this;
        JVM INSTR monitorexit ;
        com.orbotix.common.stat.Stat stat = StatForPacketFactory.statForPacketAndIdentifier(abyte0, _radioInfo.getSerialNumber());
        if (stat != null)
        {
            StatRecorder.getInstance().recordStat(stat);
        }
        _lastPacket = abyte0;
        sendRaw(abyte0, flag);
        if (byte1 == 0 && byte0 == 48)
        {
            _mpState = MainProcessorState.JumpToBootloaderRequested;
        }
        if (byte1 == 1 && byte0 == 4)
        {
            _mpState = MainProcessorState.JumpToMainAppRequested;
        }
        if (byte1 == 0 && byte0 == 34)
        {
            _mpState = MainProcessorState.OfflineRequested;
            writeRobotTxPower((short)2);
        }
        return;
    }

    void setRSSI(Integer integer)
    {
        if (integer.intValue() > -5)
        {
            return;
        }
        if (_RSSI == null)
        {
            _RSSI = integer;
            _prevRssi = _RSSI.intValue();
        }
        _RSSI = Integer.valueOf((_prevRssi + _RSSI.intValue() + integer.intValue()) / 3);
        _prevRssi = integer.intValue();
    }

    protected void setRfState(LEConnectionState leconnectionstate)
    {
        _rfState = leconnectionstate;
    }

    public void streamCommand(DeviceCommand devicecommand)
    {
        sendCommand(devicecommand, true);
    }

    public String toString()
    {
        String s;
        if ((new StringBuilder()).append("<LeLink ").append(_peripheral).toString() == null)
        {
            s = "unlinked";
        } else
        {
            s = (new StringBuilder()).append(_peripheral.getAddress()).append(" rf").append(_rfState).append(" mp").append(_mpState).toString();
        }
        return String.format(s, new Object[0]);
    }

    public void writeRobotTxPower(short word0)
    {
        if (_txPowerCharacteristic == null || _gatt == null)
        {
            DLog.e("TX power characteristic or gatt is null and cannot write power");
            return;
        } else
        {
            DLog.i((new StringBuilder()).append("Writing TX Power: ").append(word0).toString());
            byte abyte0[] = new byte[1];
            abyte0[0] = (byte)word0;
            _txPowerCharacteristic.setValue(abyte0);
            final BluetoothGatt f_gatt = _gatt;
            sendThread.execute(new Runnable() {

                final LeLink this$0;
                final BluetoothGatt val$f_gatt;

                public void run()
                {
                    f_gatt.writeCharacteristic(_txPowerCharacteristic);
                }

            
            {
                this$0 = LeLink.this;
                f_gatt = bluetoothgatt;
                super();
            }
            });
            return;
        }
    }

    static 
    {
        boolean flag;
        if (!com/orbotix/le/LeLink.desiredAssertionStatus())
        {
            flag = true;
        } else
        {
            flag = false;
        }
        $assertionsDisabled = flag;
    }









}
