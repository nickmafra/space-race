package com.nickmafra.spacerace;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

public class PhysicalBody {

    public Matrix4 transform = new Matrix4();
    public Vector3 velocity = Vector3.Zero.cpy();

    public void update(float deltaTime) {
        transform.translate(velocity.cpy().scl(deltaTime));
    }
}
