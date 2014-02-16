package de.gianasista;

import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3IRSensor;
import lejos.utility.Delay;

public class MyIR {

	public static void main(String[] args) 
	{
		EV3IRSensor ir = new EV3IRSensor(SensorPort.S2);
		float [] sample = new float[ir.sampleSize()];
		int control = 0;
		int distance = 0;
		System.out.println("Starte mit dem Auslesen...");
		
		for(;;)
		{
			control = ir.getRemoteCommand(0);
			ir.fetchSample(sample, 0);
			distance = (int)sample[0];
			System.out.println("Control: " + control + " Distance: " + distance);
			Delay.msDelay(500);
		}
	}

}
