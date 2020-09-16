package com.xiaohei.java.lib.socket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Header {
    public Map<String, List<String>> headerMap = new HashMap<>();

    public Header() {
    }

    public Header addHeader(String key, String value) {
       if(headerMap.containsKey(key)){
           headerMap.get(key).add(value);
           return this;
       }
        List<String> list = new ArrayList<>();
        list.add(value);
        headerMap.put(key, list);
       return this;
    }

    @Override
    public String toString() {
        if(headerMap.size()>0){
            StringBuffer sb=new StringBuffer();
            for (String key:headerMap.keySet()){
                sb.append(key).append("\n\t");
                for (String value:headerMap.get(key)){
                    sb.append(value).append("\t");
                }

                sb.append("\n");
            }
            return sb.toString();
        }
        return super.toString();
    }
}
