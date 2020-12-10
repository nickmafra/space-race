package com.nickmafra.spacerace;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Quaternion;
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

    private final Vector3 tempV = new Vector3();
    private final Quaternion tempQ = new Quaternion();

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        boosting = true;
        initialPosition.set(screenX, screenY);
        currentPosition.set(initialPosition);
        ship.propLeft.propellantEmitter.start();
        ship.propRight.propellantEmitter.start();
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
        ship.propLeft.propellantEmitter.end();
        ship.propRight.propellantEmitter.end();
        return true;
    }

    public void update(float deltaTime) {
        if (boosting) {
            Vector2 diff = currentPosition.cpy().sub(initialPosition);
            float rotationPercent = calcRotationPercent(diff.len());
            Vector2 dir = new Vector2(diff.x, -diff.y).nor();
            Vector3 relativeDir = calcRelativeDir(rotationPercent, dir);
            Quaternion rotation = tempQ.setFromCross(Vector3.Z, relativeDir);
            ship.propLeft.setRotation(rotation);
            ship.propRight.setRotation(rotation);

            Vector3 shipDir = minusZ.cpy().rot(ship.physicalBody.worldTransform).nor();
            float velocityDelta = linearAcceleration * deltaTime;
            ship.physicalBody.linearVelocity.add(shipDir.scl(velocityDelta));

            if (rotationPercent > 0) {
                float angularVelocityDelta = rotationPercent * angularAcceleration * deltaTime;
                Vector3 rotAxis3d = tempV.set(relativeDir).crs(Vector3.Z).rot(ship.physicalBody.worldTransform).nor();
                ship.physicalBody.angularVelocity.rotate(rotAxis3d, angularVelocityDelta);
            }
        }
    }

    /**
     * Translates diff and maxDiffSize in rotation percent. Uses deadZone.
     * <br>
     * note: 0 <= percent <= 1
     */
    private float calcRotationPercent(float distance) {
        float percent = distance / maxDiffSize;
        if (percent <= deadZone) {
            return 0;
        }
        if (percent >= 1) {
            return 1;
        }
        return percent;
    }

    private Vector3 calcRelativeDir(float rotationPercent, Vector2 dir) {
        if (rotationPercent == 0) {
            return Vector3.Z;
        }
        return Vector3.Z.cpy()
                .rotateRad(Vector3.Y, rotationPercent * MathUtils.PI / 2)
                .rotateRad(Vector3.Z, dir.angleRad());
    }
}
