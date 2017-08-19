package pervasive.BandB;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.Font;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.PilotProps;

public class Avatar {
	final static int SPEED = 70;
	static EV3LargeRegulatedMotor leftMotor = new EV3LargeRegulatedMotor(MotorPort.B);
	static EV3LargeRegulatedMotor rightMotor = new EV3LargeRegulatedMotor(MotorPort.C);
	static EV3ColorSensor colorSensor = new EV3ColorSensor(SensorPort.S2);
	static float lastColor = 0.0f;
	static Arbitrator arbitrator;
	static String zone = "2";
	static Wheel wheel1 = WheeledChassis.modelWheel(leftMotor, 43.2).offset(-72);
	static Wheel wheel2 = WheeledChassis.modelWheel(rightMotor, 43.2).offset(72);
	static Chassis chassis = new WheeledChassis(new Wheel[] { wheel1, wheel2 }, WheeledChassis.TYPE_DIFFERENTIAL); 
	static MovePilot pilot = new MovePilot(chassis);
	
	
	public static void setColor(float color) {
		synchronized (colorSensor) {
			lastColor = color;
		}
	}
	public static float getColor() {
		synchronized (colorSensor) {
			return lastColor;
		}
	}
	public static int getZone() {
		synchronized (Avatar.zone) {
			return Integer.parseInt(Avatar.zone);
		}
	}

	public static void main(String[] args) {		

		leftMotor.resetTachoCount();
		rightMotor.resetTachoCount();
		leftMotor.setSpeed(SPEED);
		rightMotor.setSpeed(SPEED);
		leftMotor.setAcceleration(80);
		rightMotor.setAcceleration(80);
		
		pilot.setLinearSpeed(SPEED);
		pilot.setAngularSpeed(SPEED);
			
		ExecutorService taskList = Executors.newFixedThreadPool(10);
		taskList.execute(new ServerThread());
		taskList.execute(new ColorDetector());
		Behavior b1 = new Follower();
		Behavior[] behaviorList = { b1 };

		arbitrator = new Arbitrator(behaviorList);
		LCD.drawString("Avatar", 0, 1);
		arbitrator.go();

		taskList.shutdown();
	}
}
