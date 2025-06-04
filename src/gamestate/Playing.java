package gamestate;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import entities.AlienManager;
import entities.Player;
import levels.LevelManager;
import main.Game;
import ui.GameOverOverlay;
import ui.PauseOverlay;
import utilz.LoadSave;
import static utilz.Constants.Environment.*;

public class Playing extends State implements Statemethods {
	private Player player;
	private LevelManager levelManager;
	private AlienManager alienManager;
	private PauseOverlay pauseOverlay;
	private GameOverOverlay gameOverOverlay;
	private boolean paused = false;

	private int xLvlOffset;
	private int leftBorder = (int) (0.2 * Game.GAME_WIDTH);
	private int rightBorder = (int) (0.8 * Game.GAME_WIDTH);
	private int lvlTilesWide = LoadSave.GetLevelData()[0].length;
	private int maxTilesOffset = lvlTilesWide - Game.TILES_IN_WIDTH;
	private int maxLvlOffsetX = maxTilesOffset * Game.TILES_SIZE;

	private BufferedImage backgroundImg, bigCloud, smallCloud;
	private int[] smallCloudsPos;
	private Random rnd = new Random();

	private boolean gameOver;

	public Playing(Game game) {
		super(game);
		initClasses();

		backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BACKGROUND_IMG);
		bigCloud = LoadSave.GetSpriteAtlas(LoadSave.BIG_CLOUDS);
		smallCloud = LoadSave.GetSpriteAtlas(LoadSave.SMALL_CLOUDS);
		smallCloudsPos = new int[8];
		for (int i = 0; i < smallCloudsPos.length; i++)
			smallCloudsPos[i] = (int) (90 * Game.SCALE) + rnd.nextInt((int) (100 * Game.SCALE));
	}

	private void initClasses() {
		levelManager = new LevelManager(game);
		alienManager = new AlienManager(this);
		player = new Player(200, 200, (int) (64 * Game.SCALE), (int) (40 * Game.SCALE), this);
		player.loadLvlData(LevelManager.getCurrentLevel().getLevelData());
		pauseOverlay = new PauseOverlay(this);
		gameOverOverlay = new GameOverOverlay(this);

	}

	@Override
	public void update() {
		if (!paused && !gameOver) {
			levelManager.update();
			player.update();
			alienManager.update(LevelManager.getCurrentLevel().getLevelData(), player);
			checkCloseToBorder();
		} else {
			pauseOverlay.update();
		}

	}

	private void checkCloseToBorder() {
		int playerX = (int) player.getHitbox().x;
		int diff = playerX - xLvlOffset;

		if (diff > rightBorder)
			xLvlOffset += diff - rightBorder;
		else if (diff < leftBorder)
			xLvlOffset += diff - leftBorder;

		if (xLvlOffset > maxLvlOffsetX)
			xLvlOffset = maxLvlOffsetX;
		else if (xLvlOffset < 0)
			xLvlOffset = 0;

	}

	@Override
	public void draw(Graphics g) {
		g.drawImage(backgroundImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);

		drawClouds(g);

		levelManager.draw(g, xLvlOffset);
		player.render(g, xLvlOffset);
		alienManager.draw(g, xLvlOffset);

		if (paused) {
			g.setColor(new Color(45, 56, 33, 200));
			g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
			pauseOverlay.draw(g);
		}

		else if (gameOver) {
			gameOverOverlay.draw(g);
		}

	}

	private void drawClouds(Graphics g) {

		for (int i = 0; i < 3; i++)

			g.drawImage(bigCloud, i * BIG_CLOUD_WIDTH - (int) (xLvlOffset * 0.3), (int) (220 * Game.SCALE),
					BIG_CLOUD_WIDTH, BIG_CLOUD_HEIGHT, null);

		for (int i = 0; i < smallCloudsPos.length; i++)
			g.drawImage(smallCloud, SMALL_CLOUD_WIDTH * 4 * i - (int) (xLvlOffset * 0.7), smallCloudsPos[i],
					SMALL_CLOUD_WIDTH, SMALL_CLOUD_HEIGHT, null);
	}

	public void resetAll() {
		// TODO: reset player, enemy, lvl, ...
		gameOver = false;
		paused = false;
		player.resetAll();
		alienManager.resetAllAliens();
	}

	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}

	public void checkAlienHit(Rectangle2D.Float attackBox) {
		alienManager.checkAlienHit(attackBox);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (!gameOver)
			if (e.getButton() == MouseEvent.BUTTON1) {
				player.setAttacking(true);
			}

	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (gameOver)
			gameOverOverlay.keyPressed(e);
		else
			switch (e.getKeyCode()) {

			case KeyEvent.VK_W:
				player.setJump(true);
				break;
			case KeyEvent.VK_A:
				player.setLeft(true);
				break;
			case KeyEvent.VK_D:
				player.setRight(true);
				break;
			case KeyEvent.VK_ESCAPE:
				paused = !paused;
			case KeyEvent.VK_J:
				player.setAttacking(true);
				break;
			}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (!gameOver)

			switch (e.getKeyCode()) {

			case KeyEvent.VK_W:
				player.setJump(false);
				break;
			case KeyEvent.VK_A:
				player.setLeft(false);
				break;
			case KeyEvent.VK_D:
				player.setRight(false);
				break;
			case KeyEvent.VK_J:
				player.setAttacking(true);
				break;
			}

	}

	public void mouseDragged(MouseEvent e) {
		if (!gameOver)
			if (paused)
				pauseOverlay.mouseDragged(e);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (!gameOver)
			if (paused)
				pauseOverlay.mousePressed(e);

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (!gameOver)
			if (paused)
				pauseOverlay.mouseReleased(e);

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (!gameOver)
			if (paused)
				pauseOverlay.mouseMoved(e);

	}

	public void unpauseGame() {
		paused = false;
	}

	public void windowFocusLost() {
		player.resetDirBooleans(); // Set boolean to false if window lost focus (key release)
	}

	public Player getPlayer() {
		return player;
	}
}