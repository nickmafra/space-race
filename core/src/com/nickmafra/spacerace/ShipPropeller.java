package com.nickmafra.spacerace;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import lombok.Setter;

public class ShipPropeller {

    public static final String MODEL_NAME = "ship_prop.g3db";

    public Ship ship;
    public final ModelObjectBody modelObjectBody = new ModelObjectBody();

    final ObjectBody propellantObjectBody = new ObjectBody();
    public final PropellantEmitter propellantEmitter =  new PropellantEmitter();

    private Vector3 relativePosition = new Vector3(); // TODO ajustar
    private final Matrix4 relativeTransform = new Matrix4();

    private static final Vector3 offset = new Vector3(0, 0, 0.7f);
    private static final Vector3 offsetNeg = new Vector3(offset).scl(-1);

    @Setter
    private boolean bosting;
    private final Matrix4 rotation = new Matrix4();

    public void setRelativeTransform(float x, float y, float z) {
        relativePosition.set(x, 0, 0.5f);
        relativeTransform.setToTranslation(x, y, z);
    }

    public void load(AssetManager assets) {
        modelObjectBody.modelInstance = new ModelInstance((Model) assets.get(MODEL_NAME));
        updateLocalTransform();

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
        updateBoost(deltaTime);
    }

    public void drawParticles() {
        propellantEmitter.draw();
    }

    public void setRotation(Quaternion rotation) {
        this.rotation.set(rotation);
    }

    private void updateLocalTransform() {
        modelObjectBody.localTransform.setToTranslation(offset).mul(this.rotation).translate(offsetNeg)
                .mulLeft(relativeTransform);
    }

    private static final Vector3 minusZ = Vector3.Z.cpy().scl(-1);
    public float linearForce = 0.5f;
    public float mass = 1f;
    public float momentOfInertia = 0.1f * mass; // TODO deveria ser vetor

    private final Vector3 tempV = new Vector3();
    private final Vector3 tempV2 = new Vector3();
    private final Quaternion tempQ = new Quaternion();

    public void updateBoost(float deltaTime) {
        if (!bosting) {
            propellantEmitter.end();
            return;
        }
        updateLocalTransform();
        propellantEmitter.start();
        // tempV = relative delta linear momentum
        tempV.set(minusZ).rot(rotation).scl(deltaTime * linearForce);
        // tempV2 = absolute delta linear velocity
        tempV2.set(tempV).scl(1 / mass).rot(ship.physicalBody.worldTransform);
        ship.physicalBody.linearVelocity.add(tempV2);

        // tempV2 = relative delta angular momentum
        tempV2.set(relativePosition).crs(tempV);
        // tempV2 = absolute delta angular velocity
        tempV2.scl(1 / momentOfInertia).rot(ship.physicalBody.worldTransform);
        // tempQ = absolute delta angular velocity
        tempQ.set(tempV2, tempV2.len());
        ship.physicalBody.angularVelocity.rotate(tempQ);
    }
}
