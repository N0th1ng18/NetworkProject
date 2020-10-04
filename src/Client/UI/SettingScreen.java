package Client.UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Client.Entities.Button;
import Client.Entities.Entities;
import Client.Client;
import Client.MouseInput;

public class SettingScreen {
	
	public static boolean isLoaded = false;
	private static Button fullscreen;
	private static Button resolution;
	private static Button back;
	private static Font font = new Font("TimesRoman", Font.PLAIN, 60);
	
	private static boolean fullscreen_bool = false;
	
	private static int gwidth_save;
	private static int gHeight_save;
	
	public static void load() {
		isLoaded = true;
		
		fullscreen = new Button(100, 250, 400, 100, Color.GRAY, Color.BLACK, "Fullscreen", font);
		Entities.addButton(fullscreen);
		
		resolution = new Button(100, 400, 400, 100, Color.GRAY, Color.BLACK, Client.gWidth + "x" + Client.gHeight, font);
		Entities.addButton(resolution);
		
		back = new Button(100, 550, 400, 100, Color.GRAY, Color.BLACK, "Back", font);
		Entities.addButton(back);
		
	}
	
	public static void unload() {
		isLoaded = false;
		
		Entities.removeButton(fullscreen);
		Entities.removeButton(resolution);
		Entities.removeButton(back);
	}
	
	
	public static void checkButtons(float x, float y) {
		//Check Buttons
		for(int i=0; i < Entities.buttonCount; i++) {
			if(Entities.buttons[i].checkBounds(x, y) == true) {
				Entities.buttons[i].setBColor(Color.WHITE);
				
				if(MouseInput.leftPressed == true) {
					switch(Entities.buttons[i].identity) {
					case 1://Fullscreen
						fullscreen();
						MouseInput.leftPressed = false;
						break;
					case 2://Resolution
						if(fullscreen_bool == false) {
							setResolution();
						}
						MouseInput.leftPressed = false;
						break;
					case 3://Back
						UIManager.goTo(UIManager.MENU_SCREEN);
						MouseInput.leftPressed = false;
						break;
					}
				}
			}else {
				Entities.buttons[i].setDefaultColor();
			}
		}
	}
	
	public static void setResolution() {
		Entities.buttons[1].setDefaultColor(Color.GRAY);
        String string = JOptionPane.showInputDialog(Client.frame, "Example: 1920x1080", "Resolution", JOptionPane.PLAIN_MESSAGE);
        if(string != null && string.compareTo("") != 0) {
        	int index = string.indexOf('x');
        	
        	if(index >= 0) {
        		
        		//get X and Y 
        		String x_s = string.substring(0, index);
        		String y_s = string.substring(index+1, string.length());
        		
            	try {
    	        	int x = Integer.valueOf(x_s);
    	        	int y = Integer.valueOf(y_s);
    	        	
    	        	//Change Resolution
    				Client.gWidth = x;
    				Client.gHeight = y;
    				
    				Client.frame.dispose();
    				Client.frame.setSize(Client.gWidth, Client.gHeight);
    				Client.frame.setLocation((Client.sWidth/2) - (Client.gWidth/2), (Client.sHeight/2) - (Client.gHeight/2));
    				Client.frame.setUndecorated(false);
    				Client.frame.setVisible(true);
    	        	
    	        	Entities.buttons[1].setText(string);
    	        	Entities.buttons[1].setDefaultColor(Color.GRAY);
    	        	
    	        	
            	}catch(NumberFormatException e) {
            		Entities.buttons[1].setDefaultColor(Color.RED);
    	        	return;
    	        }
            	
        	}else {
        		Entities.buttons[1].setDefaultColor(Color.RED);
        		return;
        	}
        	

	        	
     
        }
	}
	
	public static void fullscreen() {
		if(fullscreen_bool == false) {
			fullscreen_bool = true;
			//Save Prev gwidth & gHeight
			gwidth_save = Client.gWidth;
			gHeight_save = Client.gHeight;
			//Set gWidth & gHeight to screen width and height
			Client.gWidth = Client.sWidth;
			Client.gHeight = Client.sHeight;
			//Correct Resolution Texxt
			Entities.buttons[1].setText(Client.gWidth + "x" + Client.gHeight);
			Entities.buttons[1].setDefaultColor(Color.DARK_GRAY);
			//set jframe to fullscreen
			Client.frame.dispose();
			Client.frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
			Client.frame.setUndecorated(true);
			Client.frame.setVisible(true);
			
			
		}else {
			fullscreen_bool = false;
			Client.gWidth = gwidth_save;
			Client.gHeight = gHeight_save;
			
			//Correct Resolution Texxt
			Entities.buttons[1].setText(Client.gWidth + "x" + Client.gHeight);
			Entities.buttons[1].setDefaultColor(Color.GRAY);
			
			Client.frame.dispose();
			Client.frame.setSize(Client.gWidth, Client.gHeight);
			Client.frame.setLocation((Client.sWidth/2) - (Client.gWidth/2), (Client.sHeight/2) - (Client.gHeight/2));
			Client.frame.setUndecorated(false);
			Client.frame.setVisible(true);
		}
	}
}
