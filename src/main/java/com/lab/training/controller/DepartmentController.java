package com.lab.training.controller;

import com.lab.training.common.Result;
import com.lab.training.entity.Department;
import com.lab.training.service.DepartmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 部门控制层（对外暴露 HTTP 接口）。
 *
 * <p><b>@RestController</b> = @Controller + @ResponseBody，
 * 意思是"这个类里方法的返回值，直接序列化成 JSON 写回给浏览器"。
 *
 * <p><b>这一层只做三件事：</b>
 * <ol>
 *     <li>接参数（@PathVariable / @RequestBody / @RequestParam）；</li>
 *     <li>调用 Service；</li>
 *     <li>把结果包成 Result 返回。</li>
 * </ol>
 * <b>绝对不要</b>在 Controller 里写 if-else 业务判断或 SQL——那是 Service 的职责。
 *
 * <p><b>本模块接口清单（已约定好，前端按这个对接）：</b>
 * <pre>
 * GET    /api/departments        查询全部（TODO 9）
 * GET    /api/departments/{id}   根据 ID 查询（已实现）
 * POST   /api/departments        新增（TODO 10）
 * PUT    /api/departments/{id}   修改（TODO 11）
 * DELETE /api/departments/{id}   删除（TODO 12）
 * </pre>
 */
@Slf4j
@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    /** 构造器注入，和 Service 层同样的理由：final + 单构造器，Spring 自动注入 */
    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    /* ======================================================================
     * 已实现：GET /api/departments/{id}
     *
     * 这一条就是"打通全链路"的样板：
     *   浏览器 → Controller(取 id) → Service(校验+查库) → Mapper(执行 SQL) → MySQL
     *   然后原路返回，结果被包装成 {"code":200,"message":"success","data":{...}}
     * ====================================================================== */
    @GetMapping("/{id}")
    public Result<Department> getById(@PathVariable("id") Long id) {
        // @PathVariable 把 URL 里的 {id} 取出来，自动转成 Long
        Department department = departmentService.getById(id);
        return Result.success(department);
    }

    /* ======================================================================
     * 以下 4 个接口留给你实现。
     * 路由注解我已经写好了，你只需要把 throw 那行换成真正的三行代码：
     *   xxxService.xxx(...) → return Result.success(...)
     * ====================================================================== */

    /**
     * TODO 9：查询全部部门。
     * 请求示例：GET http://localhost:8080/api/departments
     */
    @GetMapping
    public Result<List<Department>> listAll() {
        // TODO：return Result.success(departmentService.listAll());
        throw new UnsupportedOperationException("TODO 9：查询全部接口还没实现");
    }

    /**
     * TODO 10：新增部门。
     * 请求示例：POST http://localhost:8080/api/departments
     * 请求体：{"name":"测试部","code":"TEST","leader":"张三","phone":"13800000000","status":1}
     *
     * <p>提示：@RequestBody 表示"从请求体的 JSON 里反序列化出对象"。
     * 想加参数校验的话，在参数上加 @Valid、在 Department 字段上加 @NotBlank。
     */
    @PostMapping
    public Result<Department> create(@RequestBody Department department) {
        // TODO：departmentService.create(department);
        //       return Result.success("新增成功", department); // 新增后 id 会被回填
        throw new UnsupportedOperationException("TODO 10：新增接口还没实现");
    }

    /**
     * TODO 11：修改部门。
     * 请求示例：PUT http://localhost:8080/api/departments/1
     * 请求体：{"name":"研发一部","leader":"李四"}
     *
     * <p>注意：这里有两个参数来源——路径里的 id 和请求体里的字段。
     * 必须把路径的 id 塞回 department 对象（department.setId(id)），
     * 否则 Mapper 里 WHERE id = #{id} 会拿到 null，一条都改不掉。
     */
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable("id") Long id,
                               @RequestBody Department department) {
        throw new UnsupportedOperationException("TODO 11：修改接口还没实现");
    }

    /**
     * TODO 12：删除部门。
     * 请求示例：DELETE http://localhost:8080/api/departments/1
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable("id") Long id) {
        // TODO：departmentService.deleteById(id);
        //       return Result.success();
        throw new UnsupportedOperationException("TODO 12：删除接口还没实现");
    }
}
