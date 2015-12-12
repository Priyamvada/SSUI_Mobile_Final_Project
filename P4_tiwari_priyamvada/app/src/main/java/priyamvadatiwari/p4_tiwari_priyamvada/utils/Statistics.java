package priyamvadatiwari.p4_tiwari_priyamvada.utils;

import java.util.ArrayList;

/**
 * Created by priyamvadatiwari on 12/10/15.
 */
public class Statistics {
    public static double mean(double[] data) {
        double mean = 0;
        for(int i = 0; i < data.length; i++)
            mean+=data[i];
        mean = mean/data.length;
        return mean;
    }

    public static float mean(float[] data) {
        float mean = 0;
        for(int i = 0; i < data.length; i++)
            mean+=data[i];
        mean = mean/data.length;
        return mean;
    }

    public static double standardDeviation(double[] data)   {
        double sd = 0, sumSqrDev = 0;
        double mean = mean(data);
        for(int i = 0; i < data.length; i++)    {
            sumSqrDev += Math.pow(data[i] - mean, 2);
        }
        sd = sumSqrDev/data.length;
        return sd;
    }

    public static float[] lowPass( float[] input, float[] output, float ALPHA) {

        if ( output == null ) return input;

        for ( int i=0; i<input.length; i++ ) {
            output[i] = output[i] + ALPHA * (input[i] - output[i]);
        }
        return output;
    }
}
