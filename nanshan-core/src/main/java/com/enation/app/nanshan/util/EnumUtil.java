package com.enation.app.nanshan.util;

import com.enation.app.nanshan.constant.DefaultArticleIdEnum;

public class EnumUtil {
	
	public static boolean isInclude(int id ){
        boolean include = false;
        for (DefaultArticleIdEnum e: DefaultArticleIdEnum.values()){
            if(e.getId()==id){
                include = true;
                break;
            }
        }
        return include;
    }

}
