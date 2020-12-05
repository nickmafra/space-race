package com.nickmafra.spacerace;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.decals.CameraGroupStrategy;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class SpaceRaceGame implements ApplicationListener {
	
	PerspectiveCamera cam;
	ThirdPerson3DCameraBehavior thirdPerson;
	ShipInputProcessor inputProcessor;

	ModelBatch modelBatch;
	DecalBatch decalBatch;
	AssetManager assets;
	Array<ModelInstance> instances = new Array<>();
	Environment environment;
	boolean loading;

	float ambientLightIntensity = 0.06f;
	Color ambientColor = new Color(ambientLightIntensity, ambientLightIntensity, ambientLightIntensity, 1f);

	Ship ship = new Ship();
	ModelInstance shipInstance2;
	ModelInstance skyboxInstance;

	float skyboxScale = 2;
	Vector3 skyboxScaleV = new Vector3(skyboxScale, skyboxScale, skyboxScale);

	Sprite sunSprite;
	Decal sun;
	float sunSize = 100;
	float sunScale;
	Vector3 sunDisplacement = new Vector3(0, 0, -100);
	Vector3 sunUp = new Vector3(0, 1, 0);

	@Override
	public void create () {
		modelBatch = new ModelBatch();
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, ambientColor));
		environment.add(new DirectionalLight().set(Color.WHITE, new Vector3(sunDisplacement).scl(-1f)));

		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(1f, 1f, 1f);
		cam.lookAt(0,0,0);
		cam.near = 1f;
		cam.far = 300f;
		cam.update();
		thirdPerson = new ThirdPerson3DCameraBehavior();
		thirdPerson.camera = cam;

		decalBatch = new DecalBatch(new CameraGroupStrategy(cam));

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
		Model skybox = assets.get("mc-stars.obj", Model.class);
		Texture sunTexture = assets.get("sun.png", Texture.class);

		ship.modelInstance = new ModelInstance(shipModel);
        instances.add(ship.modelInstance);
		ship.preModelTransform.rotate(Vector3.Y, 180);
		ship.physicalBody.transform.translate(0, 0, 4);

		shipInstance2 = new ModelInstance(shipModel);
		shipInstance2.transform.translate(0, 2, -4);
		instances.add(shipInstance2);

		sunSprite = new Sprite(sunTexture);
		float sunTextureSize = Math.max(sunSprite.getWidth(), sunSprite.getHeight());
		sunScale = sunSize / sunTextureSize;
		sun = Decal.newDecal(sunSprite, true);
		sun.setScale(sunScale);

		skyboxInstance = new ModelInstance(skybox);
		instances.add(skyboxInstance);

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

		ship.updateModelInstance();
		shipInstance2.transform.rotate(Vector3.X, 0.2f);

		thirdPerson.modelMatrix = ship.physicalBody.transform;
		thirdPerson.update();

		skyboxInstance.transform.setToTranslationAndScaling(cam.position, skyboxScaleV);

		Vector3 sunPosition = new Vector3(sunDisplacement).add(cam.position);
		sun.setPosition(sunPosition);
		sun.lookAt(cam.position, sunUp);

		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);

		modelBatch.begin(cam);
		modelBatch.render(instances, environment);
		modelBatch.end();

		decalBatch.add(sun);
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
