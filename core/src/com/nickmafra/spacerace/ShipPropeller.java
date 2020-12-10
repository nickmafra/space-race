package com.nickmafra.spacerace;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;

public class ShipPropeller {

    public static final String MODEL_NAME = "ship_prop.g3db";

    public Ship ship;
    public final ModelObjectBody modelObjectBody = new ModelObjectBody();

    ObjectBody propellantObjectBody = new ObjectBody();
    PropellantEmitter propellantEmitter =  new PropellantEmitter();

    public void load(AssetManager assets) {
        modelObjectBody.modelInstance = new ModelInstance((Model) assets.get(MODEL_NAME));

        propellantObjectBody.setParent(modelObjectBody);
        propellantObjectBody.localTransform.translate(0, 0, 0.9f).rotate(Vector3.X, 90);
        propellantEmitter.create();
        propellantEmitter.start();
    }

    public void update(float deltaTime) {
        propellantObjectBody.updateWorldTransform();
        propellantEmitter.update(deltaTime);
        propellantEmitter.setTransform(propellantObjectBody.worldTransform);
        propellantEmitter.setVelocity(ship.physicalBody.linearVelocity);
    }

    public void drawParticles() {
        propellantEmitter.draw();
    }
}
