package de.gianasista;
import java.net.DatagramSocket;

import lejos.hardware.Button;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.I2CPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.port.UARTPort;
import lejos.hardware.sensor.EV3IRSensor;



public class TestSendSensorMessage {

	/*
    DatagramSocket Socket;
    static String destination = "legolass";
    static SendSensorMessage sSM1;
    static SendSensorMessage sSM2;
    static SendSensorMessage sSM3;
    static SendSensorMessage sSM4;
    static SendSensorMessage sSM5;

    public TestSendSensorMessage() {

    }

    public static void main(String[] args) throws InterruptedException {

       UltrasonicSensor uSensor = new UltrasonicSensor((I2CPort) SensorPort.S1);
       uSensor.continuous();
       CompassMindSensor compass = new CompassMindSensor((I2CPort)SensorPort.S2, 2);
       EV3IRSensor iRSensor = new EV3IRSensor((UARTPort)SensorPort.S3);
       GyroSensor gyroSensor = new GyroSensor((ADSensorPort)SensorPort.S4);

       
       while (!Button.ESCAPE.isDown()) {

        sSM1 = new SendSensorMessage(new SensorMessage
             (1, "EV3", "Compass",compass.getDegrees()),destination);
       
        sSM2= new SendSensorMessage(new SensorMessage
             (2, "EV3", "EV3IRSensor",iRSensor.getRange()),destination);
       
        sSM3 = new SendSensorMessage(new SensorMessage
             (3, "EV3", "GyroSensor",gyroSensor.getAngularVelocity()),destination);
       
        sSM4 = new SendSensorMessage(new SensorMessage
              (4, "EV3", "UltrasonicSensor",uSensor.getRange()),destination);

        sSM5 = new SendSensorMessage(new SensorMessage
               (5, "EV3", "MotorA",Motor.A.getTachoCount()),destination);
       
        sSM5 = new SendSensorMessage(new SensorMessage
                (6, "EV3", "MotorB",Motor.B.getTachoCount()),destination);
         
       }

    }
    */

}
