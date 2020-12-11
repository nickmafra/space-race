package com.nickmafra.spacerace;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.List;

public class MeteorSet {

    public static final String[] MODEL_NAMES = new String[] {
            "meteor1.g3db",
            "meteor2.g3db",
            "meteor3.g3db"
    };
    private static float maxModelSize = 1f;

    private static final Model[] models = new Model[MODEL_NAMES.length];

    private final List<ModelPhysicalBody> meteors = new ArrayList<>();
    private final List<PhysicalBody> bodies = new ArrayList<>();
    public final List<ModelInstance> modelInstances = new ArrayList<>();

    public Vector3 centerOfSet = new Vector3();
    public float distance = 8f;
    public float baseSize = 8f;
    public float scaleRandomFactor = 2f;

    public static void load(AssetManager assets) {
        for (String modelName : MODEL_NAMES) {
            assets.load(modelName, Model.class);
        }
    }

    public static void createStatic(AssetManager assets) {
        for (int i = 0; i < MODEL_NAMES.length; i++) {
            models[i] = assets.get(MODEL_NAMES[i]);
        }
    }

    public void create(int quantity) {
        for (int i = 0; i < quantity; i++) {
            int modelIndex = MathUtils.random(0, models.length - 1);
            ModelPhysicalBody meteor = new ModelPhysicalBody();
            meteor.modelInstance = new ModelInstance(models[modelIndex]);
            meteors.add(meteor);
            bodies.add(meteor.physicalBody);
            modelInstances.add(meteor.modelInstance);
        }

        float minScale = (baseSize / maxModelSize) / scaleRandomFactor;
        float maxScale = (baseSize / maxModelSize) * scaleRandomFactor;
        ShipMathUtils.randomizePositions(bodies, distance);
        for (ModelPhysicalBody meteor : meteors) {
            ShipMathUtils.randomizeAngle(meteor.physicalBody.rotation);
            float scale = MathUtils.random(minScale, maxScale);
            meteor.modelInstance.transform.scale(scale, scale, scale);
            meteor.physicalBody.position.add(centerOfSet);
        }
    }

    public void update(float deltaTime) {
        for (ModelPhysicalBody meteor : meteors) {
            meteor.update(deltaTime);
        }
    }
}
