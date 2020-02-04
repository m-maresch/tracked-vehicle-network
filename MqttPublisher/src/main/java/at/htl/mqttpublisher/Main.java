package at.htl.mqttpublisher;

import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
    	var scanner = new Scanner(System.in);

		System.out.println("Use Fixed Sequence");
		boolean useFixedSequence = scanner.nextBoolean();

		var pingIndex = 1;
		var sequence = List.of(
				"{\"type\":\"registerVehicleRequest\",\"id\":2,\"name\":\"Vehicle 2\"}",
				"{\"type\":\"pingResponse\",\"id\":2}",
				"{\"type\":\"temperatureMeasuredEvent\",\"id\":2,\"value\":23.0}",
				"{\"type\":\"humidityMeasuredEvent\",\"id\":2,\"value\":52.0}",
				"{\"type\":\"lightIntensityMeasuredEvent\",\"id\":2,\"value\":80.0}",
				"{\"type\":\"gpsDataMeasuredEvent\",\"id\": 2,\"latitude\": 40.714224,\"longitude\": -73.961452}"
		);

	    MqttHandler handler = new MqttHandler("tcp://192.168.99.100:1883","MqttPublisher","iothub-in");
	    handler.start();
	    System.out.println("Connected");

		if (!useFixedSequence) {
			while (true) {
				var str = scanner.nextLine();
				if (str.equals("exit")) {
					break;
				}

				if (!str.equals("")) {
					handler.publish(str);
					System.out.println("Published");
				}
			}
		} else {
			for (String str : sequence) {
				handler.publish(str);
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
			}
			System.out.println("Published Sequence");

			System.out.println("Continue");
			if (scanner.nextBoolean()) {
				while (true) {
					handler.publish(sequence.get(pingIndex));
					System.out.println("Published");
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						throw new RuntimeException(e);
					}
				}
			}
		}

		handler.stop();

		System.exit(0);
	}
}
