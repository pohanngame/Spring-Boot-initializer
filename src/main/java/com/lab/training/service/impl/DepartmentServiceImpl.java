package com.lab.training.service.impl;

import com.lab.training.entity.Department;
import com.lab.training.mapper.DepartmentMapper;
import com.lab.training.service.DepartmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 部门业务层实现类。
 *
 * <p><b>@Service 的作用：</b>把这个类交给 Spring 管理（创建对象、放进容器）。
 * 这样 Controller 里才能注入它。忘了加这个注解，启动时会报
 * "Consider defining a bean of type 'DepartmentService'"。
 *
 * <p><b>@Slf4j 是 Lombok 注解：</b>自动生成一个 log 对象，
 * 直接用 log.info() / log.error() 打日志，不用自己写 LoggerFactory.getLogger(...)。
 *
 * <p><b>为什么用构造器注入，而不是 @Autowired 写在字段上？</b>
 * <ol>
 *     <li>字段可以加 final，对象创建后不可变，更安全；</li>
 *     <li>依赖关系一眼能看清（构造器上有几个参数，就依赖几个对象）；</li>
 *     <li>写单元测试时可以直接 new 出来传假对象，不需要启动 Spring。</li>
 * </ol>
 * 这是目前 Spring 官方推荐的方式，也是公司代码的常见写法。
 */
@Slf4j
@Service
public class DepartmentServiceImpl implements DepartmentService {

    /**
     * 依赖的持久层。声明为 final，并通过构造器赋值——
     * 只有一个构造器时，Spring 会自动用它来注入，不需要写 @Autowired。
     */
    private final DepartmentMapper departmentMapper;

    public DepartmentServiceImpl(DepartmentMapper departmentMapper) {
        this.departmentMapper = departmentMapper;
    }

    /* ======================================================================
     * 已实现：根据 ID 查询部门
     *
     * 这是本次实训的"标准答案"，请逐行读懂它，然后照着它写剩下的 4 个方法。
     * 它体现了业务层三个固定动作：校验参数 → 调用 Mapper → 校验结果。
     * ====================================================================== */
    @Override
    public Department getById(Long id) {

        // 第 1 步：校验参数。永远不要相信调用方传进来的东西。
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("部门 ID 不合法，必须为正整数");
        }

        // 第 2 步：调用 Mapper 查库。这里只写一行，具体 SQL 在 XML 里。
        Department department = departmentMapper.selectById(id);

        // 第 3 步：校验结果。查不到要给出明确原因，而不是返回 null 让前端猜。
        if (department == null) {
            throw new IllegalArgumentException("部门不存在，id = " + id);
        }

        // 关键节点打日志，方便排查问题（生产环境不要打太多 info，先用 debug）
        log.info("查询部门成功，id = {}, name = {}", department.getId(), department.getName());

        return department;
    }

    /* ======================================================================
     * 以下 4 个方法留给你实现。
     *
     * 现在它们会抛 UnsupportedOperationException，
     * 所以你调用对应接口时会看到 501 和"TODO x 还没实现"的提示——这是刻意设计的。
     * 每实现完一个，就把 throw 那行删掉，换成真正的逻辑。
     * ====================================================================== */

    @Override
    public List<Department> listAll() {
        // TODO 5：调用 departmentMapper.selectAll() 拿到列表。
        //         好的习惯：如果返回 null，转成 Collections.emptyList()，
        //         让上层永远不用做 null 判断。
        throw new UnsupportedOperationException("TODO 5：listAll() 还没实现，请先补全 Mapper 的 SQL");
    }

    @Override
    public boolean create(Department department) {
        // TODO 6：实现步骤建议（照抄 getById 的三步走思路）
        //   1. 判空：department 为 null 直接抛 IllegalArgumentException；
        //   2. 校验 name、code 非空（用 StringUtils.hasText 或 trim().isEmpty()）；
        //   3. 校验 code 唯一：先按 code 查一次，查到就抛"部门编码已存在"；
        //   4. status 为 null 时默认设为 1；
        //   5. 调用 insert，返回 rows > 0。
        //   注意：这里"先查再插"在高并发下仍可能重复，
        //        真正保险的是数据库给 code 加 UNIQUE 索引（init.sql 里已经加了）。
        throw new UnsupportedOperationException("TODO 6：create() 还没实现");
    }

    @Override
    public boolean update(Department department) {
        // TODO 7：实现步骤建议
        //   1. 校验 department 和 id 非空；
        //   2. 先 getById(department.getId()) 确认部门存在（复用已实现的方法，不要重复写）；
        //   3. 如果传了 code，检查是否被其他部门占用（查询结果 id 不等于当前 id 就是冲突）；
        //   4. 调用 updateById，返回 rows > 0。
        throw new UnsupportedOperationException("TODO 7：update() 还没实现");
    }

    @Override
    public boolean deleteById(Long id) {
        // TODO 8：实现步骤建议
        //   1. 校验 id；
        //   2. 先确认部门存在，不存在就抛"部门不存在"，不要静默返回 false；
        //   3. 调用 deleteById，返回 rows > 0。
        throw new UnsupportedOperationException("TODO 8：deleteById() 还没实现");
    }
}
