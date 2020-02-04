# Tracked Vehicle Network
This repository contains the Backend for a tracked vehicle network. The project consists of tracked vehicles being controlled via a Web app (e.g. rotate left/right, drive forward) which also displays data measured by the vehicles (temperature, humidity, light intensity, gps). These talk to the Backend via MQTT (VerneMQ). The Web app communicates with the Backend via HTTP. Data is persisted in an Ignite cluster and in Kafka.

The VehicleManagementService folder contains the Backend application.

The MockAPIGateway folder contains a Backend application used for testing by the frontend team.

The MqttListener and MqttPublisher folders contain applications used for testing the communication with the vehicles.

If you have any questions about the application or you'd like to know how to run it then feel free to contact me via mmaresch.com.

# Dependencies
Thanks to everyone contributing to any of the following projects:
- Any Spring project
- Kafka
- Lombok
- Reactor
- Paho
- Ignite
- Vavr
- JJWT 
- ASP.NET Core
- SignalR
