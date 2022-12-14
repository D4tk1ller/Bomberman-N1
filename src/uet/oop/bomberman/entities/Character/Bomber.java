package uet.oop.bomberman.entities.Character;


import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;

import uet.oop.bomberman.entities.Bomb.Bomb;
import uet.oop.bomberman.entities.Bomb.BombManagement;
import uet.oop.bomberman.entities.Character.Enemy.EnemyManagement;
import uet.oop.bomberman.entities.Item.Item;
import uet.oop.bomberman.entities.Item.Portal;
import uet.oop.bomberman.entities.StillObject.Brick;
import uet.oop.bomberman.entities.StillObject.Grass;
import uet.oop.bomberman.entities.StillObject.Wall;
import uet.oop.bomberman.graphics.Sprite;

import java.awt.*;
import java.util.List;

import uet.oop.bomberman.utils.State;
import uet.oop.bomberman.BombermanGame;

public class Bomber extends Character {
    private static final int EPSILON = 10 * Sprite.SCALE;
    private int speedMoveAtEdgeDivideBy = 15;
    private boolean isPressSpace = false;
    private boolean isBombermanKillAllEnemies = false;
    private int hp = 3;
    private boolean isWin;

    private int entityLeftSideX;
    private int entityRightSideX;
    private int entityTopY;
    private int entityBottomY;


    public Bomber(int x, int y, Image img, BombermanGame game) {
        super(x, y, img, game);
        this.speed = 2;
        MAX_TIME_STOP = 20;
    }

    @Override
    public void initSolidArea() {
        solidArea = new Rectangle(0 * Sprite.SCALE,
                0 * Sprite.SCALE,
                10 * Sprite.SCALE,
                14 * Sprite.SCALE);
    }

    private boolean isBrickOrWall(int x, int y) {
        return Wall.isWall(get_xUnit(x), get_yUnit(y))
                || Brick.isBrick(get_xUnit(x), get_yUnit(y));
    }

    private boolean isBrickOrWall(int x, int y, boolean checkBrick) {
        return Wall.isWall(get_xUnit(x), get_yUnit(y))
                || Brick.isBrick(get_xUnit(x), get_yUnit(y), checkBrick);
    }

    private void smoothMovement() {
        //  Brick brick = new Brick(0, 0);
        if (isCollisionOn == true) {
            boolean checkBrick = !getPassBrick();
            // down
            if (_state == State.GO_SOUTH
                    && Grass.isGrass(entityLeftSideX + EPSILON, entityTopY + Sprite.SCALED_SIZE + EPSILON, checkBrick)
                    && Grass.isGrass(entityLeftSideX + EPSILON, entityTopY + EPSILON, checkBrick)
                    && isBrickOrWall(entityLeftSideX, entityBottomY + EPSILON, checkBrick)) {

                this.x += EPSILON / speedMoveAtEdgeDivideBy;
                return;
            }
            if (_state == State.GO_SOUTH
                    && Grass.isGrass(entityRightSideX - EPSILON, entityTopY + Sprite.SCALED_SIZE + EPSILON, checkBrick)
                    && Grass.isGrass(entityRightSideX - EPSILON, entityTopY + EPSILON, checkBrick)
                    && isBrickOrWall(entityRightSideX, entityBottomY + EPSILON, checkBrick)) {

                this.x -= EPSILON / speedMoveAtEdgeDivideBy;
                return;
            }

            // up
            if (_state == State.GO_NORTH
                    && Grass.isGrass(entityLeftSideX + EPSILON, entityTopY - EPSILON, checkBrick)
                    && Grass.isGrass(entityLeftSideX + EPSILON, entityTopY, checkBrick)
                    && isBrickOrWall(entityLeftSideX, entityTopY - EPSILON, checkBrick)) {

                this.x += EPSILON / speedMoveAtEdgeDivideBy;
                return;
            }

            if (_state == State.GO_NORTH
                    && Grass.isGrass(entityRightSideX - EPSILON, entityTopY - EPSILON, checkBrick)
                    && Grass.isGrass(entityRightSideX - EPSILON, entityTopY, checkBrick)
                    && isBrickOrWall(entityRightSideX, entityTopY - EPSILON, checkBrick)) {

                this.x -= EPSILON / speedMoveAtEdgeDivideBy;
                return;
            }

            // right
            if (_state == State.GO_EAST
                    && Grass.isGrass(entityRightSideX + EPSILON, entityBottomY - EPSILON, checkBrick)
                    && Grass.isGrass(entityRightSideX, entityBottomY - EPSILON, checkBrick)
                    && isBrickOrWall(entityRightSideX + EPSILON, entityBottomY, checkBrick)) {

                this.y -= EPSILON / speedMoveAtEdgeDivideBy;
                return;
            } else if (_state == State.GO_EAST
                    && Grass.isGrass(entityRightSideX + EPSILON, entityTopY + EPSILON, checkBrick)
                    && Grass.isGrass(entityRightSideX, entityTopY + EPSILON, checkBrick)
                    && isBrickOrWall(entityRightSideX + EPSILON, entityTopY, checkBrick)) {

                this.y += EPSILON / speedMoveAtEdgeDivideBy;
                return;
            }

            // left
            if (_state == State.GO_WEST
                    && Grass.isGrass(entityLeftSideX - EPSILON, entityBottomY - EPSILON, checkBrick)
                    && Grass.isGrass(entityLeftSideX, entityBottomY - EPSILON, checkBrick)
                    && isBrickOrWall(entityLeftSideX - EPSILON, entityBottomY, checkBrick)) {
                this.y -= EPSILON / speedMoveAtEdgeDivideBy;
                return;
            } else if (_state == State.GO_WEST
                    && Grass.isGrass(entityLeftSideX - EPSILON, entityTopY + EPSILON, checkBrick)
                    && Grass.isGrass(entityLeftSideX, entityTopY + EPSILON, checkBrick)
                    && isBrickOrWall(entityLeftSideX - EPSILON, entityTopY, checkBrick)) {
                this.y += EPSILON / speedMoveAtEdgeDivideBy;
                return;
            }
        }
    }

