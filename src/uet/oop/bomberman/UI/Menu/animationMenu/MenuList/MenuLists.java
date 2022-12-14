package uet.oop.bomberman.UI.Menu.animationMenu.MenuList;

import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import uet.oop.bomberman.UI.Menu.Menu;
import uet.oop.bomberman.UI.Menu.animationMenu.TextGraphicsList;
import uet.oop.bomberman.sound.SoundManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MenuLists {
    private static final String[] listTypes = {"MAIN", "OPTIONS", "HIGHSCORE", "INFO", "QUESTION", "START", "EXIT"};
    private List<TextGraphicsList> menuLists;
    private Menu menu;
    private int currentIndex = 0;

    public MenuLists(int screenWidth, int screenHeight, Scene scene, Menu menu) {
        menuLists = new ArrayList<>();
        menuLists.add(new MainMenuList(screenWidth, screenHeight, scene));
        menuLists.add(new OptionList(screenWidth, screenHeight, scene));
        menuLists.add(new HighscoreList(screenWidth, screenHeight, scene));
        menuLists.add(new InfoList(screenWidth, screenHeight, scene));
        menuLists.add(new QuestionList(screenWidth, screenHeight, scene));
        this.menu = menu;

    }

    public void render(GraphicsContext gc) {
        menuLists.get(currentIndex).render(gc);
    }

    public void update(Stage stage) {
        menuLists.get(currentIndex).update();

        //Handle list changes
        if ((!Objects.equals(menuLists.get(currentIndex).isExiting(), "FALSE"))) {
            int oldIndex = currentIndex;
            currentIndex = getCurrentIndex((menuLists.get(currentIndex).isExiting()));

            if (currentIndex == 4) {
                handleQuestionList(oldIndex, stage);
            }

            //Delete this after adding start and exit functionalities
            if (currentIndex == 5) {
                currentIndex = 0;
                startGame();
            } else if (currentIndex == 6) {
                currentIndex = 0;
            }

            //Exit from the old list
            menuLists.get(oldIndex).exit();
        }
    }

    public void handleQuestionList(int oldIndex, Stage stage) {
        QuestionList questionList = (QuestionList) menuLists.get(4);
        questionList.setBeforeIndex(oldIndex);

        //Handle reset highscore
        if (oldIndex == 1) {
            HighscoreList highscoreList = (HighscoreList) menuLists.get(2);
            questionList.setCallback(new QuestionList.Callback() {
                @Override
                public void execute() {
                    highscoreList.resetHighscore();
                }
            });
        }

        //Handle exit
        if (oldIndex == 0) {
            questionList.setCallback(new QuestionList.Callback() {
                @Override
                public void execute() {
                    exit(stage);
                }
            });
        }

    }

    public int getCurrentIndex(String type) {
        for (int i = 0; i < listTypes.length; i++) {
            if (Objects.equals(type, listTypes[i])) {
                return i;
            }
        }

        return 0;
    }

    public static String getListType(int index) {
        switch (index) {
            case 1:
                return "OPTIONS";
            case 2:
                return "HIGHSCORE";
            case 3:
                return "INFO";
            case 4:
                return "QUESTION";
            case 5:
                return "START";
            case 6:
                return "EXIT";
        }

        return "MAIN";
    }

    public void exit(Stage stage) {
        stage.close();
    }

    public void startGame() {
        menu.setEnd();
    }


}
