package com.example.carbonfootprintcalculatorapp;

/**
 * This interface defines a callback for handling incoming MQTT messages.
 * Implement this interface to receive and process MQTT messages when they arrive.
 */
public interface MQTTCallback {

    /**
     * Called when an MQTT message arrives.
     *
     * @param message The received MQTT message as a String.
     */
    void onMessageArrived(String message);
}

