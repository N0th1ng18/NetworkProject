package Tools;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Images {
	
	public static ArrayList<BufferedImage> images = new ArrayList<BufferedImage>();
	
	public static void load() {
		try {
			BufferedImage imgRGB = ImageIO.read(Thread.currentThread().getContextClassLoader().getResourceAsStream("Sprites/Pixel_Face_Sheet.png"));
			images.add(imgRGB);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			BufferedImage img = ImageIO.read(Thread.currentThread().getContextClassLoader().getResourceAsStream("Sprites/logo.png"));
			images.add(img);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static BufferedImage get(int i) {
		return images.get(i);
	}
}
