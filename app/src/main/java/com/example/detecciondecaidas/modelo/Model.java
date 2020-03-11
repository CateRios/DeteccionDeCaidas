package com.example.detecciondecaidas.modelo;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.ajts.androidmads.library.SQLiteToExcel;
import com.example.detecciondecaidas.AppMediator;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;



public class Model extends AppCompatActivity implements ModelInterface, SensorEventListener {

    private static Model singleton = null;
    private AppMediator appMediator;
    private SensorManager sensorManager;
    private AppDatabase db;
    private Executor executor;
    private SQLiteToExcel sqLiteToExcel;
    private Timer timer;
    SimpleDateFormat dateFormat;

    private float[] rotationMatrix = new float[9];
    private float[] magneticMatrix = new float[3];
    private float[] gravityMatrix = new float[3];
    private float[] orientationAngles = new float[3];
    private float[] acelerometterMatrix = new float[3];
    private float[] giroscopeMatrix = new float[3];

    public long tmpID;
    public String passedId, passedMov;
    //public String location;
    public int indice;
    public int periodo;


    private Model(){
        appMediator = AppMediator.getInstance();
        executor = Executors.newSingleThreadExecutor();
        db = Room.databaseBuilder(appMediator.getApplicationContext(), AppDatabase.class, "MovimientosCapturados").fallbackToDestructiveMigration().build();
        dateFormat = new SimpleDateFormat("HH:mm:ss:SSS");
    }

    public static Model getInstance(){
        if(singleton == null){
            singleton = new Model();
        }
        return singleton;
    }

    @Override
    public void initSensorDataRecollection(Context context){
        //Create an instance of SensorManager
        sensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);
        //Register Listeners
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), 10000);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), 10000);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), 10000);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY), 10000);
        executor.execute(new Runnable() {
            @Override
            public void run() {
                insertMovToDatabase();
            }
        });
        timer = new Timer();
        TimerTask insertData = new TimerTask() {
            @Override
            public void run() {
                insertCapturaToDatabase(tmpID, orientationAngles, acelerometterMatrix, giroscopeMatrix);
            }
        };
        timer.scheduleAtFixedRate(insertData, 10, this.periodo);
    }

    @Override
    public void getMovementData(String id, String movimiento, int indice, int periodo){
        this.passedId = id;
        this.passedMov = movimiento;
        this.indice = indice;
        this.periodo = periodo;
    }


    @Override
    public void onSensorChanged(final SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            //Extraer y almacenar datos->decima de segundo
            acelerometterMatrix = event.values;
            Log.e("Acc", "Acelerometro " + event.values[0]);
        }else if(event.sensor.getType() == Sensor.TYPE_GYROSCOPE){
            //Extraer y almacenar datos
            giroscopeMatrix = event.values;
        }else if(event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){
            magneticMatrix = event.values;
        }else if(event.sensor.getType() == Sensor.TYPE_GRAVITY){
            gravityMatrix = event.values;
        }

        SensorManager.getRotationMatrix(rotationMatrix, null, gravityMatrix, magneticMatrix);
        SensorManager.getOrientation(rotationMatrix, orientationAngles);

        //Insertar datos
        /*executor.execute(new Runnable() {
            @Override
            public void run() {
                insertCapturaToDatabase(tmpID, orientationAngles, acelerometterMatrix, giroscopeMatrix);
            }
        });*/

    }




    @Override
    public void insertMovToDatabase(){
                //Insertar Movimiento
                Movimiento tmp = new Movimiento();
                tmp.idUser = passedId;
                tmp.tipoMovimiento = passedMov;
                tmp.periodo = this.periodo;
                Date date  = Calendar.getInstance().getTime();
                tmp.fecha = "" + date;
                tmpID = db.movimientoDao().insert(tmp);
                Log.e("Mov", "" + tmpID);
                //insertCapturaToDatabase(tmpID, rotationMatrix, acelerometterMatrix, giroscopeMatrix);

    }

    @Override
    public void generateFile(Context context) {
        sqLiteToExcel = new SQLiteToExcel(context, "MovimientosCapturados");
        long time = Calendar.getInstance().getTimeInMillis();
        sqLiteToExcel.exportAllTables( time + ".xls", new SQLiteToExcel.ExportListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onCompleted(String filePath) {
               Log.e("DB", filePath);
               //location = filePath;
            }

            @Override
            public void onError(Exception e) {
                Log.e("DB Error", ""+e);
                //location = "" + e;

            }
        });
    }

    @Override
    public String getLocation() {
        //return this.location;
        return null;
    }


    private void insertCapturaToDatabase(long tmpID, float[] orientationAngles, float[] acelerometterMatrix, float[] giroscopeMatrix) {

        Captura captura = new Captura();
        captura.idMov = tmpID;
        //Datos del acelerómetro
        captura.x = acelerometterMatrix[0];
        captura.y = acelerometterMatrix[1];
        captura.z = acelerometterMatrix[2];
        //Datos del giroscopo
        captura.roll = giroscopeMatrix[0];
        captura.pitch = giroscopeMatrix[1];
        captura.yaw = giroscopeMatrix[2];
        //Instante de tiempo en que se registra la muestra
        captura.time = "" + dateFormat.format(new Date());
        //azimut
        captura.compass = orientationAngles[0];
        //Indice
        captura.indiceMov = this.indice;

        long id = db.capturaDao().insert(captura);
        Log.e("Captura" , "" + id + " tiempo: " + captura.time);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void endSensorDataRecollection(){
        sensorManager.unregisterListener(this);
        timer.cancel();
    }




}
