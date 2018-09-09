package com.java.wangyihan.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tools {

    public static boolean isURL(String str){
        //转换为小写
        str = str.toLowerCase();
        String regex = "^((https|http|ftp|rtsp|mms)?://)"
                + "?(([0-9a-z_!~*'().&=+$%-]+: )?[0-9a-z_!~*'().&=+$%-]+@)?" //ftp的user@
                + "(([0-9]{1,3}\\.){3}[0-9]{1,3}" // IP形式的URL- 199.194.52.184
                + "|" // 允许IP和DOMAIN（域名）
                + "([0-9a-z_!~*'()-]+\\.)*" // 域名- www.
                + "([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]\\." // 二级域名
                + "[a-z]{2,6})" // first level domain- .com or .museum
                + "(:[0-9]{1,4})?" // 端口- :80
                + "((/?)|" // a slash isn't required if there is no file name
                + "(/[0-9a-z_!~*'().;?:@&=+$,%#-]+)+/?)$";
        return str.matches(regex);


    }

    public static String deHtml(String src)
    {
        Pattern re = Pattern.compile("<([^<>]*)>");
        Matcher m = re.matcher(src);

        int start = 0, end = 0;
        String ans ="";
        if (!m.find())
            return src;
        m.reset();
        while (m.find())
        {
            end = m.start();
            ans += src.substring(start, end);
            //System.out.print(src.substring(start, end));
            start = m.end();
        }

        ans = ans.replace("&nbsp", " ");
        return ans;
    }

}
