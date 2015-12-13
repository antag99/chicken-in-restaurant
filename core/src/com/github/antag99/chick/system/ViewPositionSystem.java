package com.github.antag99.chick.system;

import com.github.antag99.chick.component.CameraShake;
import com.github.antag99.chick.component.Position;
import com.github.antag99.chick.component.Size;
import com.github.antag99.chick.component.View;
import com.github.antag99.retinazer.EntityProcessorSystem;
import com.github.antag99.retinazer.Family;
import com.github.antag99.retinazer.Mapper;

public final class ViewPositionSystem extends EntityProcessorSystem {
    private Mapper<View> mView;
    private Mapper<Position> mPosition;
    private Mapper<Size> mSize;
    private Mapper<CameraShake> mCameraShake;

    public ViewPositionSystem() {
        super(Family.with(View.class, Position.class, Size.class));
    }

    @Override
    protected void process(int entity) {
        View view = mView.get(entity);
        Position position = mPosition.get(entity);
        Size size = mSize.get(entity);
        CameraShake cameraShake = mCameraShake.get(entity);

        float x = position.x + size.width * 0.5f;
        float y = position.y + size.height * 0.5f;

        if (cameraShake != null) {
            x += cameraShake.moveX;
            y += cameraShake.moveY;
            cameraShake.moveX = 0f;
            cameraShake.moveY = 0f;
        }

        float w = view.camera.viewportWidth * view.camera.zoom;
        float h = view.camera.viewportHeight * view.camera.zoom;

        if (x - w / 2 < view.minX)
            x = view.minX + w / 2;
        if (x + w / 2 > view.maxX)
            x = view.maxX - w / 2;

        if (y - h / 2 < view.minY)
            y = view.minY + h / 2;
        if (y + h / 2 > view.maxY)
            y = view.maxY - h / 2;

        view.camera.position.x = x;
        view.camera.position.y = y;
        view.camera.update();
    }
}
