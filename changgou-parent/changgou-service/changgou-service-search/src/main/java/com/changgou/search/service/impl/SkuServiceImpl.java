package com.changgou.search.service.impl;

import com.changgou.search.pojo.SkuInfo;
import com.alibaba.fastjson.JSON;
import com.changgou.common.entity.Result;
import com.changgou.goods.feign.SkuFeign;
import com.changgou.goods.pojo.Sku;
import com.changgou.search.dao.SkuEsMapper;
import com.changgou.search.service.SkuService;
import io.swagger.models.auth.In;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @File: SkuServiceImpl
 * @Description:
 * @Author: tom
 * @Create: 2020-06-04 09:13
 **/
@Service
public class SkuServiceImpl implements SkuService {

    @Autowired
    private SkuFeign skuFeign;

    @Autowired
    private SkuEsMapper skuEsMapper;

    /**
     * ElasticsearchTemplate： 可以实现索引库的增删改查
     */
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    /**
     * 多条件搜索
     * @param searchMap
     * @return
     */
    @Override
    public Map<String, Object> search(Map<String, String> searchMap){
        // 基本条件构建
        NativeSearchQueryBuilder builder = buildBasicQuery(searchMap);

        // 集合搜索
        // 当用户没有输入集合选择的时候，则需要去搜索，若选择了，则无需再去搜索
        Map<String, Object> resultMap = searchList(builder);
        // 获取分组数据
        Map<String, Object> groupMap = searchGroupList(builder, searchMap);
        resultMap.putAll(groupMap);

        return resultMap;
    }

    /**
     * 查询条件构建
     * @param searchMap
     * @return
     */
    private NativeSearchQueryBuilder buildBasicQuery(Map<String, String> searchMap) {
        // 构建搜索对象，用于封装各种搜索条件的
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();

        // 构建多条件对象
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();

        /**
         * 根据关键词搜索
         * 构造对应的关键词搜索对象
         */
        if (searchMap != null && searchMap.size() > 0) {
            // 根据关键词搜索
            String keywords = searchMap.get("keywords");
            if (!StringUtils.isEmpty(keywords)) {
                boolQueryBuilder.must(QueryBuilders.queryStringQuery(keywords).field("name"));
            }

            // 根据关键词搜索
            String category = searchMap.get("category");
            if (!StringUtils.isEmpty(category)) {
                boolQueryBuilder.must(QueryBuilders.termQuery("categoryName", category));
            }

            // 根据关键词搜索
            String brand = searchMap.get("brand");
            if (!StringUtils.isEmpty(brand)) {
                boolQueryBuilder.must(QueryBuilders.termQuery("brandName", brand));
            }

            // 规格过滤实现
            // 由于可能存在多个规格，故需要循环取出以spec_开头的内容
            for (Map.Entry<String, String> entry : searchMap.entrySet()) {
                String key = entry.getKey();
                if (key.startsWith("spec_")) {
                    String value = entry.getValue();
                    boolQueryBuilder.must(QueryBuilders.termQuery("specMap." + key.substring(5) + ".keyword", value));
                }
            }

            // 加个区间筛选 0-500元 500-1000元 ... 3000元以上
            // 去掉元和以上，根据-分割 [0,500] ... [3000]
            // 根据关键词搜索
            String price = searchMap.get("price");
            if (!StringUtils.isEmpty(price)) {
                price = price.replace("元", "").replace("以上", "");
                String[] prices = price.split("-");
                if (prices != null && prices.length > 0) {
                    if (prices[0] != null) {
                        boolQueryBuilder.must(QueryBuilders.rangeQuery("price").gt(Integer.parseInt(prices[0])));
                    }
                    if (prices.length > 1 && Double.valueOf(prices[1]) > Double.valueOf(prices[0])) {
                        boolQueryBuilder.must(QueryBuilders.rangeQuery("price").lte(Integer.parseInt(prices[1])));
                    }
                }
            }

            /**
             * 排序实现
             */
            String sortField = searchMap.get("sortField");
            String sortRole = searchMap.get("sortRule");
            if (!StringUtils.isEmpty(sortField) && !StringUtils.isEmpty(sortRole)) {
                builder.withSort(new FieldSortBuilder(sortField).order(SortOrder.valueOf(sortRole.toUpperCase())));
            }
        }

        // 分页处理，若不传则默认第一页
        Map<String, Integer> pageMap = coverterPage(searchMap);
        builder.withPageable(PageRequest.of(pageMap.get("pageNum") - 1, pageMap.get("pageSize")));

        builder.withQuery(boolQueryBuilder);
        return builder;
    }

