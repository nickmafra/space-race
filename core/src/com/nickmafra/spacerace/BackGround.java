package com.nickmafra.spacerace;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.math.Vector3;

public class BackGround {

    Camera camera;

    float ambientLightIntensity = 0.5f;
    Color ambientColor = new Color(ambientLightIntensity, ambientLightIntensity, ambientLightIntensity, 1f);

    float skyboxScale = 2;
    Vector3 skyboxScaleV = new Vector3(skyboxScale, skyboxScale, skyboxScale);

    float sunSize = 100;
    float sunDistance = 100;
    Vector3 sunDisplacement = Vector3.X.cpy().scl(sunDistance);
    Vector3 sunUp = Vector3.Y;

    ModelInstance skyboxInstance;
    Decal sunDecal;

    DirectionalLight sunLight;


    public BackGround() {
        sunLight = new DirectionalLight().set(Color.WHITE, new Vector3(sunDisplacement).scl(-1f));
    }

    public void makeSun(Texture sunTexture) {
        float sunTextureSize = Math.max(sunTexture.getWidth(), sunTexture.getHeight());

        sunDecal = Decal.newDecal(new Sprite(sunTexture), true);
        sunDecal.setScale(sunSize / sunTextureSize);
    }

    public void update() {
        if (camera == null) {
            return;
        }
        skyboxInstance.transform.setToTranslationAndScaling(camera.position, skyboxScaleV);

        if (sunDecal != null) {
            Vector3 sunPosition = sunDisplacement.cpy().add(camera.position);
            sunDecal.setPosition(sunPosition);
            sunDecal.lookAt(camera.position, sunUp);
        }
    }

    public void configEnvironment(Environment environment) {
        environment.clear();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, ambientColor));
        environment.add(sunLight);
    }
}
