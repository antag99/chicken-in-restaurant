package com.github.antag99.chick.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public final class WineActor extends Actor {
    private TextureRegion glassTexture;
    private TextureRegion wineTexture;

    public float rate = 1f;

    public WineActor(TextureAtlas atlas) {
        this.glassTexture = atlas.findRegion("glass");
        this.wineTexture = atlas.findRegion("wine");
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setColor(Color.WHITE);
        float x = getX(), y = getY(), w = getWidth(), h = getHeight();
        batch.draw(glassTexture, x, y, w, h);
        batch.draw(wineTexture, x + w * 2f / 16f, y + h * 11f / 16f, w * 12f / 16f, h * 5f / 16f * rate);
    }
}
