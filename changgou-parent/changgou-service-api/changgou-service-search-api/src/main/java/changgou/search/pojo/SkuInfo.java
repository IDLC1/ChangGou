package changgou.search.pojo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

@Document(indexName = "skuinfo", type = "docs")
@Data
public class SkuInfo implements Serializable {
    //商品id，同时也是商品编号
    @Id
    @Field(index = true, store = true, type = FieldType.Keyword)
    private Long id;

    //SKU名称
    /**
     * index: 添加数据的时候，是否分词
     * type ： text 支持分词
     * analyzer ： 创建索引的分词器
     * store：是否存储
     * searchAnalyzer: 搜索的时候使用的分词器
     */
    @Field(index = true, store = false, type = FieldType.Text, analyzer = "ik_smart")
    private String name;

    //商品价格，单位为：元
    @Field(type = FieldType.Double)
    private Long price;

    //库存数量
    private Integer num;

    //商品图片
    private String image;

    //商品状态，1-正常，2-下架，3-删除
    private String status;

    //创建时间
    private Date createTime;

    //更新时间
    private Date updateTime;

    //是否默认
    private String isDefault;

    //SPUID
    private Long spuId;

    //类目ID
    private Long categoryId;

    //类目名称
    /**
     * Type = FieldType.Keyword：不分词
     * 为了确保搜索更精准，不应对类目进行分词
     * 例如：手机、电脑
     */
    @Field(type = FieldType.Keyword)
    private String categoryName;

    //品牌名称
    /**
     * 为了确保品牌搜索更为精准，不应对品牌进行分词
     * 例如华为，就是华为
     */
    @Field(type = FieldType.Keyword)
    private String brandName;

    //规格
    private String spec;

    //规格参数
    private Map<String, Object> specMap;
}