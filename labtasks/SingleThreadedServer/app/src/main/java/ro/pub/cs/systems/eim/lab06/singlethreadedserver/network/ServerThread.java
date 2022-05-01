package ro.pub.cs.systems.eim.lab06.singlethreadedserver.network;

import android.util.Log;
import android.widget.EditText;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import ro.pub.cs.systems.eim.lab06.singlethreadedserver.general.Constants;
import ro.pub.cs.systems.eim.lab06.singlethreadedserver.general.Utilities;

class CommunicationThread extends Thread {
    private Socket socket;
    private EditText serverTextEditText;

    public CommunicationThread(Socket socket, EditText serverEditText) {
        this.socket = socket;
        this.serverTextEditText = serverEditText;
    }

    @Override
    public void run() {
        try {
            Log.v(Constants.TAG, "Connection opened with "+socket.getInetAddress()+":"+socket.getLocalPort());
            try {
                Thread.sleep(3000);
            } catch (InterruptedException interruptedException) {
                Log.e(Constants.TAG, interruptedException.getMessage());
                if (Constants.DEBUG) {
                    interruptedException.printStackTrace();
                }
            }
            PrintWriter printWriter = Utilities.getWriter(socket);
            printWriter.println(serverTextEditText.getText().toString());
            socket.close();
            Log.v(Constants.TAG, "Connection closed");
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "An exception has occurred: "+ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        }
    }
}


public class ServerThread extends Thread {

    private boolean isRunning;

    private ServerSocket serverSocket;
    private EditText serverTextEditText;

    public ServerThread(EditText serverEditText) {
        this.serverTextEditText = serverEditText;
    }

    public void startServer() {
        isRunning = true;
        start();
        Log.v(Constants.TAG, "startServer() method invoked " + serverSocket);
    }

    public void stopServer() {
        isRunning = false;
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        }
        Log.v(Constants.TAG, "stopServer() method invoked");
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(Constants.SERVER_PORT);
            Log.d(Constants.TAG, "Server's IP is: " + serverSocket.getInetAddress());
            while (isRunning) {
                Socket socket = serverSocket.accept();
                new CommunicationThread(socket, serverTextEditText).start();

                // TODO exercise 5c
                // simulate the fact the communication routine between the server and the client takes 3 seconds
                // TODO done: look up!


                // TODO exercise 5d
                // move the communication routine between the server and the client on a separate thread (each)
                // TODO done: look up!
            }
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        }
    }
}
