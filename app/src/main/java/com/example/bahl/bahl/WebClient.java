package com.example.bahl.bahl;

/**
 * Created by bahl on 4/6/2016.
 */

/*import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class WebClient {

    private static final String NAMESPACE = "com.service.ServiceImpl";
    private static final String URL =
            "http://192.168.202.124:9000/AndroidWS/wsdl/ServiceImpl.wsdl";
    private static final String SOAP_ACTION = "ServiceImpl";
    private static final String METHOD_NAME = "message";

    private static final String[] sampleACTV = new String[] {
            "android", "iphone", "blackberry"
    };

    public static void Execute(){

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        SoapSerializationEnvelope envelope =
                new SoapSerializationEnvelope(SoapEnvelope.VER11);

        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapObject resultsRequestSOAP = (SoapObject) envelope.bodyIn;
           // ACTV.setHint("Received :" + resultsRequestSOAP.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}*/
