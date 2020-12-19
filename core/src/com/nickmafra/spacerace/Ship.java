package com.nickmafra.spacerace;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;

public class Ship {

    public static final String MODEL_NAME = "ship_body.g3db";
    protected static final String[] MODEL_NAMES = new String[] {MODEL_NAME, ShipPropeller.MODEL_NAME };

    public final PhysicalBody physicalBody = new PhysicalBody();

    public final ModelObjectBody modelObjectBody = new ModelObjectBody();
    public final ShipPropeller propLeft = new ShipPropeller();
    public final ShipPropeller propRight = new ShipPropeller();

    public Ship() {
        propLeft.ship = this;
        propRight.ship = this;
    }

    public void load(AssetManager assets) {
        modelObjectBody.modelInstance = new ModelInstance((Model) assets.get(MODEL_NAME));
        modelObjectBody.setParent(physicalBody);

        propLeft.modelObjectBody.setParent(modelObjectBody);
        propLeft.setRelativeTransform(-0.5f, -0.05f, 0);
        propLeft.load(assets);

        propRight.modelObjectBody.setParent(modelObjectBody);
        propRight.setRelativeTransform(0.5f, -0.05f, 0);
        propRight.load(assets);

        modelObjectBody.updateInstancesList();
    }

    public void update(float deltaTime) {
        physicalBody.updateByPhysics(deltaTime);
        physicalBody.updateWorldTransform();
        modelObjectBody.updateWorldTransform(); // recursive
        propLeft.update(deltaTime);
        propRight.update(deltaTime);
    }

    public void drawParticles() {
        propLeft.drawParticles();
        propRight.drawParticles();
    }
}
