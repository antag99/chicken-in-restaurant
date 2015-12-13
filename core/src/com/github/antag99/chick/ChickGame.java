package com.github.antag99.chick;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public final class ChickGame extends Game {
    public Batch batch;
    public Skin skin;

    @Override
    public void create() {
        batch = new SpriteBatch();
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("skin.atlas"));
        // Images such as "belly_0", "belly_1" are handled specially by TextureAtlas; revert this.
        for (AtlasRegion region : atlas.getRegions()) {
            if (region.index != -1) {
                atlas.addRegion(region.name + "_" + region.index, region);
            }
        }
        skin = new Skin(Gdx.files.internal("skin.json"), atlas);
        setScreen(new GameScreen(this));
    }
}
