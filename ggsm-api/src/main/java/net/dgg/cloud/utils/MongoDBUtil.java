//package net.dgg.cloud.utils;
//
//import com.google.gson.Gson;
//import com.mongodb.*;
//import com.mongodb.client.*;
//import com.mongodb.client.model.Filters;
//import com.mongodb.client.result.DeleteResult;
//import com.mongodb.client.result.UpdateResult;
//import com.mongodb.util.JSON;
//import org.apache.commons.beanutils.BeanUtils;
//import org.apache.commons.beanutils.ConversionException;
//import org.apache.commons.collections.map.HashedMap;
//import org.apache.commons.configuration.CompositeConfiguration;
//import org.apache.commons.configuration.ConfigurationException;
//import org.apache.commons.configuration.PropertiesConfiguration;
//import org.bson.Document;
//import org.bson.conversions.Bson;
//import org.bson.types.ObjectId;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.util.StringUtils;
//
//import java.beans.BeanInfo;
//import java.beans.Introspector;
//import java.beans.PropertyDescriptor;
//import java.lang.reflect.Field;
//import java.lang.reflect.Method;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.*;
//
///**
// * MongoDB工具类 Mongo实例代表了一个数据库连接池，即使在多线程的环境中，一个Mongo实例对我们来说已经足够了<br>
// * 注意Mongo已经实现了连接池，并且是线程安全的。 <br>
// * 设计为单例模式， 因 MongoDB的Java驱动是线程安全的，对于一般的应用，只要一个Mongo实例即可，<br>
// * Mongo有个内置的连接池（默认为10个） 对于有大量写和读的环境中，为了确保在一个Session中使用同一个DB时，<br>
// * DB和DBCollection是绝对线程安全的<br>
// *
// * @author tangxiyao
// */
//public class MongoDBUtil {
//    private static MongoClient mongoClient;
//    private static Logger logger = LoggerFactory.getLogger(MongoDBUtil.class);
//
//    static {
//        logger.info("===============MongoDBUtil初始化========================");
//        System.out.println("===============MongoDBUtil初始化========================");
//        CompositeConfiguration config = new CompositeConfiguration();
//        try {
//            config.addConfiguration(new PropertiesConfiguration("application.properties"));
//        } catch (ConfigurationException e) {
//            e.printStackTrace();
//        }
//        String ip = config.getString("spring.data.mongodb.host");
//        int port = config.getInt("spring.data.mongodb.port");
//        mongoClient = new MongoClient(ip, port);
//        // 大部分用户使用mongodb都在安全内网下，但如果将mongodb设为安全验证模式，就需要在客户端提供用户名和密码：
//        String myUserName = config.getString("spring.data.mongodb.username");
//        String myPassword = config.getString("spring.data.mongodb.password");
//        /*        boolean auth = db.authenticate(myUserName, myPassword);*/
//        MongoClientOptions.Builder options = new MongoClientOptions.Builder();
//        // options.autoConnectRetry(true);// 自动重连true
//        //options.maxAutoConnectRetryTime(10); // the maximum auto connect
//        // retry time
//        options.connectionsPerHost(300);// 连接池设置为300个连接,默认为100
//        options.connectTimeout(15000);// 连接超时，推荐>3000毫秒
//        options.maxWaitTime(5000); //
//        options.socketTimeout(0);// 套接字超时时间，0无限制
//        // 线程队列数，如果连接线程排满了队列就会抛出“Out of semaphores to get db”错误。
//        options.threadsAllowedToBlockForConnectionMultiplier(5000);
//        options.writeConcern(WriteConcern.SAFE);
//        options.build();
//    }
//
//
//    /**
//     * 获取DB实例 - 指定DB
//     *
//     * @param dbName
//     * @return
//     */
//    public static MongoDatabase getDB(String dbName) {
//        if (dbName != null && !"".equals(dbName)) {
//            MongoDatabase database = mongoClient.getDatabase(dbName);
//            return database;
//        }
//        return null;
//    }
//
//    /**
//     * 获取collection对象 - 指定Collection
//     *
//     * @param collName
//     * @return
//     */
//    public static MongoCollection<Document> getCollection(String dbName,
//                                                          String collName) {
//        if (null == collName || "".equals(collName)) {
//            return null;
//        }
//        if (null == dbName || "".equals(dbName)) {
//            return null;
//        }
//        MongoCollection<Document> collection = mongoClient.getDatabase(dbName).getCollection(collName);
//        return collection;
//    }
//
//
//    /**
//     * 查询DB下的所有表名
//     */
//    public static List<String> getAllCollections(String dbName) {
//        MongoIterable<String> colls = getDB(dbName).listCollectionNames();
//        List<String> _list = new ArrayList<String>();
//        for (String s : colls) {
//            _list.add(s);
//        }
//        return _list;
//    }
//
//
//    /**
//     * 获取所有数据库名称列表
//     *
//     * @return
//     */
//    public static MongoIterable<String> getAllDBNames() {
//        MongoIterable<String> s = mongoClient.listDatabaseNames();
//        return s;
//    }
//
//
//    /**
//     * 删除一个数据库
//     */
//    public static void dropDB(String dbName) {
//        getDB(dbName).drop();
//    }
//
//
//    /***
//     * 删除文档
//     *
//     * @param dbName
//     * @param collName
//     */
//    public static void dropCollection(String dbName, String collName) {
//        getDB(dbName).getCollection(collName).drop();
//    }
//
//
//    /**
//     * 查找对象 - 根据主键_id
//     *
//     * @param coll
//     * @param id
//     * @return
//     */
//    public static Document findById(MongoCollection<Document> coll, String id) {
//        try {
//            ObjectId _id = null;
//            try {
//                _id = new ObjectId(id);
//            } catch (Exception e) {
//                return null;
//            }
//            Document myDoc = coll.find(Filters.eq("_id", _id)).first();
//            return myDoc;
//        } catch (Exception e) {
//            e.printStackTrace();
//            //close();
//        }
//        return null;
//
//    }
//
//    /***
//     * 条件查询对象
//     * @param coll
//     * @param filter
//     * @return
//     */
//    public static Document findByNames(MongoCollection<Document> coll, Bson filter) {
//        try {
//            return coll.find(filter).first();
//        } catch (Exception e) {
//            e.printStackTrace();
//            //close();
//        }
//        return null;
//
//    }
//
//    /***
//     * 条件查询对象
//     * @param coll
//     * @param filter
//     * @return
//     */
//    public static FindIterable<Document> findByPrams(MongoCollection<Document> coll, Bson filter) {
//        try {
//            return coll.find(filter);
//        } catch (Exception e) {
//            e.printStackTrace();
//            //close();
//        }
//        return null;
//
//    }
//
//    /***
//     * 多条件查询对象
//     * @param coll
//     * @param
//     * @return
//     */
//    public static Document findByNames(MongoCollection<Document> coll, Map<String, Object> map) {
//        try {
//            return coll.find(new BasicDBObject(map)).first();
//        } catch (Exception e) {
//            e.printStackTrace();
//            //close();
//        }
//        return null;
//
//    }
//
//    /**
//     * 统计数
//     */
//    public static int getCount(MongoCollection<Document> coll) {
//        try {
//            int count = (int) coll.count();
//            return count;
//        } catch (Exception e) {
//            e.printStackTrace();
//            //close();
//        }
//        return 0;
//
//    }
//
//    /**
//     * 查询 多个集合文档
//     */
//    public static MongoCursor<Document> find(MongoCollection<Document> coll, Bson filter) {
//        try {
//            return coll.find(filter).iterator();
//        } catch (Exception e) {
//            e.printStackTrace();
//            //close();
//        }
//        return null;
//    }
//
//    /**
//     * map集合 多条件查询
//     *
//     * @param coll
//     * @param map
//     * @return
//     */
//    public static MongoCursor<Document> find(MongoCollection<Document> coll, Map<String, Object> map) {
//        try {
//            return coll.find(new BasicDBObject(map)).iterator();
//        } catch (Exception e) {
//            e.printStackTrace();
//            //close();
//        }
//        return null;
//
//    }
//
//
//    /***
//     * 分页查询     默认按_id字段降序
//     * @param coll
//     * @param map
//     * @param pageNo
//     * @param pageSize
//     * @return
//     */
//    public static MongoCursor<Document> findByPage(MongoCollection<Document> coll, Map<String, Object> map, int pageNo, int pageSize) {
//        try {
//            Bson orderBy = new BasicDBObject("_id", -1);
//            return coll.find(new BasicDBObject(map)).sort(orderBy).skip((pageNo - 1) * pageSize).limit(pageSize).iterator();
//        } catch (Exception e) {
//            e.printStackTrace();
//            //close();
//        }
//        return null;
//
//    }
//
//    /**
//     * 分页查询 自定义排序
//     *
//     * @param coll
//     * @param sorting  1 为升序排列，而-1是用于降序排列。
//     * @param name     排序字段
//     * @param map      查询条件
//     * @param pageNo   页数
//     * @param pageSize 每页显示的数量
//     * @return
//     */
//    public static MongoCursor<Document> findByPage(MongoCollection<Document> coll, String sorting, String name,
//                                                   Map<String, Object> map, int pageNo, int pageSize) {
//        try {
//            Bson orderBy = null;
//            //降序
//            if (sorting.equals("desc")) {
//                orderBy = new BasicDBObject(name, -1);
//            } else {
//                orderBy = new BasicDBObject(name, 1);
//            }
//            return coll.find(new BasicDBObject(map)).sort(orderBy).skip((pageNo - 1) * pageSize).limit(pageSize).iterator();
//        } catch (Exception e) {
//            e.printStackTrace();
//            //close();
//        }
//        return null;
//
//    }
//
//    /**
//     * 分页查询 自定义排序
//     *
//     * @param coll
//     * @param sortMap  value  1 为升序排列，而-1是用于降序排列。 key 排序字段
//     * @param map      查询条件
//     * @param pageNo   页数
//     * @param pageSize 每页显示的数量
//     * @return
//     */
//    public static FindIterable<Document> findByPage(MongoCollection<Document> coll, Map<String, Object> sortMap,
//                                                    Map<String, Object> map, int pageNo, int pageSize) {
//        try {
//            FindIterable findIterable = null;
//            if (StringUtils.isEmpty(map) || map.isEmpty()) {
//                findIterable = coll.find();
//            } else {
//                findIterable = coll.find(new BasicDBObject(map));
//            }
//
//            //降序
//            if (null != sortMap && !sortMap.isEmpty()) {
//                Iterator iterator = sortMap.entrySet().iterator();
//                while (iterator.hasNext()) {
//                    Map.Entry entry = (Map.Entry) iterator.next();
//                    String name = String.valueOf(entry.getKey());     //返回对应的键
//                    String value = String.valueOf(entry.getValue());//返回对应的值
//                    Bson orderBy = null;
//                    if (value.equals("desc")) {
//                        orderBy = new BasicDBObject(name, -1);
//                    } else {
//                        orderBy = new BasicDBObject(name, 1);
//                    }
//                    findIterable.sort(orderBy);
//                }
//            }
//            return findIterable.skip((pageNo - 1) * pageSize).limit(pageSize);
//        } catch (Exception e) {
//            logger.error("多条件排序查询出错================================================>" + e);
//            e.printStackTrace();
//            //close();
//        }
//        return null;
//    }
//
//    /**
//     * 分页查询 自定义排序
//     *
//     * @param coll
//     * @param sortMap  value  1 为升序排列，而-1是用于降序排列。 key 排序字段
//     * @param map      查询条件
//     * @param pageNo   页数
//     * @param pageSize 每页显示的数量
//     * @return
//     */
//    public static MongoCursor<Document> findByPageReturnIterator(MongoCollection<Document> coll, Map<String, Object> sortMap,
//                                                                 Map<String, Object> map, int pageNo, int pageSize) {
//        try {
//            FindIterable findIterable = coll.find(new BasicDBObject(map));
//            //降序
//            if (null != sortMap && !sortMap.isEmpty()) {
//                Iterator iterator = sortMap.entrySet().iterator();
//                while (iterator.hasNext()) {
//                    Map.Entry entry = (Map.Entry) iterator.next();
//                    String name = String.valueOf(entry.getKey());     //返回对应的键
//                    String value = String.valueOf(entry.getValue());//返回对应的值
//                    Bson orderBy = null;
//                    if (value.equals("desc")) {
//                        orderBy = new BasicDBObject(name, -1);
//                    } else {
//                        orderBy = new BasicDBObject(name, 1);
//                    }
//                    findIterable.sort(orderBy);
//                }
//            }
//            return findIterable.skip((pageNo - 1) * pageSize).limit(pageSize).iterator();
//        } catch (Exception e) {
//            logger.error("多条件排序查询出错================================================>" + e);
//            e.printStackTrace();
//            //close();
//        }
//        return null;
//    }
//
//    /**
//     * 分页查询 自定义排序
//     *
//     * @param coll
//     * @param sortMap  value  1 为升序排列，而-1是用于降序排列。 key 排序字段
//     * @param map      查询条件
//     * @param pageNo   页数
//     * @param pageSize 每页显示的数量
//     * @return
//     */
//    public static List<Map<String, Object>> findByPageReturnList(MongoCollection<Document> coll, Map<String, Object> sortMap,
//                                                                 Map<String, Object> map, int pageNo, int pageSize) {
//        try {
//            FindIterable findIterable = null;
//            if (StringUtils.isEmpty(map) || map.size() == 0) {
//                findIterable = coll.find();
//            } else {
//                findIterable = coll.find(new BasicDBObject(map));
//            }
//            //降序
//            if (null != sortMap && !sortMap.isEmpty()) {
//                Iterator iterator = sortMap.entrySet().iterator();
//                while (iterator.hasNext()) {
//                    Map.Entry entry = (Map.Entry) iterator.next();
//                    String name = String.valueOf(entry.getKey());     //返回对应的键
//                    String value = String.valueOf(entry.getValue());//返回对应的值
//                    Bson orderBy = null;
//                    if (value.equals("desc")) {
//                        orderBy = new BasicDBObject(name, -1);
//                    } else {
//                        orderBy = new BasicDBObject(name, 1);
//                    }
//                    findIterable.sort(orderBy);
//                }
//            }
//            MongoCursor<Document> mc = findIterable.skip((pageNo - 1) * pageSize).limit(pageSize).iterator();
//            List l = new ArrayList();
//            while (mc.hasNext()) {
//                Document document = mc.next();
//                Iterator iterator = document.entrySet().iterator();
//                Map<String, Object> map1 = new HashedMap();
//                while (iterator.hasNext()) {
//                    Map.Entry entry = (Map.Entry) iterator.next();
//                    String name = String.valueOf(entry.getKey());     //返回对应的键
//                    String value = String.valueOf(entry.getValue());//返回对应的值
//                    if (!"_id".equals(name)) {
//                        Boolean r = isValidDate(value);
//                        if (r) {
//                            //如果是时间格式
//                            map1.put(name, fromISODate(value));
//                        }else {
//                            map1.put(name, value);
//                        }
//                    }
//                }
//                l.add(map1);
//            }
//            return l;
//        } catch (Exception e) {
//            logger.error("多条件排序查询出错================================================>" + e);
//            e.printStackTrace();
//            //close();
//        }
//        return null;
//    }
//
//
//    /**
//     * 分页查询 自定义排序
//     *
//     * @param coll
//     * @param sortMap  value  1 为升序排列，而-1是用于降序排列。 key 排序字段
//     * @param o        查询条件
//     * @param pageNo   页数
//     * @param pageSize 每页显示的数量
//     * @return
//     */
//    public static List<Map<String, Object>> findByPageReturnList(MongoCollection<Document> coll, Map<String, Object> sortMap,
//                                                                 Object o, int pageNo, int pageSize) {
//        try {
//            FindIterable findIterable = null;
//            if (StringUtils.isEmpty(o)) {
//                findIterable = coll.find();
//            } else {
//                findIterable = coll.find(new BasicDBObject(ObjectToMapHaveValue(o)));
//            }
//            //降序
//            if (null != sortMap && !sortMap.isEmpty()) {
//                Iterator iterator = sortMap.entrySet().iterator();
//                while (iterator.hasNext()) {
//                    Map.Entry entry = (Map.Entry) iterator.next();
//                    String name = String.valueOf(entry.getKey());     //返回对应的键
//                    String value = String.valueOf(entry.getValue());//返回对应的值
//                    Bson orderBy = null;
//                    if (value.equals("desc")) {
//                        orderBy = new BasicDBObject(name, -1);
//                    } else {
//                        orderBy = new BasicDBObject(name, 1);
//                    }
//                    findIterable.sort(orderBy);
//                }
//            }
//            MongoCursor<Document> mc = findIterable.skip((pageNo - 1) * pageSize).limit(pageSize).iterator();
//            List l = new ArrayList();
//            while (mc.hasNext()) {
//                Document document = mc.next();
//                Iterator iterator = document.entrySet().iterator();
//                Map<String, Object> map1 = new HashedMap();
//                while (iterator.hasNext()) {
//                    Map.Entry entry = (Map.Entry) iterator.next();
//                    String name = String.valueOf(entry.getKey());     //返回对应的键
//                    String value = String.valueOf(entry.getValue());//返回对应的值
//                    if (!"_id".equals(name)) {
//                        System.out.println(document);
//                        map1.put(name, value);
//                    }
//                }
//                l.add(map1);
//            }
//            return l;
//        } catch (Exception e) {
//            logger.error("多条件排序查询出错================================================>" + e);
//            e.printStackTrace();
//            //close();
//        }
//        return null;
//    }
//
//    /**
//     * 分页查询 自定义排序
//     *
//     * @param coll
//     * @param sortMap  value  1 为升序排列，而-1是用于降序排列。 key 排序字段
//     * @param o        查询条件
//     * @param pageNo   页数
//     * @param pageSize 每页显示的数量
//     * @return
//     */
//    public static List<Map<String, Object>> findByPageReturnList(MongoCollection<Document> coll, Map<String, Object> sortMap, Object o, Map<String, Object> timeMap, int pageNo, int pageSize) {
//        try {
//
//            FindIterable findIterable = null;
//            BasicDBObject bson = null;
//            if (StringUtils.isEmpty(o)) {
//                bson = new BasicDBObject();
//            } else {
//                bson = new BasicDBObject(ObjectToMapHaveValue(o));
//            }
//            if (!StringUtils.isEmpty(timeMap) && timeMap.size() > 0 && !timeMap.isEmpty()) {
//
//                Iterator iterator = timeMap.entrySet().iterator();
//                while (iterator.hasNext()) {
//                    Map.Entry entry = (Map.Entry) iterator.next();
//                    String name = String.valueOf(entry.getKey());     //返回对应的键
//                    Map<String, Object> value = (Map<String, Object>) entry.getValue();//返回对应的值
//                    Iterator iteratorValue = value.entrySet().iterator();
//                    while (iteratorValue.hasNext()) {
//                        Map.Entry entryValue = (Map.Entry) iteratorValue.next();
//                        try {
//                            bson.append(name, new BasicDBObject(String.valueOf(entryValue.getKey()), (String) entryValue.getValue()));
//                        }catch (ClassCastException e){
//                            bson.append(name, new BasicDBObject(String.valueOf(entryValue.getKey()), (Date) entryValue.getValue()));
//                        }
//                    }
//                }
//            }
//            findIterable = coll.find(bson);
//            //降序
//            if (null != sortMap && !sortMap.isEmpty()) {
//                Iterator iterator = sortMap.entrySet().iterator();
//                while (iterator.hasNext()) {
//                    Map.Entry entry = (Map.Entry) iterator.next();
//                    String name = String.valueOf(entry.getKey());     //返回对应的键
//                    String value = String.valueOf(entry.getValue());//返回对应的值
//                    Bson orderBy = null;
//                    if (value.equals("desc")) {
//                        orderBy = new BasicDBObject(name, -1);
//                    } else {
//                        orderBy = new BasicDBObject(name, 1);
//                    }
//                    findIterable.sort(orderBy);
//                }
//            }
//            MongoCursor<Document> mc = findIterable.skip((pageNo - 1) * pageSize).limit(pageSize).iterator();
//            List l = new ArrayList();
//            while (mc.hasNext()) {
//                Document document = mc.next();
//                Iterator iterator = document.entrySet().iterator();
//                Map<String, Object> map1 = new HashedMap();
//                while (iterator.hasNext()) {
//                    Map.Entry entry = (Map.Entry) iterator.next();
//                    String name = String.valueOf(entry.getKey());     //返回对应的键
//                    String value = String.valueOf(entry.getValue());//返回对应的值
//                    if (!"_id".equals(name)) {
//                        //System.out.println(document);
//                        Boolean r = isValidDate(value);
//                        if(r){
//                            System.out.println("这个值是时间============================>");
//                        }
//                        map1.put(name, value);
//                    }
//                }
//                l.add(map1);
//            }
//            return l;
//        } catch (Exception e) {
//            logger.error("多条件排序查询出错================================================>" + e);
//            e.printStackTrace();
//            //close();
//        }
//        return null;
//    }
//
//    /**
//     * 分页查询 自定义排序
//     *
//     * @param coll
//     * @param sortMap value  1 为升序排列，而-1是用于降序排列。 key 排序字段
//     * @param o       查询条件
//     * @return
//     */
//    public static List<Map<String, Object>> findByPageReturnList(MongoCollection<Document> coll, Map<String, Object> sortMap,
//                                                                 Object o) {
//        try {
//            FindIterable findIterable = null;
//            if (StringUtils.isEmpty(o)) {
//                findIterable = coll.find();
//            } else {
//                findIterable = coll.find(new BasicDBObject(ObjectToMapHaveValue(o)));
//            }
//            //降序
//            if (null != sortMap && !sortMap.isEmpty()) {
//                Iterator iterator = sortMap.entrySet().iterator();
//                while (iterator.hasNext()) {
//                    Map.Entry entry = (Map.Entry) iterator.next();
//                    String name = String.valueOf(entry.getKey());     //返回对应的键
//                    String value = String.valueOf(entry.getValue());//返回对应的值
//                    Bson orderBy = null;
//                    if (value.equals("desc")) {
//                        orderBy = new BasicDBObject(name, -1);
//                    } else {
//                        orderBy = new BasicDBObject(name, 1);
//                    }
//                    findIterable.sort(orderBy);
//                }
//            }
//            MongoCursor<Document> mc = findIterable.iterator();
//            List l = new ArrayList();
//            while (mc.hasNext()) {
//                Document document = mc.next();
//                Iterator iterator = document.entrySet().iterator();
//                Map<String, Object> map1 = new HashedMap();
//                while (iterator.hasNext()) {
//                    Map.Entry entry = (Map.Entry) iterator.next();
//                    String name = String.valueOf(entry.getKey());     //返回对应的键
//                    String value = String.valueOf(entry.getValue());//返回对应的值
//                    if (!"_id".equals(name)) {
//                        //System.out.println(document);
//                        map1.put(name, value);
//                    }
//                }
//                l.add(map1);
//            }
//            return l;
//        } catch (Exception e) {
//            logger.error("多条件排序查询出错================================================>" + e);
//            e.printStackTrace();
//            //close();
//        }
//        return null;
//    }
//
//    /**
//     * 分页查询 自定义排序
//     *
//     * @param coll
//     * @param sortMap value  1 为升序排列，而-1是用于降序排列。 key 排序字段
//     * @param map     查询条件
//     * @return
//     */
//    public static List<Map<String, Object>> findByPageReturnList(MongoCollection<Document> coll, Map<String, Object> sortMap,
//                                                                 Map<String, Object> map) {
//        try {
//            FindIterable findIterable = null;
//            if (StringUtils.isEmpty(map)) {
//                findIterable = coll.find();
//            } else {
//                findIterable = coll.find(new BasicDBObject(map));
//            }
//            //降序
//            if (null != sortMap && !sortMap.isEmpty()) {
//                Iterator iterator = sortMap.entrySet().iterator();
//                while (iterator.hasNext()) {
//                    Map.Entry entry = (Map.Entry) iterator.next();
//                    String name = String.valueOf(entry.getKey());     //返回对应的键
//                    String value = String.valueOf(entry.getValue());//返回对应的值
//                    Bson orderBy = null;
//                    if (value.equals("desc")) {
//                        orderBy = new BasicDBObject(name, -1);
//                    } else {
//                        orderBy = new BasicDBObject(name, 1);
//                    }
//                    findIterable.sort(orderBy);
//                }
//            }
//            MongoCursor<Document> mc = findIterable.iterator();
//            List l = new ArrayList();
//            while (mc.hasNext()) {
//                Document document = mc.next();
//                Iterator iterator = document.entrySet().iterator();
//                Map<String, Object> map1 = new HashedMap();
//                while (iterator.hasNext()) {
//                    Map.Entry entry = (Map.Entry) iterator.next();
//                    String name = String.valueOf(entry.getKey());     //返回对应的键
//                    String value = String.valueOf(entry.getValue());//返回对应的值
//                    if (!"_id".equals(name)) {
//                        //System.out.println(document);
//                        map1.put(name, value);
//                    }
//                }
//                l.add(map1);
//            }
//            return l;
//        } catch (Exception e) {
//            logger.error("多条件排序查询出错================================================>" + e);
//            e.printStackTrace();
//            //close();
//        }
//        return null;
//    }
//
//
//    /**
//     * 分页查询 自定义排序
//     *
//     * @param coll
//     * @param
//     * @param map  查询条件
//     * @return
//     */
//    public static Long findByPageReturnCount(MongoCollection<Document> coll,
//                                             Map<String, Object> map) {
//        try {
//            if (StringUtils.isEmpty(map) || map.isEmpty()) {
//                return coll.count();
//            } else {
//                return coll.count(new BasicDBObject(map));
//            }
//
//        } catch (Exception e) {
//            logger.error("多条件排序查询出错================================================>" + e);
//            e.printStackTrace();
//            //close();
//        }
//        return null;
//    }
//
//    /**
//     * 分页查询 自定义排序
//     *
//     * @param coll
//     * @param sortMap value  1 为升序排列，而-1是用于降序排列。 key 排序字段
//     * @return
//     */
//    public static Long findByPageReturnCount(MongoCollection<Document> coll, Map<String, Object> sortMap,
//                                             Map<String, Object> map) {
//        try {
//            FindIterable findIterable = null;
//            if (StringUtils.isEmpty(map)) {
//                findIterable = coll.find();
//            } else {
//                findIterable = coll.find(new BasicDBObject(map));
//            }
//            //降序
//            if (null != sortMap && !sortMap.isEmpty()) {
//                Iterator iterator = sortMap.entrySet().iterator();
//                while (iterator.hasNext()) {
//                    Map.Entry entry = (Map.Entry) iterator.next();
//                    String name = String.valueOf(entry.getKey());     //返回对应的键
//                    String value = String.valueOf(entry.getValue());//返回对应的值
//                    Bson orderBy = null;
//                    if (value.equals("desc")) {
//                        orderBy = new BasicDBObject(name, -1);
//                    } else {
//                        orderBy = new BasicDBObject(name, 1);
//                    }
//                    findIterable.sort(orderBy);
//                }
//            }
//            MongoCursor<Document> mc = findIterable.iterator();
//            Long count = 0l;
//
//            while (mc.hasNext()) {
//                //System.out.println(mc.next());
//                mc.next();
//                count++;
//            }
//            return count;
//        } catch (Exception e) {
//            logger.error("多条件排序查询出错================================================>" + e);
//            e.printStackTrace();
//            //close();
//        }
//        return null;
//    }
//
//
//    /**
//     * 分页查询 自定义排序
//     *
//     * @param coll
//     * @param sortMap value  1 为升序排列，而-1是用于降序排列。 key 排序字段
//     * @return
//     */
//    public static Long findByPageReturnCount(MongoCollection<Document> coll, Map<String, Object> sortMap,
//                                             Object o) {
//        try {
//            FindIterable findIterable = null;
//            if (StringUtils.isEmpty(o)) {
//                findIterable = coll.find();
//            } else {
//                findIterable = coll.find(new BasicDBObject(ObjectToMapHaveValue(o)));
//            }
//            //降序
//            if (null != sortMap && !sortMap.isEmpty()) {
//                Iterator iterator = sortMap.entrySet().iterator();
//                while (iterator.hasNext()) {
//                    Map.Entry entry = (Map.Entry) iterator.next();
//                    String name = String.valueOf(entry.getKey());     //返回对应的键
//                    String value = String.valueOf(entry.getValue());//返回对应的值
//                    Bson orderBy = null;
//                    if (value.equals("desc")) {
//                        orderBy = new BasicDBObject(name, -1);
//                    } else {
//                        orderBy = new BasicDBObject(name, 1);
//                    }
//                    findIterable.sort(orderBy);
//                }
//            }
//            MongoCursor<Document> mc = findIterable.iterator();
//            Long count = 0l;
//
//            while (mc.hasNext()) {
//                count++;
//            }
//            return count;
//        } catch (Exception e) {
//            logger.error("多条件排序查询出错================================================>" + e);
//            e.printStackTrace();
//            //close();
//        }
//        return null;
//    }
//
//
//    /**
//     * 分页查询 自定义排序
//     *
//     * @param coll
//     * @param sortMap value  1 为升序排列，而-1是用于降序排列。 key 排序字段
//     * @return
//     */
//    public static Long findByPageReturnCount(MongoCollection<Document> coll, Map<String, Object> sortMap,
//                                             Object o, Map<String, Object> timeMap) {
//        try {
//            FindIterable findIterable = null;
//            BasicDBObject bson = null;
//            if (StringUtils.isEmpty(o)) {
//                bson = new BasicDBObject();
//            } else {
//                bson = new BasicDBObject(ObjectToMapHaveValue(o));
//            }
//            if (!StringUtils.isEmpty(timeMap) && timeMap.size() > 0 && !timeMap.isEmpty()) {
//
//                Iterator iterator = timeMap.entrySet().iterator();
//                while (iterator.hasNext()) {
//                    Map.Entry entry = (Map.Entry) iterator.next();
//                    String name = String.valueOf(entry.getKey());     //返回对应的键
//                    Map<String, Object> value = (Map<String, Object>) entry.getValue();//返回对应的值
//                    Iterator iteratorValue = value.entrySet().iterator();
//                    while (iteratorValue.hasNext()) {
//                        Map.Entry entryValue = (Map.Entry) iteratorValue.next();
//                        try {
//                            bson.append(name, new BasicDBObject(String.valueOf(entryValue.getKey()), (String) entryValue.getValue()));
//                        }catch (ClassCastException e){
//                            bson.append(name, new BasicDBObject(String.valueOf(entryValue.getKey()), (Date) entryValue.getValue()));
//                        }
//                    }
//                }
//            }
//            findIterable = coll.find(bson);
//            //降序
//            if (null != sortMap && !sortMap.isEmpty()) {
//                Iterator iterator = sortMap.entrySet().iterator();
//                while (iterator.hasNext()) {
//                    Map.Entry entry = (Map.Entry) iterator.next();
//                    String name = String.valueOf(entry.getKey());     //返回对应的键
//                    String value = String.valueOf(entry.getValue());//返回对应的值
//                    Bson orderBy = null;
//                    if (value.equals("desc")) {
//                        orderBy = new BasicDBObject(name, -1);
//                    } else {
//                        orderBy = new BasicDBObject(name, 1);
//                    }
//                    findIterable.sort(orderBy);
//                }
//            }
//            MongoCursor<Document> mc = findIterable.iterator();
//            List l = new ArrayList();
//            Long count = 0l;
//            while (mc.hasNext()) {
//                Document document = mc.next();
//                count++;
//            }
//            return count;
//        } catch (Exception e) {
//            logger.error("多条件排序查询出错================================================>" + e);
//            e.printStackTrace();
//            //close();
//        }
//        return 0l;
//    }
//
//    /**
//     * 分页查询 自定义排序
//     *
//     * @param coll
//     * @param
//     * @param o    查询条件
//     * @return
//     */
//    public static Long findByPageReturnCount(MongoCollection<Document> coll,
//                                             Object o) {
//        try {
//            if (StringUtils.isEmpty(o)) {
//                return coll.count();
//            } else {
//                return coll.count(new BasicDBObject(ObjectToMapHaveValue(o)));
//            }
//        } catch (Exception e) {
//            logger.error("多条件排序查询出错================================================>" + e);
//            e.printStackTrace();
//            //close();
//        }
//        return null;
//    }
//
//
//    /**
//     * 通过ID删除
//     *
//     * @param coll
//     * @param id
//     * @return
//     */
//    public static int deleteById(MongoCollection<Document> coll, String id) {
//        try {
//            int count = 0;
//            ObjectId _id = null;
//            _id = new ObjectId(id);
//            Bson filter = Filters.eq("_id", _id);
//            DeleteResult deleteResult = coll.deleteOne(filter);
//            count = (int) deleteResult.getDeletedCount();
//            return count;
//        } catch (Exception e) {
//            e.printStackTrace();
//            //close();
//        }
//        return 0;
//    }
//
//    /**
//     * 通过条件删除
//     *
//     * @param coll
//     * @param map
//     * @return
//     */
//    public static int deleteByPrams(MongoCollection<Document> coll, Map<String, Object> map) {
//        try {
//            int count = 0;
//            //Bson filter = Filters.eq("_id", _id);
//            Bson filter = null;
//            if (StringUtils.isEmpty(map) || map.isEmpty()) {
//                filter = new BasicDBObject();
//            } else {
//                filter = new BasicDBObject(map);
//            }
//            DeleteResult deleteResult = coll.deleteOne(filter);
//            count = (int) deleteResult.getDeletedCount();
//            return count;
//        } catch (Exception e) {
//            e.printStackTrace();
//            //close();
//        }
//        return 0;
//    }
//
//    /**
//     * 通过条件删除
//     *
//     * @param coll
//     * @param o
//     * @return
//     */
//    public static int deleteByPrams(MongoCollection<Document> coll, Object o) {
//        try {
//            int count = 0;
//            //Bson filter = Filters.eq("_id", _id);
//            Bson filter = null;
//            if (StringUtils.isEmpty(o)) {
//                filter = new BasicDBObject();
//            } else {
//                filter = new BasicDBObject(ObjectToMapHaveValue(o));
//            }
//            DeleteResult deleteResult = coll.deleteOne(filter);
//            count = (int) deleteResult.getDeletedCount();
//            return count;
//        } catch (Exception e) {
//            e.printStackTrace();
//            //close();
//        }
//        return 0;
//    }
//
//
//    /**
//     * 修改
//     *
//     * @param coll
//     * @param id
//     * @param newdoc
//     * @return
//     */
//    public static UpdateResult updateById(MongoCollection<Document> coll, String id, Document newdoc) {
//        ObjectId _idobj = null;
//        try {
//            _idobj = new ObjectId(id);
//            Bson filter = Filters.eq("_id", _idobj);
//            Document document = new Document("$set", newdoc);
//            return coll.updateOne(filter, document);
//        } catch (Exception e) {
//            e.printStackTrace();
//            //close();
//        }
//        return null;
//    }
//
//    /**
//     * 修改
//     *
//     * @param coll
//     * @param id
//     * @param newdoc
//     * @return
//     */
//    public static UpdateResult replaceById(MongoCollection<Document> coll, String id, Document newdoc) {
//        ObjectId _idobj = null;
//        try {
//            _idobj = new ObjectId(id);
//            Bson filter = Filters.eq("_id", _idobj);
//            Document document = new Document("$set", newdoc);
//            return coll.replaceOne(filter, document); // 完全替代
//        } catch (Exception e) {
//            e.printStackTrace();
//            //close();
//        }
//        return null;
//    }
//
//
//    /**
//     * 修改
//     *
//     * @param coll
//     * @param search 查询条件
//     * @param o      修改的实体
//     * @return
//     */
//    public static UpdateResult updateByPrams(MongoCollection<Document> coll, Object search, Object o) throws Exception {
//        //ObjectId _idobj = null;
//        try {
//            //_idobj = new ObjectId(id);
//            //Bson filter = Filters.eq("_id", _idobj);
//            Bson filter = new BasicDBObject(ObjectToMapHaveValue(search));
//            Document document = new Document("$set", ObjectToDcument(o));
//            return coll.updateMany(filter, document);
//        } catch (Exception e) {
//            e.printStackTrace();
//            //close();
//        }
//        return null;
//    }
//
//
//    /**
//     * 添加
//     *
//     * @param coll
//     * @param doc
//     * @return
//     */
//    public static boolean save(MongoCollection<Document> coll, Document doc) {
//        boolean falg = false;
//        try {
//            coll.insertOne(doc);
//            falg = true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            logger.error("添加异常，异常信息：", e);
//        } finally {
//            //close();
//        }
//        return falg;
//    }
//
//    /**
//     * 添加实体
//     *
//     * @param coll
//     * @param o    实体
//     * @return
//     */
//    public static boolean save(MongoCollection<Document> coll, Object o) throws Exception {
//        Document doc = ObjectToDcument(o);
//        boolean falg = false;
//        try {
//            coll.insertOne(doc);
//            falg = true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            logger.error("添加异常，异常信息：", e);
//        } finally {
//            //close();
//        }
//        return falg;
//    }
//
//
//    /**
//     * 添加
//     *
//     * @param coll
//     * @param doc
//     * @return
//     */
//    public static String saveReturnId(MongoCollection<Document> coll, Document doc) {
//        String falg = null;
//        try {
//            coll.insertOne(doc);
//            falg = doc.getString("_id");
//        } catch (Exception e) {
//            e.printStackTrace();
//            logger.error("添加异常，异常信息：", e);
//        } finally {
//            //close();
//        }
//        return falg;
//    }
//
//
//    /**
//     * 添加
//     *
//     * @param coll
//     * @param doc
//     * @return
//     */
//    public static boolean save(MongoCollection<Document> coll, List<Document> doc) {
//        boolean falg = false;
//        try {
//            coll.insertMany(doc);
//            falg = true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            logger.error("添加异常，异常信息：", e);
//        } finally {
//            //close();
//        }
//        return falg;
//    }
//
//    /**
//     * 返回可以保存的dbObject实体类对象
//     *
//     * @param
//     */
//    public static DBObject getDBObject(Object o) {
//        Gson gson = new Gson();
//        return (DBObject) JSON.parse(gson.toJson(o));
//    }
//
//    /**
//     * 将实体格式化为map
//     */
//    public static Map<String, Object> ObjectToMap(Object bean) throws Exception {
//        Class type = bean.getClass();
//        Map returnMap = new HashMap();
//        BeanInfo beanInfo = Introspector.getBeanInfo(type);
//        PropertyDescriptor[] propertyDescriptors = beanInfo
//                .getPropertyDescriptors();
//        for (int i = 0, n = propertyDescriptors.length; i < n; i++) {
//            PropertyDescriptor descriptor = propertyDescriptors[i];
//            String propertyName = descriptor.getName();
//            if (!propertyName.equals("class")) {
//                Method readMethod = descriptor.getReadMethod();
//                Object result = readMethod.invoke(bean, new Object[0]);
//                if (result != null) {
//                    returnMap.put(propertyName, result);
//                } else {
//                    returnMap.put(propertyName, "");
//                }
//            }
//        }
//        return returnMap;
//    }
//
//
//    /**
//     * 将实体格式化为map
//     */
//    public static Map<String, Object> ObjectToMapHaveValue(Object bean) throws Exception {
//        Class type = bean.getClass();
//        Map returnMap = new HashMap();
//        BeanInfo beanInfo = Introspector.getBeanInfo(type);
//        PropertyDescriptor[] propertyDescriptors = beanInfo
//                .getPropertyDescriptors();
//        for (int i = 0, n = propertyDescriptors.length; i < n; i++) {
//            PropertyDescriptor descriptor = propertyDescriptors[i];
//            String propertyName = descriptor.getName();
//            if (!propertyName.equals("class")) {
//                Method readMethod = descriptor.getReadMethod();
//                Object result = readMethod.invoke(bean, new Object[0]);
//                if (result != null) {
//                    returnMap.put(propertyName, result);
//                }
//            }
//        }
//        return returnMap;
//    }
//
//
//    /**
//     * 将实体格式化为map
//     */
//    public static Document ObjectToDcument(Object bean) throws Exception {
//        Class type = bean.getClass();
//        Map<String, Object> returnMap = new HashMap();
//        BeanInfo beanInfo = Introspector.getBeanInfo(type);
//        PropertyDescriptor[] propertyDescriptors = beanInfo
//                .getPropertyDescriptors();
//        for (int i = 0, n = propertyDescriptors.length; i < n; i++) {
//            PropertyDescriptor descriptor = propertyDescriptors[i];
//            String propertyName = descriptor.getName();
//            if (!propertyName.equals("class")) {
//                Method readMethod = descriptor.getReadMethod();
//                Object result = readMethod.invoke(bean, new Object[0]);
//                if (result != null) {
//                    returnMap.put(propertyName, result);
//                } else {
//                    returnMap.put(propertyName, "");
//                }
//            }
//        }
//        Document document = new Document();
//        for (Map.Entry<String, Object> entry : returnMap.entrySet()) {
//            //System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
//            document.append(entry.getKey(), entry.getValue());
//
//        }
//        return document;
//    }
//
//
//    /**
//     * 将 Map对象转化为JavaBean   此方法已经测试通过
//     *
//     * @param
//     * @param map
//     * @return Object对象
//     * @author wyply115
//     * @version 2016年3月20日 11:03:01
//     */
//    public static <T> T convertMap2Bean(Map map, Class T) throws Exception {
//        if (map == null || map.size() == 0) {
//            return null;
//        }
//        BeanInfo beanInfo = Introspector.getBeanInfo(T);
//        T bean = (T) T.newInstance();
//        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
//        for (int i = 0, n = propertyDescriptors.length; i < n; i++) {
//            PropertyDescriptor descriptor = propertyDescriptors[i];
//            String propertyName = descriptor.getName();
//            String upperPropertyName = propertyName.toUpperCase();
//            if (map.containsKey(upperPropertyName)) {
//                Object value = map.get(upperPropertyName);
//                //这个方法不会报参数类型不匹配的错误。
//                BeanUtils.copyProperty(bean, propertyName, value);
////用这个方法对int等类型会报参数类型不匹配错误，需要我们手动判断类型进行转换，比较麻烦。
////descriptor.getWriteMethod().invoke(bean, value);
////用这个方法对时间等类型会报参数类型不匹配错误，也需要我们手动判断类型进行转换，比较麻烦。
////BeanUtils.setProperty(bean, propertyName, value);
//            }
//        }
//        return bean;
//    }
//
//
//    /**
//     * 将Map对象通过反射机制转换成Bean对象
//     *
//     * @param map 存放数据的map对象
//     * @param T   待转换的class
//     * @return 转换后的Bean对象
//     * @throws Exception 异常
//     */
//    public static <T> T mapToObject(Map map, Class T) throws Exception {
//        if (map == null || map.size() == 0) {
//            return null;
//        }
//        BeanInfo beanInfo = Introspector.getBeanInfo(T);
//        T bean = (T) T.newInstance();
//        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
//        for (int i = 0, n = propertyDescriptors.length; i < n; i++) {
//            PropertyDescriptor descriptor = propertyDescriptors[i];
//            String propertyName = descriptor.getName();
//            String upperPropertyName = propertyName.toUpperCase();
//            if (map.containsKey(propertyName)) {
//                Object value = map.get(propertyName);
//                //BeanUtils.getProperty(bean,propertyName);
//                //这个方法不会报参数类型不匹配的错误。
//                try {
//                    if (!StringUtils.isEmpty(value)) {
//                        BeanUtils.copyProperty(bean, propertyName, value);
//                    }
//                } catch (ConversionException c) {
//                    //时间格式转换异常
//                    SimpleDateFormat sdf1 = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
//                    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                    Date vd = sdf1.parse(String.valueOf(value));
//                    value = sdf2.format(sdf1.parse(String.valueOf(value)));
//                    BeanUtils.copyProperty(bean, propertyName, vd);
//                } catch (Exception e) {
//                    System.out.println("===========================================>转换异常" + e);
//                }
//
//
////用这个方法对int等类型会报参数类型不匹配错误，需要我们手动判断类型进行转换，比较麻烦。
//                //descriptor.getWriteMethod().invoke(bean, value);
////用这个方法对时间等类型会报参数类型不匹配错误，也需要我们手动判断类型进行转换，比较麻烦。
//                //BeanUtils.setProperty(bean, propertyName, value);
//            }
//        }
//        return bean;
//    }
//
//    /**
//     * 将Object类型的值，转换成bean对象属性里对应的类型值
//     *
//     * @param value          Object对象值
//     * @param fieldTypeClass 属性的类型
//     * @return 转换后的值
//     */
//    private static Object convertValType(Object value, Class<?> fieldTypeClass) {
//        Object retVal = null;
//        if (Long.class.getName().equals(fieldTypeClass.getName())
//                || long.class.getName().equals(fieldTypeClass.getName())) {
//            retVal = Long.parseLong(value.toString());
//        } else if (Integer.class.getName().equals(fieldTypeClass.getName())
//                || int.class.getName().equals(fieldTypeClass.getName())) {
//            retVal = Integer.parseInt(value.toString());
//        } else if (Float.class.getName().equals(fieldTypeClass.getName())
//                || float.class.getName().equals(fieldTypeClass.getName())) {
//            retVal = Float.parseFloat(value.toString());
//        } else if (Double.class.getName().equals(fieldTypeClass.getName())
//                || double.class.getName().equals(fieldTypeClass.getName())) {
//            retVal = Double.parseDouble(value.toString());
//        } else {
//            retVal = value;
//        }
//        return retVal;
//    }
//
//    /**
//     * 获取指定字段名称查找在class中的对应的Field对象(包括查找父类)
//     *
//     * @param clazz     指定的class
//     * @param fieldName 字段名称
//     * @return Field对象
//     */
//    private static Field getClassField(Class<?> clazz, String fieldName) {
//        if (Object.class.getName().equals(clazz.getName())) {
//            return null;
//        }
//        Field[] declaredFields = clazz.getDeclaredFields();
//        for (Field field : declaredFields) {
//            if (field.getName().equals(fieldName)) {
//                return field;
//            }
//        }
//
//        Class<?> superClass = clazz.getSuperclass();
//        if (superClass != null) {// 简单的递归一下
//            return getClassField(superClass, fieldName);
//        }
//        return null;
//    }
//
//    public static Map<?, ?> objectToMap(Object obj) {
//        if (obj == null)
//            return null;
//
//        return new org.apache.commons.beanutils.BeanMap(obj);
//    }
//
//
//    public static boolean isValidDate(String str) {
//        try {
//            if (str.matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.\\d{3}Z")) {
//                return true;
//            }
//        } catch (Exception e) {
//            return false;
//        }
//        return  false;
//    }
//
//
//    /**
//     * 关闭Mongodb
//     */
//    public static void close() {
//        if (mongoClient != null) {
//            mongoClient.close();
//            mongoClient = null;
//        }
//    }
//
//    //遍历结果
//    public static void traverseResult(MongoCollection<Document> list) {
//        //检索所有文档
//        /**
//         * 1. 获取迭代器FindIterable<Document>
//         * 2. 获取游标MongoCursor<Document>
//         * 3. 通过游标遍历检索出的文档集合
//         * */
//        FindIterable<Document> findIterable = list.find();
//        MongoCursor<Document> mongoCursor = findIterable.iterator();
//        Integer sort = 0;
//        while (mongoCursor.hasNext()) {
//            sort++;
//            //System.out.println("----------------------------------------------->" + sort);
//            Document document = mongoCursor.next();
//            //System.out.println(document);
//            //System.out.println(document.get("_id"));
//            //JsonUtils.json2Obj(mongoCursor.next().toJson(), Map.class);
//            //System.out.println("----------------------------------------------->" + sort);
//        }
//    }
//
//
//    public static Date fromISODate(String time) {
//        if (!time.matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.\\d{3}Z")) {
//            return null;
//        }
//        time = time.replaceFirst("T", " ").replaceFirst(".\\d{3}Z", "");
//        Date date = formatDate(time);
//        Calendar ca = Calendar.getInstance();
//        ca.setTime(date);
//        ca.add(Calendar.HOUR_OF_DAY, 8);
//        return ca.getTime();
//    }
//
//    public static Date formatDate(String time) {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        try {
//            return sdf.parse(time);
//        } catch (ParseException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//
//    public static void main(String[] args) {
//
//        MongoCollection<Document> list = getCollection("Chairman", "user");
//        //System.out.println(list.count());
//        Document d = list.find().first();
//        //System.out.println("userid====" + d.get("userid"));
//        //System.out.println(d);
//    }
//}
