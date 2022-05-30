package ro.pub.cs.systems.eim.practicaltest02.network;

import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import ro.pub.cs.systems.eim.practicaltest02.general.Constants;
import ro.pub.cs.systems.eim.practicaltest02.general.Utilities;

public class ClientThread extends Thread {

    private String address;
    private int port;
    private String word;
    private TextView getFortuneTextView;

    private Socket socket;

    public ClientThread(String address, int port, String word, TextView getFortuneTextView) {
        this.address = address;
        this.port = port;
        this.word = word;
        this.getFortuneTextView = getFortuneTextView;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(address, port);
            if (socket == null) {
                Log.e(Constants.TAG, "[CLIENT THREAD] Could not create socket!");
                return;
            }
            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);

            if (bufferedReader == null || printWriter == null) {
                Log.e(Constants.TAG, "[CLIENT THREAD] Buffered Reader / Print Writer are null!");
                return;
            }

            Log.v(Constants.TAG, "[CLIENT THREAD] Printiiing");

            printWriter.println(word);
            printWriter.flush();

            Log.v(Constants.TAG, "[CLIENT THREAD] Printed");

            String fortuneInformation;
            while ((fortuneInformation = bufferedReader.readLine()) != null) {
                final String finalizedFortuneInformation = fortuneInformation;
                getFortuneTextView.post(new Runnable() {
                   @Override
                    public void run() {
                       getFortuneTextView.setText(finalizedFortuneInformation);
                   }
                });
            }
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ioException) {
                    Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
                    if (Constants.DEBUG) {
                        ioException.printStackTrace();
                    }
                }
            }
        }
    }

}
