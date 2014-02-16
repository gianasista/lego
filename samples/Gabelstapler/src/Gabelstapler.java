import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3IRSensor;
import lejos.robotics.MirrorMotor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;

public class Gabelstapler 
{
	static RegulatedMotor leftMotor = MirrorMotor.invertMotor(Motor.A);
    static RegulatedMotor rightMotor = MirrorMotor.invertMotor(Motor.B);
    static RegulatedMotor schaufelMotor = MirrorMotor.invertMotor(Motor.D);
    static IRSensor sensor;
    
    public static void main(String[] args)
    {
      leftMotor.resetTachoCount();
      rightMotor.resetTachoCount();
      schaufelMotor.resetTachoCount();
      
      leftMotor.rotateTo(0);
      rightMotor.rotateTo(0);
      schaufelMotor.rotateTo(0);
      
      leftMotor.setSpeed(400);
      rightMotor.setSpeed(400);
      schaufelMotor.setSpeed(200);
      
      leftMotor.setAcceleration(800);
      rightMotor.setAcceleration(800);
      schaufelMotor.setAcceleration(800);
      
      sensor = new IRSensor();
      sensor.setDaemon(true);
      sensor.start();
      
      Behavior b1 = new DriveForward();
      Behavior b2 = new DetectWall();
      Behavior schaufel = new Schaufel();
      Behavior[] behaviorList =
      {
        schaufel
      };
      
      Arbitrator arbitrator = new Arbitrator(behaviorList);
      LCD.drawString("Bumper Car",0,1);
      Button.LEDPattern(6);
      Button.waitForAnyPress();
      System.out.println("Los gehts");
      arbitrator.start();
    }
}

class IRSensor extends Thread
{
	public static final int BUTTON_TOP_LEFT = 1;
	public static final int BUTTON_BOTTOM_LEFT = 2;
	
    EV3IRSensor ir = new EV3IRSensor(SensorPort.S1);
    public int control = 0;
    public int distance = 255;

    IRSensor()
    {

    }
    
    public void run()
    {
        while (true)
        {
            float [] sample = new float[ir.sampleSize()];
            control = ir.getRemoteCommand(0);
            ir.fetchSample(sample, 0);
            distance = (int)sample[0];
            System.out.println("Control: " + control + " Distance: " + distance);
            
        }
        
    }
}

class DriveForward implements Behavior
{

  private boolean _suppressed = false;

  public boolean takeControl()
  {
    return true;
  }

  public void suppress()
  {
    _suppressed = true;
  }

  public void action()
  {
    _suppressed = false;
    while (!_suppressed)
    {
    	Gabelstapler.leftMotor.forward();
    	Gabelstapler.rightMotor.forward();
    	
    	if ((Button.waitForAnyPress() & Button.ID_ESCAPE) != 0)
        {
            Button.LEDPattern(0);
            System.exit(1);
        }
    	Thread.yield(); //don't exit till suppressed
    }
    Gabelstapler.leftMotor.stop(true); 
    Gabelstapler.rightMotor.stop(true);
  }
}


class DetectWall implements Behavior
{
	
  public DetectWall()
  {
  }
  
  private boolean checkDistance()
  {

      int dist = Gabelstapler.sensor.distance;
      if (dist < 30)
      {
          Button.LEDPattern(2);
          return true;
      }
      else
      {
          Button.LEDPattern(1);
          return false;
      }
  }
  
  public boolean takeControl()
  {
    return checkDistance();
  }

  public void suppress()
  {
    //Since  this is highest priority behavior, suppress will never be called.
  }

  public void action()
  {
      Gabelstapler.leftMotor.rotate(-180, true);// start Motor.A rotating backward
      Gabelstapler.rightMotor.rotate(-180);  // rotate C farther to make the turn
    if ((System.currentTimeMillis() & 0x1) != 0)
    {
    	Gabelstapler.leftMotor.rotate(-180, true);// start Motor.A rotating backward
    	Gabelstapler.rightMotor.rotate(180);  // rotate C farther to make the turn
    }
    else
    {
    	Gabelstapler.rightMotor.rotate(-180, true);// start Motor.A rotating backward
    	Gabelstapler.leftMotor.rotate(180);  // rotate C farther to make the turn        
    }
  }
}

class Schaufel implements Behavior
{
	private boolean _suppressed = false;
	
	@Override
	public boolean takeControl() 
	{
		return true; //Gabelstapler.sensor.control != 0;
	}

	@Override
	public void action() 
	{
		if(!_suppressed)
		{
			int control = Gabelstapler.sensor.control;
			switch (control)
			{
				case IRSensor.BUTTON_TOP_LEFT: schaufelHoch(); break;
				case IRSensor.BUTTON_BOTTOM_LEFT: schaufelRunter(); break;
			default:
				System.out.println("Anderer Button"); break;
			}
		}
	}
	
	private void schaufelHoch()
	{
		System.out.println("Hoch");
		Gabelstapler.schaufelMotor.rotate(-120);
	}
	
	private void schaufelRunter()
	{
		System.out.println("Runter");
		Gabelstapler.schaufelMotor.rotate(120);
	}

	@Override
	public void suppress() 
	{
		_suppressed = true;
	}
	
}
