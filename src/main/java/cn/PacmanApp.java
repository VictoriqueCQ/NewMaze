/*
 * The MIT License (MIT)
 *
 * FXGL - JavaFX Game Library
 *
 * Copyright (c) 2015-2017 AlmasB (almaslvl@gmail.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package cn;

import com.almasb.fxgl.app.ApplicationMode;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.gameplay.Level;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.parser.text.TextLevelParser;
import com.almasb.fxgl.settings.GameSettings;
import cn.control.PlayerControl;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;

import java.util.Map;

import static com.almasb.fxgl.app.DSLKt.geti;
import static com.almasb.fxgl.app.DSLKt.inc;

/**
 * This is a basic demo of Pacman.
 *
 * Assets taken from opengameart.org
 * (Carlos Alface 2014 kalface@gmail.com, http://c-toy.blogspot.pt/).
 *
 * @author Almas Baimagambetov (AlmasB) (almaslvl@gmail.com)
 */
public class PacmanApp extends GameApplication {

    public static final int BLOCK_SIZE = 40;

    public static final int MAP_SIZE = 21;

    private static final int UI_SIZE = 200;

    // seconds
    public static final int TIME_PER_LEVEL = 100;

    public Entity getPlayer() {
        return getGameWorld().getSingleton(PacmanType.PLAYER).get();
    }

    public PlayerControl getPlayerControl() {
        return getPlayer().getControl(PlayerControl.class);
    }


    @Override
    protected void initSettings(GameSettings settings) {
//        settings.setWidth(MAP_SIZE * BLOCK_SIZE + UI_SIZE);
        settings.setWidth(MAP_SIZE * BLOCK_SIZE * 2);
        settings.setHeight(MAP_SIZE * BLOCK_SIZE);
        settings.setTitle("NewMaze");
        settings.setProfilingEnabled(false);
        settings.setApplicationMode(ApplicationMode.DEVELOPER);
    }

    @Override
    protected void initInput() {
        getInput().addAction(new UserAction("Up") {
            @Override
            protected void onAction() {
                getPlayerControl().up();
            }
        }, KeyCode.UP);

        getInput().addAction(new UserAction("Down") {
            @Override
            protected void onAction() {
                getPlayerControl().down();
            }
        }, KeyCode.DOWN);

        getInput().addAction(new UserAction("Left") {
            @Override
            protected void onAction() {
                getPlayerControl().left();
            }
        }, KeyCode.LEFT);

        getInput().addAction(new UserAction("Right") {
            @Override
            protected void onAction() {
                getPlayerControl().right();
            }
        }, KeyCode.RIGHT);
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("score", 0);
        vars.put("coins", 0);
        vars.put("teleport", 0);
        vars.put("time", TIME_PER_LEVEL);
    }

    @Override
    protected void initGame() {
        TextLevelParser parser = new TextLevelParser(getGameWorld().getEntityFactory());

        Level level = parser.parse("pacman_level0.txt");
        getGameWorld().setLevel(level);


        // find out number of coins
        getGameState().setValue("coins", getGameWorld().getEntitiesByType(PacmanType.COIN).size());

        getMasterTimer().runAtInterval(
                () -> inc("time", -1),
                Duration.seconds(1)
        );
    }

//    private PacmanUIController uiController;

//    @Override
//    protected void initUI() {
//        uiController = new PacmanUIController();
//        getStateMachine().getPlayState().addStateListener(uiController);
//
//        UI ui = getAssetLoader().loadUI("pacman_ui.fxml", uiController);
//        ui.getRoot().setTranslateX(MAP_SIZE * BLOCK_SIZE);

//        uiController.getLabelScore().textProperty().bind(getGameState().intProperty("score").asString("Score:\n[%d]"));
//        uiController.getLabelTeleport().textProperty().bind(getGameState().intProperty("teleport").asString("Teleports:\n[%d]"));

//        getGameScene().addUI(ui);
//    }

//    @Override
//    protected void onPostUpdate(double tpf) {
//        if (requestNewGame) {
//            requestNewGame = false;
//            getStateMachine().getPlayState().removeStateListener(uiController);
//            startNewGame();
//        }
//    }

    public void onCoinPickup() {
        inc("coins", -1);
        inc("score", +50);

        if (geti("score") % 2000 == 0) {
            inc("teleport", +1);
        }

        if (geti("coins") == 0) {
            gameOver();
        }
    }

    private void gameOver() {
        getDisplay().showMessageBox("Demo Over. Press OK to exit", this::exit);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
