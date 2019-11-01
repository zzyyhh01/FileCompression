package huffman;

import java.io.*;
import java.util.*;

public class HuffmanCompress {

    //此类不可创建
    private HuffmanCompress(){}


    /**
     * 统计字节编码和字节编码的出现次数（权重）
     * 生成哈夫曼结点列表
     * @param bytes 字节码数组
     * @return 哈夫曼结点
     */
    private static List<HuffmanNode> getNodes(byte[] bytes){
        List<HuffmanNode> nodes = new ArrayList<>();
        //使用map统计编码的权重
        Map<Byte,Integer> map = new HashMap<>();
        //遍历bytes 并 统计 byte的出现的次数(权重)
        for (byte b : bytes) {
            map.merge(b,1, Integer::sum);
        }
        //遍历map构建结点列表
        map.forEach((data,weight)->{
            nodes.add(new HuffmanNode(data,weight));
        });
        //对结点进行排序
        Collections.sort(nodes);
        return nodes;
    }


    /**
     * 构建赫夫曼树
     * @param nodes 赫夫曼结点列表
     * @return 赫夫曼树根结点
     */
    private static HuffmanNode createTree(List<HuffmanNode> nodes){
        while(nodes.size() > 1){
            //取出小的两个结点
            HuffmanNode leftNode = nodes.remove(0);
            HuffmanNode rightNode = nodes.remove(0);
            //创建父结点
            HuffmanNode parentNode = new HuffmanNode(null, leftNode.getWeight() + rightNode.getWeight());
            //连接
            parentNode.setLeft(leftNode);
            parentNode.setRight(rightNode);
            //将父结点添加到森林中
            nodes.add(parentNode);
            //排序
            Collections.sort(nodes);
        }
        return nodes.get(0);
    }

    /**
     * 前序遍历树
     * @param root 哈夫曼树的根结点
     */
    public static void preOrder(HuffmanNode root){
        if (root != null)
            root.preOrder();
        else
            System.out.println("empty tree");
    }

    //赫夫曼编码表
    private static Map<Byte,String> huffmanCodes = new HashMap<>();
    //重置赫夫曼编码表
    private static void clearCodes(){
        huffmanCodes.clear();
    }
    //拼接叶子结点的编码
    private static StringBuilder sb = new StringBuilder();
    /**
     * 生成哈夫曼编码
     * 编码：
     * 1.遍历所有叶子结点（huffman树中所有数据都在叶子结点中）
     * 2.遍历时统计每个叶子结点的路径，向左遍历记作0，向右遍历记作1
     * 3.将每个叶子结点的路径保存在map集合中，则这个路径就是这结点data的huffman编码
     * @param root huffman树的根结点
     * @param code 拼接Huffman编码
     * @param way 记录结点遍历方向
     */
    private static void getCodes(HuffmanNode root,StringBuilder code,String way){
        StringBuilder stringBuilder = new StringBuilder(code);
        stringBuilder.append(way);

        if (root != null){

            if (root.getData() != null){
                huffmanCodes.put(root.getData(),stringBuilder.toString());
            }else{
                getCodes(root.getLeft(),stringBuilder,"0");
                getCodes(root.getRight(),stringBuilder,"1");
            }
        }
    }

    /**
     * 根据结点生成赫夫曼编码
     * @param root huffman树的根结点
     * @return huffman编码
     */
    private static Map<Byte,String> getCodes(HuffmanNode root){
        if (root != null)
            getCodes(root,sb,"");
        return huffmanCodes;
    }

    /**
     * 将字节根据huffmanCodes进行编码
     * @param bytes 原始字节
     * @return 编码字节
     */
    private static byte[] encode(byte[] bytes){
        StringBuilder stringBuilder = new StringBuilder();

        for (byte b : bytes) {
            stringBuilder.append(huffmanCodes.get(b));
        }
        // System.out.println(stringBuilder);

        int len = (stringBuilder.length() + 7) / 8;


        byte[] zipCodes = new byte[ len ];


        for (int i = 0; i < len ; i++){

            if (i == len - 1){
                zipCodes[i] = (byte)Integer.parseInt(stringBuilder.substring(i*8),2);
            }else{
                zipCodes[i] = (byte)Integer.parseInt(stringBuilder.substring(i*8,(i+1)*8),2);
            }
        }

        return zipCodes;
    }

