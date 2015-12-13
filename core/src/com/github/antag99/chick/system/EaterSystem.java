package com.github.antag99.chick.system;

import com.github.antag99.chick.Tuning;
import com.github.antag99.chick.component.Acting;
import com.github.antag99.chick.component.Eater;
import com.github.antag99.chick.component.Food;
import com.github.antag99.chick.component.Location;
import com.github.antag99.chick.component.Nausea;
import com.github.antag99.chick.component.Position;
import com.github.antag99.chick.component.Size;
import com.github.antag99.chick.component.Wine;
import com.github.antag99.chick.util.CollisionListener;
import com.github.antag99.chick.util.WineActor;
import com.github.antag99.retinazer.EntityProcessorSystem;
import com.github.antag99.retinazer.Family;
import com.github.antag99.retinazer.Mapper;

public final class EaterSystem extends EntityProcessorSystem {
    private DeltaSystem deltaSystem;
    private CollisionSystem collisionSystem;
    private SoundSystem soundSystem;
    private AssetSystem assetSystem;
    private Mapper<Location> mLocation;
    private Mapper<Position> mPosition;
    private Mapper<Size> mSize;
    private Mapper<Eater> mEater;
    private Mapper<Food> mFood;
    private Mapper<Wine> mWine;
    private Mapper<Nausea> mNausea;
    private Mapper<Acting> mActing;

    public EaterSystem() {
        super(Family.with(Eater.class));
    }

    @Override
    protected void initialize() {
        collisionSystem.collisionListener(new CollisionListener() {
            @Override
            public void onCollison(int eaterEntity, int foodEntity) {
                float deltaTime = deltaSystem.getDeltaTime();
                Eater eater = mEater.get(eaterEntity);
                Food food = mFood.get(foodEntity);

                // cannot eat more than one food at once
                if (eater.eating || food.amount <= 0f)
                    return;
                eater.eating = true;

                eater.biteCounter += deltaTime;
                while (eater.biteCounter > eater.biteDelay && food.amount > 0f) {
                    eater.biteCounter -= eater.biteDelay;
                    float consume = Math.min(food.amount, food.consumePerBite);
                    food.amount -= consume;

                    if (!mWine.has(foodEntity)) {
                        eater.food += consume;
                        soundSystem.play(mLocation.get(eaterEntity).room,
                                mPosition.get(eaterEntity).x + mSize.get(eaterEntity).width * 0.5f,
                                mPosition.get(eaterEntity).y + mSize.get(eaterEntity).height * 0.5f,
                                assetSystem.eatSound);
                        if (food.amount <= 0f) {
                            engine.destroyEntity(foodEntity);
                        }
                    } else {
                        soundSystem.play(mLocation.get(eaterEntity).room,
                                mPosition.get(eaterEntity).x + mSize.get(eaterEntity).width * 0.5f,
                                mPosition.get(eaterEntity).y + mSize.get(eaterEntity).height * 0.5f,
                                assetSystem.sipSound);
                        mNausea.get(eaterEntity).amount += consume;
                        ((WineActor) mActing.get(foodEntity).actor).rate = food.amount / Tuning.WINE_AMOUNT;
                    }
                }
            }
        }).a(Family.with(Eater.class)).b(Family.with(Food.class)).add();
    }

    @Override
    protected void process(int entity) {
        // reset flag every frame
        mEater.get(entity).eating = false;
    }
}
