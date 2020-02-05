# Tracked Vehicle Network
This project, developed by a few colleagues and me, consists of **tracked vehicles** being controlled via a **Web app** (e.g. rotate left/right, drive forward). Said app **displays data measured** by the vehicles (**temperature, humidity, light intensity, gps**). 

This repository contains (basically everything I developed) the **IoT-backend**, a **In-Memory Computing Platform-based, reactive Microservice**, and related software (e.g. used for testing). The vehicles talk to the IoT-backend via **MQTT** (VerneMQ). The Web app communicates with the IoT-backend via HTTP. Data is persisted in an **Ignite cluster** and in **Kafka**.

The VehicleManagementService folder contains the IoT-backend application.

The MockAPIGateway folder contains a Backend application used for testing by the frontend team.

The MqttListener and MqttPublisher folders contain applications used for testing the communication with the vehicles.

If you have any questions about the applications or you'd like to know how to run it then feel free to contact me via [mmaresch.com](http://mmaresch.com).

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
