# 新生后端实训脚手架 · Spring Boot 3 + MyBatis

> 面向大一 / 大二零基础同学的后端入门工程。
> **给足工程规范，留白核心实现** —— 环境、分层、返回格式、异常处理全部配好，
> 你只需要把 5 个方法 + 4 条 SQL 补全，然后独立做一个新模块。

---

## 一、这个项目能跑起来什么

一句话：**打开浏览器访问一个地址，就能从 MySQL 里查出一条部门数据，并以统一的 JSON 格式返回。**

```
http://localhost:8080/api/departments/1
```

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "name": "研发部",
    "code": "DEV",
    "leader": "张伟",
    "phone": "13800000001",
    "status": 1,
    "createTime": "2026-10-30 10:00:00",
    "updateTime": "2026-10-30 10:00:00"
  }
}
```

这一条链路的每一个环节你都要能讲清楚：**浏览器 → Controller → Service → Mapper → MySQL → 原路返回**。
它是整个后端开发的最小完整模型，剩下的所有功能都是这条链路的重复与变体。

---

## 二、你需要具备什么

| 需要会 | 不需要会（本项目已帮你搞定） |
| --- | --- |
| Java 基础语法、类与对象、集合 | Maven 依赖版本冲突 |
| SQL 的 SELECT / INSERT / UPDATE / DELETE | Spring Boot 自动装配原理 |
| 会用 IDEA 打开项目 | Tomcat 配置与部署 |
| 会装 MySQL 并连上 | JSON 序列化、日期格式、异常统一处理 |

**技术栈版本（已全部锁定，不要自己升级）**

| 组件 | 版本 | 说明 |
| --- | --- | --- |
| JDK | 17 | Spring Boot 3 的最低要求，低于 17 直接跑不起来 |
| Spring Boot | 3.2.5 | 注意包名是 `jakarta.*` 不是 `javax.*` |
| MyBatis Starter | 3.0.3 | 不在 Spring Boot 版本管理内，pom 里单独指定 |
| MySQL | 8.0 | 驱动 `mysql-connector-j` |
| Lombok | 由父工程管理 | 需要 IDEA 装插件 |

> **本实训禁止使用 MyBatis-Plus。** 原因很直接：MP 会帮你自动生成 SQL，
> 你写了 14 天，仍然不知道 `WHERE id = ?` 是谁拼出来的。
> 手写一遍 SQL，你才看得懂索引、执行计划、参数映射。

---

## 三、30 分钟跑起来（5 步）

### 第 1 步：确认环境

打开命令行，逐条执行，都要能输出版本号：

```bash
java -version     # 必须 17.x
mvn -v            # 3.6 以上
mysql --version   # 8.0.x
```

`java -version` 不是 17 的，先装 JDK 17 并配好 `JAVA_HOME`，不要跳过。

### 第 2 步：建库 + 导数据

在项目根目录执行（或者在 Navicat 里整段运行 `sql/init.sql`）：

```bash
mysql -u root -p < sql/init.sql
```

执行完应该看到最后一条 `SELECT` 输出 **6 行部门数据**。
看不到 6 行，说明脚本没跑完，**不要继续往下走**。

### 第 3 步：填数据库账号密码

打开 `src/main/resources/application.yml`，只有两行需要你改：

```yaml
    username:      # 改成你的 MySQL 账号，一般是 root
    password:      # 改成你的 MySQL 密码
```

注意 `username:` 后面**要有空格再写值**，缩进用空格不能用 Tab。

### 第 4 步：IDEA 打开项目

1. `File → Open`，选中 `pom.xml` 所在目录，选择 **Open as Project**；
2. 右下角会提示 Maven 导入，点 **Load Maven Changes**（或右侧 Maven 面板点刷新）；
3. 等依赖下载完成，进度条消失、代码里不再有红色报错；
4. 确认已开启注解处理：`Settings → Build → Compiler → Annotation Processors → Enable annotation processing` 打勾（Lombok 依赖它）。

### 第 5 步：启动

运行 `TrainingApplication` 的 `main` 方法。控制台出现下面这段就成功了：

```
====================================================
  项目启动成功！
  测试接口: http://localhost:8080/api/departments/1
====================================================
```

浏览器访问 `http://localhost:8080/api/departments/1`，能拿到上面那段 JSON 就通关了。
再试 `http://localhost:8080/api/departments/999`，应该返回：

```json
{ "code": 400, "message": "部门不存在，id = 999", "data": null }
```

**这两次访问都正常，才说明全链路（含异常处理）真的通了。**

---

