package com.nickmafra.spacerace;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;

public class ShipPropeller {

    public static final String MODEL_NAME = "ship_prop.g3db";

    public Ship ship;
    public final ModelObjectBody modelObjectBody = new ModelObjectBody();

    final ObjectBody propellantObjectBody = new ObjectBody();
    public final PropellantEmitter propellantEmitter =  new PropellantEmitter();
    public final Matrix4 relativeTransform = new Matrix4();

    private static final Vector3 offset = new Vector3(0, 0, 0.7f);
    private static final Vector3 offsetNeg = new Vector3(offset).scl(-1);

    private final Matrix4 rotation = new Matrix4();

    public void load(AssetManager assets) {
        modelObjectBody.modelInstance = new ModelInstance((Model) assets.get(MODEL_NAME));
        udpateLocalTransform();

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

    public void setRotation(Quaternion rotation) {
        this.rotation.set(rotation);
        udpateLocalTransform();
    }

    private void udpateLocalTransform() {
        modelObjectBody.localTransform.setToTranslation(offset).mul(this.rotation).translate(offsetNeg)
                .mulLeft(relativeTransform);
    }
}
