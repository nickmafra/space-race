package com.nickmafra.spacerace;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

import java.util.Arrays;
import java.util.List;

public class Ship {

    private static final String MODEL_BODY_NAME = "ship_body.g3db";
    private static final String MODEL_PROP_NAME = "ship_prop.g3db";
    public static final String[] MODEL_NAMES = new String[] { MODEL_BODY_NAME, MODEL_PROP_NAME };

    public ModelInstance modelInstance;
    private ModelInstance propLeft;
    private ModelInstance propRight;
    public List<ModelInstance> instances;

    public PhysicalBody physicalBody = new PhysicalBody();
    public Matrix4 preBodyTransform = new Matrix4();
    public Matrix4 prePropLeftTransform = new Matrix4().translate(-0.5f, 0, 0);
    public Matrix4 prePropRightTransform = new Matrix4().translate(0.5f, 0, 0);

    public void load(AssetManager assets) {
        Model bodyModel = assets.get(MODEL_BODY_NAME, Model.class);
        Model propModel = assets.get(MODEL_PROP_NAME, Model.class);
        modelInstance = new ModelInstance(bodyModel);
        propLeft = new ModelInstance(propModel);
        propRight = new ModelInstance(propModel);
        instances = Arrays.asList(modelInstance, propLeft, propRight);
    }

    public void updatePhysics(float deltaTime) {
        physicalBody.update(deltaTime);
    }

    public void updateModelInstance() {
        modelInstance.transform.set(preBodyTransform).mulLeft(physicalBody.transform);
        propLeft.transform.set(prePropLeftTransform).mulLeft(physicalBody.transform);
        propRight.transform.set(prePropRightTransform).mulLeft(physicalBody.transform);
    }
}
