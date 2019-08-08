package com.wxrem.controller;

import com.wxrem.common.QRCodeUtil;

/**
 * 微信二维码生成test
 *
 *
 */
public class TestEwmController {

    public static void main(String[] args) throws Exception {
        // 存放在二维码中的内容
        String text = "微信二维码11111";
        // 嵌入二维码的图片路径
        String imgPath = "E:\\acWork\\work\\wxImage\\qrImage.jpg";
        // 生成的二维码的路径及名称
        String destPath = "E:\\acWork\\work\\wxImage\\scImage.jpg";
        //生成二维码
        QRCodeUtil.encode(text, imgPath, destPath, true);
//        QRCodeUtil.encode(text,null, destPath, true);
        // 解析二维码
        String str = QRCodeUtil.decode(destPath);
        // 打印出解析出的内容
        System.out.println("解析二维码信息：" + str);


    }

}
