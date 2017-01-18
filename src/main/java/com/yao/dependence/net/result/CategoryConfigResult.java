package com.yao.dependence.net.result;

import com.yao.dependence.model.Category;

import java.util.List;

/**
 *
 * Created by huichuan on 16/4/14.
 */
public class CategoryConfigResult {

    private String check_code;
    public List<Category> category_list;


    @Override
    public String toString() {
        return "CategoryCofig{" +
                "check_code='" + check_code + '\'' +
                ", category_list=" + category_list +
                '}';
    }
}
