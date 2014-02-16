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
		 
		/*
		System.out.println("L: 100 - R: 100");
		motorLeft.setSpeed(100);
		motorRight.setSpeed(100);
		motorLeft.forward();
		motorRight.forward();
		Thread.sleep(2000);
		
		System.out.println("L: 200 - R: 200");
		motorLeft.setSpeed(200);
		motorRight.setSpeed(200);
		motorLeft.forward();
		motorRight.forward();
		Thread.sleep(2000);
		
		System.out.println("L: 300 - R: 300");
		motorLeft.setSpeed(300);
		motorRight.setSpeed(300);
		motorLeft.forward();
		motorRight.forward();
		Thread.sleep(2000);
		
		System.out.println("L: 300 - R: 100");
		motorLeft.setSpeed(300);
		motorRight.setSpeed(100);
		motorLeft.forward();
		motorRight.forward();
		Thread.sleep(2000);
		
		System.out.println("L: 100 - R: 300");
		motorLeft.setSpeed(100);
		motorRight.setSpeed(300);
		motorLeft.forward();
		motorRight.forward();
		Thread.sleep(2000);
		*/
		LineDetector lineDetector = new LineDetector(new EV3ColorSensor(SensorPort.S1));
		lineDetector.setDaemon(true);
		lineDetector.start();
		
		int speedLeft = 0;
		int speedRight = 0;
		
		DetectionMode lastMode = null;
		
		while(!Button.ESCAPE.isDown())
		{
			System.out.println("Next Round");
			DetectionMode detectedMode = lineDetector.getDetectedMode();
			
			if (lastMode != detectedMode)
			{
				switch(detectedMode)
				{
				case BLACK: speedLeft =  0; speedRight =  100; System.out.println("\nBlack"); break;
				case EDGE : speedLeft =  100; speedRight =  100; System.out.println("\nEdge");break;
				case WHITE: speedLeft =  100; speedRight =  0; System.out.println("\nWhite");break;
				default:    speedLeft =  10; speedRight =  10; break;
				}
				System.out.println(String.format("SpeedLeft: %d - SpeedRight: %d", speedLeft, speedRight));
				motorLeft.setSpeed(speedLeft);
				motorRight.setSpeed(speedRight);
				motorLeft.forward();
				motorRight.forward();
			}
			try {
				Thread.sleep(200);
				//motorLeft.stop();
				//motorRight.stop();
				//System.out.println("Motoren gestoppt... Warten 5 sek");
				//Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		/*
		Behavior driveForward = new DriveRobot(motorLeft, motorRight, lineDetector);
		
		Behavior[] behaviourList = { driveForward };
		Arbitrator arbitrator = new Arbitrator(behaviourList);
		arbitrator.start();
		*/
	}
	
}

enum DetectionMode { BLACK, EDGE, WHITE	}

class DriveRobot implements Behavior
{
	private RegulatedMotor motorLeft;
	private RegulatedMotor motorRight;
	private LineDetector lineDetector;
	private boolean suppressed;
	
	//private int INITIAL_SPEED = 100;
	//private int SPEED_STEP = 25;
	private int speedLeft;
	private int speedRight;
	
	public DriveRobot(RegulatedMotor motorLeft, RegulatedMotor motorRight, LineDetector lineDetector)
	{
		this.motorLeft = motorLeft;
		this.motorRight = motorRight;
		this.lineDetector = lineDetector;
	}
	
	@Override
	public boolean takeControl() 
	{
		if(Button.readButtons() == Button.ID_ESCAPE)
		{
			suppressed = true;
			System.exit(1);
		}
		return true;
	}

	@Override
	public void action() 
	{
		System.out.println("\nDriveForward - action");
		while(!suppressed)
		{
			switch(lineDetector.getDetectedMode())
			{
				case BLACK: speedLeft =  20; speedRight =  60; System.out.println("\nB"); break;
				case EDGE : speedLeft =  60; speedRight =  60; System.out.println("\nE");break;
				case WHITE: speedLeft =  60; speedRight =  20; System.out.println("\nW");break;
				default:    speedLeft =  10; speedRight =  10; break;
			}
			motorLeft.setSpeed(speedLeft);
			motorRight.setSpeed(speedRight);
			motorLeft.forward();
			motorRight.forward();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			/*
			motorLeft.stop();
			motorRight.stop();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			*/
			Thread.yield();
		}
		motorLeft.stop();
		motorRight.stop();
		//System.exit(1);
	}

	@Override
	public void suppress() 
	{
		suppressed = true;
	}
	
}

class LineDetector extends Thread
{
	private SensorMode sensorMode;
	private float[] sample;
	
	private float lightValue;
	
	private static final float BLACK_VALUE = 0.04f;
	private static final float WHITE_VALUE = 0.88f;
	
	private float lowerEdgeThreshold;
	private float upperEdgeThreshold;
	
	//private static final float MIDDLE_VALUE = (BLACK_VALUE-WHITE_VALUE)/2f;
	
	public LineDetector(EV3ColorSensor colorSensor)
	{
		this.sensorMode = colorSensor.getRedMode();
		this.sample = new float[sensorMode.sampleSize()];
		
		float middle = (WHITE_VALUE - BLACK_VALUE)/2f;
		float border = (WHITE_VALUE - BLACK_VALUE)/6f; 
		System.out.println(String.format("Middle: %f - Border: %f - Test: %f", middle, border, 0.02f));
		this.lowerEdgeThreshold = middle - border;
		this.upperEdgeThreshold = middle + border;
		
		System.out.println(String.format("White: %f - Black: %f - LowerETH: %f - UpperETH: %f", WHITE_VALUE, BLACK_VALUE, lowerEdgeThreshold, upperEdgeThreshold));
	}
	
	public void run()
    {
        while (true)
        {
            sensorMode.fetchSample(this.sample, 0);
            lightValue = sample[0];
        }
    }
	
	public boolean isLineLost()
	{
		return lightValue > 0.1;
	}
	
	public DetectionMode getDetectedMode()
	{
		System.out.println("LightVal: ["+lightValue+"]");
		if(lightValue < lowerEdgeThreshold)
			return DetectionMode.BLACK;
		if(lightValue > upperEdgeThreshold)
			return DetectionMode.WHITE;
		
		return DetectionMode.EDGE;
	}
}
