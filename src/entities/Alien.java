
package entities;

import static utilz.Constants.AlienConstants.*;

public abstract class Alien extends Entity {

    private int aniIndex, alienState, alienType;
    private int aniTick, aniSpeed = 25;

    public Alien(float x, float y, int width, int height, int alienType) {
        super(x, y, width, height);
        this.alienType = alienType;
        this.alienState = IDLE; // Khởi tạo mặc định
        initHitbox(x, y, width, height);
    }

    private void updateAnimationTick() {
        aniTick++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;
            int maxFrames = getSpriteAmount(alienType, alienState);
            if (aniIndex >= maxFrames) {
                aniIndex = 0;
            }
        }
    }

    public void update() {
        updateAnimationTick();
    }

    public int getAniIndex() {
        return aniIndex;
    }

    public int getAlienState() {
        return alienState;
    }

    public void setAlienState(int newState) {
        if (newState != this.alienState) {
            this.alienState = newState;
            aniIndex = 0;
            aniTick = 0;
        }
    }
}
