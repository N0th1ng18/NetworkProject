package Client.Entities;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Animation {
	
	private BufferedImage[] images;
	private int imageCount;
	
	private int currentImage;
	private double nextFrameTime;
	private boolean ready;
	
	
	public Animation(int imgSize) {
		images = new BufferedImage[imgSize];
		imageCount = 0;
		currentImage = -1;
		nextFrameTime = 0.0;
		ready = true;
	}
	public Animation(int imgSize, int defaultImage) {
		images = new BufferedImage[imgSize];
		imageCount = 0;
		currentImage = defaultImage;
		nextFrameTime = 0.0;
		ready = true;
	}
	
	public void play(double time, double frameRatePerSec) {
		//Get start time
		if(ready == true) {
			nextFrameTime = time;
			ready = false;
		}
		
		if(time >= nextFrameTime && currentImage < imageCount) {
			currentImage++;
			currentImage = currentImage % (imageCount);
			nextFrameTime = nextFrameTime + frameRatePerSec;
		}
	}
	
	
	public int getImageCount() {
		return imageCount;
	}
	
	public BufferedImage getImg() {
		if(currentImage > -1)
			return images[currentImage];
		else
			return null;
	}
	
	public void setImg(int i) {
		currentImage = i;
	}
	
	public void addImage(String path, int x, int y, int w, int h) {
		try {
			 BufferedImage img = ImageIO.read(new File(path));
			 if(img.getWidth() < w)w = img.getWidth();
			 if(img.getHeight() < h)h = img.getHeight();
			 images[imageCount] = img.getSubimage(x, y, w, h);
		} catch (IOException e) {
			e.printStackTrace();
		}
		imageCount++;
	}
	
	public void addImage(BufferedImage img, int x, int y, int w, int h) {
		 if(img.getWidth() < w)w = img.getWidth();
		 if(img.getHeight() < h)h = img.getHeight();
		 images[imageCount] = img.getSubimage(x, y, w, h);
		 imageCount++;
	}
}
