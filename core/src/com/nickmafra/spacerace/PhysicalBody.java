package com.nickmafra.spacerace;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;

public class PhysicalBody {

    /** calculated by position and rotation */
    public Matrix4 transform = new Matrix4();
    public Vector3 position = new Vector3();
    public Matrix4 rotation = new Matrix4();
    public Vector3 linearVelocity = Vector3.Zero.cpy();
    public Matrix4 angularVelocity = new Matrix4();

    private Vector3 tempV1 = new Vector3();
    private Matrix4 tempM1 = new Matrix4();

    public void update(float deltaTime) {
        // position
        tempV1.set(linearVelocity).scl(deltaTime);
        position.add(tempV1);
        // rotation
        Quaternion qRotation = angularVelocity.getRotation(new Quaternion(), true).exp(deltaTime);
        tempM1.idt().rotate(qRotation);
        rotation.mulLeft(tempM1);
        // update transform matrix
        transform.setToTranslation(position);
        transform.mul(rotation);
    }
}
