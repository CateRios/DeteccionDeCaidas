package com.example.detecciondecaidas;

import android.app.Application;

import com.example.detecciondecaidas.modelo.ModelInterface;
import com.example.detecciondecaidas.presentador.MainPresenter;
import com.example.detecciondecaidas.presentador.MainPresenterInterface;
import com.example.detecciondecaidas.vista.MainActivityInterface;

public class AppMediator extends Application {

    private static AppMediator singleton;

    //App Interfaces
    private MainActivityInterface mainActivity;
    private MainPresenterInterface mainPresenter;
    private ModelInterface model;

    //Methods
    public static AppMediator getInstance(){
        return singleton;
    }

    public MainActivityInterface getMainActivity() {
        return mainActivity;
    }

    public MainPresenterInterface getMainPresenter() {
        if(mainPresenter == null){
            mainPresenter = new MainPresenter();
        }
        return mainPresenter;
    }

    public void setMainActivity (MainActivityInterface mainActivity){
        this.mainActivity = mainActivity;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        mainPresenter = null;
        singleton = this;
    }


}
