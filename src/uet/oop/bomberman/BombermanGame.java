package uet.oop.bomberman;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import uet.oop.bomberman.UI.GameUI.Board;
import uet.oop.bomberman.UI.MiniInfo.MiniInfoManagement;
import uet.oop.bomberman.entities.*;
import uet.oop.bomberman.entities.Bomb.Bomb;
import uet.oop.bomberman.entities.Character.Enemy.Enemy;
import uet.oop.bomberman.entities.Character.Enemy.EnemyManagement;
import uet.oop.bomberman.entities.Item.Item;
import uet.oop.bomberman.entities.Item.ItemManagement;
import uet.oop.bomberman.entities.Score.Score;
import uet.oop.bomberman.entities.StillObject.Brick;
import uet.oop.bomberman.entities.StillObject.Grass;
import uet.oop.bomberman.entities.StillObject.Wall;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.entities.Character.Bomber;
import uet.oop.bomberman.sound.Soundtrack;
import uet.oop.bomberman.utils.CollisionChecker;
import uet.oop.bomberman.entities.Bomb.BombManagement;
import uet.oop.bomberman.utils.State;

public class BombermanGame {
    public static final int WIDTH = 31;
    public static final int HEIGHT = 13;
    public static final int TIME_WIN = 180;
    public static final int TIME_LOSE = 330;
    public static final int TIME_GAME = 60 * 300;
    public static final int TIME_ADD_ENEMY = 30;
    private int currentTimeWin = 0;
    private int currentTimeLose = 0;
    private int currentTimeGame = TIME_GAME;
    private int currentTimeAddEnemy = 0;
    private static final int NUM_ENEMIES_IS_ADDED = 15;
    private int currentNumEnemiesIsAdded = 0;

    public Score getBomberScore() {
        return bomberScore;
    }

    private Score bomberScore = new Score();

    public Game getGame() {
        return game;
    }

    private Game game;
    private Canvas canvas;
    private GraphicsContext gc;
    private Soundtrack soundTrack = new Soundtrack();

    public CollisionChecker collisionChecker = new CollisionChecker(this);
    public Bomber bomberman = new Bomber(1, 1, Sprite.player_right.getFxImage(), this);
    private List<Entity> stillObjects = new ArrayList<>();
    private ItemManagement itemManagement = new ItemManagement();
    private EnemyManagement enemyManagement = new EnemyManagement();
    private BombManagement bomberBombManagement = new BombManagement(1, 1, this);
    private BombManagement enemyBombManagement = new BombManagement(50, 6, this);

    private MiniInfoManagement miniInfoManagement = new MiniInfoManagement();

    private boolean isRun = true;

    private boolean isAdd = false;

    private boolean isWin = false;

    private boolean isLose = false;
    private int levelDone = 0;
    private int justDie = 0;
    public Soundtrack getSoundTrack() {
        return soundTrack;
    }

    public List<Entity> getStillObjects() {
        return stillObjects;
    }

    public MiniInfoManagement getMiniInfoManagement() {
        return miniInfoManagement;
    }

    public void findBrickAndDelete(int xUnit, int yUnit) {
        for (Entity e : stillObjects) {
            if (e instanceof Brick) {
                if (e.get_xUnit() == xUnit && e.get_yUnit() == yUnit) {
                    e.setImg(Sprite.grass.getFxImage());
                }
            }
        }
    }

    public BombManagement getBomberBombManagement() {
        return this.bomberBombManagement;
    }

    public BombManagement getEnemyBombManagement() {
        return this.enemyBombManagement;
    }

    private Board board = new Board();
    int level = 1;

    public boolean isRun() {
        return isRun;
    }

    private void setWin() {
        if (isWin)
            return;

        bomberScore.setScore(bomberScore.getCurrentScore() + getCurrentTimeGame() * 5);
        isWin = true;
        soundTrack.stopLevelThemeAt(level);
        soundTrack.playLevelDone();
    }

