package entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import gamestate.Playing;
import utilz.LoadSave;
import static utilz.Constants.AlienConstants.*;

public class AlienManager {

	private Playing playing;
	private BufferedImage[][] crabbyArr;
	private ArrayList<Slime> slimes = new ArrayList<>();

	public AlienManager(Playing playing) {
		this.playing = playing;
		loadAlienImgs();
		addAliens();
	}

	private void addAliens() {
		slimes = LoadSave.GetSlime();

	}

	public void update(int[][] lvlData, Player player) {
		for (Slime slime : slimes)
			slime.update(lvlData, player);
	}

	public void draw(Graphics g, int xLvlOffset) {
		drawAlien(g, xLvlOffset);
	}

	private void drawAlien(Graphics g, int xLvlOffset) {
		for (Slime slime : slimes) {
			g.drawImage(crabbyArr[slime.getEnemyState()][slime.getAniIndex()],
					(int) slime.getHitbox().x - xLvlOffset - SLIME_DRAWOFFSET_X,
					(int) slime.getHitbox().y - SLIME_DRAWOFFSET_Y, ALIEN_WIDTH, ALIEN_HEIGHT, null);
//			slime.drawHitbox(g, xLvlOffset);
		}

	}

	private void loadAlienImgs() {
		crabbyArr = new BufferedImage[5][9];
		BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.SLIME_SPRITE);
		for (int j = 0; j < crabbyArr.length; j++)
			for (int i = 0; i < crabbyArr[j].length; i++)
				crabbyArr[j][i] = temp.getSubimage(i * ALIEN_DEFAULT_WIDTH, j * ALIEN_DEFAULT_HEIGHT,
						ALIEN_DEFAULT_WIDTH, ALIEN_DEFAULT_HEIGHT);
	}
}
