package ubersystem.logger;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ubersystem.Enums.LogLevel;
import ubersystem.Result.Result;
import ubersystem.service.LogService;

@Component
@Aspect
@Slf4j
public class LogAspect {

    @Autowired
    private LogService logService;

    @Before("execution(* ubersystem.controller.*.*(..))")
    public void logControllerBefore(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        String name = signature.getName(); // get method name in the controller
        String className = signature.getDeclaringTypeName(); // get class name
        //get last layer of class name
        className = className.substring(className.lastIndexOf(".") + 1);
        logService.log(className, LogLevel.INFO, name);
    }

    @AfterReturning(value = "execution(* ubersystem.controller.*.*(..))", returning = "result")
    public void logControllerAfterReturning(JoinPoint joinPoint, Object result) {
        Signature signature = joinPoint.getSignature();
        String name = signature.getName(); // get method name in the controller
        String className = signature.getDeclaringTypeName(); // get class name
        //get last layer of class name
        className = className.substring(className.lastIndexOf(".") + 1);
        LogLevel level = LogLevel.INFO;
        try {
            result = ((Result<?>) result).getStatus();
            if (result.equals("200")) {
                level = LogLevel.INFO;
            } else if (result.equals("400")) {
                level = LogLevel.WARNING;
            } else {
                level = LogLevel.ERROR;
            }
            logService.log(className, level, name + " : " + result);
        } catch (Exception e) {
            level = LogLevel.ERROR;
            logService.log(className, level, "Error in " + name + " : " + "returning value is not Result");
        }
    }

    @AfterThrowing(value = "execution(* ubersystem.service.*.*(..))", throwing = "e")
    public void afterThrowingService(JoinPoint joinPoint, Exception e) {
        Signature signature = joinPoint.getSignature();
        String name = signature.getName();
        String className = signature.getDeclaringTypeName();
        className = className.substring(className.lastIndexOf(".") + 1);
        logService.log(className, LogLevel.ERROR, name + " : " + e.getMessage());
    }
}