## 四、代码地图

```
src/main/java/com/lab/training
├── TrainingApplication.java        启动类，程序入口
├── common
│   ├── Result.java                 统一响应体 Result<T>
│   └── GlobalExceptionHandler.java 全局异常处理，把异常转成统一 JSON
├── entity
│   └── Department.java             实体类，和数据库表一一对应
├── mapper
│   └── DepartmentMapper.java       持久层接口，只写方法签名
├── service
│   ├── DepartmentService.java      业务层接口（契约）
│   └── impl/DepartmentServiceImpl  业务层实现（校验 + 编排）
└── controller
    └── DepartmentController.java   控制层，暴露 HTTP 接口

src/main/resources
├── application.yml                 配置（端口、数据库、MyBatis）
└── mapper/DepartmentMapper.xml     SQL 写在这里
```

**四层各管什么，一句话记住：**

| 层 | 职责 | 绝对不要做的事 |
| --- | --- | --- |
| Controller | 接参数、调 Service、包 Result | 写业务判断、写 SQL |
| Service | 业务校验、编排多个 Mapper | 处理 HTTP、拼 JSON |
| Mapper | 执行 SQL，只做数据存取 | 写 if-else 业务逻辑 |
| Entity | 承载数据 | 放业务方法 |

**为什么要分这么多层？**
因为一个功能出问题时，你要能立刻定位到是哪一层。如果所有代码堆在 Controller 里，
查一个"新增失败"要在 200 行里翻——这就是很多同学后期改不动的根本原因。

### 一次 GET 请求的完整流转

```
浏览器
  │  GET /api/departments/1
  ▼
DispatcherServlet（Spring MVC 的调度中心，自动帮你分发）
  │  按路径匹配到 DepartmentController#getById
  ▼
DepartmentController.getById(1L)
  │  @PathVariable 把 "1" 转成 Long 1
  ▼
DepartmentServiceImpl.getById(1L)
  │  ① 校验 id > 0        ② 调用 Mapper        ③ 校验返回值非 null
  ▼
DepartmentMapper.selectById(1L)      ← 接口，没有实现类
  │  MyBatis 按「接口全限定名 + 方法名」找到 XML 里的 selectById
  ▼
DepartmentMapper.xml
  │  SELECT id, name, ... FROM department WHERE id = ?
  ▼
MySQL（返回一行结果）
  │  resultMap 把列名映射成 Java 属性
  ▼
Department 对象 → Result.success(dept) → Jackson 序列化成 JSON
  ▼
浏览器
```

**中途任何一步抛异常**，都会被 `GlobalExceptionHandler` 接住，转成
`{"code":400/500,"message":"...","data":null}`，前端永远只需处理一种结构。

---

## 五、已实现的样板：`getById` 拆解

这是本次实训的"标准答案"，请逐行读，读完照着写剩下的 4 个方法。

### 1. Entity：字段怎么对应

数据库 `create_time` ←→ Java `createTime`。
靠 `application.yml` 里的 `map-underscore-to-camel-case: true` 自动转换。
**这个开关忘了开，你会得到一堆 null 字段，且不报错**——这是最阴的坑之一。

### 2. Mapper 接口：只写签名

```java
Department selectById(@Param("id") Long id);
```

- `@Mapper`：告诉 MyBatis 生成实现类；
- `@Param("id")`：给参数起名，XML 里的 `#{id}` 就靠它取值。

### 3. XML：写 SQL

```xml
<select id="selectById" resultMap="BaseResultMap">
    SELECT <include refid="Base_Column_List"/>
    FROM department
    WHERE id = #{id}
</select>
```

- `namespace` = 接口全限定名，`id` = 方法名，**两者错一个字母就会报 `Invalid bound statement`**；
- `#{}` 是预编译占位符（安全，防 SQL 注入），`${}` 是字符串拼接（危险，本项目禁用）。

### 4. Service：三步走

```java
// ① 校验参数：永远不要相信调用方
if (id == null || id <= 0) throw new IllegalArgumentException("部门 ID 不合法");
// ② 调 Mapper
Department department = departmentMapper.selectById(id);
// ③ 校验结果：查不到要说明原因，不要返回 null 让前端猜
if (department == null) throw new IllegalArgumentException("部门不存在，id = " + id);
return department;
```

**记住这个"校验参数 → 执行 → 校验结果"的骨架**，你已经会写 80% 的业务方法了。

### 5. Controller：三行

```java
@GetMapping("/{id}")
public Result<Department> getById(@PathVariable("id") Long id) {
    return Result.success(departmentService.getById(id));
}
```

