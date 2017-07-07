package pervasive.BandB;

import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.robotics.subsumption.Behavior;

public class DetectWall implements Behavior {
	int sampleSize = Avatar.touch.sampleSize();
	float[] sample = new float[sampleSize];
	
	@Override
	public boolean takeControl() {
		Avatar.touch.fetchSample(sample, 0);
		if (sample[0] == 0)
			return false;
		else
			return true;
	}

	@Override
	public void action() {
		Avatar.leftMotor.stop();
		Avatar.rightMotor.stop();
		System.exit(1);
	}

	@Override
	public void suppress() {
	}

}
