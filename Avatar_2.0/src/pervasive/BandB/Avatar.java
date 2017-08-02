package pervasive.BandB;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.Font;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class Avatar {
	static EV3LargeRegulatedMotor leftMotor = new EV3LargeRegulatedMotor(MotorPort.B);
	static EV3LargeRegulatedMotor rightMotor = new EV3LargeRegulatedMotor(MotorPort.C);
	//static EV3TouchSensor touch = new EV3TouchSensor(SensorPort.S1);
	//static EV3ColorSensor colorSensor = new EV3ColorSensor(SensorPort.S3);
	static float lastColor = 0.0f;
	static Arbitrator arbitrator;
	static String zone = "2";
	
	/*public static void setColor(float color) {
		synchronized (colorSensor) {
			lastColor = color;
		}
	}
	public static float getColor() {
		synchronized (colorSensor) {
			return lastColor;
		}
	}*/

	public static void introMessage() {

		GraphicsLCD g = LocalEV3.get().getGraphicsLCD();
		g.drawString("Line follower Demo", 5, 0, 0);
		g.setFont(Font.getSmallFont());
		g.drawString("Demonstration of the Behavior", 2, 20, 0);
		g.drawString("subsumption classes. Requires", 2, 30, 0);
		g.drawString("a wheeled vehicle with two", 2, 40, 0);
		g.drawString("independently controlled", 2, 50, 0);
		g.drawString("motors connected to motor", 2, 60, 0);
		g.drawString("ports B and C, and a", 2, 70, 0);
		g.drawString("color sensor connected", 2, 80, 0);
		g.drawString("to port 3.", 2, 90, 0);

		// Quit GUI button:
		g.setFont(Font.getSmallFont()); // can also get specific size using
										// Font.getFont()
		int y_quit = 100;
		int width_quit = 45;
		int height_quit = width_quit / 2;
		int arc_diam = 6;
		g.drawString("QUIT", 9, y_quit + 7, 0);
		g.drawLine(0, y_quit, 45, y_quit); // top line
		g.drawLine(0, y_quit, 0, y_quit + height_quit - arc_diam / 2); // left
																		// line
		g.drawLine(width_quit, y_quit, width_quit, y_quit + height_quit / 2); // right
																				// line
		g.drawLine(0 + arc_diam / 2, y_quit + height_quit, width_quit - 10, y_quit + height_quit); // bottom
																									// line
		g.drawLine(width_quit - 10, y_quit + height_quit, width_quit, y_quit + height_quit / 2); // diagonal
		g.drawArc(0, y_quit + height_quit - arc_diam, arc_diam, arc_diam, 180, 90);

		// Enter GUI button:
		g.fillRect(width_quit + 10, y_quit, height_quit, height_quit);
		g.drawString("GO", width_quit + 15, y_quit + 7, 0, true);

		Button.waitForAnyPress();
		if (Button.ESCAPE.isDown())
			System.exit(0);
		g.clear();
	}

	public static void main(String[] args) {

		introMessage();
		leftMotor.resetTachoCount();
		rightMotor.resetTachoCount();
		leftMotor.rotateTo(0);
		rightMotor.rotateTo(0);
		leftMotor.setSpeed(40);
		rightMotor.setSpeed(40);
		leftMotor.setAcceleration(80);
		rightMotor.setAcceleration(80);
		
		ExecutorService taskList = Executors.newFixedThreadPool(10);
		taskList.execute(new ServerThread());
		taskList.execute(new ColorDetector());
		Behavior b1 = new Follower();
		Behavior[] behaviorList = { b1 };
		
		arbitrator = new Arbitrator(behaviorList);
		LCD.drawString("Avatar", 0, 1);
		Button.LEDPattern(6);
		Button.waitForAnyPress();
		arbitrator.go();

		taskList.shutdown();
	}
}
