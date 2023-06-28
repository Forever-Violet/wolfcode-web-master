package cn.wolfcode.web.modules.custContractInfo.validateUtils;

import cn.wolfcode.web.modules.custContractInfo.validateUtils.validateAnnotation.ValidNumber;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NumberValidator implements ConstraintValidator<ValidNumber, String> {
    private String message;
    @Override
    public void initialize(ValidNumber constraintAnnotation) {
        // 初始化方法，可以在这里获取验证注解的属性值
        message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // 允许为空
        }

        String doubleString = String.valueOf(value);
        boolean isValid = doubleString.matches("\\d+(\\.\\d+)?");

        if (!isValid) {
            // 自定义错误信息
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
        }

        return isValid;
    }
}
