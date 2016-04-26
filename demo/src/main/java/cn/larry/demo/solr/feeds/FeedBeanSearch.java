package cn.larry.demo.solr.feeds;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.ModifiableSolrParams;

import java.io.IOException;

/**
 * Created by fugz on 2016/4/14.
 */
public class FeedBeanSearch {
    Logger logger = Logger.getLogger(FeedBeanSearch.class);
    private HttpSolrClient solrClient;

    public static void main(String[] args) {
        FeedBeanSearch search = new FeedBeanSearch();
        search.search();
    }

    private String solrUrl = "http://127.0.0.1:8983/solr/feeds";

    public FeedBeanSearch() {
        this.solrClient = new HttpSolrClient(solrUrl);
    }

    public void search() {
        ModifiableSolrParams params = new ModifiableSolrParams();
        params.set("q", "培训 会议");
        params.set("q.op", "and");
        params.set("start", 0);
        params.set("rows", 50);
        params.set("gt","asd");
     //   params.set("fl", "*,score");
        try {
            QueryResponse response = solrClient.query(params);
            SolrDocumentList list = response.getResults();
            logger.info("########### 总共 ： " + list.getNumFound() + "条记录");
            for (SolrDocument doc : list) {
                logger.info("id : " + doc.get("id")+",content:"+doc.get("feedContent"));
            }
        } catch (SolrServerException e) {
            logger.error("", e);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
