package com.lab.training.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 统一响应体。
 *
 * <p>为什么需要它？如果没有统一格式，有人返回对象、有人返回字符串、有人返回 null，
 * 前端就没法写通用的判断逻辑。约定好之后，前端只需要判断 code 是不是 200。
 *
 * <p>最终返回的 JSON 长这样：
 * <pre>
 * {
 *   "code": 200,
 *   "message": "success",
 *   "data": { "id": 1, "name": "研发部", ... }
 * }
 * </pre>
 *
 * @param <T> 业务数据的类型，比如 Result&lt;Department&gt;、Result&lt;List&lt;Department&gt;&gt;
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> implements Serializable {

    /** 成功状态码 */
    public static final int CODE_SUCCESS = 200;
    /** 通用失败状态码（服务端异常） */
    public static final int CODE_ERROR = 500;

    private static final String DEFAULT_SUCCESS_MESSAGE = "success";

    /** 业务状态码：200 表示成功，其它数字表示失败（具体含义见 README） */
    private Integer code;

    /** 给前端/开发者看的提示信息 */
    private String message;

    /** 真正的业务数据，失败时为 null */
    private T data;

    /* ==================== 成功 ==================== */

    /** 成功，不带数据（一般用于删除、修改这类只需要知道成功与否的接口） */
    public static <T> Result<T> success() {
        return new Result<>(CODE_SUCCESS, DEFAULT_SUCCESS_MESSAGE, null);
    }

    /** 成功，带数据（最常用） */
    public static <T> Result<T> success(T data) {
        return new Result<>(CODE_SUCCESS, DEFAULT_SUCCESS_MESSAGE, data);
    }

    /** 成功，自定义提示 + 带数据 */
    public static <T> Result<T> success(String message, T data) {
        return new Result<>(CODE_SUCCESS, message, data);
    }

    /* ==================== 失败 ==================== */

    /** 失败，默认 500 */
    public static <T> Result<T> fail(String message) {
        return new Result<>(CODE_ERROR, message, null);
    }

    /** 失败，自定义状态码（例如 400 参数错误、404 数据不存在、409 数据冲突） */
    public static <T> Result<T> fail(Integer code, String message) {
        return new Result<>(code, message, null);
    }

    /**
     * 判断是否成功的小工具，写单元测试时会用到。
     *
     * <p><b>为什么方法上要加 @JsonIgnore？</b>
     * Jackson 序列化对象时，会把所有 public 的 {@code isXxx()} / {@code getXxx()} 方法
     * 当成"属性"输出。如果这里不加注解，前端收到的 JSON 就会莫名其妙多一个
     * {@code "success": true} 字段，和我们约定的 {code, message, data} 结构不一致。
     *
     * <p>记住这条规律：<b>你在返回类里多写一个 getter，API 就会多一个字段。</b>
     * 这也是为什么实体类不要随便加 {@code getFullName()} 这类方法的原因。
     */
    @JsonIgnore
    public boolean isSuccess() {
        return CODE_SUCCESS == this.code;
    }
}