    /**
     * 接收前端传入的分页参数
     * @param searchMap
     * @return
     */
    public Map<String,Integer> coverterPage(Map<String,String> searchMap) {
        Map<String, Integer> map = new HashMap<String, Integer>();
        Integer defPageNum = 1;
        Integer defPageSize = 10;

        if (searchMap != null){
            try {
                if (!StringUtils.isEmpty(searchMap.get("pageNum"))) {
                    map.put("pageNum",Integer.parseInt(searchMap.get("pageNum")));
                } else {
                    map.put("pageNum", defPageNum);
                }
                if (!StringUtils.isEmpty(searchMap.get("pageSize"))) {
                    map.put("pageSize",Integer.parseInt(searchMap.get("pageSize")));
                } else {
                    map.put("pageSize", defPageSize);
                }
            } catch (Exception e) {
                map.put("pageNum", defPageNum);
                map.put("pageSize", defPageSize);
            }
        }
        return map;
    }

    /**
     * 结果集搜索
     * @param builder
     * @return
     */
    private Map<String, Object> searchList(NativeSearchQueryBuilder builder) {

        ///////  开启高亮和配置  ///////
        // 将商品名称作为高亮对象
        HighlightBuilder.Field field = new HighlightBuilder.Field("name");
        // 前缀 <em style="color:red">
        field.preTags("<em style=\"color:red\">");
        // 后缀 </em>
        field.postTags("</em>");
        // 后缀长度 关键词数据的长度
        field.fragmentSize(100);
        // 添加高亮
        builder.withHighlightFields(field);

        /**
         * 执行搜索
         * 1. 搜索条件封装
         * 2. 搜索结果集（集合数据），需要转换的类型
         * 3. AggregatedPage<SkuInfo> 搜索结果集的封装
         */
        AggregatedPage<SkuInfo> page = elasticsearchTemplate
                .queryForPage(
                        builder.build(), // 搜索条件封装
                        SkuInfo.class, // 数据集合要转换的类型的字节码
                        new SearchResultMapper() {  // 执行搜索后，将数据结果集封装到该对象中
                            @Override
                            public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> aClass, Pageable pageable) {
                                // 存储所有转换后的高亮数据对象
                                List<T> list = new ArrayList<>();

                                // 执行查询，获取所有数据=>结果集[非高亮数据，高亮数据]
                                for (SearchHit hit : response.getHits()) {
                                    // 分析结果集数据，获取非高亮数据
                                    SkuInfo skuInfo = JSON.parseObject(hit.getSourceAsString(), SkuInfo.class);
                                    // 分析结果集数据，获取高亮数据 => 只有某个域的高亮数据
                                    HighlightField highlightField = hit.getHighlightFields().get("name");
                                    if (highlightField != null && highlightField.getFragments() != null) {
                                        // 将高亮数据读取出来
                                        Text[] fragments = highlightField.getFragments();
                                        StringBuffer buffer = new StringBuffer();
                                        for (Text fragment : fragments) {
                                            buffer.append(fragment.toString());
                                        }
                                        // 将非高亮数据中指定的域替换成高亮数据
                                        skuInfo.setName(buffer.toString());
                                    }
                                    // 将高亮数据添加到集合中
                                    list.add((T)skuInfo);
                                }
                                    // 将数据返回
                                /**
                                 * 1. 搜索的集合数据（携带高亮）
                                 * 2. 分页对象信息
                                 * 3. 搜索记录的总条数
                                 */
                                return new AggregatedPageImpl<T>(list, pageable, response.getHits().getTotalHits());
                            }
                        });

        // 获取数据结果集
        long totalElements = page.getTotalElements();
        int totalPages = page.getTotalPages();
        List<SkuInfo> contents = page.getContent();

        // 封装一个Map存储所有数据，并返回
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("rows", contents);
        resultMap.put("total", totalElements);
        resultMap.put("totalPages", totalPages);

        // 分页数据
        // 获取搜索封装信息
        NativeSearchQuery query = builder.build();
        Pageable pageable = query.getPageable();
        int pageSize = pageable.getPageSize();
        int pageNumber = pageable.getPageNumber();
        resultMap.put("pageSize", pageSize);
        resultMap.put("pageNumber", pageNumber);

