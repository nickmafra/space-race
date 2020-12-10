package com.nickmafra.spacerace;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.particles.ParticleController;
import com.badlogic.gdx.graphics.g3d.particles.ParticleShader;
import com.badlogic.gdx.graphics.g3d.particles.batches.BillboardParticleBatch;
import com.badlogic.gdx.graphics.g3d.particles.emitters.RegularEmitter;
import com.badlogic.gdx.graphics.g3d.particles.influencers.ColorInfluencer;
import com.badlogic.gdx.graphics.g3d.particles.influencers.RegionInfluencer;
import com.badlogic.gdx.graphics.g3d.particles.influencers.ScaleInfluencer;
import com.badlogic.gdx.graphics.g3d.particles.renderers.BillboardRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

public class PropellantEmitter {

    public static final String TEXTURE_NAME = "particle.png";

    private static final float[] colorsTimeline = new float[] {0};
    private static final Color[] colors = new Color[] { new Color(1f, 0.3f, 0.05f, 1f) };

    public static BillboardParticleBatch batch;
    public static Texture texture;

    RegularEmitter emitter;
    RegionInfluencer regionInfluencer;
    FixedSpawnInfluencer spawnSource;
    ScaleInfluencer scaleInfluencer;
    ColorInfluencer.Single colorInfluencer;

    InertialInfluencer inertialInfluencer;
    PolarInertialModifier polarInertialModifier;

    public ParticleController controller;

    float speed = 1f;
    float size = 0.5f;
    float strength = 0.2f;
    float life = 500f;
    int count = 50;

    private static float[] getFloatColors() {
        float[] floatColors = new float[3 * colors.length];
        for (int i = 0; i < colors.length; i++) {
            floatColors[3 * i] = colors[i].r;
            floatColors[3 * i + 1] = colors[i].g;
            floatColors[3 * i + 2] = colors[i].b;
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
        batch.setTexture(texture);
    }

    public void create() {
        checkCreated();

        // Emission
        emitter = new RegularEmitter();
        emitter.getDuration().setActive(false);
        emitter.getEmission().setHigh(count / 2f, count);
        emitter.getLife().setHigh(life, life * 2);
        emitter.setMaxParticleCount(count);
        emitter.setEmissionMode(RegularEmitter.EmissionMode.EnabledUntilCycleEnd);

        regionInfluencer = new RegionInfluencer.Single(texture);

        // Spawn
        spawnSource = new FixedSpawnInfluencer();

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

        // InertialInfluencer
        inertialInfluencer = new InertialInfluencer();
        polarInertialModifier = new PolarInertialModifier();
        polarInertialModifier.strengthValue.setLow(strength, 2 * strength);
        polarInertialModifier.phiValue.setLowMax(5);
        polarInertialModifier.thetaValue.setLow(0, 360);
        inertialInfluencer.modifiers.add(polarInertialModifier);


        controller = new ParticleController("PropellantEmitter", emitter, new BillboardRenderer(batch),
                regionInfluencer,
                spawnSource,
                scaleInfluencer,
                colorInfluencer,
                inertialInfluencer
        );
        controller.init();
    }

    public void start() {
        controller.start();
        emitter.setEmissionMode(RegularEmitter.EmissionMode.Enabled);
    }

    public void end() {
        emitter.setEmissionMode(RegularEmitter.EmissionMode.EnabledUntilCycleEnd);
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