---

## 六、待完成任务清单（12 个 TODO）

用 IDEA 全局搜索 `TODO` 就能定位。**建议按序号顺序做，不要跳。**

### 阶段一任务（必修）

| 编号 | 文件 | 任务 | 完成判定 |
| --- | --- | --- | --- |
| TODO 1 | DepartmentMapper.xml | `selectAll` 查询全部 | 接口返回 6 条数据，按 id 升序 |
| TODO 2 | DepartmentMapper.xml | `insert` 新增 | 新增后返回的 `data.id` 有值（说明主键回填成功） |
| TODO 3 | DepartmentMapper.xml | `updateById` 动态更新 | 只传 `{"name":"x"}` 时，其他字段在数据库里没被清空 |
| TODO 4 | DepartmentMapper.xml | `deleteById` 删除 | 删除后再查该 id 返回 400 |
| TODO 5 | DepartmentServiceImpl | `listAll()` | 无数据时返回 `[]` 而不是 `null` |
| TODO 6 | DepartmentServiceImpl | `create()` | `code` 重复时返回 400「部门编码已存在」 |
| TODO 7 | DepartmentServiceImpl | `update()` | 把 A 的 code 改成 B 的 code 时被拦截 |
| TODO 8 | DepartmentServiceImpl | `deleteById()` | 删除不存在的 id 返回 400，不是 500 |
| TODO 9 | DepartmentController | `GET /api/departments` | 返回数组 |
| TODO 10 | DepartmentController | `POST /api/departments` | 用 Postman/Apifox 发 JSON 能插入成功 |
| TODO 11 | DepartmentController | `PUT /api/departments/{id}` | 路径 id 正确回填，只改传了的字段 |
| TODO 12 | DepartmentController | `DELETE /api/departments/{id}` | 返回 `{"code":200,...}` |

> 未实现的方法现在会抛 `UnsupportedOperationException`，所以你调用时会看到
> **501 + "TODO x 还没实现"**。这是刻意设计的，不要以为是项目坏了。
> 看到 501 就去全局搜 TODO；看到 500 才是真的出错了（比如数据库没启动）。

### 阶段二任务（独立设计）

自己设计并实现 **Company（公司）** 或 **Employee（员工）** 模块，要求：

1. 至少 6 个业务字段，含唯一约束、非空约束；
2. 必须和 `department` 建立关联（`department_id`）；
3. 完整四层代码 + 完整 CRUD（这一次**不允许留 TODO**）；
4. 提供一个关联查询接口，例如 `GET /api/employees?departmentId=1`；
5. 在 `sql/init.sql` 里补上你的建表语句和测试数据。

---

## 七、14 天实训任务书

每天按 **2~3 小时** 估算。每天的"自检"没通过，不要进入下一天。

### 阶段一 · 跑通并补全 Department（Day 1 – Day 5）

| 天 | 任务 | 产出 / 自检 |
| --- | --- | --- |
| **D1** | 装 JDK17 / Maven / MySQL8；执行 `init.sql`；IDEA 导入项目 | 命令行 `mvn -v` 有输出；数据库里能 `SELECT` 出 6 行 |
| **D2** | 改 `application.yml`，启动项目，访问 `/api/departments/1` 和 `/999`；读四层代码 | 能画出请求流转图（手画拍照即可）；能口述四层职责 |
| **D3** | 完成 TODO 1 + TODO 5 + TODO 9（列表查询链路） | `GET /api/departments` 返回 6 条；控制台能看到打印的 SQL |
| **D4** | 完成 TODO 2 + TODO 6 + TODO 10（新增链路） | 新增成功且 `data.id` 有值；重复 code 被拦截 |
| **D5** | 完成 TODO 3、4、7、8、11、12（改 + 删）；阶段验收 | 用 Apifox 把 5 个接口全跑一遍；异常场景都有明确提示 |

**D5 阶段验收（口头提问，必须答对）**

1. `@RestController` 和 `@Controller` 有什么区别？
2. `#{ }` 和 `${ }` 的区别？为什么后者危险？
3. 为什么 `insert` 要加 `useGeneratedKeys="true"`？
4. 你的 `update` 不做 `<if>` 判断会发生什么？去数据库里验证一下。

### 阶段二 · 独立设计新模块（Day 6 – Day 10）

