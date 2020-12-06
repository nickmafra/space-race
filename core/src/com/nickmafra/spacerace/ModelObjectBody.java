package com.nickmafra.spacerace;

import com.badlogic.gdx.graphics.g3d.ModelInstance;

import java.util.ArrayList;
import java.util.List;

public class ModelObjectBody extends ObjectBody {

    public ModelInstance modelInstance;
    public List<ModelObjectBody> children = new ArrayList<>();
    public List<ModelInstance> instances = new ArrayList<>();

    public void setParent(ObjectBody parent) {
        super.setParent(parent);
        if (parent instanceof ModelObjectBody) {
            ModelObjectBody modelParent = (ModelObjectBody) parent;
            modelParent.children.add(this);
        }
    }

    @Override
    public void updateWorldTransform() {
        super.updateWorldTransform();
        modelInstance.transform.set(worldTransform);
        for (ModelObjectBody child : children) {
            child.updateWorldTransform();
        }
    }

    public void updateInstancesList() {
        instances.clear();
        instances.add(modelInstance);
        for (ModelObjectBody child : children) {
            child.updateInstancesList();
            instances.addAll(child.instances);
        }
    }
}