        return resultMap;
    }

    /**
     * 分组查询 => 根据分类分组、品牌分组、规格分组
     * @param builder
     * @return
     */
    private Map<String, Object> searchGroupList(NativeSearchQueryBuilder builder, Map<String, String> searchMap) {
        /**
         * 根据分类名称进行分组
         * addAggregation 添加一个聚合操作
         * 1）skuCategory  取别名
         * 2）categoryName  表示根据哪个域进行分组查询
         */
        if (searchMap == null || StringUtils.isEmpty(searchMap.get("category"))) {
            builder.addAggregation(AggregationBuilders.terms("skuCategory").field("categoryName"));
        }
        if (searchMap == null || StringUtils.isEmpty(searchMap.get("brand"))) {
            builder.addAggregation(AggregationBuilders.terms("skuBrand").field("brandName"));
        }
        builder.addAggregation(AggregationBuilders.terms("skuSpec").field("spec.keyword"));
        AggregatedPage<SkuInfo> aggregatedPage = elasticsearchTemplate.queryForPage(builder.build(), SkuInfo.class);

        // 定义一个Map，存储所有分组结果
        Map<String, Object> groupMapResult = new HashMap<String, Object>();

        /**
         * 获取分组数据
         * getAggregations 获取的是集合，可以根据多个域进行分组
         * .get("skuCategory") 获取指定域的集合数据
         */
        if (searchMap == null || StringUtils.isEmpty(searchMap.get("category"))) {
            StringTerms categoryTerms = aggregatedPage.getAggregations().get("skuCategory");
            // 获取分类分组集合数据
            List<String> categoryList = getGroupList(categoryTerms);
            groupMapResult.put("categoryList", categoryList);
        }
        if (searchMap == null || StringUtils.isEmpty(searchMap.get("brand"))) {
            StringTerms brandTerms = aggregatedPage.getAggregations().get("skuBrand");
            // 获取品牌分组集合数据
            List<String> brandList = getGroupList(brandTerms);
            groupMapResult.put("brandList", brandList);
        }

        // 获取规格分组集合数据
        StringTerms specTerms = aggregatedPage.getAggregations().get("skuSpec");
        List<String> specList = getGroupList(specTerms);
        Map<String, Set<String>> specMap = putAllSpec(specList);
        groupMapResult.put("specList", specMap);

        return groupMapResult;
    }

    private Map<String, Set<String>> putAllSpec(List<String> specList) {
        Map<String, Set<String>> allSpec = new HashMap<String, Set<String>>();
        for (String spec : specList) {
            // 将每个json字符串转成Map
            Map<String, String> specMap = JSON.parseObject(spec, Map.class);
            for (Map.Entry<String, String> entry : specMap.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();

                // 将当前循环的数据合并到一个 map 中
                Set<String> specSet = allSpec.get(key);
                if (specSet == null) {
                    specSet = new HashSet<String>();
                }
                specSet.add(value);
                allSpec.put(key, specSet);
            }
        }
        return allSpec;
    }

    /**
     * 获取分组集合数据
     * @param stringTerms
     * @return
     */
    public List<String> getGroupList(StringTerms stringTerms) {
        List<String> groupList = new ArrayList<String>();
        for (StringTerms.Bucket bucket : stringTerms.getBuckets()) {
            String keyName = bucket.getKeyAsString();
            groupList.add(keyName);
        }
        return groupList;
    }

    /**
     * 导入索引库
     */
    @Override
    public void importData() {
        // Feign调用，查出SKU数据
        Result<List<Sku>> skuResult = skuFeign.findAll();

        // 将LIST<SKU>转换成LIST<SKUINFO>
        List<SkuInfo> skuInfoList = JSON.parseArray(JSON.toJSONString(skuResult.getData()), SkuInfo.class);

        // 循环当前 skuInfoList
        for (SkuInfo skuInfo : skuInfoList) {
            // 获取spec => Map(Strig) => Map类型
            Map<String, Object> specMap = JSON.parseObject(skuInfo.getSpec(), Map.class);
            // 如果需要生成动态的域，只需要将该域存入到一个Map<String,Object>对象中即可，该
            // Map<String,Object>的key会生成一个域，域的名字为该Map的key
            // 当前Map<String,Object>后面Object的值会作为当前Sku对象该域的值
            skuInfo.setSpecMap(specMap);
        }
        // 调用Dao实现批量导入数据
        skuEsMapper.saveAll(skuInfoList);
    }
}