    private static synchronized byte[] zip(byte[] beforePress) {



        //获取森林
        List<HuffmanNode> nodes = getNodes(beforePress);

        //构建赫夫曼树
        HuffmanNode root = createTree(nodes);

        //获取赫夫曼编码
        clearCodes();
        getCodes(root);

        //返回压缩字节
        return encode(beforePress);

    }


    /**
     * 压缩
     * @param path 压缩文件路径
     * @param desPath 压缩后文件的路径
     * @throws IOException 抛异常
     */
    public static void zipFile(String path,String desPath) throws IOException {
        FileInputStream is = new FileInputStream(path);
        FileOutputStream os = new FileOutputStream(desPath);

        byte[] bytes = new byte[is.available()];

        int len = is.read(bytes);

        byte[] zipCodes = zip(bytes);

        ObjectOutputStream oos = new ObjectOutputStream(os);

        oos.writeObject(zipCodes);

        oos.writeObject(huffmanCodes);

        is.close();
        os.close();
        oos.close();
    }


    /**
     * 处理最后一位之前的字节
     * 将字节转换成二进制字符串
     * @param b 字节
     * @return 转换后的字符串
     */
    private static String byteToString(byte b){

        int tem = b;
        if (tem >= 0){
            // System.out.println(tem);
            tem = b | 256;
        }
        String binaryString = Integer.toBinaryString(tem);
        // System.out.println(binaryString);
        return binaryString.substring(binaryString.length() - 8);
    }

    /**
     * 处理最后一位之前的字节
     * 将字节转换成二进制字符串
     * @param flag 无所谓
     * @param b 字节
     * @return 转换后的字符串
     */
    private static String byteToString(boolean flag,byte b){
        return Integer.toBinaryString((int) b);
    }

    private static byte[] decode(Map<Byte,String> huffmanCodes,byte[] huffmanBytes){

        // System.out.println(huffmanBytes.length);

        StringBuilder stringBuilder = new StringBuilder();
        int i = 0;
        for(i = 0; i < huffmanBytes.length - 1; i++){
            stringBuilder.append(byteToString(huffmanBytes[i]));
        }
        stringBuilder.append(byteToString(true,huffmanBytes[i]));

        // System.out.println(stringBuilder);

        //反转huffmanCodes
        Map<String,Byte> map = new HashMap<>();
        huffmanCodes.forEach((data,code)->{
            map.put(code,data);
        });

        int site = 0;
        List<Byte> byteList = new ArrayList<>();
        /*
        注意 由于字符串的截取 substring 截取时 取前不取后
        所以i一定要往后多走一位，保证最后一位也被扫描到
        所以这里的 i 是 小于等于 而不是 小于
        */
        for (i = 1 ; i <= stringBuilder.length(); i++){
            Byte b = map.get(stringBuilder.substring(site, i));
            if (b != null){
                byteList.add(b);
                site = i;
            }
        }


        byte[] bytes = new byte[byteList.size()];
        for(i = 0; i < bytes.length;i++){
            bytes[i] = byteList.get(i);
        }

        return bytes;

    }


    public static void unzipFile(String desPath,String path) throws IOException, ClassNotFoundException {
        FileInputStream is = new FileInputStream(desPath);
        ObjectInputStream ios = new ObjectInputStream(is);
        FileOutputStream os = new FileOutputStream(path);

        byte[] zipCodes = (byte[]) ios.readObject();

        Map<Byte,String > huffmanCodes = (Map<Byte, String>) ios.readObject();

        byte[] decode = decode(huffmanCodes, zipCodes);

        os.write(decode);
        os.flush();

        is.close();
        ios.close();
        os.flush();


    }



}
