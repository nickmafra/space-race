package com.nickmafra.spacerace;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.particles.ParticleController;
import com.badlogic.gdx.graphics.g3d.particles.ParticleShader;
import com.badlogic.gdx.graphics.g3d.particles.batches.BillboardParticleBatch;
import com.badlogic.gdx.graphics.g3d.particles.emitters.RegularEmitter;
import com.badlogic.gdx.graphics.g3d.particles.influencers.*;
import com.badlogic.gdx.graphics.g3d.particles.renderers.BillboardRenderer;
import com.badlogic.gdx.graphics.g3d.particles.values.PointSpawnShapeValue;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

import java.awt.*;

public class PropellantEmitter {

    public static final String TEXTURE_NAME = "particle.png";

    private static float[] colorsTimeline = new float[] {0};
    private static Color[] colors = new Color[] { new Color(1f, 0.2f, 0.05f) };

    public static BillboardParticleBatch batch;
    public static Texture texture;

    RegularEmitter emitter;
    RegionInfluencer regionInfluencer;
    SpawnInfluencer spawnSource;
    ScaleInfluencer scaleInfluencer;
    ColorInfluencer.Single colorInfluencer;

    InertialInfluencer inertialInfluencer;

    DynamicsInfluencer dynamicsInfluencer;
    DynamicsModifier.PolarAcceleration velocityPolarModifier;

    public ParticleController controller;

    float speed = 1f;
    float size = 0.3f;
    float strength = 1f;
    float life = 500f;
    int count = 1000;

    private static float[] tempColorArray = new float[3];
    private static float[] getFloatColors() {
        float[] floatColors = new float[3 * colors.length];
        for (int i = 0; i < colors.length; i++) {
            colors[i].getColorComponents(tempColorArray);
            floatColors[3 * i] = tempColorArray[0];
            floatColors[3 * i + 1] = tempColorArray[1];
            floatColors[3 * i + 2] = tempColorArray[2];
        }
        return floatColors;
    }

    public static void createStatic(Camera camera) {
        batch = new BillboardParticleBatch(ParticleShader.AlignMode.Screen, false, 100,
                new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE), null);
        batch.setCamera(camera);
    }

    private static void checkCreated() {
        if (batch == null) {
            throw new IllegalStateException("must call createStatic before this method");
        }
    }

    public static void load(AssetManager assets) {
        checkCreated();
        texture = assets.get(TEXTURE_NAME);
        //regionInfluencer.add(new TextureRegion(texture));
        batch.setTexture(texture);
    }

    public void create() {
        checkCreated();

        // Emission
        emitter = new RegularEmitter();
        emitter.getDuration().setActive(false);
        emitter.getEmission().setHigh(2900);
        emitter.getLife().setHigh(life, life * 2);
        emitter.setMaxParticleCount(count);
        //emitter.setEmissionMode(RegularEmitter.EmissionMode.EnabledUntilCycleEnd);

        regionInfluencer = new RegionInfluencer.Single(texture);

        // Spawn
        spawnSource = new SpawnInfluencer(new PointSpawnShapeValue());

        // Scale
        scaleInfluencer = new ScaleInfluencer();
        scaleInfluencer.value.setTimeline(new float[]{0, 1});
        scaleInfluencer.value.setScaling(new float[]{0, size});
        scaleInfluencer.value.setLow(0);
        scaleInfluencer.value.setHigh(0.3f);

        // Color
        colorInfluencer = new ColorInfluencer.Single();
        colorInfluencer.alphaValue.setTimeline(new float[] {0, 0.5f, 0.8f, 1});
        colorInfluencer.alphaValue.setScaling(new float[] {0, 0.15f, 0.5f, 0});
        colorInfluencer.colorValue.setTimeline(colorsTimeline);
        colorInfluencer.colorValue.setColors(getFloatColors());

        // Dynamics
        dynamicsInfluencer = new DynamicsInfluencer();
        velocityPolarModifier = new DynamicsModifier.PolarAcceleration();
        velocityPolarModifier.strengthValue.setHigh(strength, 2 * strength);
        velocityPolarModifier.thetaValue.setHigh(0, 360);
        velocityPolarModifier.phiValue.setActive(true);
        velocityPolarModifier.phiValue.setHigh(-35, 35);
        velocityPolarModifier.phiValue.setScaling(new float[] { 1, 0, 0});
        velocityPolarModifier.phiValue.setTimeline(new float[] { 0, 0.5f, 1});
        dynamicsInfluencer.velocities.add(velocityPolarModifier);

        // InertialInfluencer
        inertialInfluencer = new InertialInfluencer();

        controller = new ParticleController("PropellantEmitter", emitter, new BillboardRenderer(batch),
                regionInfluencer,
                spawnSource,
                scaleInfluencer,
                colorInfluencer,
                dynamicsInfluencer,
                inertialInfluencer
        );
        controller.init();
    }

    public void start() {
        controller.start();
    }

    public void end() {
        controller.end();
    }

    public void update(float deltaTime) {
        controller.update(deltaTime * speed);
    }

    public void draw() {
        controller.draw();
    }

    public void setVelocity(Vector3 velocity) {
        inertialInfluencer.velocity.set(velocity);
    }

    public void setTransform(Matrix4 transform) {
        controller.setTransform(transform);
    }
}
