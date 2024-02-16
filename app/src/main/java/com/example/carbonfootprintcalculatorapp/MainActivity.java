package com.example.carbonfootprintcalculatorapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * MainActivity class handles the user interface and interaction with the MQTT server.
 * It allows users to input their CO2 consumption data and publish it to an MQTT topic.
 * The user can also receive the average CO2 consumption.
 */
public class MainActivity extends AppCompatActivity implements MQTTCallback {
    private TextView tvUserData, tvAvgData;
    private final EditText[] valueFields = new EditText[10];
    private Button bSendData;
    private MqttHandler mqttHandler;
    private final Co2Calculator co2Calculator = new Co2Calculator();

    /**
     * Called when the activity is starting. Initializes the UI and mqTTHandler.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the data it most recently supplied.
     *                           Otherwise, it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeViews();
        initializeMqttHandler();
    }


    /**
     * Initializes the views in the activity. This includes setting up TextViews, EditTexts, and the Button.
     */
    private void initializeViews() {
        // Initialize TextViews
        tvUserData = findViewById(R.id.TVusdata);
        tvAvgData = findViewById(R.id.TVavgdata);

        // Initialize EditTexts for values
        valueFields[0] = findViewById(R.id.value1Field);
        valueFields[1] = findViewById(R.id.value2Field);
        valueFields[2] = findViewById(R.id.value3Field);
        valueFields[3] = findViewById(R.id.value4Field);
        valueFields[4] = findViewById(R.id.value5Field);
        valueFields[5] = findViewById(R.id.value6Field);
        valueFields[6] = findViewById(R.id.value7Field);
        valueFields[7] = findViewById(R.id.value8Field);
        valueFields[8] = findViewById(R.id.value9Field);
        valueFields[9] = findViewById(R.id.value10Field);

        // Initialize Button and set onClickListener
        bSendData = findViewById(R.id.Bsenddata);
        bSendData.setOnClickListener(this::onSendDataClicked);
    }

    /**
     * Initializes the MQTT handler for managing MQTT connections and message handling.
     */
    private void initializeMqttHandler() {
        mqttHandler = new MqttHandler();
        mqttHandler.setCallback(this);
        mqttHandler.init(getApplicationContext(), () -> {
            // This block will be executed when the MQTT client successfully connects
        });
    }

    /**
     * Called when the 'Send Data' button is clicked. It publishes the user's CO2 data to the MQTT server.
     * @param view The view that was clicked.
     */
    private void onSendDataClicked(View view) {
        try {
            publishUserData();
        } catch (NumberFormatException e) {
            // Handle exception if input is not a valid number
            e.printStackTrace();
        }
    }

    /**
     * Publishes the user's CO2 data as an MQTT message. It gathers data from input fields,
     * calculates the total CO2 consumption, and sends the data to the MQTT server.
     */
    private void publishUserData() {
        int[] values = new int[valueFields.length];
        // Store entered values from the user
        for (int i = 0; i < valueFields.length; i++) {
            values[i] = parseIntOrDefault(valueFields[i].getText().toString());
        }
        float totalConsumption = co2Calculator.calculateTotalCo2(values);
        String formattedCo2 = String.format("%.3f", totalConsumption);
        tvUserData.setText(formattedCo2);
        // Publish the CO2 consumption with the timestamp
        String mqttMessage = getCurrentTimestamp() + "_" + formattedCo2;
        mqttHandler.publishMessage(mqttMessage);
    }

    /**
     * Callback method that is triggered when an MQTT message arrives.
     * Sets the message in the Global Average TextField.
     * @param message The message that arrived.
     */
    @Override
    public void onMessageArrived(String message) {
        runOnUiThread(() -> tvAvgData.setText(message));
    }

    /**
     * Generates the current timestamp in the needed format.
     * @return A string representing the current timestamp.
     */
    private String getCurrentTimestamp() {
        long timestamp = System.currentTimeMillis();
        StringBuilder timestampString = new StringBuilder(String.valueOf(timestamp));
        while (timestampString.length() < 13) {
            timestampString.insert(0, "0");
        }
        return timestampString.toString();
    }

    /**
     * Parses an integer from the given string. Returns a default value if parsing fails.
     *
     * @param value The string to parse.
     * @return The parsed integer, or the default value if parsing fails.
     */
    private int parseIntOrDefault(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
