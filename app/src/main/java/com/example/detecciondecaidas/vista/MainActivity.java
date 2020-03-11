package com.example.detecciondecaidas.vista;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.detecciondecaidas.AppMediator;
import com.example.detecciondecaidas.R;
import com.example.detecciondecaidas.presentador.MainPresenterInterface;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements MainActivityInterface, View.OnClickListener {

    //Global variables
    Button button;
    Button generateFileButton;
    Spinner movTypeSelector;
    Timer buttonTimer;
    Toast initToast, endToast, checkIdToast, locationToast;
    EditText editId;
    EditText editPeriod;

    private MainPresenterInterface presenter;
    private AppMediator appMediator;
    Context context;
    MediaPlayer mp;

    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appMediator = AppMediator.getInstance();
        presenter = appMediator.getMainPresenter();
        context = getApplicationContext();

        //Get view elements
        editId = findViewById(R.id.editId);
        button = findViewById(R.id.button);
        button.setOnClickListener(this);
        generateFileButton = findViewById(R.id.generateFileButton);
        generateFileButton.setOnClickListener(this);
        buttonTimer = new Timer();
        movTypeSelector = findViewById(R.id.movTypeSelector);
        mp = MediaPlayer.create(this, R.raw.sample);
        editPeriod = findViewById(R.id.editPeriod);

        //Create toast
        initToast = Toast.makeText(this, "Iniciando captura de movimientos", Toast.LENGTH_SHORT);
        endToast = Toast.makeText(this, "Captura de movimientos finalizada", Toast.LENGTH_SHORT);
        checkIdToast = Toast.makeText(this, "Los campos no pueden estar vacíos", Toast.LENGTH_SHORT);
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == findViewById(R.id.button).getId()){
            //Obtener datos del movimiento
            String id = this.editId.getText().toString();
            String movimiento = this.movTypeSelector.getSelectedItem().toString();
            String periodo = editPeriod.getText().toString();
            int indice = movTypeSelector.getSelectedItemPosition();
            //Comprobar que campo Id no está vacío
            if(!id.isEmpty() && !periodo.isEmpty()){
                button.setEnabled(false);
                //Pasar datos del movimiento al modelo
                presenter.passDataToModel(id, movimiento, indice, Integer.parseInt(periodo));
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //Empezar a tomar datos
                initToast.show();
                mp.start();
                presenter.initSensorDataRecollection(context);
                buttonTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //Parar sensor
                                presenter.endSensorDataRecollection();
                                button.setEnabled(true);
                                endToast.show();
                                mp.start();
                            }
                        });
                    }
                }, 15000);
            }else{
                checkIdToast.show();
            }

        }else if(v.getId() == findViewById(R.id.generateFileButton).getId()){

            presenter.generateFile(context);
            //locationToast = Toast.makeText(this, location, Toast.LENGTH_LONG);
            //locationToast.show();
        }
    }
}
