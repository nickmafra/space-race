package com.nickmafra.spacerace;

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

    private float[] colorsTimeline = new float[] {0};
    private Color[] colors = new Color[] { new Color(1f, 0.2f, 0.05f) };

    RegularEmitter emitter;
    PointSpawnShapeValue pointSpawnShapeValue;
    ScaleInfluencer scaleInfluencer;
    ColorInfluencer.Single colorInfluencer;
    InertialInfluencer inertialInfluencer;
    DynamicsModifier.PolarAcceleration velocityPolarModifier;
    public ParticleController controller;

    float speed = 1f;

    private float[] tempColorArray = new float[3];
    private float[] getFloatColors() {
        float[] floatColors = new float[3 * colors.length];
        for (int i = 0; i < colors.length; i++) {
            colors[i].getColorComponents(tempColorArray);
            floatColors[3 * i] = tempColorArray[0];
            floatColors[3 * i + 1] = tempColorArray[1];
            floatColors[3 * i + 2] = tempColorArray[2];
        }
        return floatColors;
    }

    public static BillboardParticleBatch createBatch() {
        return new BillboardParticleBatch(ParticleShader.AlignMode.Screen, false, 100,
                new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE), null);
    }

    public void create(Texture particleTexture, BillboardParticleBatch batch) {
        // Emission
        emitter = new RegularEmitter();
        emitter.getDuration().setActive(false);
        emitter.getEmission().setHigh(2900);
        emitter.getLife().setHigh(500, 1000);
        emitter.setMaxParticleCount(3000);
        //emitter.setEmissionMode(RegularEmitter.EmissionMode.EnabledUntilCycleEnd);

        // Spawn
        pointSpawnShapeValue = new PointSpawnShapeValue();
        SpawnInfluencer spawnSource = new SpawnInfluencer(pointSpawnShapeValue);

        // Scale
        scaleInfluencer = new ScaleInfluencer();
        scaleInfluencer.value.setTimeline(new float[]{0, 1});
        scaleInfluencer.value.setScaling(new float[]{0, 1});
        scaleInfluencer.value.setLow(0);
        scaleInfluencer.value.setHigh(0.3f);

        // Color
        colorInfluencer = new ColorInfluencer.Single();
        colorInfluencer.alphaValue.setTimeline(new float[] {0, 0.5f, 0.8f, 1});
        colorInfluencer.alphaValue.setScaling(new float[] {0, 0.15f, 0.5f, 0});
        colorInfluencer.colorValue.setTimeline(colorsTimeline);
        colorInfluencer.colorValue.setColors(getFloatColors());

        // InertialInfluencer
        inertialInfluencer = new InertialInfluencer();

        // Dynamics
        DynamicsInfluencer dynamicsInfluencer = new DynamicsInfluencer();
        velocityPolarModifier = new DynamicsModifier.PolarAcceleration();
        velocityPolarModifier.strengthValue.setHigh(5, 10);
        velocityPolarModifier.thetaValue.setHigh(0, 360);
        velocityPolarModifier.phiValue.setActive(true);
        velocityPolarModifier.phiValue.setHigh(-35, 35);
        velocityPolarModifier.phiValue.setScaling(new float[] { 1, 0, 0});
        velocityPolarModifier.phiValue.setTimeline(new float[] { 0, 0.5f, 1});
        dynamicsInfluencer.velocities.add(velocityPolarModifier);

        controller = new ParticleController("Billboard Controller", emitter, new BillboardRenderer(batch),
                new RegionInfluencer.Single(particleTexture),
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
