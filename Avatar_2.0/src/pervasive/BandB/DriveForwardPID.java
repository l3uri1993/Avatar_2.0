package pervasive.BandB;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;

public class DriveForwardPID implements Behavior {
	private boolean isSuppressed = false;
	double kp = 0.5;
	double ki = 0; 
	double kd = 0; 
	int error = 0;
	int integral = 0;
	int derivative = 0;
	int lastError = 0;
	double correction = 0;
	int threshold = 34;
	int color = 0;
	double cTurn;
	double bTurn;
	boolean Debug = true;

	@Override
	public boolean takeControl() {
		return true;
	}

	@Override
	public void action() {
		isSuppressed = false;
		if(Debug == true)
		{
			Debug = false;
			LCD.clear();
			LCD.drawString("Ora pid, hit UP", 0, 6, false);
			Button.waitForAnyPress();
		}
		while (isSuppressed == false) {
			if(Button.DOWN.isDown() == true)
			{
				Debug = true;
				Avatar.leftMotor.stop();
				Avatar.rightMotor.stop();		
				Avatar.arbitrator.stop();
				Behavior b1 = new Follower();
				Behavior[] behaviorList = { b1 };			
				Avatar.arbitrator = new Arbitrator(behaviorList);
				LCD.clear();
				LCD.drawString("Pid->foll..up", 0, 6, false);
				Button.waitForAnyPress();
				Avatar.arbitrator.go();
			}
			Avatar.leftMotor.forward();
			Avatar.rightMotor.forward();
			color = (int)(Avatar.getColor()*100);
			error = color - threshold;
			integral = error + integral;
			derivative = error - lastError;
			correction = kp * error + ki * integral + kd * derivative;
			bTurn = Avatar.SPEED - correction;
			cTurn = Avatar.SPEED + correction;

			Avatar.leftMotor.setSpeed(new Double(bTurn).intValue());
			Avatar.leftMotor.forward();
			Avatar.rightMotor.setSpeed(new Double(cTurn).intValue());
			Avatar.rightMotor.forward();

			lastError = error;
		}
	}

	@Override
	public void suppress() {
		isSuppressed = true;
	}

}
