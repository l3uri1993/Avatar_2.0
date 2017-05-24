package pervasive.BandB;

import java.awt.datatransfer.FlavorTable;
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
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.HiTechnicCompass;
import lejos.hardware.sensor.SensorMode;
import lejos.utility.Delay;

public class Main
{
	static RegulatedMotor leftMotor;
	static RegulatedMotor rightMotor;
	static MovePilot pilot;
	static EV3UltrasonicSensor UltrasonicSensor;
	static EV3GyroSensor GyroScope;
	static EV3ColorSensor colorSensor;
	
	static boolean exit = false; 
	
	public static void main(String[] args) throws Exception {
		
		//*****MOTORI & RUOTE
    	Wheel leftWheel = WheeledChassis.modelWheel(Motor.D, 55.0).offset(60).gearRatio(1);
    	Wheel rightWheel = WheeledChassis.modelWheel(Motor.A, 55.0).offset(-60).gearRatio(1);
    	Chassis myChassis = new WheeledChassis( new Wheel[]{leftWheel, rightWheel}, WheeledChassis.TYPE_DIFFERENTIAL);
    	pilot = new MovePilot(myChassis);     	
    	pilot.setLinearSpeed(500);
    	
    	
    	//*****ULTRASUONI
		UltrasonicSensor = new EV3UltrasonicSensor(SensorPort.S4);
    	SampleProvider distance = UltrasonicSensor.getMode("Distance");
    	float[] ultrasonic_distance = new float[1];  	
    	
    	//*****GYROSCOPE
    	GyroScope = new EV3GyroSensor(SensorPort.S2);
    	SampleProvider angle = GyroScope.getAngleMode();
    	float[] gyroscope_angle = new float[1]; 
    	float[] previousangle = new float[1];
    	
    	//*****COLOR SENSOR
    	colorSensor =  new EV3ColorSensor(SensorPort.S3);
    	SensorMode color = colorSensor.getRedMode();
    	float[] colorsensor_color = new float[color.sampleSize()]; 
    	
    	//*****PROGRAMMA
    	float white = 0.93f;
		float black = 0.17f;
		float midpoint = (white-black)/2+black;
		
		pilot.forward();
    	while(Button.DOWN.isUp())
    	{
    		/*
    		distance.fetchSample(ultrasonic_distance, 0);
    		System.out.println(ultrasonic_distance[0]*100 + "cm");
    		
    		if(ultrasonic_distance[0]*100.0 < 24.0)
    		{
    			pilot.stop();
    			pilot.rotate(180);
    			distance.fetchSample(ultrasonic_distance, 0);
    			if(ultrasonic_distance[0]*100.0 > 24.0)
    	    		pilot.forward();
    			System.out.println(ultrasonic_distance[0]*100 + "cm");	
    		
    		color.fetchSample(colorsensor_color, 0);
    		System.out.println(colorsensor_color[0]);
    		Delay.msDelay(500);
    		*/
    		boolean exit = false;
    		float[] dentrowhile = new float[color.sampleSize()];
    		color.fetchSample(colorsensor_color, 0);
    		if(colorsensor_color[0]>midpoint)
    		{
    			angle.fetchSample(previousangle, 0);
    			angle.fetchSample(gyroscope_angle, 0);
    			pilot.rotate(30);
    			while(exit == false)
    			{    				
    				color.fetchSample(dentrowhile, 0);
    				if(dentrowhile[0] < midpoint)
    					exit = true;
    			}
    			pilot.stop();
    			pilot.forward();
    			exit = false;
    			color.fetchSample(colorsensor_color, 0);
    			if(colorsensor_color[0]>midpoint)
    			{
    				pilot.rotate(-60);
    				while(exit == false)
    				{    				
    					color.fetchSample(dentrowhile, 0);
        				if(dentrowhile[0] < midpoint)
        					exit = true;
    				}
        			exit = false;
    			}
				pilot.stop();
				pilot.forward();
    		}
    					
    	}
	}	
}

