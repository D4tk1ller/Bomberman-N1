package uet.oop.bomberman.entities.Bomb;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Pair;
import uet.oop.bomberman.entities.Character.Bomber;
import uet.oop.bomberman.entities.Character.Enemy.Enemy;
import uet.oop.bomberman.entities.StillObject.Brick;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.Management;
import uet.oop.bomberman.entities.StillObject.Wall;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.entities.Character.Character;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.utils.State;

import java.util.ArrayList;
import java.util.List;

public class BombManagement extends Management<Bomb> {
    private BombermanGame game;

    //    private int explodedLength = 1;
    private int explodedLength = 1;
    private int maxBomb = 5;

    private boolean isPressSpace = false;

    public BombManagement(int maxBomb, int explodedLength, BombermanGame game) {
        this.maxBomb = maxBomb;
        this.explodedLength = explodedLength;
        this.game = game;
    }

    public int getMaxBomb() {
        return maxBomb;
    }

    public int getLeftBomb() {
        return maxBomb - list.size();
    }

    public void setMaxBomb(int maxBomb) {
        this.maxBomb = maxBomb;
    }

    public int getFlame() {
        return explodedLength;
    }

    public void setFlame(int flame) {
        this.explodedLength = flame;
    }

    public BombermanGame getGame() {
        return this.game;
    }

    public void setGame(BombermanGame game) {
        this.game = game;
    }

    public BombManagement(BombermanGame game) {
        this.game = game;
    }

    public void powerUpFlameBomb() {
        this.explodedLength++;
    }

    public void powerUpMaxBomb() {
        maxBomb++;
    }

    public int getExplodedLength() {
        return explodedLength;
    }

    public boolean isBomb(int xUnit, int yUnit) {
        for (Entity b : list) {
            if (b instanceof  Bomb) {
                if (b.get_xUnit() == xUnit && b.get_yUnit() == yUnit) {
                    return true;
                }
            }
        }
        return false;
    }

    public void add(Bomb b) {
        if (list.size() == maxBomb)
            return;
        game.getSoundTrack().playPlaceBomb();
        list.add(b);
        BombermanGame.diagramMap[b.get_yUnit()][b.get_xUnit()] = Bomb.bombDiagram;
    }

    public boolean isCharacterCanMoveThroughBomb(int xUnit, int yUnit, Character other) {
        if (other.getPassBomb()) {
            return false;
        }

        for (Bomb b : list) {
            if (b.get_xUnit() != xUnit || b.get_yUnit() != yUnit)
                continue;
            if (!b.isCharacterInBomb(other) && !b.isExploded()) {
                return true;
            }
        }

        return false;
    }

    public void updatePressKey(KeyEvent event) {
        if (event.getCode() ==  KeyCode.SPACE) {
            if (isPressSpace) {
                return;
            }

            isPressSpace = true;
            int bomb_xUnit = game.getBomberman().get_xUnitCenter();
            int bomb_yUnit = game.getBomberman().get_yUnitCenter();

            if (Brick.isBrick(bomb_xUnit, bomb_yUnit)
                    || Wall.isWall(bomb_xUnit, bomb_yUnit)
                    || game.getBomberBombManagement().isBomb(bomb_xUnit, bomb_yUnit)) {
                return;
            }
            Bomb b = new Bomb(bomb_xUnit, bomb_yUnit, game.getBomberBombManagement(), game);
            game.getBomberBombManagement().add(b);
        }
    }

    public void updateReleaseKey(KeyEvent event) {
        if (event.getCode() ==  KeyCode.SPACE) {
            isPressSpace = false;
        }
    }

    @Override
    public void update() {
        super.update();
        updateRemoveBomb();
    }

    private void updateRemoveBomb() {
        if (list.size() == 0)
            return;

        List<Bomb> newList = new ArrayList<>();
        for (Bomb b : list) {
            if (!b.isEnd()) {
                newList.add(b);
            } else {
                BombermanGame.diagramMap[b.get_yUnit()][b.get_xUnit()] = ' ';
            }
        }
        list = newList;
    }

    public boolean isDestroyBrick(Brick brick) {
        for (Bomb b : list) {
            for (Pair<Integer, Integer> bombCoordinate : b.destroyedBricks) {
                int bomb_xUnit = bombCoordinate.getKey();
                int bomb_yUnit = bombCoordinate.getValue();
                if (bomb_xUnit == brick.get_xUnit() && bomb_yUnit == brick.get_yUnit())
                    return true;
            }
        }

        return false;
    }

    public boolean isDestroyCharacter(Character character) {
        if (character.getPassFlame()) {
            return false;
        }

        for (Bomb bomb : list) {
            for (Pair<Integer, Integer> bombCoordinate : bomb.explodedCells) {
                int bomb_xUnit = bombCoordinate.getKey();
                int bomb_yUnit = bombCoordinate.getValue();
                if (character.isImpact(bomb_xUnit * Sprite.SCALED_SIZE,
                        bomb_yUnit * Sprite.SCALED_SIZE,
                        bomb_xUnit * Sprite.SCALED_SIZE + Sprite.SCALED_SIZE,
                        bomb_yUnit * Sprite.SCALED_SIZE + Sprite.SCALED_SIZE)) {
                    // System.out.println(enemy.getX() + " " + enemy.getY());
                    return true;
                }
            }
        }

        return false;
    }
}
