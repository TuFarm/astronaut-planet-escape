package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

public abstract class Entity {
	protected float x, y;
	protected int width, height;
	protected Rectangle2D.Float hitbox;

	public Entity(float x, float y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	protected void drawHitbox(Graphics g) {
		// For debugging the hitbox
		g.setColor(Color.PINK);
		g.drawRect((int) hitbox.x, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
	}

	protected void initHitbox(float x, float y, int width, int height) {
		// Create a rec hitbox at x, y
		hitbox = new Rectangle2D.Float(x, y, width, height);
	}

	// Create a new hitbox at new x, y when entities move
	// Only player and enemies class can access this method
//	protected void updateHitbox() {
//		hitbox.x = (int) x;
//		hitbox.y = (int) y;
//	}

	public Rectangle2D.Float getHitbox() {
		return hitbox;
	}
}