    public boolean isWin() {
        return isWin;
    }

    public int getLevel() {
        return level;
    }

    public int getCurrentTimeGame() {
        return currentTimeGame / 60;
    }

    public int getRealCurrentTimeGame() {
        return currentTimeGame;
    }

    private void updateCurrentTimeGame() {
        if (currentTimeGame <= 0)
            return;

        currentTimeGame--;
    }

    public EnemyManagement getEnemyManagement() {
        return enemyManagement;
    }

    public void setBomber(Bomber bomberman) {
        this.bomberman.setHP(bomberman.getHP());
        this.bomberman.setSpeed(bomberman.getSpeed());
        this.bomberman.getBombManagement().setMaxBomb(bomberman.getBombManagement().getMaxBomb());
        this.bomberman.getBombManagement().setFlame(bomberman.getBombManagement().getFlame());
        if (bomberman.getPassBrick()) {
            this.bomberman.setPassBrick(true);
        }
        if (bomberman.getPassBomb()) {
            this.bomberman.setPassBomb(true);
        }
        if (bomberman.getPassFlame()) {
            this.bomberman.setPassFlame(true);
        }
    }

    public BombermanGame(int level, Game game) {
        canvas = new Canvas(Sprite.SCALED_SIZE * BombermanGame.WIDTH, Sprite.SCALED_SIZE * BombermanGame.HEIGHT);
        gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        this.level = level;
        createMap(level);
        this.game = game;
    }

    public Bomber getBomberman() {
        return bomberman;
    }

    public void setBomberman(int hp) {
        this.bomberman.setHP(hp);
    }

    public static final char[][] diagramMap = new char[HEIGHT][WIDTH];

