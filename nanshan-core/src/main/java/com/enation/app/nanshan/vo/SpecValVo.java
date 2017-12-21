package com.enation.app.nanshan.vo;

/**
 * Created by yulong on 17/12/20.
 */
public class SpecValVo {

    private long id;

    private String name;

    private boolean selected;

    public SpecValVo() {
    }

    public SpecValVo(long id, String name, boolean selected) {
        this.id = id;
        this.name = name;
        this.selected = selected;
    }

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

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
