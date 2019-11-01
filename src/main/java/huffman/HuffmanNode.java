package huffman;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 赫夫曼树结点
 * 实现接口Comparable 以便于 集合排序
 */
@Setter
@Getter
public class HuffmanNode implements Comparable<HuffmanNode>, Serializable {


    private static final long serialVersionUID = -4509479794854424795L;


    //字节编码
    private Byte data;

    //权重
    private Integer weight;

    //左子结点
    private HuffmanNode left;

    //右子结点
    private HuffmanNode right;


    //编写结点方法***********************************


    /**
     * 前序遍历
     */
    public void preOrder(){
        System.out.println(this);
        if (this.left != null)
            this.left.preOrder();
        if (this.right != null)
            this.right.preOrder();
    }

    /**
     * 实现Comparable接口的方法
     * 按从小到大的顺序排列
     * @param o 对比结点
     * @return 整型
     */
    @Override
    public int compareTo(HuffmanNode o) {
        return this.weight - o.weight;
    }




    //**********************************************************
    //构造方法和toString方法

    public HuffmanNode(Byte data, Integer weight) {
        this.data = data;
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "HuffmanNode{" +
                "data=" + data +
                ", weight=" + weight +
                '}';
    }










}
