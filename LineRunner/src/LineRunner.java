//import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.Button;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.hardware.sensor.SensorModes;
import lejos.robotics.MirrorMotor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;


public class LineRunner 
{
	
	public static void main(String[] args) throws Exception
	{
		System.out.println("Programmstart");
		//RegulatedMotor motorLeft = new EV3LargeRegulatedMotor(MotorPort.A);
		RegulatedMotor motorLeft = Motor.B;
		//RegulatedMotor motorRight = new EV3LargeRegulatedMotor(MotorPort.B); 
		RegulatedMotor motorRight = Motor.A;
		motorLeft.resetTachoCount();
		motorRight.resetTachoCount();
		motorLeft.rotate(0);
		motorRight.rotate(0);
		motorLeft.setAcceleration(800);
		motorRight.setAcceleration(800);
		System.out.println("Motoren erzeugt");
		
		LineDetector lineDetector = new LineDetector(new EV3ColorSensor(SensorPort.S1));
		lineDetector.setDaemon(true);
		lineDetector.start();
		
		System.out.println("Hack start");
		motorLeft.setSpeed(100);
		motorRight.setSpeed(100);
		lineDetector.getSensorValue();
		motorLeft.forward();
		motorRight.forward();
		lineDetector.getSensorValue();
		lineDetector.getSensorValue();
		
		try {
			Thread.sleep(3000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Hack stop");
	
		
		int speedLeft = 0;
		int speedRight = 0;
		
		int targetPowerLevel = 100;
		int offset = 31; // (65 - 4)/2 aka Middle White and Black Value
		int slopeAkaKp = 4; // (0-100)/-31-0)
		
		while(!Button.ESCAPE.isDown())
		{
			int lightValue = lineDetector.getSensorValue();
			int error = lightValue - offset;
			int turn = slopeAkaKp * error;
			speedLeft = targetPowerLevel + turn;
			speedRight = targetPowerLevel - turn;
			
			System.out.println(String.format("LightValue: %d, Error: %d, Turn: %d, Left: %d, Right: %d", lightValue, error, turn, speedLeft, speedRight));
			
			motorLeft.setSpeed(speedLeft);
			motorRight.setSpeed(speedRight);
			
			motorRight.forward();
			motorLeft.forward();
			
			try {
				Thread.sleep(10);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}

class LineDetector extends Thread
{
	private SensorMode sensorMode;
	private float[] sample;
	
	private float lightValue;
	
	public LineDetector(EV3ColorSensor colorSensor)
	{
		this.sensorMode = colorSensor.getRedMode();
		this.sample = new float[sensorMode.sampleSize()];
	}
	
	public void run()
    {
        while (true)
        {
            sensorMode.fetchSample(this.sample, 0);
            lightValue = sample[0];
        }
    }
	
	public int getSensorValue()
	{
		return (int)(lightValue * 100);
	}
}