    public BombManagement getBombManagement() {
        return game.getBomberBombManagement();
    }

    public int getHP() {
        return hp;
    }

    public void setHP(int hp) {
        this.hp = hp;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    @Override
    protected void initSprite() {

        this.last_sprite_left = Sprite.player_left_2;
        this.last_sprite_right = Sprite.player_right_2;

        this.sprite_character_up = Sprite.player_up;
        this.sprite_character_up_1 = Sprite.player_up_1;
        this.sprite_character_up_2 = Sprite.player_up_2;

        this.sprite_character_down = Sprite.player_down;
        this.sprite_character_down_1 = Sprite.player_down_1;
        this.sprite_character_down_2 = Sprite.player_down_2;

        this.sprite_character_left = Sprite.player_left;
        this.sprite_character_left_1 = Sprite.player_left_1;
        this.sprite_character_left_2 = Sprite.player_left_2;

        this.sprite_character_right = Sprite.player_right;
        this.sprite_character_right_1 = Sprite.player_right_1;
        this.sprite_character_right_2 = Sprite.player_right_2;

        this.sprite_character_dead = Sprite.player_dead1;
        this.sprite_character_dead_1 = Sprite.player_dead1;
        this.sprite_character_dead_2 = Sprite.player_dead2;
        this.sprite_character_dead_3 = Sprite.player_dead3;


        this._sprite = this.sprite_character_right;
    }

    @Override
    public void setDead() {
        if (isDead) {
            return;
        }
        super.setDead();
        hp--;
    }

    @Override
    protected void initState() {
        this._state = State.GO_EAST;
    }

    public boolean isBombermanKillAllEnemies() {
        return isBombermanKillAllEnemies;
    }

    public void updateBombermanKillAllEnemies(EnemyManagement enemyManagement) {
        isBombermanKillAllEnemies = enemyManagement.getList().size() == 0;
    }

    public void updatePressKey(KeyEvent event) {
        if (isDead)
            return;
        switch (event.getCode()) {
            case W:
            case UP:
                _state = State.GO_NORTH;
                goNorth = true;
                break;
            case S:
            case DOWN:
                _state = State.GO_SOUTH;
                goSouth = true;
                break;
            case A:
            case LEFT:
                _state = State.GO_WEST;
                goWest = true;
                break;
            case D:
            case RIGHT:
                _state = State.GO_EAST;
                goEast = true;
                break;
            case SHIFT:
                running = true;
                break;
        }
    }

    public void updateReleaseKey(KeyEvent event) {
        if (isDead)
            return;
        switch (event.getCode()) {
            case W:
            case UP:
                goNorth = false;
                previousState = State.GO_NORTH;
                break;
            case S:
            case DOWN:
                previousState = State.GO_SOUTH;
                goSouth = false;
                break;
            case A:
            case LEFT:
                previousState = State.GO_WEST;
                goWest = false;
                break;
            case D:
            case RIGHT:
                previousState = State.GO_EAST;
                goEast = false;
                break;
            case SHIFT:
                running = false;
                break;
        }
    }

    private void playTakeItem() {
        game.getSoundTrack().playTakeItem();
    }

    public void takeItem(Item item) {
        if (isDead || item.isTaken())
            return;

        if (!(item instanceof Portal)) {
            playTakeItem();
        }

        switch (item.getDiagramItem()) {
            case Item.bombItemDiagram:
                game.getBomberBombManagement().powerUpMaxBomb();
                break;
            case Item.speedItemDiagram:
                speed += 1;
                break;
            case Item.flameItemDiagram:
                game.getBomberBombManagement().powerUpFlameBomb();
                break;
            case Item.hpItemDiagram:
                hp++;
                break;
            case Item.portalItemDiagram:
                if (isBombermanKillAllEnemies() && isInCell(item.get_xUnit(), item.get_yUnit())) {
                    setBomberWin();
                }
                break;
            case Item.passBrickDiagram:
                setPassBrick(true);
                break;
            case Item.flamePassDiagram:
                setPassFlame(true);
                break;
            case Item.bombPassDiagram:
                setPassBomb(true);
                break;
        }
    }

    private void updateMaxBombWhenPassFlame() {
        if (passFlame && game.getBomberBombManagement().getMaxBomb() > 3) {
            game.getBomberBombManagement().setMaxBomb(3);
        }
    }

    public void setBomberWin() {
        isWin = true;
    }

    public boolean isWin() {
        return isWin;
    }

    @Override
    public void update() {
        super.update();

        entityLeftSideX = x + solidArea.x;
        entityRightSideX = entityLeftSideX + solidArea.width;
        entityTopY = y + solidArea.y;
        entityBottomY = entityTopY + solidArea.height;

        updateCurrentState();
        smoothMovement();
        updateMaxBombWhenPassFlame();
    }

    @Override
    public void render(GraphicsContext gc) {
        updateCurrentState();
        super.render(gc);
    }


}
