package com.nickmafra.spacerace;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.math.Vector3;

public class BackGround {

    Camera camera;

    float skyboxScale = 2;
    Vector3 skyboxScaleV = new Vector3(skyboxScale, skyboxScale, skyboxScale);

    float sunSize = 100;
    float sunDistance = -100;
    Vector3 sunDisplacement = Vector3.Z.cpy().scl(sunDistance);
    Vector3 sunUp = Vector3.Y;

    ModelInstance skyboxInstance;
    Decal sunDecal;

    public void makeSun(Texture sunTexture) {
        float sunTextureSize = Math.max(sunTexture.getWidth(), sunTexture.getHeight());

        sunDecal = Decal.newDecal(new Sprite(sunTexture), true);
        sunDecal.setScale(sunSize / sunTextureSize);
    }

    public void update() {
        skyboxInstance.transform.setToTranslationAndScaling(camera.position, skyboxScaleV);

        Vector3 sunPosition = sunDisplacement.cpy().add(camera.position);
        sunDecal.setPosition(sunPosition);
        sunDecal.lookAt(camera.position, sunUp);
    }
}
