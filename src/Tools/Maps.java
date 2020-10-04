package Tools;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Maps {
	
	public static ArrayList<BufferedImage> maps = new ArrayList<BufferedImage>();
	
	
	public static void load() {
		
		//Map image
		try {
			BufferedImage map1 = ImageIO.read(Thread.currentThread().getContextClassLoader().getResourceAsStream("Sprites/map1.png"));
			maps.add(map1);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//Map Objects
		
		
	}

}
