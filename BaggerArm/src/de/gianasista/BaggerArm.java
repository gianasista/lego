package de.gianasista;

import lejos.hardware.Button;
import lejos.hardware.motor.Motor;
import lejos.robotics.MirrorMotor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class BaggerArm 
{
    
	public static void main(String[] args) throws Exception 
	{
		System.out.println("Start Programm");

		Behavior roatetBehavior = new RotateArm();
		Behavior[] behaviors = { roatetBehavior };
		Arbitrator arbitrator = new Arbitrator(behaviors);
		System.out.println("Arbitrator gestartet");
		arbitrator.start();
	}
	
}

class RotateArm implements Behavior
{
	private boolean _suppressed = false;
	static RegulatedMotor rotationMotor = Motor.A;	
	
	public RotateArm() 
	{
		rotationMotor.setSpeed(10);
	}
	
	@Override
	public boolean takeControl() 
	{
		return true;
	}

	@Override
	public void action() 
	{
		System.out.println("Take control");
		while (!_suppressed)
		{
			if(Button.LEFT.isDown())
		    {
		    	System.out.println("Left");
		    	rotationMotor.forward();
		    }
		    else if(Button.RIGHT.isDown())
		    {
		    	System.out.println("Right");
		    	rotationMotor.backward();
		    }
		    else if(Button.ESCAPE.isDown())
		    {
		    	System.exit(1);
		    }
		    else
		    {
		    	rotationMotor.stop();
		    }
		}
		
		try 
		{
			Thread.sleep(1000);
		} 
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
	}

	@Override
	public void suppress() 
	{
		_suppressed = true;		
	}
	
}
