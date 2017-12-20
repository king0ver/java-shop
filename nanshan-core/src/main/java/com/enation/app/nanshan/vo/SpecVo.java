package com.enation.app.nanshan.vo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yulong on 17/12/20.
 */
public class SpecVo implements Serializable{

    private long id;

    private String name;

    private List<SpecValVo> specValVos;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SpecValVo> getSpecValVos() {
        return specValVos;
    }

    public void setSpecValVos(List<SpecValVo> specValVos) {
        this.specValVos = specValVos;
    }

}
