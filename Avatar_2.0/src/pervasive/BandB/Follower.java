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
	
	double kp = 0.5;
	double ki = 0.0000; 
	double kd = 0.05; 
	int error = 0;
	int integral = 0;
	int derivative = 0;
	int lastError = 0;
	double correction = 0;
	int threshold = 326;
	int center = 326;
	double cTurn;
	double bTurn;

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
		Avatar.leftMotor.setSpeed(Avatar.SPEED);
		Avatar.rightMotor.setSpeed(Avatar.SPEED);
		while(!Suppressed)
		{
			PID();
			/*
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
						Avatar.pilot.stop();
						Avatar.pilot.rotate(-30);
						Avatar.pilot.forward();
						break;
					case "1":
						Avatar.pilot.stop();
						Avatar.pilot.rotate(-20);
						Avatar.pilot.forward();
						break;
					case "2":
						StraightForward(); //Procede dritto
						break;
					case "3":
						Avatar.pilot.stop();
						Avatar.pilot.rotate(20);
						Avatar.pilot.forward();
						break;
					case "4":
						Avatar.pilot.stop();
						Avatar.pilot.rotate(30);
						Avatar.pilot.forward();
						break;
					default:
						//Stop
						break;
					}
				}
			}*/
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

	void PID()
	{
		synchronized (Avatar.zone) {
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

			Avatar.leftMotor.forward();
			Avatar.rightMotor.forward();
			center = Integer.parseInt(Avatar.zone);
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
			//Thread.yield(); // don't exit till suppressed
		}
	}

	void StraightForward()
	{

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
