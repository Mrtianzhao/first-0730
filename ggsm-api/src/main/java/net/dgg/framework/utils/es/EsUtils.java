package net.dgg.framework.utils.es;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by wu on 2017/8/24.
 */
public class EsUtils {


    static Log log = LogFactory.getLog(EsUtils.class);

    /**
     * 添加文档
     *
     * @param index
     * @param type
     * @param id
     * @param json
     */
    public static void add(String index, String type, String id, String json) {
        try {
            EsClientUtils.getBulkProcessor().add(new IndexRequest(index, type, id).source(json));//添加文档，以便自动提交
        } catch (Exception e) {
            log.error("add文档时出现异常：e=" + e + " json=" + json, e);
        }
    }

    /**
     * 添加文档
     *
     * @param index
     * @param type
     * @param id
     * @param source
     */
    public static void add(String index, String type, String id, Map<String, Object> source) {
        IndexResponse indexResponse = EsClientUtils.getClient().prepareIndex(index, type, id).setSource(source).get();
        System.out.println(indexResponse.getVersion());
    }

    /**
     * 添加文档
     *
     * @param index
     * @param type
     * @param id
     * @param object
     * @throws JsonProcessingException
     */
    public static void add(String index, String type, String id, Object object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String source = mapper.writeValueAsString(object);
        IndexResponse indexResponse = EsClientUtils.getClient().prepareIndex(index, type, id).setSource(source).get();
    }

    /**
     * 更新文档
     *
     * @param index
     * @param type
     * @param id
     * @param source
     * @throws IOException
     */
    public static void update(String index, String type, String id, Map source) throws IOException {
        UpdateResponse updateResponse = EsClientUtils.getClient().prepareUpdate(index, type, id).setDoc(source).get();
        System.out.println(updateResponse.getVersion());
    }

    /**
     * 删除索引
     *
     * @param index
     * @param type
     * @param id
     */
    public static void delete(String index, String type, String id) {
        DeleteResponse deleteResponse = EsClientUtils.getClient().prepareDelete(index, type, id).get();
        deleteResponse.status();
    }

    /**
     * 根据ID查询文档
     */
    public static Map get(String index, String type, String id) {
        GetResponse response = EsClientUtils.getClient().prepareGet(index, type, id).get();
        return response.getSource();
    }

    /**
     * 查询文档，根据index，type，以及查询条件
     *
     * @param index
     * @param type
     * @param condition
     * @return
     */
    public static List<Map> get(String index, String type, Map<String, String> condition, Map multiCondition, int startCount, int endCount) {
        SearchRequestBuilder searchRequestBuilder = EsClientUtils.getClient().prepareSearch(index)
                .setTypes(type).setSearchType(SearchType.DFS_QUERY_THEN_FETCH);

        BoolQueryBuilder andQuery = QueryBuilders.boolQuery();
        Iterator<String> iterator = condition.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            String value = condition.get(key);
            String[] values = value.split(",");
            BoolQueryBuilder orQuery = QueryBuilders.boolQuery();
            for (String val : values) {
                orQuery.should(QueryBuilders.matchPhraseQuery(key, val));
            }
            andQuery.must(orQuery);
        }
        if (multiCondition != null) {
            Iterator<String> miltiIte = multiCondition.keySet().iterator();
            while (miltiIte.hasNext()) {
                String key = miltiIte.next();
                String[] value = (String[]) multiCondition.get(key);
                andQuery.must(QueryBuilders.multiMatchQuery(key, value).type(MultiMatchQueryBuilder.Type.PHRASE));
            }
        }
        searchRequestBuilder.setQuery(andQuery);
        searchRequestBuilder.setFrom(startCount).setSize(endCount - startCount);

        SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();
        SearchHits hits = searchResponse.getHits();
        SearchHit[] hitArr = hits.getHits();
        List<Map> list = new ArrayList<>();
        for (SearchHit hit : hitArr) {
            list.add(hit.getSource());
        }
        return list;
    }

    /**
     * 全匹配查询
     *
     * @param index
     * @param type
     * @return
     */
    public static List matchAll(String index, String type, Map<String, String> condition, int startCount, int endCount) {
        SearchRequestBuilder searchRequestBuilder = EsClientUtils.getClient().prepareSearch(index)
                .setTypes(type).setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
        Iterator<String> iterator = condition.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            String value = condition.get(key);
            searchRequestBuilder.setQuery(QueryBuilders.matchPhraseQuery(key, value));
        }
        searchRequestBuilder.setFrom(startCount).setSize(endCount - startCount);
        SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();
        SearchHits hits = searchResponse.getHits();
        SearchHit[] hitArr = hits.getHits();
        List<Map> list = new ArrayList<>();
        for (SearchHit hit : hitArr) {
            list.add(hit.getSource());
        }
        return list;
    }

    public static long count(String index, String type, Map<String, String> condition,Map multiCondition) {
        SearchRequestBuilder searchRequestBuilder = EsClientUtils.getClient().prepareSearch(index)
                .setTypes(type);
        BoolQueryBuilder andQuery = QueryBuilders.boolQuery();
        Iterator<String> iterator = condition.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            String value = condition.get(key);
            String[] values = value.split(",");
            BoolQueryBuilder orQuery = QueryBuilders.boolQuery();
            for (String val : values) {
                orQuery.should(QueryBuilders.matchPhraseQuery(key, val));
            }
            andQuery.must(orQuery);
        }

        if (multiCondition != null) {
            Iterator<String> miltiIte = multiCondition.keySet().iterator();
            while (miltiIte.hasNext()) {
                String key = miltiIte.next();
                String[] value = (String[]) multiCondition.get(key);
                andQuery.must(QueryBuilders.multiMatchQuery(key, value).type(MultiMatchQueryBuilder.Type.PHRASE));

            }
        }

        searchRequestBuilder.setQuery(andQuery);
        return searchRequestBuilder.get().getHits().getTotalHits();
    }

    private static XContentBuilder createIKMapping(String indexType) {
        XContentBuilder mapping = null;
        try {
            mapping = XContentFactory.jsonBuilder().startObject()
                    // 索引库名（类似数据库中的表）
                    .startObject(indexType).startObject("properties").endObject()
                    .startObject("_all").field("analyzer", "ik").field("search_analyzer", "ik").endObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mapping;
    }


}
