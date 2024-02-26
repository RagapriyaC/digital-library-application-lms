package com.raga.library.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * This class is responsible for logging method entry, method return and
 * exceptions in the specified packages such as controller, service and
 * repository
 */
@Aspect
@Component
public class LoggingAspect {

	private final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

	/**
	 * This Logs before entering a method
	 *
	 * @param joinPoint
	 */
	@Before("execution(* com.raga.maids.cc.library.controller.*.*(..)) || "
			+ "execution(* com.raga.maids.cc.library.service.*.*(..)) || "
			+ "execution(* com.raga.maids.cc.library.repository.*.*(..))")
	public void logBeforeEnteringMethod(JoinPoint joinPoint) {
		logger.info("Entering method : {}", joinPoint.getSignature().toShortString());
	}

	/**
	 * This Logs after a method has successfully returned
	 *
	 * @param joinPoint
	 * @param result
	 */
	@AfterReturning(pointcut = "execution(* com.raga.maids.cc.library.controller.*.*(..)) || "
			+ "execution(* com.raga.maids.cc.library.service.*.*(..)) || "
			+ "execution(* com.raga.maids.cc.library.repository.*.*(..))", returning = "result")
	public void logAfterReturningMethod(JoinPoint joinPoint, Object result) {
		logger.info("Method {} has returned : {}", joinPoint.getSignature().toShortString(), result);
	}

	/**
	 * This Logs after an exception is thrown in a method
	 * 
	 * @param joinPoint
	 * @param exception
	 */
	@AfterThrowing(pointcut = "execution(* com.raga.maids.cc.library.controller.*.*(..)) || "
			+ "execution(* com.raga.maids.cc.library.service.*.*(..)) || "
			+ "execution(* com.raga.maids.cc.library.repository.*.*(..))", throwing = "exception")
	public void logAfterThrowingException(JoinPoint joinPoint, Throwable exception) {
		logger.error("Exception occured in {} : {}", joinPoint.getSignature().toShortString(), exception.getMessage());
	}
}
