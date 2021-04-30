package com.sync.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * @Description:
 * @Author: Yan XinYu
 * @Date: 2021-04-13 14:52
 */
@Slf4j
public class DateUtilTest {

    public static void main(String[] args) {

        DateUtil du = new DateUtil();
        System.out.println("获取当天日期:" + du.getNowTime("yyyy-MM-dd"));
        System.out.println("获取本周一日期:" + du.getMondayOFWeek());
        System.out.println("获取本周日的日期~:" + du.getCurrentWeekday());
        System.out.println("获取上周一日期:" + du.getPreviousWeekday());
        System.out.println("获取上周日日期:" + du.getPreviousWeekSunday());
        System.out.println("获取下周一日期:" + du.getNextMonday());
        System.out.println("获取下周日日期:" + du.getNextSunday());
        System.out.println("获得相应周的周六的日期:" + du.getNowTime("yyyy-MM-dd"));
        System.out.println("获取本月第一天日期:" + du.getFirstDayOfMonth());
        System.out.println("获取本月最后一天日期:" + du.getDefaultDay());
        System.out.println("获取上月第一天日期:" + du.getPreviousMonthFirst());
        System.out.println("获取上月最后一天的日期:" + du.getPreviousMonthEnd());
        System.out.println("获取下月第一天日期:" + du.getNextMonthFirst());
        System.out.println("获取下月最后一天日期:" + du.getNextMonthEnd());
        System.out.println("获取本年的第一天日期:" + du.getCurrentYearFirst());
        System.out.println("获取本年最后一天日期:" + du.getCurrentYearEnd());
        System.out.println("获取去年的第一天日期:" + du.getPreviousYearFirst());
        System.out.println("获取去年的最后一天日期:" + du.getPreviousYearEnd());
        System.out.println("获取明年第一天日期:" + du.getNextYearFirst());
        System.out.println("获取明年最后一天日期:" + du.getNextYearEnd());
        System.out.println("获取本季度第一天到最后一天:" + du.getThisSeasonTime(11));
        System.out.println("获取本季度第一天:" + du.getThisSeasonTime(DateUtil.getToMonth()).split(";"));
        System.out.println("获取两个日期之间间隔天数2008-12-1~2008-9.29:" + DateUtil.getTwoDay("2008-12-1", "2008-9-29"));

        String birth = "2008-02-31";
        Date tomorrow = DateUtil.getTomorrow();
        Date now = new Date();
        System.out.println("当前天:" + DateUtil.DateToString(now, DateUtil.LONG_DATE_FORMAT));
        System.out.println(tomorrow.after(now));
        log.debug(DateUtil.getAstro(birth));
    }

}