package com.nickmafra.spacerace;

import com.badlogic.gdx.math.Matrix4;
import lombok.Getter;

public class ObjectBody {

    public Matrix4 localTransform = new Matrix4();

    public Matrix4 worldTransform = new Matrix4();

    @Getter
    private ObjectBody parent;

    public void setParent(ObjectBody parent) {
        this.parent = parent;
    }

    public void updateWorldTransform() {
        worldTransform.set(localTransform);
        if (parent != null) {
            worldTransform.mulLeft(parent.worldTransform);
        }
    }
}
