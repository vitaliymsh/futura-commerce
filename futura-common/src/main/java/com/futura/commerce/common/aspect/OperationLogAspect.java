package com.futura.commerce.common.aspect;

import com.futura.commerce.common.annotation.OperationLog;
import com.futura.commerce.common.service.OperationLogService;
import com.futura.commerce.mbg.model.SysOperationLog;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * AOP aspect for recording global operation logs across modules
 *
 * @author Vitalii
 */
@Aspect
@Component
public class OperationLogAspect {

    @Autowired(required = false)
    private OperationLogService operationLogService;

    @Pointcut("@annotation(com.futura.commerce.common.annotation.OperationLog)")
    public void operationLogPointcut() {
    }

    @Around("operationLogPointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = null;
        try {
            result = joinPoint.proceed();
        } finally {
            saveOperationLog(joinPoint, result);
        }
        return result;
    }

    private void saveOperationLog(ProceedingJoinPoint joinPoint, Object result) {
        if (operationLogService == null) {
            return;
        }

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        OperationLog operationLogAnnotation = method.getAnnotation(OperationLog.class);
        if (operationLogAnnotation == null) {
            return;
        }

        SysOperationLog operationLog = new SysOperationLog();
        operationLog.setModule(operationLogAnnotation.module());
        operationLog.setOperationType(operationLogAnnotation.operationType());
        operationLog.setContent(operationLogAnnotation.content());
        operationLog.setCreateTime(LocalDateTime.now());

        String businessIdParam = operationLogAnnotation.businessIdParam();
        if (StringUtils.hasText(businessIdParam)) {
            String businessId = getBusinessIdFromParams(joinPoint, businessIdParam);
            operationLog.setBusinessId(businessId);
        }

        operationLog.setOperator(getCurrentOperator());

        operationLogService.save(operationLog);
    }

    private String getBusinessIdFromParams(JoinPoint joinPoint, String businessIdParam) {
        Object[] args = joinPoint.getArgs();
        String[] parameterNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
        Map<String, Object> paramMap = new HashMap<>();

        if (parameterNames != null) {
            for (int i = 0; i < parameterNames.length && i < args.length; i++) {
                paramMap.put(parameterNames[i], args[i]);
            }
        }

        Object businessIdObj = paramMap.get(businessIdParam);
        return businessIdObj != null ? businessIdObj.toString() : null;
    }

    private String getCurrentOperator() {
        return "admin";
    }
}
