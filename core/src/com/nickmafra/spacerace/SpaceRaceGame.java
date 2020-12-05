package com.nickmafra.spacerace;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.decals.CameraGroupStrategy;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.List;

public class SpaceRaceGame implements ApplicationListener {

	ModelBatch modelBatch;
	DecalBatch decalBatch;
	AssetManager assets;
	Array<ModelInstance> instances = new Array<>();
	Environment environment;
	boolean loading;

	BackGround bg;
	ShipCamera cam;
	ShipInputProcessor inputProcessor;

	Ship shipPlayer;
	Ship ship2;
	List<Ship> ships;

	@Override
	public void create () {
		cam = new ShipCamera();

		modelBatch = new ModelBatch();
		environment = new Environment();
		decalBatch = new DecalBatch(new CameraGroupStrategy(cam));

		bg = new BackGround();
		bg.configEnvironment(environment);

		shipPlayer = new Ship();
		cam.ship = shipPlayer;
		cam.update();
		bg.camera = cam;

		inputProcessor = new ShipInputProcessor();
		inputProcessor.ship = shipPlayer;
		Gdx.input.setInputProcessor(inputProcessor);

		assets = new AssetManager();
		for (String modelName : Ship.MODEL_NAMES) {
			assets.load(modelName, Model.class);
		}
		assets.load("mc-stars.obj", Model.class);
		assets.load("sun.png", Texture.class);
		loading = true;
	}

	private void doneLoading() {
		Model skyboxModel = assets.get("mc-stars.obj", Model.class);
		Texture sunTexture = assets.get("sun.png", Texture.class);

		shipPlayer.load(assets);

		ship2 = new Ship();
		ship2.load(assets);

		bg.makeSun(sunTexture);
		bg.skyboxInstance = new ModelInstance(skyboxModel);
		instances.add(bg.skyboxInstance);

		ships = new ArrayList<>();
		ships.add(shipPlayer);
		ships.add(ship2);

		shipPlayer.physicalBody.position.set(0, 0, 0);
		shipPlayer.physicalBody.rotation.rotate(Vector3.Y, 180);

		ship2.physicalBody.position.add(-5, 0, 5);
		ship2.physicalBody.linearVelocity.set(0, 0, -0.3f);
		ship2.physicalBody.angularVelocity.rotate(Vector3.X, -20f);

		loading = false;
	}

	@Override
	public void render () {
		if (loading) {
			if (assets.update()) {
				doneLoading();
			} else {
				return; // wait
			}
		}

		float deltaTime = Gdx.graphics.getDeltaTime();
		inputProcessor.update(deltaTime);
		shipPlayer.updatePhysics(deltaTime);
		ship2.updatePhysics(deltaTime);

		shipPlayer.updateModelInstance();
		ship2.updateModelInstance();

		cam.update();

		bg.update();

		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);

		modelBatch.begin(cam);
		modelBatch.render(instances, environment);
		for (Ship ship : ships) {
			modelBatch.render(ship.instances, environment);
		}
		modelBatch.end();

		decalBatch.add(bg.sunDecal);
		decalBatch.flush();
	}

	@Override
	public void dispose () {
		modelBatch.dispose();
		decalBatch.dispose();
		instances.clear();
		assets.dispose();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
