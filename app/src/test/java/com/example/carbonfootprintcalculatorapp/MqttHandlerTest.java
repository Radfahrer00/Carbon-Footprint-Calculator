package com.example.carbonfootprintcalculatorapp;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class MqttHandlerTest {

    @Mock
    private MqttAndroidClient mockMqttAndroidClient;
    @Mock
    private IMqttDeliveryToken mockMqttToken;
    private MqttHandler mqttHandler;
    private AutoCloseable closeable;


    @Before
    public void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        mqttHandler = new MqttHandler();
        // Assume setMqttAndroidClient is a method you add to inject mock client for testing
        mqttHandler.setMqttAndroidClient(mockMqttAndroidClient);
    }

    @After
    public void releaseMocks() throws Exception {
        closeable.close();
    }

    @Test
    public void testPublishMessage() throws Exception {
        when(mockMqttAndroidClient.isConnected()).thenReturn(true);
        when(mockMqttAndroidClient.publish(anyString(), any(MqttMessage.class), any(), any())).thenReturn(mockMqttToken);

        mqttHandler.publishMessage("Test message");

        verify(mockMqttAndroidClient).publish(eq(mqttHandler.publishTopic), any(MqttMessage.class), any(), any());
    }

    @Test
    public void testSubscribeToTopic() throws Exception {
        when(mockMqttAndroidClient.subscribe(anyString(), anyInt(), any(), any())).thenReturn(mockMqttToken);

        mqttHandler.subscribeToTopic();

        verify(mockMqttAndroidClient).subscribe(eq(mqttHandler.subscriptionTopic), eq(0), any(), any());
    }
}