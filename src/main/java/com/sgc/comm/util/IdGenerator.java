package com.sgc.comm.util;

/**
 * 主键生成工具，
 * todo:使用snowflake等代替
 * Created by JimKan on 2017/4/12.
 */
public class IdGenerator {
    static SnowflakeIdWorker idWorker = new SnowflakeIdWorker(0, 0);

    public static String nextKey(){
        return  String.valueOf(idWorker.nextId());
        //return   UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static void main(String[] args) {
        IdGenerator keyGeneratorlService=new IdGenerator();
        for(int i=0;i<100;i++){
            System.out.println(  keyGeneratorlService.nextKey());
        }
    }
}
