package com.yao.dependence.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.types.LongObjectType;
import com.j256.ormlite.table.DatabaseTable;

import java.util.UUID;

/**
 *
 * Created by huichuan on 16/4/14.
 */
@DatabaseTable
public class Category {

    @DatabaseField(id = true)
    int id;
    @DatabaseField
    String name;//: "语言达人",
    @DatabaseField
    String icon;//: "http://tp4.sinaimg.cn/1805982651/50/40080855140/1"

    public Category(){}

    public Category(int id,String name,String icon){
        this.id = id;
        this.name = name;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public String getIcon() {
        return icon;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", icon='" + icon + '\'' +
                '}';
    }
}
