package com.example.mlpredict;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.widget.Toast;

import com.example.mlpredict.ml.Iris2;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;

public class Predict {

    public static String doInference(Context context,String v1,String v2,String v3,String v4){

        float inp1=Float.parseFloat(v1);
        float inp2=Float.parseFloat(v2);
        float inp3=Float.parseFloat(v3);
        float inp4=Float.parseFloat(v4);

        ByteBuffer byteBuffer= ByteBuffer.allocateDirect(4*4);
        byteBuffer.putFloat(inp1);
        byteBuffer.putFloat(inp2);
        byteBuffer.putFloat(inp3);
        byteBuffer.putFloat(inp4);

        String result="";

        try {
            Iris2 model = Iris2.newInstance(context);

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 4}, DataType.FLOAT32);
            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            Iris2.Outputs outputs = model.process(inputFeature0);
            //outputFeature0 = outputs.getOutputFeature0AsTensorBuffer().float;

            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
            float[] output=outputFeature0.getFloatArray();//Converting outputFeature0 into float array

            DecimalFormat df =new DecimalFormat();
            df.setMaximumFractionDigits(3);
            result= "Iris Setosa : "+df.format(output[0])+"%\n"+"Iris Versicolor : "+df.format(output[1])+"%\n"+"Iris Virginica :"+
                    df.format(output[2])+"%";

            // Releases model resources if no longer used.
            model.close();
        } catch (IOException e) {
            // TODO Handle the exception
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return result;
    }



}
