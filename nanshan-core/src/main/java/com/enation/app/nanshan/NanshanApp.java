package com.enation.app.nanshan;

import com.enation.eop.resource.model.EopSite;
import com.enation.eop.sdk.App;
import org.springframework.stereotype.Service;

/**
 * Created by yulong on 17/12/9.
 */
@Service("nanshan")
public class NanshanApp extends App {


    public NanshanApp(){
        super();

        tables.add("nanshan_article_category");
        tables.add("nanshan_article");
        tables.add("nanshan_clob");




        System.out.println("----------------------12345------------------------");
    }


    @Override
    public void install() {
        System.out.println("-------------------南山项目安装---------------------");


        /**
         * 安装网店数据库表结构
         */
        this.doInstall("file:com/enation/app/nanshan/nanshan.xml");

        /**
         * 安装网店数据库索引
         */
        this.doInstall("file:com/enation/app/nanshan/nanshan_index.xml");
    }

    @Override
    public void sessionDestroyed(String sessionid, EopSite site) {

    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public String getNameSpace() {
        return null;
    }
}
