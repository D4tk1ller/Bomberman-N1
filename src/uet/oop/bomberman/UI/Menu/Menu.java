package uet.oop.bomberman.UI.Menu;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


import uet.oop.bomberman.Game;
import uet.oop.bomberman.UI.GameUI.Board;
import uet.oop.bomberman.UI.Menu.animationMenu.AnimatedGraphic;
import uet.oop.bomberman.UI.Menu.animationMenu.MenuList.MenuLists;
import uet.oop.bomberman.UI.Menu.animationMenu.Background;

import uet.oop.bomberman.sound.SoundManager;

public class Menu {
    private boolean isRun = true;
    private Game game;
    private static final int WIDTH = 31;
    private static final int HEIGHT = 13;
    private AnimatedGraphic animatedGraphic;
    private AnimatedGraphic background;
    private MenuLists menuLists;

    public Menu(Scene scene, Game game) {
        this.game = game;

        menuLists = new MenuLists(Game.WIDTH, Game.HEIGHT, scene, this);

        //Add background and animation
        background = new Background("file:res/Background/mountain.png",
                0, 0, 2, Game.WIDTH, Game.HEIGHT);

        animatedGraphic = new AnimatedGraphic("file:res/textures/menu_logo.png", 0, 50);
        animatedGraphic.resize(0.25);
        animatedGraphic.setCenterHorizontal(Game.WIDTH);

        //Add music
        SoundManager.getSoundManager().addMusicInfinite(SoundManager.mainMusicFilepath);
    }

    public boolean isRun() {
        return isRun;
    }

    public void update(Stage stage) {
        SoundManager.getSoundManager().update();
        animatedGraphic.update();
        menuLists.update(stage);
        background.update();
    }

    public void stop(Stage stage) {
        SoundManager.getSoundManager().stop();
    }

    public void render(Canvas canvas, GraphicsContext gc) {
        gc.moveTo(0, 0);
        SoundManager.getSoundManager().play();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        background.render(gc);
        animatedGraphic.render(gc);
        menuLists.render(gc);
    }

    public void run(Canvas canvas, GraphicsContext gc, Stage stage) {
        if (!isRun) {
            return;
        }

        update(stage);
        render(canvas, gc);
    }

    public void setEnd() {
        isRun = false;
        game.restartCanvas();
    }

    public void setStart() {
        isRun = true;
    }
}
