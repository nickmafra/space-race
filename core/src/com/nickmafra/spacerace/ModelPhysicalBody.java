package com.nickmafra.spacerace;

public class ModelPhysicalBody extends ModelObjectBody {

    public final PhysicalBody physicalBody = new PhysicalBody();

    public ModelPhysicalBody() {
        super.setParent(physicalBody);
    }

    @Override
    public void setParent(ObjectBody parent) {
        physicalBody.setParent(parent);
    }

    public void update(float deltaTime) {
        physicalBody.updateByPhysics(deltaTime);
        physicalBody.updateWorldTransform();
        updateWorldTransform();
    }
}
