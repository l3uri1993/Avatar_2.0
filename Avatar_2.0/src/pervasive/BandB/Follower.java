package pervasive.BandB;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class Follower implements Behavior
{
	private boolean Suppressed = false;
	boolean Debug = true;

	double kp = 0.4;
	double ki = 0.0000; 
	double kd = 0.0; 
	int error = 0;
	int integral = 0;
	int derivative = 0;
	int lastError = 0;
	double correction = 0;
	int threshold = 326;
	int center = 326;
	double cTurn;
	double bTurn;
	int newzone = 326;

	@Override
	public boolean takeControl()
	{
		return true;
	}

	@Override
	public void action()
	{
		Suppressed = false;
		if(Debug == true)
		{
			Debug = false;
			LCD.clear();
			LCD.drawString("Ora foll, hit UP", 0, 6, false);
			Button.waitForAnyPress();
			Avatar.leftMotor.forward();
			Avatar.rightMotor.forward();
		}
		while(!Suppressed)
		{			
			if(Button.DOWN.isDown() == true)
			{
				Avatar.leftMotor.stop();
				Avatar.rightMotor.stop();
				Overtaking(); //Sorpasso
				Behavior b1 = new DriveForwardPID();
				Behavior[] behaviorList = { b1 };	
				Avatar.arbitrator.stop();
				Avatar.arbitrator = new Arbitrator(behaviorList);
				newzone = 326;
				LCD.clear();
				LCD.drawString("foll->pid..up", 0, 6, false);
				Button.waitForAnyPress();
				Debug = true;
				Avatar.arbitrator.go();
			}

			Avatar.leftMotor.forward();
			Avatar.rightMotor.forward();
			center = Avatar.getZone();
			error = center - threshold;
			integral = error + integral;
			derivative = error - lastError;
			correction = kp * error + ki * integral + kd * derivative;
			bTurn = Avatar.SPEED - correction;
			cTurn = Avatar.SPEED + correction;

			Avatar.leftMotor.setSpeed(new Double(cTurn).intValue());
			Avatar.leftMotor.forward();
			Avatar.rightMotor.setSpeed(new Double(bTurn).intValue());
			Avatar.rightMotor.forward();

			lastError = error;			
		}
	}

	@Override
	public void suppress()
	{		
		Suppressed = true;
	}

	public static void Overtaking()
	{
		Turn(-57);
		goStraight(130);
		Turn(+57);
		goStraight(465);
		Turn(+57);
		goStraight(130);
		Turn(-57);
		goStraight(75);
	}

	public static void Turn(int angle)
	{
		Avatar.pilot.rotate(angle);
	}

	public static void goStraight(int mm)
	{
		Avatar.pilot.travel(mm);
	}

}
