package com.github.antag99.chick.system;

import com.badlogic.gdx.math.MathUtils;
import com.github.antag99.chick.component.CameraShake;
import com.github.antag99.chick.component.Nausea;
import com.github.antag99.retinazer.EntityProcessorSystem;
import com.github.antag99.retinazer.Family;
import com.github.antag99.retinazer.Mapper;

public final class NauseaSystem extends EntityProcessorSystem {
    private DeltaSystem deltaSystem;
    private Mapper<Nausea> mNausea;
    private Mapper<CameraShake> mCameraShake;

    public NauseaSystem() {
        super(Family.with(Nausea.class, CameraShake.class));
    }

    @Override
    protected void process(int entity) {
        Nausea nausea = mNausea.get(entity);
        CameraShake cameraShake = mCameraShake.get(entity);

        float deltaTime = deltaSystem.getDeltaTime();
        nausea.inactive &= nausea.amount < nausea.threshold;
        if (!nausea.inactive) {
            nausea.rotation += (360f / nausea.rotationSpeed) * deltaTime;
            cameraShake.moveX = MathUtils.cosDeg(nausea.rotation) * (nausea.amount / nausea.threshold) * 1f;
            cameraShake.moveY = MathUtils.sinDeg(nausea.rotation) * (nausea.amount / nausea.threshold) * 1f;
        }
        nausea.amount -= nausea.recoverySpeed * deltaTime;
        if (nausea.amount <= 0f) {
            nausea.amount = 0f;
            nausea.inactive = true;
        }
    }
}
