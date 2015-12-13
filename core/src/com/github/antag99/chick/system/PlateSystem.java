package com.github.antag99.chick.system;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.github.antag99.chick.Tuning;
import com.github.antag99.chick.component.Acting;
import com.github.antag99.chick.component.Food;
import com.github.antag99.chick.component.Location;
import com.github.antag99.chick.component.Plate;
import com.github.antag99.chick.component.PlateItem;
import com.github.antag99.chick.component.Position;
import com.github.antag99.chick.component.Size;
import com.github.antag99.chick.util.EntityAdapter;
import com.github.antag99.retinazer.EntityProcessorSystem;
import com.github.antag99.retinazer.Family;
import com.github.antag99.retinazer.Mapper;

public final class PlateSystem extends EntityProcessorSystem {
    private AssetSystem assetSystem;
    private DeltaSystem deltaSystem;
    private SoundSystem soundSystem;
    private Mapper<Location> mLocation;
    private Mapper<Position> mPosition;
    private Mapper<Size> mSize;
    private Mapper<Food> mFood;
    private Mapper<Plate> mPlate;
    private Mapper<Acting> mActing;
    private Mapper<PlateItem> mPlateItem;

    public PlateSystem() {
        super(Family.with(Plate.class));
    }

    @Override
    protected void initialize() {
        engine.getFamily(Family.with(PlateItem.class)).addListener(new EntityAdapter() {
            @Override
            protected void removed(int entity) {
                Plate plate = mPlate.get(mPlateItem.get(entity).plate);
                if (plate.entity == entity)
                    plate.entity = -1;
            }
        });
    }

    @Override
    protected void process(int entity) {
        Plate plate = mPlate.get(entity);

        if ((plate.counter -= deltaSystem.getDeltaTime()) <= 0f) {
            Position platePosition = mPosition.get(entity);
            Size plateSize = mSize.get(entity);

            int oldEntity = plate.entity;
            if (plate.open = !plate.open) {
                if (plate.amount > 0f) {
                    plate.entity = engine.createEntity();
                    mLocation.create(plate.entity).room = mLocation.get(entity).room;
                    mPosition.create(plate.entity).xy(platePosition.x, platePosition.y);
                    mSize.create(plate.entity).set(plateSize.width, plateSize.height);
                    mPlateItem.create(plate.entity).plate = entity;
                    Food food = mFood.create(plate.entity);
                    food.amount = plate.amount;
                    food.consumePerBite = Tuning.CHICKEN_CONSUME_PER_BITE;
                    mActing.create(plate.entity).actor = new Image(assetSystem.skin, "food/chicken");
                } else {
                    plate.entity = -1;
                }
                plate.counter += MathUtils.random(Tuning.CHICKEN_MIN_DURATION, Tuning.CHICKEN_MAX_DURATION);
                soundSystem.play(mLocation.get(entity).room,
                        platePosition.x + plateSize.width * 0.5f,
                        platePosition.y + plateSize.height * 0.5f,
                        assetSystem.lidOpenSound);
            } else {
                plate.entity = engine.createEntity();
                mLocation.create(plate.entity).room = mLocation.get(entity).room;
                mPosition.create(plate.entity).xy(platePosition.x, platePosition.y);
                mSize.create(plate.entity).set(plateSize.width, plateSize.height);
                mPlateItem.create(plate.entity).plate = entity;
                mActing.create(plate.entity).actor = new Image(assetSystem.skin, "lid");
                plate.counter += MathUtils.random(Tuning.LID_MIN_DURATION, Tuning.LID_MAX_DURATION);
                if (oldEntity != -1)
                    plate.amount = mFood.get(oldEntity).amount;
                soundSystem.play(mLocation.get(entity).room,
                        platePosition.x + plateSize.width * 0.5f,
                        platePosition.y + plateSize.height * 0.5f,
                        assetSystem.lidCloseSound);
            }

            if (oldEntity != -1)
                engine.destroyEntity(oldEntity);
        }
    }
}
