package com.nickmafra.spacerace;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class ShipInputProcessor extends InputAdapter {

    private static final Vector3 minusZ = Vector3.Z.cpy().scl(-1);

    public Ship ship;

    public float linearAcceleration = 1f;
    public float angularAcceleration = 10f;

    public float maxDiffSize = 50;
    public float deadZone = 0.1f;

    private boolean boosting;
    private Vector2 initialPosition = new Vector2();
    private Vector2 currentPosition = new Vector2();

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        boosting = true;
        initialPosition.set(screenX, screenY);
        currentPosition.set(initialPosition);
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (boosting) {

            currentPosition.set(screenX, screenY);

            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        boosting = false;
        return true;
    }

    public void update(float deltaTime) {
        if (boosting) {
            float velocityDelta = linearAcceleration * deltaTime;
            Vector3 direction = minusZ.cpy().rot(ship.physicalBody.worldTransform).nor();
            ship.physicalBody.linearVelocity.add(direction.scl(velocityDelta));

            Vector2 diff = currentPosition.cpy().sub(initialPosition);
            float rotationPercent = calcRotationPercent(diff);
            if (rotationPercent > 0) {
                float angularVelocityDelta = rotationPercent * angularAcceleration * deltaTime;
                Vector2 rotAxis2d = new Vector2(diff.x, -diff.y).rotate90(1);
                Vector3 rotAxis3d = new Vector3(rotAxis2d, 0).rot(ship.physicalBody.worldTransform).nor();
                ship.physicalBody.angularVelocity.rotate(rotAxis3d, angularVelocityDelta);
            }
        }
    }

    /**
     * Translates diff and maxDiffSize in rotation percent. Uses deadZone.
     * <br>
     * note: 0 <= percent <= 1
     */
    private float calcRotationPercent(Vector2 diff) {
        float percent = diff.len() / maxDiffSize;
        if (percent <= deadZone) {
            return 0;
        }
        if (percent >= 1) {
            return 1;
        }
        return percent;
    }
}
