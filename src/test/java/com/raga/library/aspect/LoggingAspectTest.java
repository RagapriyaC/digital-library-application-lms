package com.raga.library.aspect;

import static org.mockito.Mockito.when;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * Unit tests for the LoggingAspect class. These tests cover the logging aspect
 * functionality
 * 
 */
@ExtendWith({ SpringExtension.class, MockitoExtension.class })
public class LoggingAspectTest {

	@Mock
	private Logger logger;

	@Mock
	private JoinPoint joinPoint;

	@Mock
	private Signature signature;

	@InjectMocks
	private LoggingAspect loggingAspect;

	/**
	 * Test the logBeforeEnteringMethod method in LoggingAspect
	 * 
	 */
	@Test
	public void testLogBeforeEnteringMethod() {
		when(joinPoint.getSignature()).thenReturn(signature);
		when(signature.toShortString()).thenReturn("MockMethod");
		loggingAspect.logBeforeEnteringMethod(joinPoint);
	}

	/**
	 * Test the logAfterReturningMethod method in LoggingAspect
	 * 
	 */
	@Test
	public void testLogAfterReturningMethod() {
		when(joinPoint.getSignature()).thenReturn(signature);
		when(signature.toShortString()).thenReturn("MockMethod");
		loggingAspect.logAfterReturningMethod(joinPoint, "MockResult");
	}

	/**
	 * Test the logAfterThrowingException method in LoggingAspect
	 * 
	 */
	@Test
	public void testLogAfterThrowingException() {
		when(joinPoint.getSignature()).thenReturn(signature);
		when(signature.toShortString()).thenReturn("MockMethod");
		Throwable exception = new RuntimeException("MockException");
		loggingAspect.logAfterThrowingException(joinPoint, exception);
	}
}
