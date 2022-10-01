package uet.oop.bomberman.entities.Character.Enemy;

import javafx.scene.image.Image;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.entities.Character.Enemy.Enemy;
import uet.oop.bomberman.graphics.Sprite;

import uet.oop.bomberman.utils.State;

import java.awt.*;

public class Oneal extends Enemy {

    @Override
    public void initSolidArea() {
        solidArea = new Rectangle(0 * Sprite.SCALE, 0 * Sprite.SCALE, 13 * Sprite.SCALE, 13 * Sprite.SCALE);
    }

    public Oneal(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
        initSprite();
        initState();
    }

    public Oneal(int xUnit, int yUnit, Image img, BombermanGame game) {
        this(xUnit, yUnit, img);
        this.game = game;
        initSolidArea();
        distanceToChangeSpeed = rand.nextInt(30 * Sprite.SCALE - 10 * Sprite.SCALE) + 10 * Sprite.SCALE;
        type = "Oneal";
    }

    @Override
    protected void initState() {
        this._state = State.GO_WEST;
    }

    @Override
    protected void initSprite() {
        this._sprite = Sprite.oneal_right1;

        this.sprite_character_left = Sprite.oneal_left2;
        this.sprite_character_right = Sprite.oneal_right2;

        this.last_sprite_left = Sprite.oneal_left3;
        this.last_sprite_right = Sprite.oneal_right3;


        this.sprite_character_right_1 = Sprite.oneal_right1;
        this.sprite_character_right_2 = Sprite.oneal_right2;
        this.sprite_character_right_3 = Sprite.oneal_right3;

        this.sprite_character_up = Sprite.oneal_right1;
        this.sprite_character_up_1 = Sprite.oneal_right2;
        this.sprite_character_up_2 = Sprite.oneal_right3;

        this.sprite_character_left_1 = Sprite.oneal_left1;
        this.sprite_character_left_2 = Sprite.oneal_left2;
        this.sprite_character_left_3 = Sprite.oneal_left3;

        this.sprite_character_down = Sprite.oneal_left1;
        this.sprite_character_down_1 = Sprite.oneal_left2;
        this.sprite_character_down_2 = Sprite.oneal_left3;

        this.sprite_character_dead = Sprite.oneal_dead;
    }


    @Override
    public int getVal() {
        return 0;
    }


}