package com.sky.Aspect;

import com.sky.annotation.AutoFill;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Aspect
@Component
@Slf4j
public class AutoFillAspect {

    /**
     * 公共字段自动填充切入点
     */
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void autoFillPointCut() {}

    /**
     * 公共字段自动填充
     */
    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint) throws Throwable {
        log.info("公共字段自动填充通知开始");
        MethodSignature signature = (MethodSignature)joinPoint.getSignature();
        AutoFill autoFill= signature.getMethod().getAnnotation(AutoFill.class);
        // 获取数据库操作类型
        Enum operationType = autoFill.value();
        // 从ThreadLocal中获取当前登录用户的id
        Long currentId = BaseContext.getCurrentId();
        // 获取当前时间
        LocalDateTime now = LocalDateTime.now();
        // 从joinPoint中获取参数
        Object[] args = joinPoint.getArgs();
        if(args==null || args.length==0){
            return;
        }
        // 从参数中获取实体对象
        Object entity = args[0];
        // 调用实体对象的方法，设置创建时间、创建人、更新时间、更新人
        if(operationType==OperationType.INSERT){
            try{
                Method setCreateTime = entity.getClass().getDeclaredMethod("setCreateTime", LocalDateTime.class);
                Method setUpdateTime = entity.getClass().getDeclaredMethod("setUpdateTime", LocalDateTime.class);
                Method setCreateUser = entity.getClass().getDeclaredMethod("setCreateUser", Long.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod("setUpdateUser", Long.class);
                setCreateTime.invoke(entity, now);
                setUpdateTime.invoke(entity, now);
                setCreateUser.invoke(entity, currentId);
                setUpdateUser.invoke(entity, currentId);
            }catch (Exception e){
                log.error("公共字段自动填充通知异常", e);
            }
        }else if(operationType==OperationType.UPDATE){
            try{
                Method setUpdateTime = entity.getClass().getDeclaredMethod("setUpdateTime", LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod("setUpdateUser", Long.class);
                setUpdateTime.invoke(entity, now);
                setUpdateUser.invoke(entity, currentId);
            }catch (Exception e){
                log.error("公共字段自动填充通知异常", e);
            }
        }

    }
}
