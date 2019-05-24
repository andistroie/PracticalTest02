package ro.pub.cs.systems.eim.practicaltest02;

import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;


public class ClientThread extends Thread {

    private String name;
    private int port;


    private Socket socket;

    private TextView abilitiesTextView;

    public ClientThread(String name, int port, TextView aTextView) {
        this.name = name;
        this.port = port;
        this.abilitiesTextView = aTextView;
    }

    @Override
    public void run() {
        try {
            socket = new Socket("localhost", port);
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

            String pokemonInformation;
            while ((pokemonInformation = bufferedReader.readLine()) != null) {
                final String finalizedPokemonInformation = pokemonInformation;
                abilitiesTextView.post(new Runnable() {
                    @Override
                    public void run() {
                        abilitiesTextView.setText(finalizedPokemonInformation);
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