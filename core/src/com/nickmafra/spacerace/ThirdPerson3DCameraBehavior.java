package com.nickmafra.spacerace;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;

public class ThirdPerson3DCameraBehavior extends ObjectBody {

    private static final Vector3 defaultCamPosition = Vector3.Zero.cpy();
    private static final Vector3 defaultCamDirection = Vector3.Z.cpy().scl(-1);
    private static final Vector3 defaultCamUp = Vector3.Y.cpy();

    public Camera camera;

    /** Whether to update the camera after it has been changed. */
    public boolean autoUpdate = true;

    public ThirdPerson3DCameraBehavior() {
        localTransform.translate(0, 1, 3).rotate(Vector3.X, -15);
    }

    public void update() {
        updateWorldTransform();
        if (camera == null) {
            return;
        }

        resetCamera();
        camera.transform(worldTransform);

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
