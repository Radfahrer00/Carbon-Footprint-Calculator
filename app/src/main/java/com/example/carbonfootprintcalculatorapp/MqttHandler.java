package com.example.carbonfootprintcalculatorapp;

import android.content.Context;
import android.util.Log;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * This class handles the MQTT (Message Queuing Telemetry Transport) communication.
 * It provides functionality for connecting to an MQTT server running on a virtual machine,
 * subscribing to a topic, and publishing messages.
 */
public class MqttHandler {
    private MQTTCallback callback;
    private MqttAndroidClient mqttAndroidClient;
    public String message_received = "No message received";
    final String serverUri = "tcp://192.168.56.1:1883"; // Private MQTT ServerUri
    final String subscriptionTopic = "footprint/average";
    final String publishTopic = "footprint/userdata";

    String clientId = "ExampleAndroidClient";

    /**
     * Sets the callback for handling incoming MQTT messages.
     *
     * @param callback The MQTT callback to set.
     */
    public void setCallback(MQTTCallback callback) {
        this.callback = callback;
    }

    /**
     * Initializes the MQTT client and establishes the connection to the MQTT server.
     *
     * @param applicationContext The application context.
     * @param onConnect          A runnable to execute when the MQTT client successfully connects.
     */
    public void init(Context applicationContext, final Runnable onConnect) {
        clientId = clientId + System.currentTimeMillis();
        mqttAndroidClient = new MqttAndroidClient(applicationContext, serverUri, clientId);
        mqttAndroidClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                if (reconnect) {
                    Log.d("TAG_mqtt", "Reconnected to : " + serverURI);
                    // Because Clean Session is true, we need to re-subscribe
                    subscribeToTopic();
                } else {
                    Log.d("TAG_mqtt", "Connected to: " + serverURI);
                    // Trigger the onConnect callback when connected
                    onConnect.run();
                }
            }

            @Override
            public void connectionLost(Throwable cause) {
                Log.d("TAG_mqtt", "The Connection was lost.");
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) {
                message_received = new String(message.getPayload());
                Log.d("TAG_mqtt", "Incoming message: " + new String(message.getPayload()));
                if (callback != null) {
                    callback.onMessageArrived(message_received);
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {}
        });

        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setCleanSession(true);

        Log.d("TAG_mqtt", "Connecting to " + serverUri + "...");

        try {
            mqttAndroidClient.connect(mqttConnectOptions, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
                    disconnectedBufferOptions.setBufferEnabled(true);
                    disconnectedBufferOptions.setBufferSize(100);
                    disconnectedBufferOptions.setPersistBuffer(false);
                    disconnectedBufferOptions.setDeleteOldestMessages(false);
                    mqttAndroidClient.setBufferOpts(disconnectedBufferOptions);
                    // Subscribe inside the connect success block, not directly
                    subscribeToTopic();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.d("TAG_mqtt", "Failed to connect to: " + serverUri +
                            ". Cause: " + ((exception.getCause() == null) ?
                            exception.toString() : exception.getCause()));
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
            Log.d("TAG_mqtt", e.toString());
        }
    }


    /**
     * Subscribes to the MQTT topic for receiving average data.
     */
    public void subscribeToTopic() {
        try {
            mqttAndroidClient.subscribe(subscriptionTopic, 0, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.d("TAG_mqtt", "Subscribed to: " + subscriptionTopic);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.d("TAG_mqtt", "Failed to subscribe");
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
            Log.d("TAG_mqtt", e.toString());
        }

    }

    /**
     * Publishes an MQTT message to the specified topic.
     *
     * @param publishMessage The message to publish.
     */
    public void publishMessage(String publishMessage) {
        MqttMessage message = new MqttMessage();
        message.setPayload(publishMessage.getBytes());
        message.setRetained(false);
        message.setQos(2); // Set the QoS level

        if (mqttAndroidClient.isConnected()) {
            try {
                mqttAndroidClient.publish(publishTopic, message, null, new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        Log.d("TAG_mqtt", "Message Published");
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                        Log.d("TAG_mqtt", "Failed to publish message");
                    }
                });
            } catch (MqttException e) {
                e.printStackTrace();
                Log.d("TAG_mqtt", e.toString());
            }
        } else {
            Log.d("TAG_mqtt", "Client not connected! Reconnecting...");
        }
    }

    public void setMqttAndroidClient(MqttAndroidClient mqttAndroidClient) {
        this.mqttAndroidClient = mqttAndroidClient;
    }
}


