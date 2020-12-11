package com.nickmafra.spacerace;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

import java.util.List;

public class ShipMathUtils {

    private static final Vector3 tempV = new Vector3();

    public static float randomOne() {
        return MathUtils.random(-1f, 1f);
    }

    public static Vector3 randomDirection(Vector3 v) {
        return v.set(randomOne(), randomOne(), randomOne()).nor();
    }

    public static void randomizeAngle(Matrix4 matrix) {
        matrix.rotate(randomDirection(tempV), MathUtils.PI2 * MathUtils.random());
    }

    public static void randomizePositions(List<? extends PhysicalBody> items, float distance) {
        if (items.isEmpty())
            return;

        int quantity = items.size();
        int layer = 1;
        int c = 0;
        float angle1Size = 0;
        float angle2Size = 0;
        float layerStart = 0;
        float layerSize = 0;

        // first item stay in zero point
        for (int i = 1; i < quantity; i++) {
            if (c == 0) {
                layer++;
                int outSize = layer * 2 - 1;
                int inSize = outSize - 2;
                int q = Math.min(quantity - i, outSize * outSize * outSize - inSize * inSize * inSize);
                c = q;
                angle1Size = MathUtils.PI / q;
                angle2Size = layer * layer * MathUtils.PI2 / q;
                layerStart += distance + layerSize;
                layerSize = distance * (layer - 1);
            }
            Vector3 position = items.get(i).position;
            position.set(layerStart + layerSize * MathUtils.random(), 0, 0);
            float angle1 = c * angle1Size;
            float angle2 = c * angle2Size;
            position.rotateRad(Vector3.Z, angle1 + angle1Size * MathUtils.random());
            position.rotateRad(Vector3.X, angle2 + angle1Size * MathUtils.random());
            c--;
        }
    }
}
