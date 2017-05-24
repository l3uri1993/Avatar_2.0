package pervasive.BandB;

import lejos.hardware.lcd.LCD;
import lejos.robotics.Color;
import lejos.robotics.SampleProvider;

public class ColorDetector implements Runnable {

	@Override
	public void run() {
		SampleProvider sampleProvider = LineFollower.colorSensor.getRedMode();
		LineFollower.colorSensor.setFloodlight(Color.RED);
		int sampleSize = sampleProvider.sampleSize();
		float[] colorSamples = new float[sampleSize];
		
		while (true) {
			sampleProvider.fetchSample(colorSamples, 0);
			LineFollower.setColor(colorSamples[0]);
			LCD.drawString("Light: " + colorSamples[0], 0, 4);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
