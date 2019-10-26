package com.github.salc2;


import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.*;
import javafx.scene.paint.*;
import javafx.scene.canvas.*;
import com.sun.javafx.perf.PerformanceTracker;

import java.util.concurrent.atomic.AtomicBoolean;

public class Main extends Application {

    public final static double GRAVITY = 0.75;
    private final Image img = new Image(Main.class.getResourceAsStream("/img/lineup.png"));
    private final AtomicBoolean isAdding = new AtomicBoolean(false);
    private int count = 12;
    private int maxCount = 200000;
    private int currentFrame = 0;
    private Bunny[] bunnies = new Bunny[maxCount];

    @Override
    public void start(Stage stage) {
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        Group root = new Group();
        Scene s = new Scene(root, primaryScreenBounds.getWidth(), primaryScreenBounds.getHeight(), Color.BLACK);

        Frame[] frames = new Frame[12];
        for(int i=0;i<12;i++){
            frames[i] = new Frame(i*36,0,36,36);
        }

        for(int i=0;i<maxCount;i++){
            currentFrame = (currentFrame + 1) % 12;
            bunnies[i] = new Bunny(new Vector(5,5),Math.random() * 10,(Math.random() * 10) - 5, frames[currentFrame]);
        }

        final Canvas canvas = new Canvas(250,250);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        final Label fpsInfo = new Label("0 FPS");
        fpsInfo.setTextFill(Color.WHITE);
        final Label bunnyInfo = new Label(count +" Bunnies");
        bunnyInfo.setTextFill(Color.WHITE);
        HBox hBox = new HBox(fpsInfo, bunnyInfo);
        hBox.setSpacing(10);
        root.getChildren().add(hBox);

        root.getChildren().add(canvas);

        stage.setScene(s);
        stage.show();

        Bound bound = new Bound();

        gc.setFill(Color.WHITE);
        gc.setFont(new Font("Verdana", 32));

        AnimationTimer timer = new AnimationTimer() {
            private long prevTime = 0;
            private double delta = System.nanoTime();
            @Override
            public void handle(long now) {
                final double w = s.getWindow().getWidth();
                final double h = s.getWindow().getHeight();

                bound.update(w,h);
                gc.getCanvas().setHeight(h);
                gc.getCanvas().setWidth(w);
                gc.clearRect(0,0,w,h);
                if (isAdding.get()){
                    count += 100;
                }

                for(int i = 0; i< count; i++){
                    Bunny bunny = bunnies[i];
                    Frame f = bunny.frame;
                    bunny.update(bound,delta);
                    gc.drawImage(img,f.sx,f.sy,f.sw,f.sh, bunny.position.x,bunny.position.y,50,50);
                }
                bunnyInfo.setText(count+" Bunnies");
                fpsInfo.setText(Math.round(PerformanceTracker.getSceneTracker(s).getInstantFPS())+" FPS");
                delta = (now - prevTime) / 1e7;
                prevTime = now;
            }

        };


        EventHandler<MouseEvent> eventHandlerPressed = e -> {
            isAdding.set(true);
        };

        EventHandler<MouseEvent> eventHandlerReleased = e -> {
            isAdding.set(false);
        };

        stage.addEventFilter(MouseEvent.MOUSE_PRESSED, eventHandlerPressed);
        stage.addEventFilter(MouseEvent.MOUSE_RELEASED, eventHandlerReleased);

        timer.start();
    }
}
