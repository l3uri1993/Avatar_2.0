package pervasive.lejos;

import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.robotics.subsumption.Behavior;

public class DetectWall implements Behavior {
	int sampleSize = LineFollower.touch.sampleSize();
	float[] sample = new float[sampleSize];
	
	@Override
	public boolean takeControl() {
		LineFollower.touch.fetchSample(sample, 0);
		if (sample[0] == 0)
			return false;
		else
			return true;
	}

	@Override
	public void action() {
		LineFollower.leftMotor.stop();
		LineFollower.rightMotor.stop();
		System.exit(1);
	}

	@Override
	public void suppress() {
	}

}
