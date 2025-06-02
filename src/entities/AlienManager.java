
package entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import gamestate.Playing;
import utilz.LoadSave;
import static utilz.Constants.AlienConstants.*;

public class AlienManager {

    private Playing playing;
    private BufferedImage[][] alienArr;
    private ArrayList<Octopus> octopus = new ArrayList<>();

    public AlienManager(Playing playing) {
        this.playing = playing;
        loadAlienImgs();
        addAliens();
    }

    private void addAliens() {
        octopus = LoadSave.GetOctopus();
        System.out.println("Size of octopus: " + octopus.size());
    }

    public void update() {
        for (Octopus o : octopus) {
            o.update();
        }
    }

    public void draw(Graphics g, int xLvlOffset) {
        drawAliens(g, xLvlOffset);
    }

    private void drawAliens(Graphics g, int xLvlOffset) {
        for (Octopus o : octopus) {
            int state = o.getAlienState();
            int index = o.getAniIndex();

            if (state < alienArr.length && index < alienArr[state].length) {
                g.drawImage(alienArr[state][index],
                    (int) o.getHitbox().x - xLvlOffset, (int) o.getHitbox().y,
                    ALIEN_WIDTH, ALIEN_HEIGHT, null);
            }
        }
    }

    private void loadAlienImgs() {
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.OCTOPUS_SPRITE);

        final int frameWidth = ALIEN_DEFAULT_WIDTH;
        final int frameHeight = ALIEN_DEFAULT_HEIGHT;

        int rows = temp.getHeight() / frameHeight;
        int cols = temp.getWidth() / frameWidth;

        alienArr = new BufferedImage[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                alienArr[i][j] = temp.getSubimage(j * frameWidth, i * frameHeight, frameWidth, frameHeight);
            }
        }
    }
}
