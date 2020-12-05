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

public class SpaceRaceGame implements ApplicationListener {

	ModelBatch modelBatch;
	DecalBatch decalBatch;
	AssetManager assets;
	Array<ModelInstance> instances = new Array<>();
	Environment environment;
	boolean loading;

	BackGround bg;
	Ship ship;
	ShipCamera cam;
	ShipInputProcessor inputProcessor;

	Ship ship2;

	@Override
	public void create () {
		cam = new ShipCamera();

		modelBatch = new ModelBatch();
		environment = new Environment();
		decalBatch = new DecalBatch(new CameraGroupStrategy(cam));

		bg = new BackGround();
		bg.configEnvironment(environment);

		ship = new Ship();
		cam.ship = ship;
		cam.update();
		bg.camera = cam;

		inputProcessor = new ShipInputProcessor();
		inputProcessor.ship = ship;
		Gdx.input.setInputProcessor(inputProcessor);

		assets = new AssetManager();
		assets.load("ship.g3db", Model.class);
		assets.load("mc-stars.obj", Model.class);
		assets.load("sun.png", Texture.class);
		loading = true;
	}

	private void doneLoading() {
		Model shipModel = assets.get("ship.g3db", Model.class);
		Model skyboxModel = assets.get("mc-stars.obj", Model.class);
		Texture sunTexture = assets.get("sun.png", Texture.class);

		ship.modelInstance = new ModelInstance(shipModel);
        instances.add(ship.modelInstance);

		ship2 = new Ship();
		ship2.modelInstance = new ModelInstance(shipModel);
		instances.add(ship2.modelInstance);

		bg.makeSun(sunTexture);
		bg.skyboxInstance = new ModelInstance(skyboxModel);
		instances.add(bg.skyboxInstance);

		ship.physicalBody.transform.translate(0, 0, 4);
		ship2.modelInstance.transform.translate(0, 2, -4);
		ship2.physicalBody.angularVelocity.rotate(Vector3.X, 20f);

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
		ship.updatePhysics(deltaTime);
		ship2.updatePhysics(deltaTime);

		ship.updateModelInstance();
		ship2.updateModelInstance();

		cam.update();

		bg.update();

		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);

		modelBatch.begin(cam);
		modelBatch.render(instances, environment);
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
