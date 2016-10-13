package it.polito.mad.polijob;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Objects;

import it.polito.mad.polijob.model.PoliJobDB;
import it.polito.mad.polijob.model.*;

/**
 * Created by luigi on 10/05/15.
 */
public class PoliJobApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        PoliJobDB.initialize(this,
                Student.class,
                Company.class,
                Position.class);
    }
}