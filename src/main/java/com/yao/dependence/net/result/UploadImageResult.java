package com.yao.dependence.net.result;


/**
 * 上传图片的结果
 * Created by huichuan on 16/4/20.
 */
public class UploadImageResult {
    public String img_url;

    //图片本地地址
    public String imageFilePath;
    //图片压缩后的本地地址
    public String imageCompressFilePath;



    @Override
    public String toString() {
        return "UploadImageResult{" +
                "img_url='" + img_url + '\'' +
                ", imageFilePath='" + imageFilePath + '\'' +
                '}';
    }
}
