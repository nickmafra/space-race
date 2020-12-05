package com.nickmafra.spacerace;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;

public class PhysicalBody {

    public Matrix4 transform = new Matrix4();
    public Vector3 linearVelocity = Vector3.Zero.cpy();
    public Matrix4 angularVelocity = new Matrix4();

    private Vector3 tempV1 = new Vector3();
    private Matrix4 tempM1 = new Matrix4();

    public void update(float deltaTime) {
        // translation
        tempV1.set(linearVelocity).scl(deltaTime);
        transform.translate(tempV1);
        // rotation
        Quaternion rotation = angularVelocity.getRotation(new Quaternion(), true).exp(deltaTime);
        tempM1.idt().rotate(rotation);
        transform.mulLeft(tempM1);
    }
}
