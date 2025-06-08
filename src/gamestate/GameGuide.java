package gamestate;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import main.Game;
import ui.PauseButton;
import ui.UrmButton;
import utilz.LoadSave;
import static utilz.Constants.UI.URMButtons.*;

public class GameGuide extends State implements Statemethods {

	private BufferedImage backgroundImg, guideBackgroundImg;
	private int bgX, bgY, bgW, bgH;
	private UrmButton returnButton;

	public GameGuide(Game game) {
		super(game);
		loadImgs();
		loadReturnButton();
	}

	private void loadReturnButton() {
		int returnButtonX = (int) (387 * Game.SCALE);
		int returnButtonY = (int) (350 * Game.SCALE);

		returnButton = new UrmButton(returnButtonX, returnButtonY, URM_SIZE, URM_SIZE, 2);

	}

	private void loadImgs() {
		backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND_IMG);
		guideBackgroundImg = LoadSave.GetSpriteAtlas(LoadSave.GUIDE_MENU);

		bgW = (int) (guideBackgroundImg.getWidth() * Game.SCALE);
		bgH = (int) (guideBackgroundImg.getHeight() * Game.SCALE);
		bgX = Game.GAME_WIDTH / 2 - bgW / 2;
		bgY = (int) (20 * Game.SCALE);

	}

	@Override
	public void update() {
		returnButton.update();

	}

	@Override
	public void draw(Graphics g) {
		g.drawImage(backgroundImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
		g.drawImage(guideBackgroundImg, bgX, bgY, bgW, bgH, null);

		returnButton.draw(g);

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (isIn(e, returnButton))
			if (returnButton.isMousePressed()) {
				Gamestate.state = Gamestate.MENU;
			}
		
		returnButton.resetBools();

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		returnButton.setMouseOver(false);
		
		if (isIn(e, returnButton))
			returnButton.setMouseOver(true);

	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (isIn(e, returnButton)) {
			returnButton.setMousePressed(true);
		}

	}

	private boolean isIn(MouseEvent e, PauseButton b) {
		return b.getBounds().contains(e.getX(), e.getY());
	}

}
