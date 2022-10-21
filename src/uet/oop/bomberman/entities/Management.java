package uet.oop.bomberman.entities;

import javafx.scene.canvas.GraphicsContext;
import java.util.ArrayList;
import java.util.List;

public abstract class Management<T extends Entity> {
    protected List<T> list = new ArrayList<>();

    public List<T> getList() {
        return list;
    }

    public void render(GraphicsContext gc) {
        for(T e : list) {
            e.render(gc);
        }
    }
    public void update() {
        for(T e : list) {
            e.update();
        }
    }

}
