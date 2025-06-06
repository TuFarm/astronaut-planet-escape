package entities;

import static utilz.Constants.AlienConstants.*;
import static utilz.HelpMethods.*;
import static utilz.Constants.*;

import java.awt.geom.Rectangle2D;
import java.util.spi.CurrencyNameProvider;

import static utilz.Constants.Direction.*;

import main.Game;

public abstract class Alien extends Entity {//enemy

	protected int enemyType;
	protected boolean firstUpdate = true;
	protected float walkSpeed = 0.35f * Game.SCALE;
	protected int walkDir = LEFT;
	protected int tileY;
	protected float attackDistance = Game.TILES_SIZE;
	protected boolean active = true;
	protected boolean attackChecked;

	public Alien(float x, float y, int width, int height, int enemyType) {
		super(x, y, width, height);
		this.enemyType = enemyType;
		maxHealth = GetMaxHealth(enemyType);
		currentHealth = maxHealth;
		walkSpeed = Game.SCALE * 0.35f;
	}

	protected void firstUpdateCheck(int[][] lvlData) {
		if (!IsEntityOnFloor(hitbox, lvlData))
			inAir = true;
		firstUpdate = false;
	}

	protected void updateInAir(int[][] lvlData) {
		if (CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
			hitbox.y += airSpeed;
			airSpeed += GRAVITY;
		} else {
			inAir = false;
			hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
			tileY = (int) (hitbox.y / Game.TILES_SIZE);
		}
	}

	protected void move(int[][] lvlData) {
		float xSpeed = (walkDir == LEFT) ? -walkSpeed : walkSpeed;

		if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData))
			if (IsFloor(hitbox, xSpeed, lvlData)) {
				hitbox.x += xSpeed;
				return;
			}

		changeWalkDir();
	}

	protected void turnTowardsPlayer(Player player) {
		if (player.hitbox.x > hitbox.x)
			walkDir = RIGHT;
		else
			walkDir = LEFT;
	}

	protected void newState(int state) {
		this.state = state;
		aniTick = 0;
		aniIndex = 0;
	}

	public void hurt(int amount) {
		currentHealth -= amount;
		if (currentHealth <= 0)
			newState(DEAD);
		else
			newState(HIT);

	}

	protected void checkAlienHit(Rectangle2D.Float attackBox, Player player) {
		if (attackBox.intersects(player.hitbox))
			player.changeHealth(-GetAlienDmg(enemyType));
		attackChecked = true;

	}

	protected boolean canSeePlayer(int[][] lvlData, Player player) {
		int playerTileY = (int) player.getHitbox().y / Game.TILES_SIZE;
		if (playerTileY == tileY) {
			if (isPlayerInRange(player)) {
				if (IsSightClear(lvlData, hitbox, player.hitbox, tileY)) {
					return true;
				}
			}
		}
		return false;
	}

	protected boolean isPlayerInRange(Player player) {
		return Math.abs(player.hitbox.x - hitbox.x) <= attackDistance * 8;
	}

	protected boolean isPlayerCloseForAttack(Player player) {
		return Math.abs(player.hitbox.x - hitbox.x) <= attackDistance;
	}

	protected void updateAnimationTick() {
		aniTick++;
		if (aniTick >= ANI_SPEED) {
			aniTick = 0;
			aniIndex++;
			if (aniIndex >= getSpriteAmount(enemyType, state)) {
				aniIndex = 0;

				switch (state) {
				case ATTACK, HIT -> state = IDLE;
				case DEAD -> active = false;
				}

			}
		}
	}

	protected void changeWalkDir() {
		walkDir = (walkDir == LEFT) ? RIGHT : LEFT;
	}

	public void resetAlien() {
		hitbox.x = x; // Set to the initial x pos of the Alien
		hitbox.y = y; // Same but y axis
		firstUpdate = true;
		currentHealth = maxHealth;
		newState(IDLE); // Set initial to IDLE
		active = true; // Set to alive
		airSpeed = 0;
	}
	
	public boolean isActive() {
		return active;
	}
}
