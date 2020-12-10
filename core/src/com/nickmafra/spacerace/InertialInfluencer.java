package com.nickmafra.spacerace;

import com.badlogic.gdx.graphics.g3d.particles.ParallelArray;
import com.badlogic.gdx.graphics.g3d.particles.ParticleChannels;
import com.badlogic.gdx.graphics.g3d.particles.ParticleController;
import com.badlogic.gdx.graphics.g3d.particles.ParticleControllerComponent;
import com.badlogic.gdx.graphics.g3d.particles.influencers.Influencer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class InertialInfluencer extends Influencer {

    public Array<ParticleControllerComponent> modifiers;
    public final Vector3 velocity;
    /** position channel */
    private ParallelArray.FloatChannel pChannel;
    /** velocity channel */
    private ParallelArray.FloatChannel vChannel;

    public InertialInfluencer() {
        this.modifiers = new Array<>(true, 1, ParticleControllerComponent.class);
        velocity = new Vector3();
    }

    public InertialInfluencer(InertialInfluencer inertialInfluencer) {
        this.modifiers = new Array<>(inertialInfluencer.modifiers.toArray(ParticleControllerComponent.class));
        this.velocity = inertialInfluencer.velocity.cpy();
    }

    @Override
    public ParticleControllerComponent copy() {
        return new InertialInfluencer(this);
    }

    @Override
    public void set (ParticleController particleController) {
        super.set(particleController);
        for (int k = 0; k < modifiers.size; ++k) {
            modifiers.items[k].set(particleController);
        }
    }

    @Override
    public void allocateChannels() {
        pChannel = controller.particles.addChannel(ParticleChannels.Position);
        vChannel = controller.particles.addChannel(ParticleChannels.Acceleration);
        for (int i = 0; i < modifiers.size; i++) {
            modifiers.items[i].allocateChannels();
        }
    }

    @Override
    public void activateParticles (int startIndex, int count) {
        int strideSize = pChannel.strideSize;
        for (int i = startIndex * strideSize, c = i + count * strideSize; i < c; i += strideSize) {
            vChannel.data[i + ParticleChannels.XOffset] = velocity.x;
            vChannel.data[i + ParticleChannels.YOffset] = velocity.y;
            vChannel.data[i + ParticleChannels.ZOffset] = velocity.z;
        }
        for (int i = 0; i < modifiers.size; i++) {
            modifiers.items[i].activateParticles(startIndex, count);
        }
    }

    @Override
    public void update() {
        for (int i = 0; i < modifiers.size; i++) {
            modifiers.items[i].update();
        }
        int strideSize = 3;
        for (int i = 0, offset = 0; i < controller.particles.size; ++i, offset += strideSize) {
            pChannel.data[offset + ParticleChannels.XOffset] += vChannel.data[offset + ParticleChannels.XOffset] * controller.deltaTime;
            pChannel.data[offset + ParticleChannels.YOffset] += vChannel.data[offset + ParticleChannels.YOffset] * controller.deltaTime;
            pChannel.data[offset + ParticleChannels.ZOffset] += vChannel.data[offset + ParticleChannels.ZOffset] * controller.deltaTime;
        }
    }
}
