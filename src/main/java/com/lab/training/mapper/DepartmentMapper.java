package com.lab.training.mapper;

import com.lab.training.entity.Department;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 部门持久层接口（DAO 层），只负责和数据库打交道，不写业务判断。
 *
 * <p><b>@Mapper 的作用：</b>告诉 MyBatis "启动时请为这个接口生成一个实现类"。
 * 有了它，你就能在 Service 里直接注入 DepartmentMapper 使用，不需要写实现类。
 *
 * <p><b>接口方法怎么和 SQL 对应？</b>
 * 靠"方法名 == XML 里标签的 id"。例如下面的 selectById()
 * 对应 DepartmentMapper.xml 里的 &lt;select id="selectById"&gt;。
 * 名字对不上，运行时会报 Invalid bound statement (not found)。
 *
 * <p><b>@Param 的作用：</b>给参数起个名字。SQL 里写 #{id} 时，
 * MyBatis 就是按这个名字去参数对象里找值的。
 * 只有一个简单类型参数时理论上可以省略，但<b>强烈建议永远写上</b>：
 * 一旦方法有多个参数，不写 @Param 就只能用 #{arg0}/#{param1}，非常容易出错。
 */
@Mapper
public interface DepartmentMapper {

    /* ==================================================================
     * 已实现：根据主键查询单个部门（本次实训的示范链路）
     * ================================================================== */
    Department selectById(@Param("id") Long id);

    /* ==================================================================
     * 以下 4 个方法只有签名，SQL 留给你在 DepartmentMapper.xml 里补全。
     * 补之前请先读 README 的「待完成任务清单」，那里写了每个方法的具体要求。
     * ================================================================== */

    /**
     * TODO 1：查询全部部门（按 id 升序）。
     *
     * <p>提示：返回 List 时，只要 SQL 查出的每一行能被映射成 Department 对象，
     * MyBatis 会自动帮你装进 List，不需要你手动 new ArrayList。
     */
    List<Department> selectAll();

    /**
     * TODO 2：新增一个部门。
     *
     * <p>提示：
     * <ul>
     *     <li>返回值 int 表示"影响的行数"，插入成功就是 1，失败是 0；</li>
     *     <li>数据库 id 是自增的，插入时不用给 id 赋值；</li>
     *     <li>想要插入后立刻拿到新生成的 id，需要在 XML 的 insert 标签上加
     *         useGeneratedKeys="true" keyProperty="id"。</li>
     * </ul>
     *
     * @param department 待插入的部门对象（id、时间字段不用填）
     * @return 影响行数
     */
    int insert(Department department);

    /**
     * TODO 3：根据主键修改部门。
     *
     * <p>提示：
     * <ul>
     *     <li>要防止"把某个字段改成 null"：用 &lt;if test="name != null"&gt; 做动态 SQL，
     *         只更新前端真正传过来的字段；</li>
     *     <li>id 必须作为 WHERE 条件，不能让前端改主键。</li>
     * </ul>
     *
     * @param department 带 id 的部门对象
     * @return 影响行数
     */
    int updateById(Department department);

    /**
     * TODO 4：根据主键删除部门。
     *
     * <p>提示：真实项目一般做"逻辑删除"（把 status 改成 0 或加 deleted 字段），
     * 这里先用物理删除 DELETE，阶段二可以思考两者的区别。
     *
     * @param id 部门主键
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id);
}
