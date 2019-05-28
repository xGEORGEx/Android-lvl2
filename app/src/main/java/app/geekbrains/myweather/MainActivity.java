package app.geekbrains.myweather;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView windView;
    private TextView pressureView;
    private TextView humidityView;
    private TextView sensorInfo;
    private TextView sensorTemperature;
    private TextView sensorHumidity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        sensorInfo = findViewById(R.id.sensorInfo);
        sensorTemperature = findViewById(R.id.sensorTemp);
        sensorHumidity = findViewById(R.id.sensorHumidity);
        windView = findViewById(R.id.textWind);
        pressureView = findViewById(R.id.textPressure);
        humidityView = findViewById(R.id.textHumidity);

        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        if (sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE) == null) {
            sensorTemperature.setText(getResources().getText(R.string.warrning_sens_temp));
        } else {
            Sensor sensorTemp = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
            sensorManager.registerListener(listenerTemp, sensorTemp, SensorManager.SENSOR_DELAY_NORMAL);
        }

        if (sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY) == null) {
            sensorHumidity.setText(getResources().getText(R.string.warrning_sens_hum));
        } else {
            Sensor sensorHum = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
            sensorManager.registerListener(listenerHum, sensorHum, SensorManager.SENSOR_DELAY_NORMAL);
        }

        final TextView weatherView = findViewById(R.id.textWeather);
        final TextView temperatureView = findViewById(R.id.textTemperature);
        final EditText editCity = findViewById(R.id.editCity);
        Button btnWeather = findViewById(R.id.button_weather);

        btnWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editCity.getText().toString().isEmpty()) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Введите название города", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    weatherView.setText(R.string.weather);
                    temperatureView.setText(R.string.temperature);
                    pressureView.setText(R.string.pressure);
                    windView.setText(R.string.wind);
                    humidityView.setText(R.string.humidity);
                }
            }
        });

    }


    private void showSensors(SensorEvent event, TextView textView) {
        StringBuilder stringBuilder = new StringBuilder();
        if (textView.getId() == R.id.sensorTemp) {
            stringBuilder.append("Температура окружающего воздуха = ").append(event.values[0]);
        } else if (textView.getId() == R.id.sensorHumidity) {
            stringBuilder.append("Влажность окружающего воздуха = ").append(event.values[0]);
        }
        textView.setText(stringBuilder);

    }

    // Слушатель датчика температуры
    SensorEventListener listenerTemp = new SensorEventListener() {

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            showSensors(event, sensorTemperature);
        }
    };

    // Слушатель датчика влажности
    SensorEventListener listenerHum = new SensorEventListener() {

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            showSensors(event, sensorHumidity);
        }
    };

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_wind) {
            item.setChecked(!item.isChecked());
            showView(windView, item.isChecked());
            return true;
        } else if (id == R.id.action_pressure) {
            item.setChecked(!item.isChecked());
            showView(pressureView, item.isChecked());
            return true;
        } else if (id == R.id.action_humidity) {
            item.setChecked(!item.isChecked());
            showView(humidityView, item.isChecked());
            return true;
        } else if (id == R.id.action_sensor) {
            item.setChecked(!item.isChecked());
            sensorInfo.setVisibility(View.VISIBLE);
            sensorTemperature.setVisibility(View.VISIBLE);
            sensorHumidity.setVisibility(View.VISIBLE);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_about_app) {
            startInfo(getResources().getString(R.string.info) + " " + BuildConfig.VERSION_NAME);
        } else if (id == R.id.nav_feedback) {
            startInfo(getResources().getString(R.string.feedback));
        } else if (id == R.id.nav_avtor) {
            startInfo(getResources().getString(R.string.about_avtor));
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showView(TextView view, boolean show) {
        if (show) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }

    private void startInfo(String info) {
        Intent intent = new Intent(this, InfoActivity.class);
        intent.putExtra("Info", info);
        startActivity(intent);
    }
}
