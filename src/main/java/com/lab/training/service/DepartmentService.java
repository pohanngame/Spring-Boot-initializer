package com.lab.training.service;

import com.lab.training.entity.Department;

import java.util.List;

/**
 * 部门业务层接口。
 *
 * <p><b>为什么要"接口 + 实现类"两套东西？</b>
 * <ul>
 *     <li>Controller 只依赖接口，不关心下面怎么实现，方便以后替换（比如从 MySQL 换成 Redis）；</li>
 *     <li>Spring 的 AOP（事务、日志）默认基于接口代理，养成习惯以后加 @Transactional 不会踩坑；</li>
 *     <li>团队协作时，接口就是契约，前端/后端可以并行开发。</li>
 * </ul>
 *
 * <p><b>业务层该做什么？</b>参数校验、业务规则判断、组装多个 Mapper 的结果。
 * 不该做的事：写 SQL（那是 Mapper 的活）、拼 HTTP 响应（那是 Controller 的活）。
 */
public interface DepartmentService {

    /* ==================================================================
     * 已实现：根据 ID 查询部门
     * ================================================================== */

    /**
     * 根据主键查询部门。
     *
     * @param id 部门主键，必须为正数
     * @return 部门对象，不会返回 null（查不到会抛异常，由全局异常处理器统一转成 JSON）
     */
    Department getById(Long id);

    /* ==================================================================
     * 以下方法签名已给出，实现留给你写（见 DepartmentServiceImpl）
     * ================================================================== */

    /**
     * TODO 5：查询全部部门。
     *
     * @return 部门列表，没有数据时返回空集合而不是 null
     */
    List<Department> listAll();

    /**
     * TODO 6：新增部门。
     *
     * <p>业务规则要求：
     * <ol>
     *     <li>name、code 不能为空；</li>
     *     <li>code 在表里必须唯一（需要你自己加一个 selectByCode 之类的方法来判断）；</li>
     *     <li>status 不传时默认设为 1（启用）。</li>
     * </ol>
     *
     * @param department 待新增的部门
     * @return true 表示新增成功
     */
    boolean create(Department department);

    /**
     * TODO 7：修改部门。
     *
     * <p>业务规则要求：
     * <ol>
     *     <li>id 不能为空，且该部门必须存在；</li>
     *     <li>如果改了 code，要检查新 code 有没有和别的部门重复（排除自己）。</li>
     * </ol>
     *
     * @param department 带 id 的部门对象
     * @return true 表示修改成功
     */
    boolean update(Department department);

    /**
     * TODO 8：删除部门。
     *
     * <p>业务规则要求：先判断部门是否存在，不存在要给出明确提示，
     * 不要直接返回 false 让前端猜。
     *
     * @param id 部门主键
     * @return true 表示删除成功
     */
    boolean deleteById(Long id);
}
