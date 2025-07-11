package entities;

import static utilz.Constants.AlienConstants.*;
import static utilz.Constants.Direction.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import main.Game;

public class Slime extends Alien {//crabby

	private int attackBoxOffsetX;

	public Slime(float x, float y) {
		super(x, y, ALIEN_WIDTH, ALIEN_HEIGHT, SLIME);
		initHitbox(22, 19);
		initAttackBox();
	}

	private void initAttackBox() {
		attackBox = new Rectangle2D.Float(x, y, (int) (82 * Game.SCALE), (int) (19 * Game.SCALE)); // 30 + 20 + 30 == 82
		attackBoxOffsetX = (int) (Game.SCALE * 30);

	}

	public void update(int[][] lvlData, Player player) {
		updateBehavior(lvlData, player);
		updateAnimationTick();
		updateAttackBox();

	}

	private void updateAttackBox() {
		attackBox.x = hitbox.x - attackBoxOffsetX;
		attackBox.y = hitbox.y;

	}

	private void updateBehavior(int[][] lvlData, Player player) {
		if (firstUpdate)
			firstUpdateCheck(lvlData);

		if (inAir)
			updateInAir(lvlData);
		else {
			switch (state) {
			case IDLE:
				newState(RUNNING);
				break;
			case RUNNING:
				if (canSeePlayer(lvlData, player))
					turnTowardsPlayer(player);
				if (isPlayerCloseForAttack(player))
					newState(ATTACK);

				move(lvlData);
				break;
			case ATTACK:
				if(aniIndex == 0) 
					attackChecked = false;
				
				if(aniIndex == 3 && !attackChecked) { // Index 3 is the attacking index
					checkAlienHit(attackBox, player);
				}
				break;
			case HIT:
				break;
			}
		}

	}

	public void drawAttackBox(Graphics g, int xLvlOffset) {
		g.setColor(Color.red);
//		g.drawRect((int) (attackBox.x - xLvlOffset), (int) attackBox.y, (int) attackBox.width, (int) attackBox.height);
	}

	public int flipX() {
		if (walkDir == RIGHT)
			return width;
		else
			return 0;
	}

	public int flipW() {
		if (walkDir == RIGHT)
			return -1;
		else
			return 1;
	}

}
