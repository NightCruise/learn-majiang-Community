package life.community.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

public class DataUtils {
    public static boolean isYesterday(long timestamp) {
        // 获取当前时区的今天日期
        LocalDate today = LocalDate.now();
        // 获取昨天的日期
        LocalDate yesterday = today.minusDays(1);
        // 将时间戳转换为当前时区的 LocalDate
        LocalDate date = Instant.ofEpochMilli(timestamp)
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        // 判断是否为昨天
        return date.isEqual(yesterday);
    }

    public static boolean isToday(long timestamp) {
        // 获取当前时区的今天日期
        LocalDate today = LocalDate.now();
        // 将时间戳转换为当前时区的 LocalDate
        LocalDate date = Instant.ofEpochMilli(timestamp)
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        // 判断是否为昨天
        return date.isEqual(today);
    }
}
