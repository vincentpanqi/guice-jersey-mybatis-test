package com.tch.test.maven_test;

import com.tch.test.guice_jersey_mybatis_test.service.ElasticSearchService;

/**
 * Created by higgs on 2017/9/7.
 */
public class EsTest {

    public static void main(String[] args) throws Exception {
        ElasticSearchService.createMapping("account", "account.json");
    }

}
