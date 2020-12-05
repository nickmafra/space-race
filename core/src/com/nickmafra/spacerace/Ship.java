package com.nickmafra.spacerace;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Matrix4;

public class Ship {

    public ModelInstance modelInstance;
    public PhysicalBody physicalBody = new PhysicalBody();
    public Matrix4 preModelTransform = new Matrix4();

    public void updatePhysics(float deltaTime) {
        physicalBody.update(deltaTime);
    }

    public void updateModelInstance() {
        modelInstance.transform.set(preModelTransform).mulLeft(physicalBody.transform);
    }
}
