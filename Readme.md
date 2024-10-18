# Daily Carbon Footprint Calculation Application
The project's primary goal is to help individuals measure their daily carbon footprint based on their means of transportation, food consumption, and household electrical appliances usage. It aims to promote environmentally friendly lifestyle choices and compare everyone's footprint to the global average of users.

## Introduction
The solution provided is a distributed system with four primary components: a desktop GUI, an Android application, an MQTT broker and the Node-RED flow-editing platform.

## Design of the System

### Global Architecture Description
The user's entry point consists of a desktop GUI and/or an Android application, integrated through a local Mosquitto MQTT broker running on a virtual machine for the publisher-subscriber system and connected to Node-RED to calculate the daily average CO2 consumption. Users can calculate and publish their daily CO2 consumption to the MQTT broker and subscribe to receive the daily average consumption data of all users.

### Node-RED Flows
The Node-RED flow establishes a connection to the MQTT broker, processes incoming user data, and computes the daily average carbon footprint, which is published to all users. Functions like "check date" ensure data relevance, and "start reset" resets the average daily.

### MQTT Topics and Messages
- `footprint/userdata`: Users publish their CO2 consumption data with QoS 2 to make accurate calculations.
- `footprint/average`: Users receive the average CO2 consumption. The Node-RED server publishes to this topic and handles disconnection notices through Last Will Message.

### Producing and Consuming Semantics in MQTT
- `footprint/userdata` uses exactly-once semantics (QoS 2) for accurate user data processing.
- `footprint/average` uses at-most-once semantics (QoS 0), ensuring low latency with a potential rare loss of data.

## User Application Implementation
The Carbon Footprint Calculator application is developed as a Java Swing-based GUI for desktop and an Android application. It allows users to enter daily consumption data, calculate CO2 emissions, and compare them with the global average. The application ensures real-time data accuracy and promotes environmental awareness.

## Future Work
- Explore Apache Kafka for scalability and detailed data distribution.
- Develop personalized insights and recommendations, and expand input options for a more accurate carbon footprint calculation.

