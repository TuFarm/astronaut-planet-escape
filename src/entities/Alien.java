package entities;

import static utilz.Constants.AlienConstants.*;
import static utilz.HelpMethods.*;
import static utilz.Constants.Direction.*;

import main.Game;

public abstract class Alien extends Entity {

	protected int aniIndex, enemyState, enemyType;
	protected int aniTick, aniSpeed = 25;
	protected boolean firstUpdate = true;
	protected boolean inAir;
	protected float fallSpeed;
	protected float gravity = 0.04f * Game.SCALE;
	protected float walkSpeed = 0.35f * Game.SCALE;
	protected int walkDir = LEFT;
	protected int tileY;
	protected float attackDistance = Game.TILES_SIZE;

	public Alien(float x, float y, int width, int height, int enemyType) {
		super(x, y, width, height);
		this.enemyType = enemyType;
		initHitbox(x, y, width, height);
	}

	protected void firstUpdateCheck(int[][] lvlData) {
		if (!IsEntityOnFloor(hitbox, lvlData))
			inAir = true;
		firstUpdate = false;
	}

	protected void updateInAir(int[][] lvlData) {
		if (CanMoveHere(hitbox.x, hitbox.y + fallSpeed, hitbox.width, hitbox.height, lvlData)) {
			hitbox.y += fallSpeed;
			fallSpeed += gravity;
		} else {
			inAir = false;
			hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, fallSpeed);
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
		this.enemyState = state;
		aniTick = 0;
		aniIndex = 0;
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
		if (aniTick >= aniSpeed) {
			aniTick = 0;
			aniIndex++;
			if (aniIndex >= getSpriteAmount(enemyType, enemyState)) {
				aniIndex = 0;
				if (enemyState == ATTACK)
					enemyState = IDLE;
			}
		}
	}

	protected void changeWalkDir() {
		walkDir = (walkDir == LEFT) ? RIGHT : LEFT;
	}

	public int getAniIndex() {
		return aniIndex;
	}

	public int getEnemyState() {
		return enemyState;
	}
}
