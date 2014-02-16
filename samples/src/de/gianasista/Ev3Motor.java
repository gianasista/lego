package de.gianasista;

import lejos.hardware.motor.Motor;
import lejos.robotics.RegulatedMotor;

public class Ev3Motor {

	public static void main (String[] args) {
	    RegulatedMotor steer = Motor.A;
	    RegulatedMotor left = lejos.robotics.MirrorMotor.invertMotor(Motor.D);
	    RegulatedMotor right = lejos.robotics.MirrorMotor.invertMotor(Motor.B);
	    steer.rotateTo(100);
	    right.setSpeed(110);
	    left.setSpeed(90);
	    right.rotate(2000, true);
	    left.rotate(1800, true);
	    System.out.println("got here");
	    while (left.isMoving() || right.isMoving());

	    System.out.println("got here");
	    left.resetTachoCount();
	    right.resetTachoCount();
	    right.rotate(-2000, true);
	    left.rotate(-1800, true);
	    System.out.println("got here");
	    while (left.isMoving() || right.isMoving());
	    System.out.println("got here");
	  }
}