    private void createDiagramMap(int level) {
        try {
            BufferedReader bufferreader = new BufferedReader(new FileReader("res/Map/map" + level + ".txt"));
            String line;
            int indexLine = 0;

            while ((line = bufferreader.readLine()) != null) {
                diagramMap[indexLine] = line.toCharArray();
                indexLine++;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void createMap(int level) {
        createDiagramMap(level);

        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                Entity object = null;
                char currentDiagramObject = diagramMap[j][i];

                if (Wall.isWall(currentDiagramObject)) {
                    object = new Wall(i, j, this);
                } else if (Brick.isBrick(currentDiagramObject)) {
                    object = new Brick(i, j, this);

                } else if (Item.isItem(currentDiagramObject)) {
                    object = new Brick(i, j, this);
                    itemManagement.add(i, j, currentDiagramObject, this);
                } else if (Enemy.isEnemy(currentDiagramObject)) {
                    object = new Grass(i, j, this);
                    enemyManagement.add(i, j, currentDiagramObject, this);
                } else {
                    object = new Grass(i, j, this);
                }

                stillObjects.add(object);
            }
        }
    }

    public void update() {
        bomberman.update();
        for (Entity entity : stillObjects) {
            if (entity instanceof Brick) {
                ((Brick) entity).update(bomberman);
            }
            entity.update();
        }
        itemManagement.update();
        enemyManagement.update();
        board.update(bomberman.getHP(), enemyManagement.getNumEnemies(),
                bomberBombManagement.getLeftBomb(), bomberBombManagement.getFlame(),
                bomberman.getSpeed(), getCurrentTimeGame(), bomberScore.getCurrentScore());
        updateCurrentTimeGame();
        bomberBombManagement.update();
        enemyBombManagement.update();
        miniInfoManagement.update();
    }

    public void updateInput(Scene scene) {
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                bomberman.updatePressKey(event);
                bomberBombManagement.updatePressKey(event);
                if (event.getCode() == KeyCode.ESCAPE) {
                    game.getPauseMenu().setStart();
                }
            }
        });

        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                bomberman.updateReleaseKey(event);
                bomberBombManagement.updateReleaseKey(event);
            }
        });
    }

    public void updateCombat(Scene scene) {

        // destroy brick
        List<Entity> newStillObjects = new ArrayList<>();
        for (Entity e : stillObjects) {
            newStillObjects.add(e);
            if (e instanceof Brick) {
                if (bomberman.getBombManagement().isDestroyBrick((Brick) e)) {
                    ((Brick) e).setDestroyed();
                    itemManagement.setItemIfBrickIsDestroyed((Brick) e);
                }

                if (((Brick) e).isEnd()) {
                    newStillObjects.add(new Grass(e.get_xUnit(), e.get_yUnit(), this));
                    newStillObjects.remove(e);
                }
            }
        }
        stillObjects = newStillObjects;


        // Bomber take item
        itemManagement.updateBomberTakeItem(bomberman);

        // Bomb kill enemy
        enemyManagement.updateEnemyIsKilledByBomb(bomberman.getBombManagement());
        enemyManagement.updateEnemyIsKilledByBomb(enemyBombManagement);

        // Bomb kill bomber
        if (bomberman.getBombManagement().isDestroyCharacter(bomberman)
                || enemyBombManagement.isDestroyCharacter(bomberman)) {
            bomberman.setDead();
        }


        // Enemy kill bomber
        if (enemyManagement.isEnemyKillCharacter(bomberman)) {
            bomberman.setDead();
        }

        // Bomber kill all enemies
        bomberman.updateBombermanKillAllEnemies(enemyManagement);

        // End Time
        if (currentTimeGame <= 0) {
            updateAddEnemyWhenEndTimeGame();
        }

        // Bomber win
        if (bomberman.isWin()) {
            setWin();
        }

        // Bomber lose
        if (bomberman.isDead()) {
            setLose();
        }

    }

    private void updateAddEnemyWhenEndTimeGame() {
        currentTimeAddEnemy++;
        if (currentTimeAddEnemy == TIME_ADD_ENEMY && currentNumEnemiesIsAdded < NUM_ENEMIES_IS_ADDED) {
            enemyManagement.add(1, 1, '7', this);
            currentTimeAddEnemy = 0;
            currentNumEnemiesIsAdded++;
        }
    }

    public void render(Canvas canvas, GraphicsContext gc, GraphicsContext boardGc) {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        stillObjects.forEach(g -> g.render(gc));
        itemManagement.render(gc);
        bomberBombManagement.render(gc);
        enemyBombManagement.render(gc);
        enemyManagement.render(gc);

        board.render(boardGc);
        bomberman.render(gc);
        miniInfoManagement.render(gc);
    }

    public void run(Canvas canvas, GraphicsContext gc, GraphicsContext boardGc, Scene scene, Group root) {
        if (!isRun) {
            return;
        }

        if (isWin) {
            currentTimeWin++;
            if (currentTimeWin > TIME_WIN) {
                setEnd();
            }
            return;
        }

        if (isLose) {
            currentTimeLose++;
            if (currentTimeLose > TIME_LOSE) {
                setEnd();
            }
        }


        if (!isAdd) {
            setAdd();
        }
        render(canvas, gc, boardGc);
        update();
        updateInput(scene);
        updateCombat(scene);

        if (bomberman.isEnd()) {
            setEnd();
        }
    }


    private void setAdd() {
        isAdd = true;
    }


    public void setEnd() {
        miniInfoManagement.clear();
        isRun = false;
        soundTrack.stopLevelThemeAt(level);
    }

    private void setLose() {
        if (isLose)
            return;
        isLose = true;
        soundTrack.stopLevelThemeAt(level);
        soundTrack.playJustDie();
    }

    public BombermanGame newLevel(int level) {
        BombermanGame newBomberGame = new BombermanGame(level, game);
        newBomberGame.setBomber(bomberman);
        newBomberGame.getBomberScore().setScore(bomberScore.getCurrentScore());
        return newBomberGame;
    }
}