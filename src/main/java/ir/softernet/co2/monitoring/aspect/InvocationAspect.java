package ir.softernet.co2.monitoring.aspect;

import ir.softernet.co2.monitoring.exception.base.AnException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.util.StopWatch;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This aspect logs every execution of methods inside controller and service package,
 * except the ones annotate with {@link ir.softernet.co2.monitoring.aspect.SkipInvokeLogging}
 *
 * @author saman
 */
@Aspect
@RequiredArgsConstructor
@Slf4j
public class InvocationAspect {


  @Pointcut("within(ir.softernet.co2.monitoring.controller..*)")
  public void requestPointcut() {
  }

  @Pointcut("within(ir.softernet.co2.monitoring.service..*)")
  public void implPointcut() {
  }


  @Around("requestPointcut()")
  public Object logAroundRequest(ProceedingJoinPoint joinPoint) throws Throwable {

    logBefore(joinPoint);
    return proceedAndLog(joinPoint);
  }

  @AfterThrowing(pointcut = "implPointcut()", throwing = "e")
  public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
    log.warn("exception in {}.{}({}): {}",
      cls(joinPoint).getSimpleName(),
      methodSignature(joinPoint).getName(),
      args(joinPoint),
      e.getClass().getSimpleName());


    if (e instanceof AnException) //print stack trace to stdout
      e.printStackTrace();
    else
      log.warn("exception trace", e);
  }


  private void logBefore(ProceedingJoinPoint joinPoint) {
    if (notLog(joinPoint))
      return;

    log.info("calling: {}.{}({})",
      cls(joinPoint).getSimpleName(),
      methodSignature(joinPoint).getName(),
      Arrays.toString(args(joinPoint)));
  }

  private Object proceedAndLog(ProceedingJoinPoint joinPoint) throws Throwable {
    try {
      final StopWatch stopWatch = new StopWatch();
      stopWatch.start();
      final Object result = joinPoint.proceed();
      stopWatch.stop();
      final long totalTimeMillis = stopWatch.getTotalTimeMillis();

      if (isVoid(joinPoint))
        logAfter(joinPoint, totalTimeMillis);
      else
        logAfter(joinPoint, result, totalTimeMillis);

      return result;
    } catch (IllegalArgumentException e) {
      log.warn("illegal argument: in {}.{}({})",
        cls(joinPoint).getSimpleName(),
        methodSignature(joinPoint).getName(),
        Arrays.toString(args(joinPoint)));

      throw e;
    }
  }

  private void logAfter(JoinPoint joinPoint, long execTime) {
    if (notLog(joinPoint))
      return;

    log.info("called: {}.{}({}) took {} ms",
      cls(joinPoint).getSimpleName(),
      methodSignature(joinPoint).getName(),
      Arrays.toString(args(joinPoint)),
      execTime);
  }

  private void logAfter(JoinPoint joinPoint, Object result, long execTime) {
    if (notLog(joinPoint))
      return;

    log.info("called: {}.{}({}) ~> {} took {} ms",
      cls(joinPoint).getSimpleName(),
      methodSignature(joinPoint).getName(),
      Arrays.toString(args(joinPoint)),
      result.toString(),
      execTime);
  }


  private Class<?> cls(JoinPoint joinPoint) {
    return joinPoint.getSignature().getDeclaringType();
  }

  private MethodSignature methodSignature(JoinPoint joinPoint) {
    return (MethodSignature) joinPoint.getSignature();
  }

  private boolean isVoid(JoinPoint joinPoint) {
    return methodSignature(joinPoint).getReturnType().equals(Void.TYPE);
  }

  private Method method(MethodSignature methodSignature) {
    return methodSignature.getMethod();
  }

  private Method method(JoinPoint joinPoint) {
    return method(methodSignature(joinPoint));
  }

  private Object[] args(JoinPoint joinPoint) {
    final List<Object> objectList = new ArrayList<>();

    for (Object anArg : joinPoint.getArgs())
      if (!(anArg instanceof MultipartFile))
        objectList.add(anArg);


    return objectList.toArray();
  }

  private boolean notLog(JoinPoint joinPoint) {
    return cls(joinPoint).isAnnotationPresent(SkipInvokeLogging.class)
      || method(joinPoint).isAnnotationPresent(SkipInvokeLogging.class);
  }

}
