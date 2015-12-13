package com.github.antag99.chick.system;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.github.antag99.chick.component.Hear;
import com.github.antag99.chick.component.Position;
import com.github.antag99.chick.component.Room;
import com.github.antag99.chick.component.Size;
import com.github.antag99.retinazer.EntitySystem;
import com.github.antag99.retinazer.Mapper;

public final class SoundSystem extends EntitySystem {
    private Mapper<Room> mRoom;
    private Mapper<Hear> mHear;
    private Mapper<Position> mPosition;
    private Mapper<Size> mSize;

    public void play(int room, float x, float y, Sound sound) {
        int player = mRoom.get(room).player;
        Position position = mPosition.get(player);
        Size size = mSize.get(player);
        Hear hear = mHear.get(player);
        float centerX = position.x + size.width * 0.5f;
        float centerY = position.y + size.height * 0.5f;
        float distance = Vector2.dst(x, y, centerX, centerY);
        float volume = 1f - MathUtils.clamp(distance / hear.range, 0f, 1f);
        float pitch = 1f;
        float pan = volume * Math.signum(x - centerX);
        sound.play(volume, pitch, pan);
    }
}
