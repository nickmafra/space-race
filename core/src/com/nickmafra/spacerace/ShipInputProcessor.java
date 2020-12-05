package com.nickmafra.spacerace;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector3;

public class ShipInputProcessor extends InputAdapter {

    private static final Vector3 defaultDirection = Vector3.Z.cpy().scl(-1);

    public Ship ship;

    public float acceleration = 1f;

    private boolean boosting;
    private float x0;
    private float y0;

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        boosting = true;
        x0 = screenX;
        y0 = screenY;
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        boosting = false;
        return true;
    }

    public void update(float deltaTime) {
        if (boosting) {
            float velocityDelta = acceleration * deltaTime;
            Vector3 direction = defaultDirection.cpy();//.rot(ship.physicalBody.transform).nor();
            ship.physicalBody.velocity.add(direction.scl(velocityDelta));
            // TODO rotation
        }
    }
}
