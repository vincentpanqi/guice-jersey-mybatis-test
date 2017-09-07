package com.tch.test.guice_jersey_mybatis_test.service;

import com.alibaba.fastjson.JSON;
import com.tch.test.guice_jersey_mybatis_test.elasticsearch.ElasticSearchUtil;
import com.tch.test.guice_jersey_mybatis_test.model.Account;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by higgs on 2017/9/7.
 */
public class ElasticSearchService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticSearchService.class);

    public static void createMapping(String index, String mappingFile) throws Exception {
        TransportClient client = ElasticSearchUtil.client();
        IndicesExistsResponse res =  client.admin().indices().prepareExists(index).execute().actionGet();
        if (res.isExists()) {
            DeleteIndexRequestBuilder delIdx = client.admin().indices().prepareDelete(index);
            delIdx.execute().actionGet();
        }
        CreateIndexRequestBuilder createIndexRequestBuilder = client.admin().indices().prepareCreate(index);

        InputStream inputStream = ElasticSearchService.class.getClassLoader().getResourceAsStream("mappings/" + mappingFile);

        byte[] mapping = IOUtils.toByteArray(inputStream);

        createIndexRequestBuilder.addMapping("public", new String(mapping));

        createIndexRequestBuilder.execute().actionGet();
    }

    public static void index(Account account) throws Exception {
        TransportClient client = ElasticSearchUtil.client();
        IndexResponse response = client.prepareIndex("account", "public", String.valueOf(account.getId()))
                .setSource(JSON.toJSONBytes(account))
                .get();
        LOGGER.info("添加索引response: {}", response);
    }

    public static List<Account> searchAccountByHobby(Account account) throws Exception {
        TransportClient client = ElasticSearchUtil.client();
        BoolQueryBuilder query = QueryBuilders.boolQuery();
        if (account.getId() != null) {
            query.must(QueryBuilders.termQuery("id", account.getId()));
        }
        if (StringUtils.isNotBlank(account.getChineseName())) {
            //精确搜索
            query.must(QueryBuilders.termQuery("chineseName", account.getChineseName()));
        }
        if (StringUtils.isNotBlank(account.getEnglishName())) {
            //精确搜索
            query.must(QueryBuilders.termQuery("englishName", account.getEnglishName()));
        }
        if (StringUtils.isNotBlank(account.getHobby())) {
            //匹配搜索(关键字分词)
            query.must(QueryBuilders.matchQuery("hobby", account.getHobby()));
        }
        SearchResponse response = client.prepareSearch("account")
                .setTypes("public")
                .setQuery(query)
                .setFrom(0)
                .setSize(100)
                .execute()
                .actionGet();
        return Arrays.stream(response.getHits().getHits()).map(hit -> JSON.parseObject(hit.getSourceAsString(), Account.class))
                .collect(Collectors.toList());
    }


}
