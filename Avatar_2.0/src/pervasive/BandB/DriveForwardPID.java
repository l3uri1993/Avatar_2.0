package pervasive.lejos;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
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
			LineFollower.leftMotor.forward();
			LineFollower.rightMotor.forward();
			color = (int)(LineFollower.getColor()*100);
			error = color - threshold;
			integral = error + integral;
			derivative = error - lastError;
			correction = kp * error + ki * integral + kd * derivative;
			bTurn = 20 - correction;
			cTurn = 20 + correction;

			//message = "bT=" + new Double(bTurn).intValue() + " cT="
			//		+ new Double(cTurn).intValue();
			//LCD.drawString(message, 0, 6, false);

			LineFollower.leftMotor.setSpeed(new Double(bTurn).intValue());
			LineFollower.leftMotor.forward();
			LineFollower.rightMotor.setSpeed(new Double(cTurn).intValue());
			LineFollower.rightMotor.forward();

			lastError = error;			
			Thread.yield(); // don't exit till suppressed
		}
	}

	@Override
	public void suppress() {
		isSuppressed = true;
	}

}
