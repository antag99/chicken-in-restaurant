package com.github.antag99.chick.system;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonJson;
import com.esotericsoftware.spine.attachments.AtlasAttachmentLoader;
import com.esotericsoftware.spine.attachments.RegionAttachment;
import com.github.antag99.retinazer.EntitySystem;
import com.github.antag99.retinazer.SkipWire;

public @SkipWire final class AssetSystem extends EntitySystem {
    public Skin skin;

    public SkeletonData playerSkeletonData;
    public AnimationStateData playerAnimationStateData;

    public Sound lidOpenSound;
    public Sound lidCloseSound;
    public Sound eatSound;
    public Sound sipSound;
    public Sound tapSound;
    public Sound explodeSound;
    public Music themeMusic;

    public AssetSystem(Skin skin) {
        this.skin = skin;
    }

    @Override
    protected void initialize() {
        playerSkeletonData = new SkeletonJson(new AtlasAttachmentLoader(skin.getAtlas()) {
            @Override
            public RegionAttachment newRegionAttachment(com.esotericsoftware.spine.Skin skin, String name, String path) {
                return super.newRegionAttachment(skin, name, "player/images/" + path);
            }
        }) {
            {
                setScale(1f / 32f);
            }
        }.readSkeletonData(Gdx.files.internal("player/player.json"));
        playerAnimationStateData = new AnimationStateData(playerSkeletonData);

        lidOpenSound = newSound("lid_open");
        lidCloseSound = newSound("lid_close");
        eatSound = newSound("eat");
        sipSound = newSound("sip");
        tapSound = newSound("tap");
        explodeSound = newSound("explode");
        themeMusic = Gdx.audio.newMusic(Gdx.files.internal("theme.ogg"));
        skin.add("theme", themeMusic);
    }

    public Sound newSound(String name) {
        Sound sound = Gdx.audio.newSound(Gdx.files.internal(name + ".wav"));
        skin.add(name, sound);
        return sound;
    }
}
