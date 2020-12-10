package com.nickmafra.spacerace;

import com.badlogic.gdx.graphics.g3d.particles.ParallelArray;
import com.badlogic.gdx.graphics.g3d.particles.ParticleChannels;
import com.badlogic.gdx.graphics.g3d.particles.ParticleControllerComponent;
import com.badlogic.gdx.graphics.g3d.particles.influencers.DynamicsModifier;
import com.badlogic.gdx.math.MathUtils;

public class PolarInertialModifier extends DynamicsModifier.Angular {

    /** velocity channel */
    private ParallelArray.FloatChannel vChannel;

    public PolarInertialModifier() {
    }

    public PolarInertialModifier(PolarInertialModifier modifier) {
        super(modifier);
    }

    @Override
    public void allocateChannels() {
        super.allocateChannels();
        vChannel = controller.particles.addChannel(ParticleChannels.Acceleration);
    }

    @Override
    public void activateParticles(int startIndex, int count) {
        super.activateParticles(startIndex, count);

        // iterate over vChannel
        for (int i = startIndex * vChannel.strideSize, c = i + count * vChannel.strideSize, s = 0, a = 0;
             i < c; s += strengthChannel.strideSize, i += vChannel.strideSize, a += angularChannel.strideSize) {

            float strength = strengthChannel.data[s + ParticleChannels.VelocityStrengthStartOffset];
            float phi = angularChannel.data[a + ParticleChannels.VelocityPhiStartOffset];
            float theta = angularChannel.data[a + ParticleChannels.VelocityThetaStartOffset];

            float cosTheta = MathUtils.cosDeg(theta);
            float sinTheta = MathUtils.sinDeg(theta);
            float cosPhi = MathUtils.cosDeg(phi);
            float sinPhi = MathUtils.sinDeg(phi);

            TMP_V3.set(cosTheta * sinPhi, cosPhi, sinTheta * sinPhi).nor().scl(strength);

            if (!isGlobal) {
                controller.transform.getRotation(TMP_Q, true);
                TMP_V3.mul(TMP_Q);
            }

            vChannel.data[i + ParticleChannels.XOffset] += TMP_V3.x;
            vChannel.data[i + ParticleChannels.YOffset] += TMP_V3.y;
            vChannel.data[i + ParticleChannels.ZOffset] += TMP_V3.z;
        }
    }

    @Override
    public ParticleControllerComponent copy() {
        return new PolarInertialModifier(this);
    }
}
