package com.igteam.immersive_geology.api.util;

import java.util.Arrays;

/**
 * Created by Pabilo8 on 26-03-2020.
 */
public class Utils
{

    public static final int NUGGET_AMOUNT = 16;
    public static final int INGOT_AMOUNT = 144;
    public static final int BLOCK_AMOUNT = 1296;
    public static final int ROD_AMOUNT = 144;
    public static final int WIRE_AMOUNT = 72;
    public static final int PLATE_AMOUNT = 144;
    public static final int GEAR_AMOUNT = 576;

    public static double fixCoordinate(double f)
    {
        return (Math.round(f*16) )/ 16.0f;
    }

    public static boolean isVertexLine(String s)
    {
        return (s.startsWith("v "));
    }

    public static String fixVertexString (String s)
    {
        if (!isVertexLine(s)) return s;
        String[] parts = s.split(" ");
        StringBuilder result = new StringBuilder();
        result.append("v ");
        for (int i = 1; i<parts.length; i++) {
            result.append(fixCoordinate(Double.valueOf(parts[i]))).append(" ");
        }
        return result.toString();
    }
}
