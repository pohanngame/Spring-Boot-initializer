package com.lab.training.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * 全局异常处理器（简易版）。
 *
 * <p><b>它解决了什么问题？</b>
 * 如果不写它，Controller 里一旦抛出异常，Spring Boot 会直接返回一坨 500 错误页
 * 或者一个跟成功响应完全不同的 JSON 结构，前端就得写两套解析逻辑。
 *
 * <p><b>它怎么工作？</b>
 * {@code @RestControllerAdvice} 会拦截所有 Controller 抛出的异常，
 * 然后按异常类型找到对应的 {@code @ExceptionHandler} 方法，
 * 把方法返回值当作正常响应写回给前端。
 *
 * <p>所以你在 Service 里 throw new IllegalArgumentException("部门不存在")，
 * 前端最终拿到的是：
 * <pre>
 * { "code": 400, "message": "部门不存在，id = 99", "data": null }
 * </pre>
 *
 * <p><b>学习建议：</b>先只用 IllegalArgumentException 表达"业务校验不通过"，
 * 等阶段二你觉得不够用了，再自己加一个 BusinessException 类来替代它。
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 业务参数不合法。
     * 这是本项目最常用的异常，Service 层做校验时直接 throw。
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public Result<Void> handleIllegalArgument(IllegalArgumentException e) {
        log.warn("业务校验失败：{}", e.getMessage());
        return Result.fail(400, e.getMessage());
    }

    /**
     * 参数绑定/校验失败。
     * 场景一：方法参数上加了 @Valid，但字段不符合 @NotBlank 等规则；
     * 场景二：URL 里的参数类型对不上（比如 ?name= 传了超长字符串）。
     * 注意：Spring 6 里 MethodArgumentNotValidException 是 BindException 的子类，
     *      所以这一个方法就能同时兜住这两种情况。
     */
    @ExceptionHandler(BindException.class)
    public Result<Void> handleBindException(BindException e) {
        FieldError fieldError = e.getBindingResult().getFieldError();
        String message = fieldError == null
                ? "请求参数不合法"
                : fieldError.getField() + " " + fieldError.getDefaultMessage();
        log.warn("参数校验失败：{}", message);
        return Result.fail(400, message);
    }

    /**
     * 路径变量类型不匹配。
     * 例如请求 /api/departments/abc，而方法签名是 getById(Long id)，就会走到这里。
     * 不处理的话前端会看到一个看不懂的 400 错误页。
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Result<Void> handleTypeMismatch(MethodArgumentTypeMismatchException e) {
        log.warn("参数类型不匹配：参数名={}, 传入值={}", e.getName(), e.getValue());
        return Result.fail(400, "参数 " + e.getName() + " 类型不正确，请检查后重试");
    }

    /**
     * 请求了一个不存在的路径 / 静态资源。
     *
     * <p><b>为什么必须单独处理它？</b>
     * 浏览器每次访问接口地址，都会顺手请求一个 {@code /favicon.ico}（标签页小图标）。
     * 这个文件我们没放，Spring 就会抛 NoResourceFoundException。
     * 如果它落到下面的兜底方法里，你会看到一屏 ERROR 堆栈，误以为程序炸了——
     * 其实只是"你要的东西这里没有"。
     *
     * <p>这里的处理体现了异常处理的核心原则：<b>先分清责任方，再决定返回什么。</b>
     * <ul>
     *     <li>客户端请求了不存在的东西 → 404，日志用 warn，不用 error；</li>
     *     <li>服务端自己出故障（数据库挂了、空指针） → 500，日志用 error 并打堆栈。</li>
     * </ul>
     * 如果这两类混在一起，线上排查时你会在几百条 favicon 噪音里找不到真正的报错。
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public Result<Void> handleNoResourceFound(NoResourceFoundException e) {
        log.warn("请求的资源不存在：{}", e.getResourcePath());
        return Result.fail(404, "请求的路径不存在：" + e.getResourcePath());
    }

    /**
     * 调用了"还没实现"的方法。
     *
     * <p>本项目所有 TODO 占位实现都是 throw new UnsupportedOperationException("TODO x ...")，
     * 单独处理成 501，目的是让你一眼区分：
     * <ul>
     *     <li>501 → 这里还留着你没写的代码，去搜 TODO；</li>
     *     <li>500 → 真的出错了（比如数据库连不上）。</li>
     * </ul>
     * 不加这个处理器的话，TODO 接口会返回 500"系统繁忙"，很容易被误判成项目坏了。
     */
    @ExceptionHandler(UnsupportedOperationException.class)
    public Result<Void> handleUnsupportedOperation(UnsupportedOperationException e) {
        log.warn("调用了未实现的方法：{}", e.getMessage());
        return Result.fail(501, e.getMessage());
    }

    /**
     * 兜底处理：所有没被上面接住的异常都会到这里。
     * 关键点：完整堆栈打到日志里给自己看，但只给前端返回一句模糊的提示，
     * 不要把 SQL、类名、堆栈暴露出去（这是安全习惯，阶段三会讲）。
     */
    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e) {
        log.error("系统异常", e);
        return Result.fail(500, "系统繁忙，请稍后重试");
    }
}
