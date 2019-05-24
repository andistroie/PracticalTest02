package ro.pub.cs.systems.eim.practicaltest02;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PracticalTest02MainActivity extends AppCompatActivity {

    private Button goButton = null;
    private Button startButton = null;
    private EditText pokemonName = null;
    private EditText serverPort = null;
    private ImageView pokemonImage = null;
    private TextView pokemonAbilities = null;

    ServerThread serverThread = null;
    ClientThread clientThread = null;


    private StartButtonClickListener startButtonClickListener = new StartButtonClickListener();
    private class StartButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            String sPort = serverPort.getText().toString();
            if (sPort == null || sPort.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Server port should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }
            serverThread = new ServerThread(Integer.parseInt(sPort));
            if (serverThread.getServerSocket() == null) {
                Log.e(Constants.TAG, "[MAIN ACTIVITY] Could not create server thread!");
                return;
            }
            serverThread.start();
        }

    }

    private GoButtonClickListener goButtonClickListener = new GoButtonClickListener();
    private class GoButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            String pName = pokemonName.getText().toString();
            if (pName == null || pName.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Pokemon name should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (serverThread == null || !serverThread.isAlive()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] There is no server to connect to!", Toast.LENGTH_SHORT).show();
                return;
            }
            pokemonAbilities.setText(Constants.EMPTY_STRING);

            clientThread = new ClientThread(pName, Integer.parseInt(serverPort.getText().toString()), pokemonAbilities);

            clientThread.start();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test02_main);

        goButton = (Button)findViewById(R.id.buttonGo);
        goButton.setOnClickListener(goButtonClickListener);

        startButton = (Button)findViewById(R.id.serverStart);
        goButton.setOnClickListener(startButtonClickListener);

        serverPort = findViewById(R.id.serverPort);

        pokemonName = findViewById(R.id.editTextName);
        pokemonAbilities = findViewById(R.id.textViewAbilities);
        pokemonImage = findViewById(R.id.imageViewClient);

    }
}
