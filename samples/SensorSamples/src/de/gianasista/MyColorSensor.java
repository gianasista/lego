package de.gianasista;

import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.Font;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.lcd.LCD;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.hardware.sensor.SensorModes;
import lejos.internal.ev3.EV3Port;
import lejos.robotics.Color;
import lejos.utility.Delay;

public class MyColorSensor {

	public static void main(String[] args) {
		
		LCD.drawString("Test", 10, 10);
		Delay.msDelay(1000);
		
		Port p = LocalEV3.get().getPort("S4");
		EV3ColorSensor sensor = new EV3ColorSensor(p);
		
		for(;;)
		{
			LCD.clear();
			while (!Button.ESCAPE.isDown())
			{
				SensorMode sensorMode = sensor.getMode(sensor.getColorIDMode().getName());
				LCD.drawString("Mode: " + sensorMode, 0, 0);
				int sampleSize = sensorMode.sampleSize();
				LCD.drawString("Samples:" + sampleSize, 0, 1);
				float samples[] = new float[sampleSize];
				sensorMode.fetchSample(samples, 0);
				for(int i = 0; i < sampleSize; i++)
				{
					LCD.drawString("Val[" + i + "]: " + samples[i], 2, i+2);
				}
				LCD.refresh();
				Delay.msDelay(3000);
			}
			LCD.clear();
			while(Button.ESCAPE.isDown())
				Delay.msDelay(2000);
		}
	}

}
