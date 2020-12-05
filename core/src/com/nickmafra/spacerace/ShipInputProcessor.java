package com.nickmafra.spacerace;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class ShipInputProcessor extends InputAdapter {

    private static final Vector3 minusZ = Vector3.Z.cpy().scl(-1);

    public Ship ship;

    public float linearAcceleration = 1f;
    public float angularAcceleration = 1f;
    public float deadZone = 10;
    public float rotationFactor = 0.01f;

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
            Vector3 direction = minusZ.cpy().rot(ship.physicalBody.transform).nor();
            ship.physicalBody.linearVelocity.add(direction.scl(velocityDelta));

            Vector2 diff = currentPosition.cpy().sub(initialPosition);
            if (diff.len() > deadZone) {
                float rotMultiplier = calcRotMultiplier(diff);
                Vector2 rotAxis2d = diff.cpy().rotate90(-1);
                Vector3 rotAxis3d = new Vector3(-rotAxis2d.x, rotAxis2d.y, 0);
                ship.physicalBody.angularVelocity.rotate(rotAxis3d, angularAcceleration * rotMultiplier);
            }
        }
    }

    private float calcRotMultiplier(Vector2 diff) {
        return diff.len() * rotationFactor;
    }
}
