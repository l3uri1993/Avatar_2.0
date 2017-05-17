package pervasive.BandB;

import java.io.IOException;

import lejos.hardware.Button;
import lejos.hardware.motor.Motor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.MoveController;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.navigation.Navigator;
import lejos.utility.PilotProps;
import lejos.hardware.Sound;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.port.*;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.utility.Delay;

public class Main
{
	static RegulatedMotor leftMotor;
	static RegulatedMotor rightMotor;
	static MovePilot pilot;
	static EV3UltrasonicSensor UltrasonicSensor;
	
	static boolean exit = false; 
	
	public static void main(String[] args) throws Exception {
		
		//*****MOTORI & RUOTE
    	Wheel leftWheel = WheeledChassis.modelWheel(Motor.D, 55.0).offset(60).gearRatio(1);
    	Wheel rightWheel = WheeledChassis.modelWheel(Motor.A, 55.0).offset(-60).gearRatio(1);
    	Chassis myChassis = new WheeledChassis( new Wheel[]{leftWheel, rightWheel}, WheeledChassis.TYPE_DIFFERENTIAL);
    	pilot = new MovePilot(myChassis);     	
    	pilot.setLinearSpeed(200);
    	
    	//*****ULTRASUONI
		UltrasonicSensor = new EV3UltrasonicSensor(SensorPort.S4);
    	SampleProvider distance = UltrasonicSensor.getMode("Distance");
    	float[] sample = new float[1];  	
    	
    	
    	//*****PROGRAMMA
    	pilot.forward();
    	while(exit == false)
    	{
    		if(Button.DOWN.isDown())
    			exit = true;
    		
    		distance.fetchSample(sample, 0);
    		System.out.println(sample[0]*100 + "cm");
    		
    		if(sample[0]*100.0 < 24.0)
    		{
    			pilot.stop();
    			pilot.rotate(180);
    			distance.fetchSample(sample, 0);
    			if(sample[0]*100.0 > 24.0)
    	    		pilot.forward();
    			System.out.println(sample[0]*100 + "cm"); 			
    		}
    	}   	
	}
}
