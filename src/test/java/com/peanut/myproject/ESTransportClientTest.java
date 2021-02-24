package com.peanut.myproject;

import com.alibaba.fastjson.JSONObject;
import com.carrotsearch.hppc.cursors.ObjectObjectCursor;
import com.peanut.myproject.model.UserInfo;
import com.peanut.myproject.model.WorkExperience;
import com.peanut.myproject.utils.CommonUtils;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.admin.indices.settings.get.GetSettingsRequestBuilder;
import org.elasticsearch.action.admin.indices.settings.get.GetSettingsResponse;
import org.elasticsearch.action.admin.indices.settings.put.UpdateSettingsResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContent;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.get.GetResult;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.metrics.cardinality.InternalCardinality;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Test;

import javax.naming.directory.SearchResult;
import java.net.InetAddress;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @Author HuaZhongmin
 * @Date 2021/1/1
 * @Time 10:51
 * @Week 周五
 **/
public class ESTransportClientTest {

//    private final static Logger log = LoggerFactory.getLogger(ESTransportClientTest.class);

    private final static String INDEX_USER_INFO = "user_info";

    private final static String INDEX_USER = "user";

    private final static String INDEX_MANAGER_INFO = "manager_info";

    private final static String INDEX_MANAGER = "manager";

    private final static String INDEX_CUSTOMER_INFO = "customer_info";

    private final static String INDEX_CUSTOMER = "customer";

    private final static String INDEX_PRODUCT = "user_info";

    private final static int INDEX_SHARD = 9;

    private final static int INDEX_REPLICA = 4;

    private final static int PAGE_SIZE = 1000;

    private String[] nameArray = new String[]{"张毅", "李二", "赵三", "王五", "陆佰", "peanut", "John Smith", "Thomas Kone Ken", "ZhengHe Yang", "cheng dou dou", "韩麒麟"};

    private String[] addressArray = new String[]{"广东省深圳市福田区景田路", "广东省深圳市南山区高新南十二路", "湖北省武汉市武昌区友谊大道",
            "湖北省武汉市洪山区光谷大道", "北京市朝阳区北京路", "香港特别行政区九龙街", "新疆乌鲁木齐市水磨沟区", "浙江省杭州市西湖区玄武大道", "海南省海口市观海区北京路"};

    private String[] schoolArray = new String[]{"北京大学", "清华大学", "湖北大学", "武汉大学", "华中科技大学", "Cambridge University",
            "Harvard University", "Boston University", "华中师范大学", "湖南大学"};

    private String[] companyArray = new String[]{"阿里巴巴", "腾讯科技", "字节跳动", "平安科技", "金蝶中国", "网易", "拼多多",
            "Google", "Microsoft", "华为有限责任公司", "美团", "百度科技"};

    private String[] sexArray = new String[]{"男", "女", "保密"};

    private int[] workYearArray = new int[]{0, 1, 2, 3, 4, 5, 10};

    private double[] salaryYearArray = new double[]{15.0, 15.5, 16.5, 16.0, 16.8, 19.5, 19.8, 20.1, 20.5, 21.0, 25.0, 23.8};

    private long[] phoneArray = new long[]{13377652763L, 15722764893L, 19928646533L, 13098477736L, 17765367265L, 15927638923L, 13977652973L, 19976382784L,
            15922673888L, 16637826528L, 17763926328L, 13698376328L, 13397378923L, 13378297283L, 15528903784L, 16397287356L, 13688922873L, 13377668888L};

    private String[] placeArray = new String[]{"深圳", "北京", "上海", "广州", "杭州", "武汉", "南京", "成都", "长沙", "西安", "重庆", "厦门"};

