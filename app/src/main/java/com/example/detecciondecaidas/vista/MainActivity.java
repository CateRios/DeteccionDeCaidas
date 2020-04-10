package com.example.detecciondecaidas.vista;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
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
    Button deleteDataButton;
    Spinner movTypeSelector;
    Timer buttonTimer;
    Toast initToast, endToast, checkIdToast;
    EditText editId;
    EditText editPeriod;

    private MainPresenterInterface presenter;
    private AppMediator appMediator;
    Context context;
    MediaPlayer mp;
    Vibrator vibrator;

    //Constants
    public static final int MY_PERMISSIONS_REQUEST_STORAGE = 100;

    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appMediator = AppMediator.getInstance();
        presenter = appMediator.getMainPresenter();
        context = getApplicationContext();

        //Get view elements
        editId = findViewById(R.id.editId);
        button = findViewById(R.id.initButton);
        button.setOnClickListener(this);
        generateFileButton = findViewById(R.id.generateFileButton);
        generateFileButton.setOnClickListener(this);
        deleteDataButton = findViewById(R.id.deleteDBButton);
        deleteDataButton.setOnClickListener(this);
        buttonTimer = new Timer();
        movTypeSelector = findViewById(R.id.movTypeSelector);
        mp = MediaPlayer.create(this, R.raw.sample);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        editPeriod = findViewById(R.id.editPeriod);

        //Create toast
        initToast = Toast.makeText(this, "Iniciando captura de movimientos", Toast.LENGTH_SHORT);
        endToast = Toast.makeText(this, "Captura de movimientos finalizada", Toast.LENGTH_SHORT);
        checkIdToast = Toast.makeText(this, "Los campos no pueden estar vacíos", Toast.LENGTH_SHORT);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == findViewById(R.id.initButton).getId()) {
            //Obtener datos del movimiento
            String id = this.editId.getText().toString();
            String movimiento = this.movTypeSelector.getSelectedItem().toString();
            String periodo = editPeriod.getText().toString();
            int indice = movTypeSelector.getSelectedItemPosition();
            //Comprobar que campo Id no está vacío
            if (!id.isEmpty() && !periodo.isEmpty()) {
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

                if (Build.VERSION.SDK_INT >= 26) {
                    vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
                }else {
                    vibrator.vibrate(200);
                }

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

                                if (Build.VERSION.SDK_INT >= 26) {
                                    vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
                                }else {
                                    vibrator.vibrate(200);
                                }

                            }
                        });
                    }
                }, 15000);
            } else {
                checkIdToast.show();
            }

        } else if (v.getId() == findViewById(R.id.generateFileButton).getId()) {

            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                // Permission is not granted
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_STORAGE);

            } else {
                // Permission has already been granted
                presenter.generateFile(context);

            }

        }else if(v.getId() == findViewById(R.id.deleteDBButton).getId()){
            //Delete DB data
            presenter.deleteDBData();
            Toast.makeText(this, "Se han eliminado los datos", Toast.LENGTH_SHORT).show();

        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                    //LLamada al modelo
                    presenter.generateFile(context);


                } else {
                    //Toast para indicar que la app no va a funcionar
                    Toast.makeText(this, "No se generará el fichero", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            default: {
                break;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }
}
