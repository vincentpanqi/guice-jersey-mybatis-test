package com.tch.test.guice_jersey_mybatis_test.elasticsearch;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;

/**
 * Created by higgs on 2017/9/7.
 */
public class ElasticSearchUtil {

    private static TransportClient client = null;

    public static TransportClient client() throws Exception {
        client = new PreBuiltTransportClient(Settings.EMPTY)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
        return client;
    }

    public static void close() {
        if (client != null) {
            client.close();
        }
    }

}
