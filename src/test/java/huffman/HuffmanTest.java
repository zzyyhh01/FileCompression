package huffman;


import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class HuffmanTest {

    @Test
    public void t1(){
        // String str = "i like like like java do you like a java";
        // byte[] bytes = str.getBytes();
        // List<HuffmanNode> nodes = HuffmanCompress.getNodes(bytes);
        //
        //
        // HuffmanNode root = HuffmanCompress.createTree(nodes);
        //
        // Map<Byte, String> codes = HuffmanCompress.getCodes(root);
        // codes.forEach((data,value)->{
        //     System.out.println(data +": " + value);
        // });
        //
        // byte[] zip = HuffmanCompress.zip(bytes);
        //
        // for (byte b : zip) {
        //     System.out.print(b);
        //     System.out.println();
        // }
        //
        // System.out.println(zip.length);
    }

    @Test
    public void t2() throws IOException {
        String path = "E:\\Notes\\redis\\redis.md";
        String desPath = "C:\\Users\\53495\\Desktop\\test\\redis";
        HuffmanCompress.zipFile(path,desPath);

    }




    @Test
    public void t3() throws IOException, ClassNotFoundException {
        String path = "C:\\Users\\53495\\Desktop\\test\\redis.md";
        String desPath = "C:\\Users\\53495\\Desktop\\test\\redis";
        HuffmanCompress.unzipFile(desPath,path);


    }

    @Test
    public void t4(){
        byte b = 124;
        if (b > 0){
            System.out.println(1);
        }
        int t = b;
         t |= 256;

        String string = Integer.toBinaryString(t);
        System.out.println(string);
    }

}
