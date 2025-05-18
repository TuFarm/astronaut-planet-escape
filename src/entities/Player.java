package entities;

import static utilz.Constants.PlayerConstants.GetSpriteAmount;
import static utilz.Constants.PlayerConstants.*;
import static utilz.HelpMethods.CanMoveHere;
import utilz.LoadSave;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import main.Game;

public class Player extends Entity {

	private BufferedImage[][] animation;
	private int aniTick, aniIndex, aniSpeed = 30;
	private int playerAction = IDLE;
	private boolean moving = false, attacking = false;
	private boolean left, up, right, down;
	private float playerSpeed = 1.5f;
	private int[][] lvlData;
	private float xDrawOffset = 21 * Game.SCALE;
	private float yDrawOffset = 4 * Game.SCALE;

	public Player(float x, float y, int width, int height) {
		super(x, y, width, height);
		loadAnimations();
		initHitbox(x, y, 20 * Game.SCALE, 28 * Game.SCALE);
	}

	public void update() {

		updatePos();
		updateAnimationTick();
		setAnimation();

	}

	public void render(Graphics g) {
		g.drawImage(animation[playerAction][aniIndex], (int) (hitbox.x - xDrawOffset), (int) (hitbox.y - yDrawOffset),
				width, height, null);
		drawHitbox(g);
	}

	private void updateAnimationTick() {

		aniTick++;
		if (aniTick >= aniSpeed) {
			aniTick = 0;
			aniIndex++;
			if (aniIndex >= GetSpriteAmount(aniIndex)) {
				aniIndex = 0;
				attacking = false;
			}
		}

	}

	private void setAnimation() {
		int startAnimation = playerAction;

		if (moving) {
			playerAction = RUNNING;
		} else {
			playerAction = IDLE;
		}

		if (attacking) {
			playerAction = ATTACK_1;
		}

		if (startAnimation != playerAction) {
			resetAnimationTick();
		}
	}

	private void resetAnimationTick() {
		aniTick = 0;
		aniIndex = 0;

	}

	private void updatePos() {

		moving = false;
		if (!left && !right && !up && !down)
			return;

		float xSpeed = 0, ySpeed = 0;

		if (left && !right)
			xSpeed = -playerSpeed;
		else if (right && !left)
			xSpeed = playerSpeed;

		if (up && !down)
			ySpeed = -playerSpeed;
		else if (down && !up)
			ySpeed = playerSpeed;

//		if (CanMoveHere(x + xSpeed, y + ySpeed, width, height, lvlData)) {
//			this.x += xSpeed;
//			this.y += ySpeed;
//			moving = true;
//		}
		
		if (CanMoveHere(hitbox.x + xSpeed, hitbox.y + ySpeed, hitbox.width, hitbox.height, lvlData)) {
			hitbox.x += xSpeed;
			hitbox.y += ySpeed;
			moving = true;
		}
	}

	private void loadAnimations() {

		BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS);

		animation = new BufferedImage[9][6];

		for (int i = 0; i < animation.length; i++) {
			for (int j = 0; j < animation[i].length; j++) {
				animation[i][j] = img.getSubimage(j * 64, i * 40, 64, 40);
			}
		}
	}

	public void loadLvlData(int[][] lvlData) {
		this.lvlData = lvlData;
	}

	public void resetDirBooleans() {
		left = false;
		right = false;
		up = false;
		down = false;
	}

	public void setAttacking(boolean attacking) {
		this.attacking = attacking;
	}

	public boolean isLeft() {
		return left;
	}

	public void setLeft(boolean left) {
		this.left = left;
	}

	public boolean isUp() {
		return up;
	}

	public void setUp(boolean up) {
		this.up = up;
	}

	public boolean isRight() {
		return right;
	}

	public void setRight(boolean right) {
		this.right = right;
	}

	public boolean isDown() {
		return down;
	}

	public void setDown(boolean down) {
		this.down = down;
	}

}
