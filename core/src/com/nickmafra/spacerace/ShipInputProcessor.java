package com.nickmafra.spacerace;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;

public class ShipInputProcessor extends InputAdapter {

    private Ship ship;
    private final ShipPropellerInputProcessor left = new ShipPropellerInputProcessor();
    private final ShipPropellerInputProcessor right = new ShipPropellerInputProcessor();

    public float screenSidePercent = 0.3f;

    public void setShip(Ship ship) {
        this.ship = ship;
        left.prop = ship.propLeft;
        right.prop = ship.propRight;
    }

    private boolean isLeft(float pX) {
        return pX < 1 - screenSidePercent;
    }

    private boolean isRight(float pX) {
        return pX > screenSidePercent;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        float pX = screenX / (float) Gdx.graphics.getWidth();
        boolean r = false;
        if (isLeft(pX)) {
            r |= left.touchDown(screenX, screenY, pointer, button);
        }
        if (isRight(pX)) {
            r |= right.touchDown(screenX, screenY, pointer, button);
        }
        return r;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        boolean r = false;
        r |= left.touchDragged(screenX, screenY, pointer);
        r |= right.touchDragged(screenX, screenY, pointer);
        return r;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        boolean r = false;
        r |= left.touchUp(screenX, screenY, pointer, button);
        r |= right.touchUp(screenX, screenY, pointer, button);
        return r;
    }
}
