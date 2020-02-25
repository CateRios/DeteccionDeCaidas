package com.example.detecciondecaidas.vista;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.detecciondecaidas.AppMediator;
import com.example.detecciondecaidas.R;
import com.example.detecciondecaidas.presentador.MainPresenterInterface;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements SensorEventListener, MainActivityInterface, View.OnClickListener {

    //Global variables
    Button button;
    Button generateFileButton;
    Spinner movTypeSelector;
    Timer buttonTimer;
    ArrayAdapter<CharSequence> adapter;
    Toast initToast, endToast, checkIdToast;
    EditText editId;

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
       // adapter = ArrayAdapter.createFromResource(this, R.array.sensor_type_array, R.layout.sensor_type_selector_item);
        //adapter.setDropDownViewResource(R.layout.sensor_type_selector_item);
        //sensorTypeSelector.setAdapter(adapter);

        //Create toast
        initToast = Toast.makeText(this, "Iniciando captura de movimientos", Toast.LENGTH_SHORT);
        endToast = Toast.makeText(this, "Captura de movimientos finalizada", Toast.LENGTH_SHORT);
        checkIdToast = Toast.makeText(this, "El campo id no puede estar vacío", Toast.LENGTH_SHORT);
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onPause(){
        super.onPause();
       // sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            //Almacenar datos
        }else if(event.sensor.getType() == Sensor.TYPE_GYROSCOPE){
            //Almacenar datos
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == findViewById(R.id.button).getId()){
            //Obtener datos del movimiento
            String id = this.editId.getText().toString();
            String movimiento = this.movTypeSelector.getSelectedItem().toString();
            //Comprobar que campo Id no está vacío
            if(!id.isEmpty()){
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //Pasar datos del movimiento al modelo
                presenter.passDataToModel(id, movimiento);
                //Empezar a tomar datos
                button.setEnabled(false);
                initToast.show();
                presenter.initSensorDataRecollection(context);
                mp.start();
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
        }
    }
}
