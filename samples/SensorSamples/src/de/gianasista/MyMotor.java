package de.gianasista;

import lejos.hardware.motor.Motor;
import lejos.robotics.RegulatedMotor;
import lejos.utility.Delay;

public class MyMotor {

	public static void main(String[] args) 
	{
		System.out.println("Starte Motoren");
		RegulatedMotor leftMotor = Motor.D;
		
		leftMotor.resetTachoCount();
	    leftMotor.rotateTo(0);
	    Delay.msDelay(1000);
	    leftMotor.setSpeed(400);
	    leftMotor.setAcceleration(800);
	    
	    leftMotor.setSpeed(400);
	    leftMotor.rotate(-120);
	    Delay.msDelay(1000);
	    
	    leftMotor.rotate(120);
	    Delay.msDelay(1000);
//        leftMotor.forward();
//        Delay.msDelay(100);
//        leftMotor.backward();
//        
//        Delay.msDelay(3000);
        System.out.println("Stoppe Motoren");
	}
}
