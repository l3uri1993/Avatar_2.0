package pervasive.BandB;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class DriveForwardPID implements Behavior {
	private boolean isSuppressed = false;
	double kp = 1.2;
	double ki = 0.0008; 
	double kd = 5; 
	int error = 0;
	int integral = 0;
	int derivative = 0;
	int lastError = 0;
	double correction = 0;
	int threshold = 50;
	int color;
	double cTurn;
	double bTurn;	

	@Override
	public boolean takeControl() {
		return true;
	}

	@Override
	public void action() {
		isSuppressed = false;		

		while (!isSuppressed) {
			Avatar.leftMotor.forward();
			Avatar.rightMotor.forward();
			color = (int)(Avatar.getColor()*100);
			error = color - threshold;
			integral = error + integral;
			derivative = error - lastError;
			correction = kp * error + ki * integral + kd * derivative;
			bTurn = 20 - correction;
			cTurn = 20 + correction;

			//message = "bT=" + new Double(bTurn).intValue() + " cT="
			//		+ new Double(cTurn).intValue();
			//LCD.drawString(message, 0, 6, false);

			Avatar.leftMotor.setSpeed(new Double(bTurn).intValue());
			Avatar.leftMotor.forward();
			Avatar.rightMotor.setSpeed(new Double(cTurn).intValue());
			Avatar.rightMotor.forward();

			lastError = error;			
			Thread.yield(); // don't exit till suppressed
		}
		
		if(Button.DOWN.isDown() == true)
		{
			Avatar.leftMotor.stop();
			Avatar.rightMotor.stop();
			Avatar.arbitrator.stop();
			Behavior b1 = new Follower();
			Behavior b2 = new DetectWall();
			Behavior[] behaviorList = { b1, b2 };			
			Avatar.arbitrator = new Arbitrator(behaviorList);
			LCD.clear();
			LCD.drawString("Premi giu per continuare", 0, 6, false);
			Button.waitForAnyPress();
			Avatar.arbitrator.go();
		}
	}

	@Override
	public void suppress() {
		isSuppressed = true;
	}

}
