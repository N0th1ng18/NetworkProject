package Client.Entities;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Label {
	int identity = -1;
	
	private int x;
	private int y;
	private int w;
	private int h;
	BufferedImage img;
	public String str;
	private int strX;
	private int strY;
	private boolean isTextLabel;
	Font font;
	Font prevFont;
	Color color;
	
	public boolean centerX = true;
	public boolean centerY = true;
	
	public Label(BufferedImage img, int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.img = img;
		isTextLabel = false;
	}
	
	public Label(String str, int x, int y, int w, int h, Font font, Color color) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.str = str;
		this.font = font;
		this.color = color;
		isTextLabel = true;
	}
	
	public void render(Graphics2D g2, double alpha) {
		if(isTextLabel == false) {
			g2.drawImage(img, x, y, w, h, null);
		}else{
			g2.setColor(color);
			prevFont = g2.getFont();
			g2.setFont(font);
			centerText(g2);
			g2.drawString(str, strX, strY);
			g2.setFont(prevFont);
		}
	}
	
	public void centerText(Graphics2D g2) {
		FontMetrics met = g2.getFontMetrics();
		
		if(centerX == true)
			strX = this.x + (this.w - met.stringWidth(this.str)) / 2;
		else
			strX = this.x;
		
		
		if(centerY == true)
			strY = this.y + ((this.h - met.getHeight()) / 2) + met.getAscent();
		else
			strY = this.y + met.getHeight()/2;
	}
}
