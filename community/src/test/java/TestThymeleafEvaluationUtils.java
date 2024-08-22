import org.thymeleaf.util.EvaluationUtils;

import java.math.BigDecimal;

public class TestThymeleafEvaluationUtils {
    public static void main(String[] args) {
        BigDecimal bigDecimal = EvaluationUtils.evaluateAsNumber(3/2);
        System.out.println(bigDecimal);
        int i = bigDecimal.intValue();
        System.out.println(i);
    }
}
