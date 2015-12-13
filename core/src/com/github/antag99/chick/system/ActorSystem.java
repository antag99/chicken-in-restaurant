package com.github.antag99.chick.system;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;
import com.github.antag99.chick.component.Acting;
import com.github.antag99.chick.util.Handle;
import com.github.antag99.retinazer.EntityListener;
import com.github.antag99.retinazer.EntitySet;
import com.github.antag99.retinazer.EntitySystem;
import com.github.antag99.retinazer.Family;
import com.github.antag99.retinazer.Mapper;
import com.github.antag99.retinazer.SkipWire;

public final class ActorSystem extends EntitySystem {
    private Mapper<Acting> mActing;

    private @SkipWire Group root;

    public ActorSystem(Group root) {
        this.root = root;
    }

    public Group getRoot() {
        return root;
    }

    @Override
    protected void initialize() {
        engine.getFamily(Family.with(Acting.class)).addListener(new EntityListener() {
            @Override
            public void inserted(EntitySet entities) {
                int[] items = entities.getIndices().items;
                for (int i = 0, n = entities.size(); i < n; i++) {
                    Acting acting = mActing.get(items[i]);
                    Actor actor = acting.actor;
                    actor.setUserObject(new Handle(engine).set(items[i]));
                    Array<Actor> children = root.getChildren();
                    int insertionIndex = 0;
                    while (insertionIndex < children.size) {
                        Acting other = mActing.get(((Handle) children.get(insertionIndex).getUserObject()).get());
                        if (other.z > acting.z)
                            break;
                        insertionIndex++;
                    }
                    root.addActorAt(insertionIndex, actor);
                }
            }

            @Override
            public void removed(EntitySet entities) {
                int[] items = entities.getIndices().items;
                for (int i = 0, n = entities.size(); i < n; i++) {
                    Actor actor = mActing.get(items[i]).actor;
                    actor.setUserObject(null);
                    root.removeActor(actor);
                }
            }
        });
    }
}
