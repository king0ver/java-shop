package com.enation.app.nanshan.tag;

import com.enation.app.nanshan.service.IArticleService;
import com.enation.app.nanshan.vo.ArticleVo;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.Page;
import com.enation.framework.util.DateUtil;
import freemarker.template.TemplateModelException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yulong on 17/12/21.
 */
@Component("articleListPageTag")
public class ArticleListPageTag extends AbstractPageTag{

    @Autowired
    private IArticleService articleService;

    @Override
    protected Object exec(Map params) throws TemplateModelException {
        Map<String, Object> map = new HashMap<>();

        HttpServletRequest request  = ThreadContextHolder.getHttpRequest();
        String catId  = request.getParameter("catId");
        if(StringUtils.isBlank(catId) && params.containsKey("catId")){
            catId = params.get("catId").toString();
        }

        String specs  = request.getParameter("specs");

        handlePageCat(map, catId);

        int pageNo = getPage();


        boolean isFree = false;

        long startTime = 0l;

        long endTime = 0l;

        if(isWap()){

            if("38".equals(catId) || "39".equals(catId)){
                String free = request.getParameter("free");
                if(StringUtils.isNotBlank(free)){
                    isFree = true;
                    map.put("freeFlag", isFree);
                }
            }

            if("40".equals(catId) || "41".equals(catId)
                    || "42".equals(catId) || "43".equals(catId)){
                String month = request.getParameter("month");

                Date tmp;

                if(StringUtils.isBlank(month)){
                    tmp = new Date();
                }else{
                    tmp = DateUtil.toDate(month, "yyyy年MM月");
                }

                Date[] dates = getLastDate(tmp);

                startTime = dates[0].getTime()/1000;
                endTime = dates[1].getTime()/1000;

                map.put("month", DateUtil.toString(tmp,"yyyy年MM月"));
            }

        }

        Page<ArticleVo> webPage =articleService.querySpecInfoByCatId(Integer.parseInt(catId),
                specs,isFree, startTime, endTime, 1, getPageSize());

        webPage.setCurrentPageNo(pageNo);

        map.put("webPage", webPage);
        map.put("items", webPage.getResult());

        map.put("blockview", ThreadContextHolder.getHttpRequest().getParameter("blockview"));

        return map;
    }

    private Date[] getLastDate(Date d){



        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date firstDayOfMonth = calendar.getTime();
        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date lastDayOfMonth = calendar.getTime();

        return new Date[]{firstDayOfMonth, lastDayOfMonth};
    }





}
