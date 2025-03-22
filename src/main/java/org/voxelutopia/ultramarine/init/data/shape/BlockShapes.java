package org.voxelutopia.ultramarine.init.data.shape;

import org.voxelutopia.ultramarine.init.data.RawVoxelShape;

public class BlockShapes {

    public static final ReShapeFunction S16_H1 = ReShapeFunction.centeredSquare(16, 1);
    public static final ReShapeFunction S16_H2 = ReShapeFunction.centeredSquare(16, 2);
    public static final ReShapeFunction S16_H4 = ReShapeFunction.centeredSquare(16, 4);
    public static final ReShapeFunction S16_H8 = ReShapeFunction.centeredSquare(16, 8);
    public static final ReShapeFunction S16_H12 = ReShapeFunction.centeredSquare(16, 12);
    public static final ReShapeFunction S16_H16 = ReShapeFunction.centeredSquare(16, 16);

    public static final ReShapeFunction BACKWARD_3B_L = ReShapeFunction.or(ReShapeFunction.cardinalRotations(new RawVoxelShape(0,0,0,16,16,32)), ReShapeFunction.cardinalRotations(new RawVoxelShape(0,16,0,16,32,16)));

}