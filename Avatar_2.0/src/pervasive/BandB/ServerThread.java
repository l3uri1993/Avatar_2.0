package pervasive.BandB;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import lejos.hardware.lcd.LCD;

public class ServerThread implements Runnable {
	ServerSocket serverSocket = null;
	Socket socket = null;
	BufferedReader readFromClient = null;
	BufferedReader readFromServer = null;
	PrintWriter writeToClient = null;
	String message = null;

	public ServerThread() {
		try {
			serverSocket = new ServerSocket(5555);
			System.out.println("Waiting for connection...");
			socket = serverSocket.accept();
			System.out.println("Connection accepted...");

			readFromClient = new BufferedReader(
					new InputStreamReader(
							socket.getInputStream()));
			writeToClient = new PrintWriter(
					socket.getOutputStream(), true);
			readFromServer = new BufferedReader(
					new InputStreamReader(
							System.in));

			new Thread(this).start();

			/* Parte inutile...credo
			 	while(true) 
			{
				message = readFromServer.readLine(); 
				writeToClient.println(message);
				writeToClient.flush(); 
				if(message.equalsIgnoreCase("exit")) 
				{
					System.exit(0);
				} 
			}
			
			*/

		} catch(IOException exp) {
			exp.printStackTrace();
		}
	}

	public void run() { 
		try {
			while(true) { 
				String msg = readFromClient.readLine(); 
				if(!msg.equalsIgnoreCase("exit")) {
					synchronized (Avatar.zone)
					{
						Avatar.zone = msg;
						LCD.clear();
						LCD.drawString(Avatar.zone, 0, 6, false);
					}
				}  
				Thread.sleep(100);
			}
		} catch(Exception exp) {
			exp.printStackTrace();
		}
	} 
}