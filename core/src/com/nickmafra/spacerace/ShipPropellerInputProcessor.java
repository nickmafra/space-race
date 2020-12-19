package com.nickmafra.spacerace;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class ShipPropellerInputProcessor extends InputAdapter {

    public ShipPropeller prop;

    public float maxDiffSize = 50;
    public float deadZone = 0.1f;

    private static final int NONE_POINTER = -10;
    public int pointer = NONE_POINTER;
    private Vector2 initialPosition = new Vector2();
    private Vector2 currentPosition = new Vector2();

    private final Quaternion tempQ = new Quaternion();

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        this.pointer = pointer;
        prop.setBosting(true);
        initialPosition.set(screenX, screenY);
        currentPosition.set(initialPosition);
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (pointer == this.pointer) {

            currentPosition.set(screenX, screenY);

            Vector2 diff = currentPosition.cpy().sub(initialPosition);
            float rotationPercent = calcRotationPercent(diff.len());
            Vector2 dir = new Vector2(diff.x, -diff.y).nor();
            Vector3 relativeDir = calcRelativeDir(rotationPercent, dir);
            Quaternion rotation = tempQ.setFromCross(Vector3.Z, relativeDir);
            prop.setRotation(rotation);

            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (pointer == this.pointer) {
            this.pointer = NONE_POINTER;
            prop.setBosting(false);
            return true;
        } else {
            return false;
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
