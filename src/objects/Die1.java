package objects;

import main.Game;

public class Die1 extends GameObject {

	public Die1(int x, int y, int objType) {
		super(x, y, objType);
		initHitbox(32, 16);
		xDrawOffset = 0;
		yDrawOffset = (int) (Game.SCALE * 16);
		hitbox.y += yDrawOffset;
	}
         
}
