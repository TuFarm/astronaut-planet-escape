package entities;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import gamestate.Playing;
import levels.Level;
import utilz.LoadSave;
import static utilz.Constants.AlienConstants.*;

public class AlienManager {

	private Playing playing;
	private BufferedImage[][] slimeArr;
	private ArrayList<Slime> slimes = new ArrayList<>();

	public AlienManager(Playing playing) {
		this.playing = playing;
		loadAlienImgs();
	}

	public void loadAliens(Level level) {
		slimes = level.getSlimes();

	}

	public void update(int[][] lvlData, Player player) {
		boolean isAnyActive = false;
		for (Slime slime : slimes)
			if (slime.isActive()) {
				slime.update(lvlData, player);
		isAnyActive = true;
	}
	if(!isAnyActive)
		playing.setLevelCompleted(true);
	}

	public void draw(Graphics g, int xLvlOffset) {
		drawAlien(g, xLvlOffset);
	}

	private void drawAlien(Graphics g, int xLvlOffset) {
		for (Slime slime : slimes)
			if (slime.isActive()) {
				g.drawImage(slimeArr[slime.getEnemyState()][slime.getAniIndex()],
						(int) slime.getHitbox().x - xLvlOffset - SLIME_DRAWOFFSET_X + slime.flipX(),
						(int) slime.getHitbox().y - SLIME_DRAWOFFSET_Y, ALIEN_WIDTH * slime.flipW(), ALIEN_HEIGHT,
						null);
			slime.drawHitbox(g, xLvlOffset);
				slime.drawAttackBox(g, xLvlOffset);
			}

	}

	public void checkAlienHit(Rectangle2D.Float attackBox) {
		for (Slime slime : slimes)
		if (slime.isActive())
			if (attackBox.intersects(slime.getHitbox())) {
				slime.hurt(10); // Player cause 10 dmg
				return;
			}
		
	}

	private void loadAlienImgs() {
		slimeArr = new BufferedImage[5][9];
		BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.SLIME_SPRITE);
		for (int j = 0; j < slimeArr.length; j++)
			for (int i = 0; i < slimeArr[j].length; i++)
				slimeArr[j][i] = temp.getSubimage(i * ALIEN_DEFAULT_WIDTH, j * ALIEN_DEFAULT_HEIGHT,
						ALIEN_DEFAULT_WIDTH, ALIEN_DEFAULT_HEIGHT);
	}
	
	public void resetAllAliens() {
		for(Slime slime : slimes) {
			slime.resetAlien();
		}
	}
}
