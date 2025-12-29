package com.tiagoportilho.ServEasy.util;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Aspect para logging estruturado de operações nos controllers e services.
 * Registra automaticamente entrada, saída e tempo de execução dos métodos.
 */
@Aspect
@Component
@Slf4j
public class LoggingAspect {

    /**
     * Pointcut para todos os métodos dos controllers REST.
     */
    @Pointcut("within(com.tiagoportilho.ServEasy.controller.api..*)")
    public void controllerMethods() {}

    /**
     * Pointcut para todos os métodos dos services.
     */
    @Pointcut("within(com.tiagoportilho.ServEasy.service..*)")
    public void serviceMethods() {}

    /**
     * Log para métodos dos controllers.
     */
    @Around("controllerMethods()")
    public Object logControllerMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        return logMethodExecution(joinPoint, "CONTROLLER");
    }

    /**
     * Log para métodos dos services.
     */
    @Around("serviceMethods()")
    public Object logServiceMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        return logMethodExecution(joinPoint, "SERVICE");
    }

    private Object logMethodExecution(ProceedingJoinPoint joinPoint, String layer) throws Throwable {
        String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        // Log de entrada
        log.debug("[{}] Iniciando {}.{}() com argumentos: {}", 
                layer, className, methodName, formatArgs(args));

        long startTime = System.currentTimeMillis();

        try {
            Object result = joinPoint.proceed();
            long duration = System.currentTimeMillis() - startTime;

            // Log de sucesso
            log.debug("[{}] Finalizado {}.{}() em {}ms", 
                    layer, className, methodName, duration);

            // Log de performance se demorou mais que 1 segundo
            if (duration > 1000) {
                log.warn("[{}] SLOW EXECUTION: {}.{}() levou {}ms", 
                        layer, className, methodName, duration);
            }

            return result;
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;

            // Log de erro
            log.error("[{}] Erro em {}.{}() após {}ms: {}", 
                    layer, className, methodName, duration, e.getMessage());

            throw e;
        }
    }

    private String formatArgs(Object[] args) {
        if (args == null || args.length == 0) {
            return "[]";
        }
        
        return Arrays.stream(args)
                .map(arg -> {
                    if (arg == null) return "null";
                    String str = arg.toString();
                    // Trunca argumentos muito longos
                    return str.length() > 100 ? str.substring(0, 100) + "..." : str;
                })
                .toList()
                .toString();
    }
}
