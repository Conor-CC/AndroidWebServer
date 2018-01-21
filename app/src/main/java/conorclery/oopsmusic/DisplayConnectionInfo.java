package conorclery.oopsmusic;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;


public class DisplayConnectionInfo extends Activity {

    String ip;
    int port;
    SimpleWebServer server;
    TextView view;
    TextView viewTwo;
    Handler handler;
    int lmao;
    boolean receivedRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        lmao = 0;
        ip = getIpAddress();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_connection_info);
        server = new SimpleWebServer(8080, getAssets());
        server.start();
        port = server.getPort();

        //Prints Inet info on app
        view = (TextView)  findViewById(R.id.infoip);
        view.setText(ip + ":" + server.getPort());
        viewTwo = (TextView) findViewById(R.id.stuff);
        handler = new Handler();
        handler.post(updateView);
        receivedRequest = true;
    }

    protected Runnable updateView = new Runnable() {

        @Override
        public void run() {
            if (server.isRequestRecv()) {
                viewTwo.setText("Received a packet");
            }
            else{
                viewTwo.setText("Got nothing");
            }
            handler.postDelayed(this, 1000);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (server != null) {
            server.stop();
        }
    }

    protected String getIpAddress() {
        WifiManager wifiMan = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInf = wifiMan.getConnectionInfo();
        int ipAddress = wifiInf.getIpAddress();
        return String.format("%d.%d.%d.%d", (ipAddress & 0xff),(ipAddress >> 8 & 0xff),(ipAddress >> 16 & 0xff),(ipAddress >> 24 & 0xff));
    }

}
