package pervasive.BandB;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class Follower implements Behavior
{
	private String newzone = "2";
	private boolean Suppressed = false;
	String lastZone = "2";
	boolean Debug = true;

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
			//Overtaking();
			Avatar.leftMotor.setSpeed(Avatar.SPEED);
			Avatar.rightMotor.setSpeed(Avatar.SPEED);
			Avatar.leftMotor.forward();
			Avatar.rightMotor.forward();
		}
		while(!Suppressed)
		{
			synchronized (Avatar.zone)
			{
				newzone = Avatar.zone;
				if(Button.DOWN.isDown() == true)
				{
					Avatar.leftMotor.stop();
					Avatar.rightMotor.stop();

					Overtaking(); //Sorpasso

					Behavior b1 = new DriveForwardPID();
					Behavior[] behaviorList = { b1 };	
					Avatar.arbitrator.stop();
					Avatar.arbitrator = new Arbitrator(behaviorList);
					lastZone = newzone = "2";
					LCD.clear();
					LCD.drawString("foll->pid..up", 0, 6, false);
					Button.waitForAnyPress();
					Debug = true;
					Avatar.arbitrator.go();
				}
				if(newzone != lastZone)
				{
					lastZone = newzone;	
					switch (newzone)
					{
					case "0":
						Avatar.leftMotor.stop();
						Avatar.rightMotor.stop();
						break;
					case "1":
						Avatar.leftMotor.stop();
						Avatar.rightMotor.stop();
						break;
					case "2":
						StraightForward(); //Procede dritto
						break;
					case "3":
						Avatar.leftMotor.stop();
						Avatar.rightMotor.stop();
						break;
					case "4":
						Avatar.leftMotor.stop();
						Avatar.rightMotor.stop();
						break;
					default:
						//Stop
						break;
					}
				}
			}
		}
	}

	@Override
	public void suppress()
	{		
		Suppressed = true;
	}

	public static void Overtaking()
	{
		Turn(+57);
		goStraight(110);
		Turn(-57);
		goStraight(400);
		Turn(-57);
		goStraight(110);
		Turn(+57);
		goStraight(50);
	}

	void Curving(int angle)
	{
		Avatar.pilot.rotate(angle);
		Avatar.leftMotor.setSpeed(Avatar.SPEED);
		Avatar.rightMotor.setSpeed(Avatar.SPEED);
		Avatar.leftMotor.forward();
		Avatar.rightMotor.forward();
	}

	void StraightForward()
	{
		Avatar.leftMotor.setSpeed(Avatar.SPEED);
		Avatar.rightMotor.setSpeed(Avatar.SPEED);
		Avatar.leftMotor.forward();
		Avatar.rightMotor.forward();
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
