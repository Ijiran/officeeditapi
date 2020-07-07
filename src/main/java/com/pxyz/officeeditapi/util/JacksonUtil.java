package com.pxyz.officeeditapi.util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pxyz.officeeditapi.bean.FileLockToken;

public class JacksonUtil {
    
    /**
     * 映射对象
     */
    private static ObjectMapper objMapper;
  
    /**
     * 获取映射对象
     * @author QLC
     * @return
     */
    public static ObjectMapper getObejctMapper() {
        if(objMapper == null){
            synchronized(JacksonUtil.class){
                objMapper = new ObjectMapper(); 
                objMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ"));//指定时间格式
            }
        }
        return objMapper;
    }

    /** 
     * Json字符串转对象 
     * @param <T> 
     * @param jsonStr Json字符串
     * @param clazz  类
     * @return 
     * @throws IOException 
     * @throws JsonMappingException 
     * @throws JsonParseException 
     * @throws Exception 
     */  
    public static synchronized  <T> T jsonToObject(String jsonStr, Class<T> clazz) throws JsonParseException, JsonMappingException, IOException{  
        ObjectMapper mapper = getObejctMapper();
        return mapper.readValue(jsonStr, clazz);  
    }  
  
    /** 
     * 对象转Json字符串 
     * @param bean 对象
     * @return 
     * @throws Exception 
     */  
    public static synchronized String objectToJson(Object bean) throws JsonProcessingException{  
        ObjectMapper mapper = getObejctMapper();
        return mapper.writeValueAsString(bean);  
    }  
    
    /**
     * 获取泛型的Collection Type
     * 
     * @param jsonStr json字符串
     * @param collectionClass 泛型的Collection
     * @param elementClasses 元素类型
     * @throws IOException 
     * @throws JsonMappingException 
     * @throws JsonParseException 
     */
    public static synchronized  <T> T jsonColectionToObject(String jsonStr,Class<T> collectionClass, Class<?>... elementClasses) 
            throws JsonParseException, JsonMappingException, IOException{
        ObjectMapper mapper = getObejctMapper();
        JavaType collection = mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
        return mapper.readValue(jsonStr, collection);
        
    }

    /**
     *
     * @author QLC
     * @param elementClasses
     * @return
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     */
    public static synchronized List jsonToList(Collection<?> col,Class<?> ... elementClasses)
            throws JsonParseException, JsonMappingException, IOException{
        List list = new ArrayList();
        if(null != col){
            list = jsonColectionToObject(objectToJson(col),List.class,elementClasses);
        }
        return list;
    }
    
    public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException {
        String json = "{\"S\":\"3e143af5-89aa-4ffd-95bc-77c819217f7d\",\"F\":4}";
//        String json = "{\"S\":\"3e143af5-89aa-4ffd-95bc-77c819217f7d\",\"F\":6,\"E\":2,\"M\":\"b79cbf6ad16e4cffb0f952e2332cd9ab\",\"P\":\"3ED247DA-5BF7-4071-A4F5-2855EC72C4E6\"}";
        FileLockToken token = JacksonUtil.jsonToObject(json, FileLockToken.class);
        System.out.println(token);
        System.out.println(JacksonUtil.objectToJson(token));
    }
    
}  