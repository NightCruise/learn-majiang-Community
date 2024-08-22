package life.community.util;

public class ObjectUtils {

    public static int roundObjectToInteger(Object o) {
        if (o == null) {
            return 1; // 默认值或抛出异常
        }

        try {
            float value = Float.parseFloat(String.valueOf(o));
            if (value < 1) {
                return 1; // 确保返回的页码至少为1
            }
            return Math.round(value);
        } catch (NumberFormatException e) {
            // 处理无效数字的情况，可以返回默认值
            return 1;
        }
    }

}
