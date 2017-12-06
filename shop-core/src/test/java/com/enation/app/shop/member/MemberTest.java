package com.enation.app.shop.member;

import com.enation.app.base.core.model.Member;
import com.enation.app.shop.member.service.IMemberManager;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.test.SpringTestSupport;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by kingapex on 2017/10/31.
 *
 * @author kingapex
 * @version 1.0
 * @since 6.4.0
 * 2017/10/31
 */
public class MemberTest extends SpringTestSupport{

    @Autowired
    private IMemberManager memberManager;
    @Test
    public void testRegister(){


        for ( int i=0;i<1000;i++){
            String username ="user"+i;
            String pwd = "111111";
            String email = "user" + i + "@test.com";
            Member member = new Member();
            HttpServletRequest request = ThreadContextHolder.getHttpRequest();

            member.setUname(username);
            member.setName(username);
            member.setPassword(pwd);
            member.setEmail(email);
            member.setRegisterip("127.0.0.1");
            boolean result = this.memberManager.register(member);
            System.out.println(username);
        }


    }
}
