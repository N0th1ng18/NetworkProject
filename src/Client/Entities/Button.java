package Client.Entities;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

public class Button {
	public int identity = -1;
	
	private int x;
	private int y;
	private int w;
	private int h;
	private Color defaultColor;
	private Color bColor;
	private Color tColor;
	private String str;
	private int strX;
	private int strY;
	private Font font;
	private boolean isInBounds;
	private boolean enabled;
	
	public Button(int x, int y, int w, int h, Color bColor, Color tColor, String str, Font font) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.defaultColor = bColor;
		this.bColor = bColor;
		this.tColor = tColor;
		this.str = str;
		this.font = font;
		enabled = true;
	}
	
	public void render(Graphics2D g2, double alpha) {
		g2.setFont(font);
		g2.setColor(bColor);
		g2.fillRect(x, y, w, h);
		g2.setColor(tColor);
		centerText(g2);
		g2.drawString(str, strX, strY);
	}
	
	public boolean checkBounds(float x, float y) {
		if(enabled) {
			isInBounds = false;
			if(x >= this.x && x < this.x + this.w && y >= this.y && y < this.y + this.h) {
				isInBounds = true;
			}
		}else {
			return false;
		}
		return isInBounds;
	}
	
	public void disable() {
		enabled = false;
	}
	
	public void enable() {
		enabled = true;
	}
	
	public void setBColor(Color c) {
		if(enabled) {
			this.bColor = c;
		}
	}
	
	public void setDefaultColor() {
		if(enabled) {
			if(bColor != defaultColor)
				bColor = defaultColor;
		}
	}
	
	public void setDefaultColor(Color c) {
		if(enabled) {
			defaultColor = c;
			if(bColor != defaultColor)
				bColor = defaultColor;
		}
	}
	
	public void setText(String s) {
		str = s;
	}
	
	public void centerText(Graphics2D g2) {
		FontMetrics met = g2.getFontMetrics();
		strX = this.x + (this.w - met.stringWidth(this.str)) / 2;
		strY = this.y + ((this.h - met.getHeight()) / 2) + met.getAscent();
	}
}
