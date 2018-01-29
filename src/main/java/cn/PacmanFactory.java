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

import com.almasb.fxgl.entity.*;
import com.almasb.fxgl.entity.component.CollidableComponent;
import com.almasb.fxgl.entity.view.EntityView;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.texture.Texture;
import cn.control.PlayerControl;
import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import static com.almasb.fxgl.app.DSLKt.texture;

//import com.almasb.fxglgames.pacman.control.AStarMoveControl;
//import com.almasb.fxglgames.pacman.control.PaletteChangingControl;

/**
 * Factory for creating in-game entities.
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
@SetEntityFactory
public class PacmanFactory implements TextEntityFactory {

    @SpawnSymbol('1')
    public Entity newBlock(SpawnData data) {
        return Entities.builder()
                .from(data)
                .type(PacmanType.BLOCK)
                .viewFromNodeWithBBox(new EntityView(new Rectangle(40, 40), RenderLayer.BACKGROUND))
                .build();
    }

    @SpawnSymbol('0')
    public Entity newCoin(SpawnData data) {
        EntityView view = new EntityView(texture("coin.png"));
        view.setTranslateX(2.5);
        view.setRenderLayer(RenderLayer.BACKGROUND);

        return Entities.builder()
                .from(data)
                .type(PacmanType.COIN)
                .bbox(new HitBox("Main", BoundingShape.box(40, 40)))
                .viewFromNodeWithBBox(view)
                .with(new CollidableComponent(true))
                .build();
    }

    @SpawnSymbol('P')
    public Entity newPlayer(SpawnData data) {
        Texture view = texture("player.png").toAnimatedTexture(2, Duration.seconds(0.33));

        return Entities.builder()
                .from(data)
                .type(PacmanType.PLAYER)
                .bbox(new HitBox("PLAYER_BODY", new Point2D(2, 2), BoundingShape.box(36, 36)))
                .viewFromNode(view)
                .with(new CollidableComponent(true))
                .with(new PlayerControl())
                .build();
    }

    @Override
    public char emptyChar() {
        return ' ';
    }

    @Override
    public int blockWidth() {
        return 40;
    }

    @Override
    public int blockHeight() {
        return 40;
    }
}
