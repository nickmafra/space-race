package com.nickmafra.spacerace;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;

public class Ship {

    private static final String MODEL_BODY_NAME = "ship_body.g3db";
    private static final String MODEL_PROP_NAME = "ship_prop.g3db";
    protected static final String[] MODEL_NAMES = new String[] { MODEL_BODY_NAME, MODEL_PROP_NAME };

    public final PhysicalBody physicalBody = new PhysicalBody();

    public final ModelObjectBody modelObjectBody = new ModelObjectBody();
    private final ModelObjectBody propLeftObjectBody = new ModelObjectBody();
    private final ModelObjectBody propRightObjectBody = new ModelObjectBody();

    public void loadModelInstances(AssetManager assets) {
        Model bodyModel = assets.get(MODEL_BODY_NAME, Model.class);
        Model propModel = assets.get(MODEL_PROP_NAME, Model.class);

        modelObjectBody.modelInstance = new ModelInstance(bodyModel);
        modelObjectBody.setParent(physicalBody);

        propLeftObjectBody.modelInstance = new ModelInstance(propModel);
        propLeftObjectBody.localTransform.translate(-0.5f, 0, 0);
        propLeftObjectBody.setParent(modelObjectBody);

        propRightObjectBody.modelInstance = new ModelInstance(propModel);
        propRightObjectBody.localTransform.translate(0.5f, 0, 0);
        propRightObjectBody.setParent(modelObjectBody);

        modelObjectBody.updateInstancesList();
    }

    public void updateWorldTransform() {
        physicalBody.updateWorldTransform();
        modelObjectBody.updateWorldTransform(); // recursive
    }
}
