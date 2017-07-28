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
			LCD.drawString("Cambio da PID a Follower effettuato, premi SU per continuare!!!", 0, 6, false);
			Button.waitForAnyPress();
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
					Avatar.arbitrator.stop();
					while(Avatar.zone != "2")
					{
						TurningMode(); //Gira su se stesso
					}

					Overtaking(); //Sorpasso

					Behavior b1 = new DriveForwardPID();
					Behavior[] behaviorList = { b1 };			
					Avatar.arbitrator = new Arbitrator(behaviorList);
					lastZone = newzone = "2";
					LCD.clear();
					LCD.drawString("Sto per passare a PID, premi SU per continuare!!!", 0, 6, false);
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
						Curving(-45); //Inizia arco con curvatura angolare
						break;
					case "1":
						Curving(-25);
						break;
					case "2":
						StraightForward(); //Procede dritto
						break;
					case "3":
						Curving(25);
						break;
					case "4":
						Curving(45);
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

	void Overtaking()
	{

	}

	void Curving(int angle)
	{

	}

	void StraightForward()
	{
		Avatar.leftMotor.setSpeed(20);
		Avatar.rightMotor.setSpeed(20);
		Avatar.leftMotor.forward();
		Avatar.rightMotor.forward();
	}

	void TurningMode()
	{

	}

}
