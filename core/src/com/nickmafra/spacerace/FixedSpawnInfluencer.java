package com.nickmafra.spacerace;

import com.badlogic.gdx.graphics.g3d.particles.ParallelArray;
import com.badlogic.gdx.graphics.g3d.particles.ParticleChannels;
import com.badlogic.gdx.graphics.g3d.particles.influencers.Influencer;

public class FixedSpawnInfluencer extends Influencer {

    ParallelArray.FloatChannel positionChannel;
    ParallelArray.FloatChannel rotationChannel;

    public FixedSpawnInfluencer () {
    }

    public FixedSpawnInfluencer (FixedSpawnInfluencer source) {
    }

    @Override
    public void init () {
    }

    @Override
    public void allocateChannels () {
        positionChannel = controller.particles.addChannel(ParticleChannels.Position);
        rotationChannel = controller.particles.addChannel(ParticleChannels.Rotation3D);
    }

    @Override
    public void activateParticles (int startIndex, int count) {
        controller.transform.getTranslation(TMP_V1);
        for (int i = startIndex * positionChannel.strideSize, c = i + count * positionChannel.strideSize; i < c; i += positionChannel.strideSize) {
            positionChannel.data[i + ParticleChannels.XOffset] = TMP_V1.x;
            positionChannel.data[i + ParticleChannels.YOffset] = TMP_V1.y;
            positionChannel.data[i + ParticleChannels.ZOffset] = TMP_V1.z;
        }
        controller.transform.getRotation(TMP_Q, true);
        for (int i = startIndex * rotationChannel.strideSize, c = i + count * rotationChannel.strideSize; i < c; i += rotationChannel.strideSize) {
            rotationChannel.data[i + ParticleChannels.XOffset] = TMP_Q.x;
            rotationChannel.data[i + ParticleChannels.YOffset] = TMP_Q.y;
            rotationChannel.data[i + ParticleChannels.ZOffset] = TMP_Q.z;
            rotationChannel.data[i + ParticleChannels.WOffset] = TMP_Q.w;
        }
    }

    @Override
    public FixedSpawnInfluencer copy () {
        return new FixedSpawnInfluencer(this);
    }
}
