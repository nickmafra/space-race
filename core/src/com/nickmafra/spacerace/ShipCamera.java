package com.nickmafra.spacerace;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;

public class ShipCamera extends PerspectiveCamera {

    Ship ship;
    ThirdPerson3DCameraBehavior thirdPerson = new ThirdPerson3DCameraBehavior();

    public ShipCamera() {
        thirdPerson.camera = this;
        thirdPerson.autoUpdate = false;
        this.fieldOfView = 67;
        this.viewportWidth = Gdx.graphics.getWidth();
        this.viewportHeight = Gdx.graphics.getHeight();
        near = 1f;
        far = 300f;
    }

    @Override
    public void update() {
        if (ship != null) {
            thirdPerson.modelMatrix = ship.physicalBody.transform;
            thirdPerson.update();
        }
        super.update();
    }
}
