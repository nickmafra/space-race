package com.nickmafra.spacerace;

import com.badlogic.gdx.graphics.g3d.particles.ParallelArray;
import com.badlogic.gdx.graphics.g3d.particles.ParticleChannels;
import com.badlogic.gdx.graphics.g3d.particles.ParticleControllerComponent;
import com.badlogic.gdx.graphics.g3d.particles.influencers.Influencer;
import com.badlogic.gdx.math.Vector3;

public class InertialInfluencer extends Influencer {

    public final Vector3 velocity;
    private ParallelArray.FloatChannel positionChannel;
    private ParallelArray.FloatChannel previousPositionChannel;

    public InertialInfluencer() {
        velocity = new Vector3();
    }

    public InertialInfluencer(InertialInfluencer inertialInfluencer) {
        this.velocity = inertialInfluencer.velocity.cpy();
    }

    @Override
    public ParticleControllerComponent copy() {
        return new InertialInfluencer(this);
    }

    @Override
    public void allocateChannels() {
        positionChannel = controller.particles.addChannel(ParticleChannels.Position);
        previousPositionChannel = controller.particles.addChannel(ParticleChannels.PreviousPosition);
    }

    @Override
    public void activateParticles (int startIndex, int count) {
        for (int i = startIndex * positionChannel.strideSize, c = i + count * positionChannel.strideSize; i < c; i += positionChannel.strideSize) {
            positionChannel.data[i + ParticleChannels.XOffset] = previousPositionChannel.data[i + ParticleChannels.XOffset] + velocity.x * controller.deltaTime;
            positionChannel.data[i + ParticleChannels.YOffset] = previousPositionChannel.data[i + ParticleChannels.YOffset] + velocity.y * controller.deltaTime;
            positionChannel.data[i + ParticleChannels.ZOffset] = previousPositionChannel.data[i + ParticleChannels.ZOffset] + velocity.z * controller.deltaTime;
        }
    }
}
