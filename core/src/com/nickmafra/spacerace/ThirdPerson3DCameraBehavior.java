package com.nickmafra.spacerace;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

public class ThirdPerson3DCameraBehavior {

    private static final Vector3 defaultCamPosition = new Vector3(Vector3.Zero);
    private static final Vector3 defaultCamDirection = new Vector3(Vector3.Z).scl(-1);
    private static final Vector3 defaultCamUp = new Vector3(Vector3.Y);

    public Camera camera;
    public Matrix4 modelMatrix;

    public Matrix4 camTransformation = new Matrix4().translate(0, 1, 3).rotate(Vector3.X, -15);

    /** Whether to update the camera after it has been changed. */
    public boolean autoUpdate = true;

    private final Vector3 tempV1 = new Vector3();

    public void update() {
        if (camera == null || modelMatrix == null) {
            return;
        }

        resetCamera();
        camera.rotate(camTransformation);
        camera.rotate(modelMatrix);
        camera.translate(camTransformation.getTranslation(tempV1).rot(modelMatrix));
        camera.translate(modelMatrix.getTranslation(tempV1));

        if (autoUpdate) {
            camera.update();
        }
    }

    public void resetCamera() {
        camera.position.set(defaultCamPosition);
        camera.direction.set(defaultCamDirection);
        camera.up.set(defaultCamUp);
    }
}