| 天 | 任务 | 产出 / 自检 |
| --- | --- | --- |
| **D6** | 选方向（Company / Employee）；画 ER 图；写建表 SQL 并执行 | 表能建成功；`DESC 表名` 结果贴进报告 |
| **D7** | 写 Entity + Mapper 接口 + XML，先只做 `selectById` | 用 SQL 客户端验证 SQL 正确，再写 Java |
| **D8** | 补齐 Mapper 全部方法 + Service 业务校验 | 单元自测：每个方法都能单独跑通 |
| **D9** | 写 Controller，补齐 5 个接口 | Apifox 全部跑通，含异常场景 |
| **D10** | 做关联查询（按部门查员工 / 按公司查部门） | 接口能返回带关联字段的数据；说明你用的是 JOIN 还是两次查询及原因 |

**重点提醒：先写 SQL，再写 Java。**
顺序反了会非常痛苦——表结构没定好，Mapper 怎么写都是错的，最后只能全部推翻重来。

### 阶段三 · 工程化与交付（Day 11 – Day 14）

| 天 | 任务 | 产出 / 自检 |
| --- | --- | --- |
| **D11** | 参数校验：加 `@Valid` + `@NotBlank` / `@Min`；补全异常提示文案 | 传空 name 时返回 400 且提示精确到字段 |
| **D12** | 代码规范自查（见下方检查项）；整理 Git 提交 | 提交历史清晰，无 `target/` 目录 |
| **D13** | 边界测试：超长字符串、负数 id、不存在的 id、重复 code、空请求体 | 每个场景都有明确返回，没有一个 500 堆栈页 |
| **D14** | 写接口文档（给前端同学）+ 演示 + 答辩 | 前端能只看文档就调通你的接口 |

**D12 代码规范自查清单**

- [ ] 方法名是动词开头（`getById` / `create` / `listAll`），不是 `department1`、`test`
- [ ] 没有 `System.out.println`，全部改用 `log.info/warn/error`
- [ ] 没有注释掉的死代码（`// departmentMapper.delete(...)` 这种必须删）
- [ ] 每个 public 方法都有注释说明"做什么、参数是什么、返回什么"
- [ ] Controller 里没有 if-else 业务判断
- [ ] SQL 里没有 `SELECT *`
- [ ] 没有把密码、数据库地址硬编码在 Java 代码里

---

## 八、验收标准（100 分）

| 维度 | 分值 | 评分要点 |
| --- | --- | --- |
| 环境与启动 | 10 | 老师在自己电脑上按 README 能一次跑起来（含 `init.sql`） |
| Department 补全 | 25 | 4 条 SQL + 4 个 Service 方法 + 4 个接口全部正确，异常有提示 |
| 新模块完整度 | 30 | 四层齐全、CRUD 完整、约束合理、无 TODO 残留 |
| 关联查询 | 10 | 能用 SQL 关联出数据，并能解释为什么这样设计 |
| 代码规范 | 15 | 命名、注释、日志、分层职责清晰 |
| 文档与答辩 | 10 | 接口文档可直接对接前端；能回答 D5 的 4 个问题 |

**扣分项（每项 −5）**：用了 MyBatis-Plus、SQL 里出现 `${}`、Controller 里写 SQL、
提交了 `target/` 目录、代码无法编译。

---

## 九、Git 提交规范

```bash
git checkout -b feature/department-crud     # 每个阶段开一个分支
git add .
git commit -m "feat(department): 补全列表查询 TODO1/5/9"
git push origin feature/department-crud
```

提交信息格式：`类型(模块): 做了什么`

| 类型 | 含义 |
| --- | --- |
| feat | 新功能 |
| fix | 修 bug |
| docs | 文档 |
| refactor | 重构（不改功能） |
| chore | 杂项（配置、依赖） |

**不要提交**：`target/`、`.idea/`、`*.iml`、本地数据库密码。

---

## 十、红线（违反直接重做）

1. **禁止**使用 MyBatis-Plus、JPA、代码生成器——本次就是要你手写。
2. **禁止**复制粘贴看不懂的代码。答辩时指着一行问你"这行干什么"，答不出来即视为抄袭。
3. **禁止**修改已实现的 `getById` 链路的方法签名（可以改实现细节，但要说明理由）。
4. **禁止**在 SQL 里用 `${}` 拼接用户输入。
5. **禁止**把异常吞掉（`catch (Exception e) { }` 空实现）。
6. **禁止**为了"能跑"而删掉 `GlobalExceptionHandler`。

---

## 十一、常见报错急救表

