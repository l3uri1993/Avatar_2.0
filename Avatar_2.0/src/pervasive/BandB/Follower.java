package pervasive.BandB;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class Follower implements Behavior
{
	public static String zone = "";
	private boolean Suppressed = false;
	String lastZone = "2";

	@Override
	public boolean takeControl()
	{
		return false;
	}

	@Override
	public void action()
	{	
		synchronized (zone)
		{
			if(zone != lastZone)
			{
				lastZone = zone;
				if(Button.DOWN.isDown() == true)
				{
					zone = "change";
					LCD.clear();
					LCD.drawString("INIZIATA PROCEDURA SCAMBIO", 0, 6, false);
				}
				switch (zone)
				{
				case "0":
					//Arco -45 gradi
					break;
				case "1":
					//Arco -25 gradi
					break;
				case "2":
					//Dritto
					break;
				case "3":
					//Arco +25 gradi
					break;
				case "4":
					//Arco +45 gradi
					break;
				case "change":
					Avatar.arbitrator.stop();
					while(zone != "2")
					{
						//ruota su se stesso
					}

					//Sorpasso

					Behavior b1 = new DriveForwardPID();
					Behavior b2 = new DetectWall();
					Behavior[] behaviorList = { b1, b2 };			
					Avatar.arbitrator = new Arbitrator(behaviorList);
					zone = "2";
					LCD.clear();
					LCD.drawString("Premi giu per continuare", 0, 6, false);
					Button.waitForAnyPress();
					Avatar.arbitrator.go();
					break;
				default:
					//Stop
					break;
				}
			}
		}
	}

	@Override
	public void suppress()
	{		
		Suppressed = true;
	}

}
