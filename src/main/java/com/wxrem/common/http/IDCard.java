package com.wxrem.common.http;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author wxb
 * 身份证号解析demo
 *
 * 根据《中华人民共和国国家标准GB 11643-1999》中有关公民身份号码的规定，
 * 公民身份号码是特征组合码，由十七位数字本体码和一位数字校验码组成。排列顺序从左至右依次为：
 * 六位数字地址码，八位数字出生日期码，三位数字顺序码和一位数字校验码。
 * 顺序码的奇数分给男性，偶数分给女性。校验码是根据前面十七位数字码，
 * 按照ISO 7064:1983.MOD 11-2校验码计算出来的检验码。
 *
 * 最后一位：18位数据计算规则：
 * 居民身份证是国家法定的证明公民个人身份的有效证件．身份证号码由十七位数字本体码和一位数字校验码组成．
 * 第1-6位是地址码，第7-14位是出生日期码，第15-17位是顺序码，即是县、区级政府所辖派出所的分配码．
 * 第18位也就是最后一位是数字校验码，是根据前面十七位数字码，按一定规则计算出来的校验码．算法如下：
 * 规定第1-17位对应的系数分别为：7，9，10，5，8，4，2，1，6，3，7，9，10，5，8，4，2．
 * 将身份证号码的前17位数字分别乘以对应的系数，再把积相加．相加的结果除以11，求出余数．
 * 余数只可能有0，1，2，3，4，5，6，7，8，9，10这11种情况．其分别对应身份证号码的第18位数字如表所示．
 * 余数	0	1	2	3	4	5	6	7	8	9	10
 * 第18位	1	0	x	9	8	7	6	5	4	3	2
 * 通过上面得知如果余数是3，则身份证的第18位数字就是9．如果余数是2，则身份证的第18位号码就是x．若
 * 某人的身份证号码的前17位依次是11010219600302011，则他身份证号码的第18位数字是3．
 * @date 2020-08-10 16:13
 */
public class IDCard {
    static List<String> allLines = null;
    static {
        try {
            File directory = new File("src/main/resources/身份证地址码对照表.txt");
            String courseFile = directory.getCanonicalPath();
            Path p = Paths.get(courseFile);
            allLines = Files.readAllLines(p);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    /** 中国公民身份证号码最小长度。 */
    public  final int CHINA_ID_MIN_LENGTH = 15;

    /** 中国公民身份证号码最大长度。 */
    public  final int CHINA_ID_MAX_LENGTH = 18;

    /**
     * 根据身份编号获取年龄
     *
     * @param idCard
     *            身份编号
     * @return 年龄
     */
    public static int getAgeByIdCard(String idCard) {
        int iAge = 0;
        Calendar cal = Calendar.getInstance();
        String year = idCard.substring(6, 10);
        int iCurrYear = cal.get(Calendar.YEAR);
        iAge = iCurrYear - Integer.valueOf(year);
        return iAge;
    }

    /**
     * 根据身份编号获取生日
     *
     * @param idCard 身份编号
     * @return 生日(yyyyMMdd)
     */
    public static String getBirthByIdCard(String idCard) {
        return idCard.substring(6, 14);
    }

    /**
     * 根据身份编号获取生日年
     *
     * @param idCard 身份编号
     * @return 生日(yyyy)
     */
    public static Short getYearByIdCard(String idCard) {
        return Short.valueOf(idCard.substring(6, 10));
    }

    /**
     * 根据身份编号获取生日月
     *
     * @param idCard
     *            身份编号
     * @return 生日(MM)
     */
    public static Short getMonthByIdCard(String idCard) {
        return Short.valueOf(idCard.substring(10, 12));
    }

    /**
     * 根据身份编号获取生日天
     *
     * @param idCard
     *            身份编号
     * @return 生日(dd)
     */
    public static Short getDateByIdCard(String idCard) {
        return Short.valueOf(idCard.substring(12, 14));
    }

    /**
     * 根据身份编号获取性别
     *
     * @param idCard 身份编号
     * @return 性别(M-男，F-女，N-未知)
     */
    public static String getGenderByIdCard(String idCard) {
        String sGender = "未知";
        String sCardNum = idCard.substring(16, 17);
        if (Integer.parseInt(sCardNum) % 2 != 0) {
            sGender = "1";//男
        } else {
            sGender = "2";//女
        }
        return sGender.equals("1")?"男":"女";
    }

    public static String getAddressByCardId(String idCard){
        String sAddress = "未知";
        String sCardNum = idCard.substring(0,6);
        Optional<String> firstValue = allLines.stream().filter(line -> line.split(" ")[0].equals(sCardNum)).findFirst();
        if (firstValue.isPresent()){
            String lineStr= firstValue.get();
            if (!lineStr.equals("")) {
                sAddress = lineStr.split(" ")[1];
            }
        }
        return sAddress;
    }

    public static  void  main(String [] a){
        String idcard="610203199209275127";
        String sr = getBirthByIdCard(idcard);
        System.out.println("生日:" + sr);
        System.out.println("所属地：" + getAddressByCardId(idcard));
        String sex= getGenderByIdCard(idcard);
        System.out.println("性别:" + sex);
        int age= getAgeByIdCard(idcard);
        System.out.println("年龄:" + age);
        Short nian=getYearByIdCard(idcard);
        Short yue=getMonthByIdCard(idcard);
        Short ri=getDateByIdCard(idcard);
        System.out.print(nian+"年"+yue+"月"+ri+"日");


    }

}
