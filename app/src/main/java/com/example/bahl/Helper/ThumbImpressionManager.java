package com.example.bahl.Helper;

import com.IDWORLD.HostUsb;
import com.IDWORLD.LAPI;
import com.example.bahl.Model.ImageDto;

/**
 * Created by bahl on 5/4/2016.
 */
public class ThumbImpressionManager {

    //------------------------------------------------------------------------------------------
    public static final int MSG_SHOW_TEXT = 101;
    public static final int MSG_SHOW_IMAGE = 102;
    public static final int MSG_VIEW_TEMPLATE_1 = 103;
    public static final int MSG_VIEW_TEMPLATE_2 = 104;
    //------------------------------------------------------------------------------------------
    public static final int BUTTONS_INITIALIZED = 0;
    public static final int BUTTONS_FINALIZED = 1;
    public static final int BUTTONS_CAPTURERIZED = 2;
    //------------------------------------------------------------------------------------------
    public static final int MESSAGE_ID_ENABLED = 403;
    public static final int BUTTONS_ID_ENABLED_FINALIZED = 404;
    public static final int BUTTONS_ID_ENABLED_INITIALIZED = 405;
    public static final int BUTTONS_ID_ENABLED_CAPTURERIZED = 406;
    //------------------------------------------------------------------------------------------
    private HostUsb m_cHost;
    private LAPI m_cLAPI;
    private int m_hUSB = 0;
    private long m_hDevice = 0;
    private byte[] m_image = new byte[LAPI.WIDTH * LAPI.HEIGHT];
    private byte[] m_itemplate_1 = new byte[LAPI.FPINFO_STD_MAX_SIZE];
    private byte[] m_itemplate_2 = new byte[LAPI.FPINFO_STD_MAX_SIZE];
    //------------------------------------------------------------------------------------------

    /*private final Handler m_fEvent = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SHOW_TEXT:
                    tvMessage.setText((String)msg.obj);
                    break;
                case MSG_VIEW_TEMPLATE_1:
                    tvTemp1.setText((String)msg.obj);
                    break;
                case MSG_VIEW_TEMPLATE_2:
                    tvTemp2.setText((String)msg.obj);
                    break;
                case MESSAGE_ID_ENABLED:
                    Button btn = (Button) findViewById(msg.arg1);
                    if (msg.arg2 != 0) btn.setEnabled(true);
                    else btn.setEnabled(false);
                    break;
                case BUTTONS_ID_ENABLED_INITIALIZED:
                    EnableBittons(BUTTONS_INITIALIZED);
                    break;
                case BUTTONS_ID_ENABLED_FINALIZED:
                    EnableBittons(BUTTONS_FINALIZED);
                    break;
                case BUTTONS_ID_ENABLED_CAPTURERIZED:
                    EnableBittons(BUTTONS_CAPTURERIZED);
                    break;
                case MSG_SHOW_IMAGE: {
                    ShowFingerBitmap ((byte[])msg.obj,msg.arg1,msg.arg2);
                    //Send the data for Verification here
                }
                break;
            }
        }
    };*/

    public void OPEN_DEVICE() {
        String msg;
        m_hUSB = m_cHost.OpenDeviceInterfaces();
        if (m_hUSB < 0) {
            msg = "Can't open host usb !";
            // m_fEvent.sendMessage(m_fEvent.obtainMessage(MSG_SHOW_TEXT, 0, 0,msg));
            return;
        }
        m_hDevice = m_cLAPI.OpenDevice(m_hUSB);
        if (m_hDevice == 0) msg = "Can't open device !";
        else {

            msg = "OpenDevice() = OK";
            // EnableBittons (BUTTONS_INITIALIZED);
        }
        //  m_fEvent.sendMessage(m_fEvent.obtainMessage(MSG_SHOW_TEXT, 0, 0,msg));
    }

    public ImageDto GET_IMAGE() {
        int ret;
        String msg;
        ret = m_cLAPI.GetImage(m_hDevice, m_image);
        if (ret != LAPI.TRUE) msg = "Can't get image !";
        else msg = "GetImage() = OK";

        ImageDto imageDto = new ImageDto();
        imageDto.Data = m_image;
        imageDto.Height = LAPI.HEIGHT;
        imageDto.Width = LAPI.WIDTH;
        return imageDto;
        // m_fEvent.sendMessage(m_fEvent.obtainMessage(MSG_SHOW_TEXT, 0, 0,msg));
        //m_fEvent.sendMessage(m_fEvent.obtainMessage(MSG_SHOW_IMAGE, LAPI.WIDTH, LAPI.HEIGHT,m_image));
    }
}
