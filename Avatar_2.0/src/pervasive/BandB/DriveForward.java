package pervasive.BandB;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.robotics.subsumption.Behavior;

public class DriveForward implements Behavior {
	private boolean isSuppressed = false;

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
			if (LineFollower.getColor() > 0.42) {
				LCD.drawString("Turn right", 0, 6);
				LineFollower.leftMotor.setSpeed(50);
				LineFollower.rightMotor.setSpeed(150);
			}
			else {
				LCD.drawString("Turn left", 0, 6);
				LineFollower.leftMotor.setSpeed(150);
				LineFollower.rightMotor.setSpeed(50);
			}
			Thread.yield(); // don't exit till suppressed
		}
	}

	@Override
	public void suppress() {
		isSuppressed = true;
	}

}
