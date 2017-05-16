package pervasive.BandB;

import java.io.IOException;

import lejos.hardware.Button;
import lejos.hardware.motor.Motor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.MoveController;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.navigation.Navigator;
import lejos.utility.PilotProps;

public class Main
{
	static RegulatedMotor leftMotor;
	static RegulatedMotor rightMotor;
	
	public static void main(String[] args) throws Exception {
		
		MovePilot pilot;
    	
    	Wheel leftWheel = WheeledChassis.modelWheel(Motor.D, 55.0).offset(60).gearRatio(1);
    	Wheel rightWheel = WheeledChassis.modelWheel(Motor.A, 55.0).offset(-60).gearRatio(1);
    	Chassis myChassis = new WheeledChassis( new Wheel[]{leftWheel, rightWheel}, WheeledChassis.TYPE_DIFFERENTIAL);
    	
    	pilot = new MovePilot(myChassis);    
    	
    	pilot.setAngularSpeed(pilot.getMaxAngularSpeed());
    	pilot.setLinearSpeed(400);
    	
    	pilot.travel(300);
    	pilot.travel(-300);
    	pilot.rotate(1080);
    	pilot.rotate(1080);

	}
}
