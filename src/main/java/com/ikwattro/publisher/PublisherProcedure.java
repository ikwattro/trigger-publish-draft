package com.ikwattro.publisher;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.Procedure;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class PublisherProcedure {


    @Procedure(name = "ga.notify")
    public Stream<SimpleResult> notify(@Name("url") String url, @Name("topic") String topic, @Name("body") String body, @Name("params") Map<String, Object> params) throws Exception {
        HttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        if (params.containsKey("token")) {
            httpPost.setHeader("Authorization", "Bearer " + params.get("token").toString());
        }
        List<NameValuePair> valuePairs = new ArrayList<NameValuePair>(2);
        valuePairs.add(new BasicNameValuePair("topic", topic));
        valuePairs.add(new BasicNameValuePair("data", body));
        httpPost.setEntity(new UrlEncodedFormEntity(valuePairs, "UTF-8"));

        HttpResponse response = client.execute(httpPost);
        HttpEntity entity = response.getEntity();
        System.out.println(EntityUtils.toString(entity));


        return Stream.of(new SimpleResult("SUCCESS"));
    }


    public class SimpleResult {
        public String result;

        public SimpleResult(String result) {
            this.result = result;
        }
    }

}
