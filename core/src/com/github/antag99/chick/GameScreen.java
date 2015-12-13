package com.github.antag99.chick;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.github.antag99.chick.component.Dead;
import com.github.antag99.chick.component.Room;
import com.github.antag99.chick.system.ActorColorSystem;
import com.github.antag99.chick.system.ActorPositionSystem;
import com.github.antag99.chick.system.ActorSizeSystem;
import com.github.antag99.chick.system.ActorSystem;
import com.github.antag99.chick.system.AssetSystem;
import com.github.antag99.chick.system.BorderSystem;
import com.github.antag99.chick.system.CollisionSystem;
import com.github.antag99.chick.system.ControlSystem;
import com.github.antag99.chick.system.DeltaSystem;
import com.github.antag99.chick.system.DirectionSystem;
import com.github.antag99.chick.system.EaterSystem;
import com.github.antag99.chick.system.FadeSystem;
import com.github.antag99.chick.system.GameSystem;
import com.github.antag99.chick.system.GravitySystem;
import com.github.antag99.chick.system.InputSystem;
import com.github.antag99.chick.system.JumpSystem;
import com.github.antag99.chick.system.MapRenderSystem;
import com.github.antag99.chick.system.NauseaSystem;
import com.github.antag99.chick.system.PlateSystem;
import com.github.antag99.chick.system.PlatformSystem;
import com.github.antag99.chick.system.PlayerSystem;
import com.github.antag99.chick.system.ScriptSystem;
import com.github.antag99.chick.system.SoundSystem;
import com.github.antag99.chick.system.SpatialSystem;
import com.github.antag99.chick.system.VelocitySystem;
import com.github.antag99.chick.system.ViewPositionSystem;
import com.github.antag99.chick.util.AreaViewport;
import com.github.antag99.retinazer.Engine;
import com.github.antag99.retinazer.EngineConfig;

public final class GameScreen extends ScreenAdapter {
    public ChickGame game;
    public Stage stage;
    public AreaViewport viewport;
    public OrthographicCamera camera;
    public Stage uiStage;
    public AreaViewport uiViewport;
    public Engine engine;

    public Table victoryTable;
    public int gameEntity;

    public GameScreen(ChickGame game) {
        this.game = game;

        camera = new OrthographicCamera();
        viewport = new AreaViewport(camera);
        viewport.setWorldArea(10000f);
        viewport.setPixelsPerUnit(16f);
        stage = new Stage(viewport, game.batch);
        uiViewport = new AreaViewport();
        uiViewport.setWorldArea(500000f);
        uiViewport.setPixelsPerUnit(1f);
        uiStage = new Stage(uiViewport, game.batch);
        victoryTable = new Table();
        victoryTable.add(new Label("Victory!", game.skin, "victory"));
        victoryTable.row();
        victoryTable.add(new Label("Press ESC to restart.", game.skin, "victory"));
        victoryTable.setFillParent(true);
        victoryTable.setVisible(false);
        uiStage.addActor(victoryTable);

        engine = new Engine(new EngineConfig()
                .addSystem(new AssetSystem(game.skin))
                .addSystem(new DeltaSystem())
                .addSystem(new GameSystem(this))
                .addSystem(new SoundSystem())

                .addSystem(new PlateSystem())
                .addSystem(new GravitySystem(Tuning.GRAVITY_X, Tuning.GRAVITY_Y,
                        Tuning.MAX_GRAVITY_X, Tuning.MAX_GRAVITY_Y))
                .addSystem(new InputSystem())
                .addSystem(new ControlSystem())
                .addSystem(new VelocitySystem())
                .addSystem(new PlatformSystem())
                .addSystem(new JumpSystem())
                .addSystem(new BorderSystem())
                .addSystem(new SpatialSystem(32, 32))
                .addSystem(new CollisionSystem())
                .addSystem(new ScriptSystem())
                .addSystem(new DirectionSystem())
                .addSystem(new PlayerSystem())
                .addSystem(new EaterSystem())
                .addSystem(new NauseaSystem())

                .addSystem(new FadeSystem())
                .addSystem(new ActorColorSystem())
                .addSystem(new ActorPositionSystem())
                .addSystem(new ActorSizeSystem())
                .addSystem(new ActorSystem(stage.getRoot()))
                .addSystem(new ViewPositionSystem())
                .addSystem(new MapRenderSystem()));
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        gameEntity = engine.getSystem(GameSystem.class).startGame();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        uiViewport.update(width, height, true);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        int player = engine.getMapper(Room.class).get(gameEntity).player;
        boolean isDead = engine.getMapper(Dead.class).has(player);

        victoryTable.setVisible(isDead);
        engine.getSystem(DeltaSystem.class).setDeltaTime(delta);
        engine.update();

        stage.act();
        stage.draw();

        uiStage.act();
        uiStage.draw();

        if (isDead && Gdx.input.isKeyPressed(Keys.ESCAPE)) {
            // WHAT, IS MY CODE MESSY? :D
            dispose();
            game.setScreen(new GameScreen(game));
        }
    }
}