    public static void main(String[] args) {
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            System.out.println(localHost);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private TransportClient getESTransportClient() {

        TransportClient transportClient = null;

        try {
            //必须设置集群名称，否则会报错 oNodeAvailableException[None of the configured nodes are available
            Settings settings = Settings.builder().put("cluster.name", "es-cluster-5.5.0").build();

            transportClient = new PreBuiltTransportClient(settings)
//                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getLocalHost(),9201))
//                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getLocalHost(),9202))
//                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getLocalHost(),9203))
//                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getLocalHost(),9204))
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9301));
        } catch (Exception e) {
            System.out.println("ES连接客户端创建失败");
        }
        return transportClient;
    }

    @Test
    public void testCreateIndex() {
        TransportClient transportClient = getESTransportClient();
        //default setting and no mapping ，default shard：5 ， default replicas ： 1
        //CreateIndexResponse createIndexResponse = transportClient.admin().indices().prepareCreate(INDEX_CUSTOMER).get();
        /*DeleteIndexResponse deleteIndexResponse = transportClient.admin().indices().prepareDelete(INDEX_CUSTOMER).get();
        if (deleteIndexResponse.isAcknowledged()){
            System.out.println("存在的索引以及被删除：" + INDEX_CUSTOMER);
        }else {
            System.out.println(INDEX_CUSTOMER + "索引删除失败！！！！");
        }*/
        //设置索引的setting 创建索引并添加mapping

        CreateIndexResponse createIndexResponse = transportClient.admin().indices().prepareCreate("manager_test08")
                .setSettings(Settings.builder()
                        //设置索引的分片数
                        .put("index.number_of_shards", 3)
                        //设置索引的副本数
                        .put("index.number_of_replicas", 2))
                .addMapping("customer", "{\n" +
                        "    \"customer\": {\n" +
                        "      \"properties\": {\n" +
                        "        \"id\": {\n" +
                        "          \"type\": \"text\"\n" +
                        "        },\n" +
                        "        \"name\": {\n" +
                        "          \"type\": \"text\"\n" +
                        "        },\n" +
                        "        \"height\": {\n" +
                        "          \"type\": \"double\"\n" +
                        "        },\n" +
                        "        \"weight\": {\n" +
                        "          \"type\": \"double\"\n" +
                        "        },\n" +
                        "        \"address\": {\n" +
                        "          \"type\": \"text\"\n" +
                        "        },\n" +
                        "        \"phone\": {\n" +
                        "          \"type\": \"long\"\n" +
                        "        },\n" +
                        "        \"sex\": {\n" +
                        "          \"type\": \"text\"\n" +
                        "        },\n" +
                        "        \"isMarried\": {\n" +
                        "          \"type\": \"boolean\"\n" +
                        "        },\n" +
                        "        \"isGraduated\": {\n" +
                        "          \"type\": \"boolean\"\n" +
                        "        },\n" +
                        "        \"school\": {\n" +
                        "          \"type\": \"text\"\n" +
                        "        },\n" +
                        "        \"createTome\": {\n" +
                        "          \"type\": \"date\"\n" +
                        "        },\n" +
                        "        \"updateTime\": {\n" +
                        "          \"type\": \"date\"\n" +
                        "        },\n" +
                        "        \"email\": {\n" +
                        "          \"type\": \"keyword\"\n" +
                        "        },\n" +
                        "        \"company\": {\n" +
                        "          \"type\": \"keyword\"\n" +
                        "        },\n" +
                        "        \"workExperience.workYear\": {\n" +
                        "          \"type\": \"integer\"\n" +
                        "        },\n" +
                        "        \"workExperience.salaryYear\": {\n" +
                        "          \"type\": \"double\"\n" +
                        "        },\n" +
                        "        \"workExperience.place\": {\n" +
                        "          \"type\": \"keyword\"\n" +
                        "        },\n" +
                        "        \"workExperience.level\": {\n" +
                        "          \"type\": \"integer\"\n" +
                        "        }\n" +
                        "      }\n" +
                        "    }\n" +
                        "  }", XContentType.JSON)
                .get();

        if (createIndexResponse.isAcknowledged()) {
            System.out.println("the index create success : " + INDEX_CUSTOMER);
        } else {
            System.out.println("the index create fail : " + INDEX_CUSTOMER);
        }
        //修改已经存在的索引的的 mapping
        /*transportClient.admin()
                .indices()
                .preparePutMapping(INDEX_CUSTOMER)
                .setType(INDEX_CUSTOMER)
                .setSource()
                .get();*/

        //刷新索引 不传参数时刷新所有索引
        //transportClient.admin().indices().prepareRefresh(INDEX_CUSTOMER , INDEX_CUSTOMER_INFO).get();

        //更新索引的设置
        /*UpdateSettingsResponse updateSettingsResponse = transportClient
                .admin()
                .indices()
                .prepareUpdateSettings(INDEX_CUSTOMER)
                .setSettings(Settings.builder()
                        .put())
                .get();
        //获取索引的setting
        GetSettingsResponse settingsResponse = transportClient.admin().indices().prepareGetSettings().get();
        ImmutableOpenMap<String, Settings> indexToSettings = settingsResponse.getIndexToSettings();
        for (ObjectObjectCursor<String, Settings> indexToSetting : indexToSettings) {
            String index = indexToSetting.key;
            Settings settings = indexToSetting.value;
            System.out.println("索引：" + index + "，settings: " + settings.toString());
        }*/
    }

    public HashMap<String, Object> getHashMap(String key, Object value) {
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put(key, value);
        return hashMap;
    }

    @Test
    public void clusterApiTest() {
        TransportClient transportClient = getESTransportClient();
        ClusterHealthResponse clusterHealthResponse = transportClient.admin().cluster().prepareHealth().get();

    }

    @Test
    public void indexDocumentText() {
        long startTime = System.currentTimeMillis();
        TransportClient transportClient = getESTransportClient();
        for (int i = 0; i < 1000; i++) {
            UserInfo userInfo = new UserInfo();
            userInfo.setId(String.valueOf(i));
            userInfo.setName(nameArray[i % nameArray.length]);
            userInfo.setAddress(addressArray[i % addressArray.length]);
            userInfo.setCompany(companyArray[i % companyArray.length]);
            userInfo.setSchool(schoolArray[i % schoolArray.length]);
            userInfo.setSex(sexArray[i % sexArray.length]);
            userInfo.setHeight(CommonUtils.getNumberic().doubleValue());
            userInfo.setWeight(CommonUtils.getNumberic().doubleValue());
            userInfo.setSex(sexArray[i % sexArray.length]);
            userInfo.setIsGraduated(Integer.parseInt(CommonUtils.getType()) > 5);
            userInfo.setIsMarried(Integer.parseInt(CommonUtils.getType()) > 5);
            userInfo.setPhone(phoneArray[i % placeArray.length]);
            userInfo.setCreateTime(CommonUtils.getRandomTime(-CommonUtils.getRandomDigital(1000000000L)));
            userInfo.setUpdateTime(CommonUtils.getRandomTime(CommonUtils.getRandomDigital(1000000000L)));
            WorkExperience workExperience = new WorkExperience();
            workExperience.setId(CommonUtils.getUUID());
            workExperience.setPlace(placeArray[i % placeArray.length]);
            workExperience.setWorkYear(workYearArray[i % workYearArray.length]);
            workExperience.setSalaryYear(salaryYearArray[i % salaryYearArray.length]);
            workExperience.setLevel(Integer.parseInt(CommonUtils.getType()));

            userInfo.setWorkExperience(workExperience);

            IndexResponse indexResponse = transportClient.prepareIndex(INDEX_CUSTOMER, INDEX_CUSTOMER)
                    .setSource(JSONObject.toJSONString(userInfo), XContentType.JSON)
                    .get();
            DocWriteResponse.Result result = indexResponse.getResult();
            if (DocWriteResponse.Result.CREATED.equals(result)) {
                System.out.println("向索引中添加文档成功!!!!!!");
            } else {
                System.out.println("向索引中添加文档失败!!!!!!");
            }
        }
        //231626
        System.out.println("循环索引1000个文档个文档耗时：" + (System.currentTimeMillis() - startTime));

    }

    @Test
    public void bulkIndexDocumentText() {
        TransportClient transportClient = getESTransportClient();
        for (int i = 0; i < 10; i++) {
            long startTime = System.currentTimeMillis();
            BulkRequestBuilder bulkRequestBuilder = transportClient.prepareBulk();
            for (int j = 100000  + (i * 10000); j < 100000  + ((i +1) * 10000); j++) {
                UserInfo userInfo = new UserInfo();
                userInfo.setId(String.valueOf(j));
                userInfo.setName(nameArray[j % nameArray.length]);
                userInfo.setAddress(addressArray[j % addressArray.length]);
                userInfo.setCompany(companyArray[j % companyArray.length]);
                userInfo.setSchool(schoolArray[j % schoolArray.length]);
                userInfo.setSex(sexArray[j % sexArray.length]);
                userInfo.setHeight(CommonUtils.getNumberic().doubleValue());
                userInfo.setWeight(CommonUtils.getNumberic().doubleValue());
                userInfo.setSex(sexArray[j % sexArray.length]);
                userInfo.setIsGraduated(Integer.parseInt(CommonUtils.getType()) > 5);
                userInfo.setIsMarried(Integer.parseInt(CommonUtils.getType()) > 5);
                userInfo.setPhone(phoneArray[j % placeArray.length]);
                userInfo.setEmail("test" + i + "@163.com");
                userInfo.setCreateTime(CommonUtils.getRandomTime(-CommonUtils.getRandomDigital(1000000000L)));
                userInfo.setUpdateTime(CommonUtils.getRandomTime(CommonUtils.getRandomDigital(1000000000L)));

                WorkExperience workExperience = new WorkExperience();
                workExperience.setId(CommonUtils.getUUID());
                workExperience.setPlace(placeArray[j % placeArray.length]);
                workExperience.setWorkYear(workYearArray[j % workYearArray.length]);
                workExperience.setSalaryYear(salaryYearArray[j % salaryYearArray.length]);
                workExperience.setLevel(Integer.parseInt(CommonUtils.getType()));

                userInfo.setWorkExperience(workExperience);

                IndexRequestBuilder indexRequestBuilder = transportClient.prepareIndex(INDEX_MANAGER, INDEX_MANAGER).setSource(JSONObject.toJSONString(userInfo), XContentType.JSON);
                indexRequestBuilder.setId(String.valueOf(j));
                bulkRequestBuilder.add(indexRequestBuilder);
            }
            BulkResponse bulkResponse = bulkRequestBuilder.get();
            if (bulkResponse.hasFailures()) {
                String message = bulkResponse.buildFailureMessage();
                System.out.println("批量索引文档失败:" + message);
            }
            //1000 -> 7743 18000 -> 14411 100000 -> 20178  100000 -> 189292
            System.out.println("循环索引10000个文档个文档耗时：" + (System.currentTimeMillis() - startTime));
        }

    }

    @Test
    public void deleteIndexTest() {
        TransportClient esTransportClient = getESTransportClient();
        DeleteIndexResponse deleteIndexResponse = esTransportClient
                .admin()
                .indices()
                .prepareDelete(".monitoring-es-6-2021.02.08").get();
        if (deleteIndexResponse.isAcknowledged()) {
            System.out.println("======索引删除成功======");
        } else {
            System.out.println("======索引删除失败======");
        }
    }

    @Test
    public void searchTest() {
        TransportClient esTransportClient = getESTransportClient();
        SearchResponse searchResponse = esTransportClient.prepareSearch(INDEX_CUSTOMER)
                .addSort("id", SortOrder.DESC)
//                .setQuery(QueryBuilders.rangeQuery("createTime").lt(System.currentTimeMillis()))
//                .setQuery(QueryBuilders.termQuery("name","John"))
//                .setQuery(QueryBuilders.matchPhraseQuery("name","John"))
                .setFrom(1)
                .setSize(PAGE_SIZE)
                .get();
        SearchHits hits = searchResponse.getHits();

        long totalHits = hits.getTotalHits();
        int count = 0;
        for (SearchHit hit : hits) {
            String sourceAsString = hit.getSourceAsString();
            System.out.println(sourceAsString);
            count++;
        }
        long pageCount = totalHits / PAGE_SIZE;
        if (totalHits % PAGE_SIZE > 0) {
            pageCount++;
        }
        System.out.println("查询到的文档总数：" + totalHits + ",分页到的当前页的数量：" + count + ",总页数：" + pageCount);
        SearchResponse response = esTransportClient.prepareSearch(INDEX_CUSTOMER, INDEX_CUSTOMER)
                .setQuery(QueryBuilders.matchAllQuery())
                .setFrom(1)
                .setSize(PAGE_SIZE)
//                .addAggregation(AggregationBuilders.count("id"))
//                .addSort("id", SortOrder.DESC)
                .get();

        /*Aggregations aggregations = response.getAggregations();
        List<Aggregation> aggregationList = aggregations.asList();
        for (Aggregation aggregation : aggregationList) {
            String name = aggregation.getName();
            String type = aggregation.getType();
            System.out.println("name : " + name + "type : " + type);
        }*/

    }

    @Test
    public void getDocument(){
        TransportClient transportClient = getESTransportClient();
        GetResponse response = transportClient.prepareGet(INDEX_MANAGER, INDEX_MANAGER, "70001").get();
        System.out.println(response.getSourceAsString());
    }

    @Test
    public void updateDocumentById(){
        TransportClient transportClient = getESTransportClient();
        String updateDocument = "{\"address\":\"海南省海口市观海区北京路\",\"company\":\"京东\",\"createTime\":1261709773921,\"height\":1786.88,\"id\":\"70001\"," +
                "\"isGraduated\":true,\"isMarried\":false,\"name\":\"ZhengHe Yang\",\"phone\":15927638923,\"school\":\"清华大学\",\"sex\":\"保密\"," +
                "\"updateTime\":1263245489287,\"weight\":1992.18,\"workExperience\":{\"id\":\"AA10EF2ADF5144E2AACF0A52B31CD25F\",\"level\":6," +
                "\"place\":\"北京\",\"salaryYear\":19.5,\"workYear\":1}}";
        UpdateResponse response = transportClient.prepareUpdate(INDEX_MANAGER, INDEX_MANAGER, "70001").setDoc(updateDocument, XContentType.JSON).get();

        GetResult getResult = response.getGetResult();

//        System.out.println(getResult.toString());

    }

    @Test
    public void structAggregationTest() {
        TransportClient esTransportClient = getESTransportClient();
        SearchRequest searchRequest = new SearchRequest(INDEX_CUSTOMER);
        try {
            SearchResponse searchResponse = esTransportClient.search(searchRequest).get();
            InternalCardinality aggregation = searchResponse.getAggregations().get(INDEX_CUSTOMER);
            aggregation.getValue();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