| 报错关键字 | 原因 | 解决 |
| --- | --- | --- |
| `Failed to configure a DataSource: 'url' attribute is not specified` | yml 缩进错了，`url` 没挂在 `datasource` 下 | 检查缩进，用空格不用 Tab |
| `Access denied for user 'root'@'localhost'` | 账号或密码错 | 改 `application.yml`，先在命令行 `mysql -u root -p` 验证 |
| `Unknown database 'lab_training'` | 没执行 `init.sql` | 执行 `mysql -u root -p < sql/init.sql` |
| `Invalid bound statement (not found): ...selectAll` | XML 的 `id` 和接口方法名不一致 / namespace 写错 / XML 不在 `resources/mapper/` | 三处逐一核对 |
| `Public Key Retrieval is not allowed` | MySQL 8 加密方式 | url 加 `allowPublicKeyRetrieval=true` |
| `The server time zone value '' is unrecognized` | 没配时区 | url 加 `serverTimezone=Asia/Shanghai` |
| 查出来的对象字段全是 `null` | 驼峰映射没开 / `resultMap` 列名写错 | 检查 `map-underscore-to-camel-case: true` |
| `找不到符号: 方法 getXXX()` | Lombok 没生效 | 装 Lombok 插件 + 开启 Annotation Processing + `mvn clean` |
| `Port 8080 was already in use` | 端口被占 | 改 `server.port`，或杀掉占用进程 |
| 请求返回 404，路径明明没错 | 类上漏了 `@RestController` / `@RequestMapping` 拼错 | 看启动日志里有没有 `Mapped "{[/api/departments/{id}]}"` |
| `415 Unsupported Media Type` | POST 没设 `Content-Type: application/json` | Apifox 里选 JSON 类型 |
| 控制台出现 `No static resource favicon.ico` | 浏览器自动请求网站图标，本项目没放这个文件 | 正常现象。已由全局异常处理器转成 404 + 一条 WARN，可以忽略 |
| 中文变问号 | 库字符集不是 utf8mb4 / url 缺 `characterEncoding=utf8` | 重建库，检查 url 参数 |

**调试万能第一步**：看控制台的 SQL 打印。
`log-impl: StdOutImpl` 会把真实执行的 SQL 和参数都打出来，
SQL 对不对，一眼就知道问题在 Java 还是在数据库。

---

## 十二、给前端同学的接口约定

**统一响应结构**

```json
{ "code": 200, "message": "success", "data": {} }
```

| code | 含义 | 前端处理 |
| --- | --- | --- |
| 200 | 成功 | 取 `data` |
| 400 | 参数错误 / 业务校验失败 | 直接 toast `message` |
| 404 | 请求的路径不存在 | 检查 URL 是否拼错 |
| 501 | 该接口还没实现（项目里还有 TODO） | 开发期提示用，联调前应全部消灭 |
| 500 | 服务端异常 | 提示"系统繁忙"并上报 |

**接口清单**（`BASE = http://localhost:8080`）

| 方法 | 路径 | 说明 | 状态 |
| --- | --- | --- | --- |
| GET | `/api/departments/{id}` | 根据 ID 查部门 | 已实现 |
| GET | `/api/departments` | 查全部 | TODO 9 |
| POST | `/api/departments` | 新增 | TODO 10 |
| PUT | `/api/departments/{id}` | 修改 | TODO 11 |
| DELETE | `/api/departments/{id}` | 删除 | TODO 12 |

**Department 字段说明**

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| id | Long | 主键，自增 |
| name | String | 部门名称，必填，≤50 字 |
| code | String | 部门编码，必填，唯一，≤20 字 |
| leader | String | 负责人，可空 |
| phone | String | 联系电话，可空 |
| status | Integer | 1 启用 / 0 停用 |
| createTime | String | `yyyy-MM-dd HH:mm:ss`，只读 |
| updateTime | String | `yyyy-MM-dd HH:mm:ss`，只读 |

---

## 十三、学完之后往哪走

按这个顺序补，每一步都能立刻用上：

1. **参数校验**：`@Valid` + `@NotBlank` + 自定义提示（阶段三已涉及）
2. **分页查询**：`LIMIT offset, size` + 一个 `count(*)`，理解为什么不能 `SELECT *` 全查
3. **事务**：`@Transactional`，用"转账"或"部门+员工同时插入"练手
4. **单元测试**：`@SpringBootTest` + MockMvc，体会"改代码不怕改坏"
5. **接口文档自动化**：Swagger / Knife4j，替代手写文档
6. **登录鉴权**：JWT + 拦截器

---

**遇到问题先做三件事**：看控制台 SQL → 看报错第一行（不是最后一行）→ 回到本文第十一节对照。
三件事做完还解决不了，再带着"报错截图 + 你已经试过什么"去问人。
