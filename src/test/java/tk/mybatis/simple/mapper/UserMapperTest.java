package tk.mybatis.simple.mapper;

import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;
import tk.mybatis.simple.mapper.BaseMapperTest;
import tk.mybatis.simple.model.SysPrivilege;
import tk.mybatis.simple.model.SysRole;
import tk.mybatis.simple.model.SysUser;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class UserMapperTest extends BaseMapperTest {

    @Test
    public void testSelectById() {
        // 使用 try-with-resources 自动关闭资源（推荐）
        try (SqlSession sqlSession = getSqlSession()) {
            // 获取 UserMapper 接口
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

            // 调用 selectById 方法，查询 id = 1 的用户
            SysUser user = userMapper.selectById(1L);

            // 断言验证
            assertNotNull(user, "用户对象不应为 null");
            assertEquals("admin", user.getUserName(), "用户名应为 admin");
        }
    }

    @Test
    public void testSelectAll() {
        try (SqlSession sqlSession = getSqlSession()) {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

            // 调用 selectAll 方法查询所有用户
            List<SysUser> userList = userMapper.selectAll();

            // 断言验证
            assertNotNull(userList, "用户列表不应为 null");
            assertTrue(userList.size() > 0, "用户数量应大于 0");
        }
    }

    @Test
    public void testSelectRolesByUserId() {
        try (SqlSession sqlSession = getSqlSession()) {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

            // 调用 selectRolesByUserId 方法查询所有用户
            List<SysRole> roleList = userMapper.selectRolesByUserId(1L);

            // 断言验证
            assertNotNull(roleList, "角色列表不应为 null");
            assertTrue(roleList.size() > 0, "角色数量应大于 0");
        }
    }

    @Test
    public void testInsert() {
        // 使用 try-with-resources 自动管理 SqlSession（推荐）
        try (SqlSession sqlSession = getSqlSession()) {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

            // 创建测试用户对象
            SysUser user = new SysUser();
            user.setUserName("test1");
            user.setUserPassword("123456");
            user.setUserEmail("test@mybatis.tk");
            user.setUserInfo("test info");
            user.setHeadImg(new byte[]{1, 2, 3});  // 模拟头像数据
            user.setCreateTime(new Date());

            // 执行插入操作
            int result = userMapper.insert(user);

            // 验证插入结果
            assertEquals(1, result, "应成功插入1条数据");

            // 验证自增ID未回填（需配置 useGeneratedKeys）
            assertNull(user.getId(), "未配置 useGeneratedKeys 时 ID 应为 null");

            // 测试环境默认回滚，无需显式调用 rollback()
        }
    }

    @Test
    public void testInsert2() {
        // 使用 try-with-resources 自动管理 SqlSession（推荐）
        try (SqlSession sqlSession = getSqlSession()) {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

            // 创建测试用户对象
            SysUser user = new SysUser();
            user.setUserName("test1");
            user.setUserPassword("123456");
            user.setUserEmail("test@mybatis.tk");
            user.setUserInfo("test info");
            user.setHeadImg(new byte[]{1, 2, 3});  // 模拟头像数据
            user.setCreateTime(new Date());

            // 执行插入操作
            int result = userMapper.insert2(user);

            // 验证插入结果
            assertEquals(1, result, "应成功插入1条数据");

            // 启用自增主键后，验证ID已回填
            assertNotNull(user.getId(), "ID 应自动回填");
            System.out.println("插入的用户ID: " + user.getId());
            // 测试环境默认回滚，无需显式调用 rollback()
        }
    }

    @Test
    public void testInsert3() {
        // 使用 try-with-resources 自动管理 SqlSession（推荐）
        try (SqlSession sqlSession = getSqlSession()) {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

            // 创建测试用户对象
            SysUser user = new SysUser();
            user.setUserName("test1");
            user.setUserPassword("123456");
            user.setUserEmail("test@mybatis.tk");
            user.setUserInfo("test info");
            user.setHeadImg(new byte[]{1, 2, 3});  // 模拟头像数据
            user.setCreateTime(new Date());

            // 执行插入操作
            int result = userMapper.insert3(user);

            // 验证插入结果
            assertEquals(1, result, "应成功插入1条数据");

            // 启用自增主键后，验证ID已回填
            assertNotNull(user.getId(), "ID 应自动回填");
            System.out.println("插入的用户ID: " + user.getId());
            // 测试环境默认回滚，无需显式调用 rollback()
        }
    }

    @Test
    public void testUpdateById() {
        try (SqlSession sqlSession = getSqlSession()) {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

            // 1. 查询原始数据
            SysUser user = userMapper.selectById(1L);
            assertNotNull(user, "用户数据不应为null");
            assertEquals("admin", user.getUserName(), "初始用户名应为admin");

            // 2. 修改字段
            user.setUserName("admin_test");
            user.setUserEmail("test@mybatis.tk");

            // 3. 执行更新
            int result = userMapper.updateById(user);
            assertEquals(1, result, "应成功更新1条数据");

            // 4. 验证更新结果
            SysUser updatedUser = userMapper.selectById(1L);
            assertEquals("admin_test", updatedUser.getUserName(), "用户名更新失败");
            assertEquals("test@mybatis.tk", updatedUser.getUserEmail(), "邮箱更新失败");
        }
    }

    @Test
    public void testDeleteById() {
        try (SqlSession sqlSession = getSqlSession()) {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

            // === 测试1：通过ID删除 ===
            // 查询ID=1的用户
            SysUser user1 = userMapper.selectById(1L);
            assertNotNull(user1, "用户1应存在");

            // 删除ID=1的用户
            assertEquals(1, userMapper.deleteById(1L), "应成功删除1条数据");

            // 验证删除结果
            assertNull(userMapper.selectById(1L), "用户1应已被删除");

            // === 测试2：通过实体对象删除 ===
            // 查询ID=1001的用户
            SysUser user2 = userMapper.selectById(1001L);
            assertNotNull(user2, "用户2应存在");

            // 删除ID=1001的用户（假设deleteById支持SysUser参数）
            assertEquals(1, userMapper.deleteById(user2.getId()), "应成功删除1条数据");

            // 验证删除结果
            assertNull(userMapper.selectById(1001L), "用户2应已被删除");
        }
    }

    @Test
    public void testSelectByUser() {
        SqlSession sqlSession = getSqlSession();
        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

            // 只查询用户名时
            SysUser query = new SysUser();
            query.setUserName("ad");
            List<SysUser> userList = userMapper.selectByUser(query);
            assertTrue(userList.size() > 0);

            // 只查询用户邮箱时
            query = new SysUser();
            query.setUserEmail("test@mybatis.tk");
            userList = userMapper.selectByUser(query);
            assertTrue(userList.size() > 0);

            // 当同时查询用户名和邮箱时
            query = new SysUser();
            query.setUserName("ad");
            query.setUserEmail("test@mybatis.tk");
            userList = userMapper.selectByUser(query);

            // 由于没有同时符合这两个条件的用户，因此查询结果数为 0
            assertTrue(userList.size() == 0);
        } finally {
            // 不要忘记关闭 sqlSession
            sqlSession.close();
        }
    }

    @Test
    public void testUpdateByIdSelective() {
        SqlSession sqlSession = getSqlSession();
        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

            // 创建一个新的 user 对象
            SysUser user = new SysUser();
            // 更新 id = 1 的用户
            user.setId(1L);
            // 修改邮箱
            user.setUserEmail("test@mybatis.tk");

            // 更新邮箱，特别注意，这里的返回值result执行的是SQL影响的行数
            int result = userMapper.updateByIdSelective(user);
            // 只更新 1 条数据
            assertEquals(1, result);

            // 根据当前 id 查询修改后的数据
            user = userMapper.selectById(1L);
            // 修改后的名字保持不变，但是邮箱变成了新的
            assertEquals("admin", user.getUserName());
            assertEquals("test@mybatis.tk", user.getUserEmail());
        } finally {
            // 为了不影响其他测试，这里选择回滚
            sqlSession.rollback();
            // 不要忘记关闭 sqlSession
            sqlSession.close();
        }
    }

    @Test
    public void testInsert2Selective() {
        SqlSession sqlSession = getSqlSession();
        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

            // 创建一个 user 对象
            SysUser user = new SysUser();
            user.setUserName("test-selective");
            user.setUserPassword("123456");
            user.setUserInfo("test info");
            user.setCreateTime(new Date());

            // 插入数据库
            userMapper.insert2(user);

            // 获取插入的这条数据
            user = userMapper.selectById(user.getId());

            // 验证数据
            assertEquals("test-selective", user.getUserName());
            assertEquals("123456", user.getUserPassword());
            assertEquals("test info", user.getUserInfo());
        } finally {
            // 回滚事务
            sqlSession.rollback();
            // 不要忘记关闭 sqlSession
            sqlSession.close();
        }
    }

    @Test
    public void testSelectByIdOrUserName() {
        try (SqlSession sqlSession = getSqlSession()) {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

            // 测试同时有ID和用户名时（应该优先使用ID）
            SysUser query = new SysUser();
            query.setId(1L);
            query.setUserName("admin");
            SysUser user = userMapper.selectByIdOrUserName(query);
            assertNotNull(user);
            assertEquals(Long.valueOf(1L), user.getId());

            // 测试只有用户名时
            query.setId(null);
            user = userMapper.selectByIdOrUserName(query);
            assertNotNull(user);
            assertEquals("admin", user.getUserName());

            // 测试ID和用户名都为空时
            query.setUserName(null);
            user = userMapper.selectByIdOrUserName(query);
            assertNull(user);

        }
        // 不要忘记关闭 sqlSession
    }

    @Test
    public void testSelectByIdList() {
        try (SqlSession sqlSession = getSqlSession()) {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

            // Prepare test data
            List<Long> idList = new ArrayList<Long>();
            idList.add(1L);
            idList.add(1001L);

            // 业务逻辑中必须校验idList.size() > 0
            List<SysUser> userList = userMapper.selectByIdList(idList);

            // Verify results
            assertEquals(2, userList.size());

            // Additional assertions could be added here
            // For example:
            // assertEquals("admin", userList.get(0).getUserName());
            // assertEquals(Long.valueOf(1001L), userList.get(1).getId());

        }
        // 不要忘记关闭 sqlSession
    }

    @Test
    public void testInsertList() {
        try(SqlSession sqlSession = getSqlSession()) {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

            // 创建一个 user 列表
            List<SysUser> userList = new ArrayList<SysUser>();
            for (int i = 0; i < 2; i++) {
                SysUser user = new SysUser();
                user.setUserName("test" + i);
                user.setUserPassword("123456");
                user.setUserEmail("test@mybatis.tk");
                userList.add(user);
            }

            // 将新建的对象批量插入数据库中
            // 特别注意，这里的返回值 result 是执行 SQL 影响的行数
            int result = userMapper.insertList(userList);
            assertEquals(2, result);

            // 可以添加验证插入数据的代码
            for (SysUser user : userList) {
                assertNotNull(user.getId()); // 验证ID是否被回填
            }
        }
    }

    @Test
    public void testUpdateByMap(){
        try(SqlSession sqlSession = getSqlSession()){
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            Map<String, Object> map = new HashMap<String, Object>();

            map.put("id", 1L);
            map.put("user_email", "test@mybatis.tk");
            map.put("user_password", "12345678");

            userMapper.updateByMap(map);

            SysUser user = userMapper.selectById(1L);
            assertEquals("test@mybatis.tk", user.getUserEmail());
        }
    }

    @Test
    public void testSelectUserAndRoleById() {
        try (SqlSession sqlSession = getSqlSession()) {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

            // Test with a user that has exactly one role (id=1001L)
            Long testUserId = 1001L;
            SysUser user = userMapper.selectUserAndRoleById(testUserId);

            // Verify user exists
            assertNotNull(user);//"User should not be null",

            // Verify role information exists
            assertNotNull(user.getRole());//"User role should not be null"
        }
        // Close sqlSession
    }

    @Test
    public void testSelectUserAndRoleById2() {
        try (SqlSession sqlSession = getSqlSession()) {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

            // Test with a user that has exactly one role (id=1001L)
            Long testUserId = 1001L;
            SysUser user = userMapper.selectUserAndRoleById2(testUserId);

            // Verify user exists
            assertNotNull(user);//"User should not be null",

            // Verify role information exists
            assertNotNull(user.getRole());//"User role should not be null"
        }
        // Close sqlSession
    }

    @Test
    public void testSelectUserAndRoleByIdSelect() {
        // 获取 SqlSession
        try (SqlSession sqlSession = getSqlSession()) {
            // 获取 UserMapper 接口
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

            // 测试用户ID（注意：避免使用硬编码，建议用测试数据）
            Long userId = 1001L; // 修正数字格式（从 lOOlL → 1001L）

            // 执行查询
            SysUser user = userMapper.selectUserAndRoleByIdSelect(userId);

            // 验证结果
            assertNotNull(user);//"用户对象不应为null",
            System.out.println("调用user.getRole()");
            assertNotNull(user.getRole());//"用户角色不应为null",

            // 关闭 SqlSession（确保资源释放）
        }
    }

    @Test
    public void testSelectAllUserAndRoles() {
        // Get sqlSession
        try (SqlSession sqlSession = getSqlSession()) {
            // Get UserMapper interface
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            List<SysUser> userList = userMapper.selectAllUserAndRoles();

            System.out.println("用户数量: " + userList.size());

            for (SysUser user : userList) {
                System.out.println("用户名: " + user.getUserName());
                for (SysRole role : user.getRoleList()) {
                    System.out.println("角色名: " + role.getRoleName());
                    for (SysPrivilege privilege : role.getPrivilegeList()){
                        System.out.println("权限名: " + privilege.getPrivilegeName());
                    }
                }
            }
        }
        // Don't forget to close sqlSession
    }

    @Test
    public void testSelectAllUserAndRolesSelect() {
        // 获取sqlSession
        try (SqlSession sqlSession = getSqlSession()) {
            // 获取UserMapper接口
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

            // 执行查询（注意参数是1L不是lL）
            SysUser user = userMapper.selectAllUserAndRolesSelect(1L);

            // 验证用户不为null
            assertNotNull( user);//"用户不应为null",
            System.out.println("用户名: " + user.getUserName());

            // 验证角色列表
            assertNotNull(user.getRoleList());//"角色列表不应为null",
            for (SysRole role : user.getRoleList()) {
                System.out.println("角色名: " + role.getRoleName());

                // 验证权限列表
                if (role.getPrivilegeList() != null) {
                    for (SysPrivilege privilege : role.getPrivilegeList()) {
                        System.out.println("权限名: " + privilege.getPrivilegeName());
                    }
                }
            }
        }
        // 关闭sqlSession（注意使用close()而不是close{)
    }

    @Test
    public void testSelectUserById() {
        // 使用try-with-resources自动关闭sqlSession（Java 7+）
        try (SqlSession sqlSession = getSqlSession()) {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

            // 准备测试数据
            SysUser user = new SysUser();
            user.setId(1L);  // 使用明确的测试ID

            // 调用存储过程（结果会自动填充到user对象）
            userMapper.selectUserById(user);

            // 验证结果
            assertNotNull(user.getUserName());//"用户名不应为null",
            System.out.println("用户名: " + user.getUserName());

        }
    }

    @Test
    public void testSelectUserPage() {
        // 使用try-with-resources自动管理SqlSession（Java 7+语法）
        try (SqlSession sqlSession = getSqlSession()) {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

            // 1. 准备参数（修正参数名和格式）
            Map<String, Object> params = new HashMap<>();
            params.put("userName", "ad");  // 模糊查询关键词
            params.put("offset", 0);       // 偏移量从0开始
            params.put("limit", 10);        // 每页10条

            // 2. 调用存储过程
            List<SysUser> userList = userMapper.selectUserPage(params);
            Long total = (Long) params.get("total");

            // 3. 打印结果（调试用）
            System.out.println("总数: " + total);
            userList.forEach(user ->
                    System.out.println("用户名: " + user.getUserName())
            );

        } // 自动关闭sqlSession（无需finally块）
    }

    @Test
    public void testInsertAndDelete() {
        // 使用try-with-resources自动管理SqlSession（推荐方式）
        try (SqlSession sqlSession = getSqlSession()) {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

            // 1. 准备测试用户数据
            SysUser user = new SysUser();
            user.setUserName("test_user");  // 修正：移除特殊字符
            user.setUserPassword("123456");
            user.setUserEmail("test@mybatis.tk");
            user.setUserInfo("test info");
            user.setHeadImg(new byte[]{1, 2, 3});  // 模拟头像二进制数据

            // 2. 插入用户及关联角色
            userMapper.insertUserAndRoles(user, "1,2");

            // 3. 验证插入结果
            assertNotNull( user.getId());//"用户ID应被自动生成",
            assertNotNull( user.getCreateTime());//"创建时间应被赋值",
            System.out.println("插入成功，用户ID: " + user.getId());

            // 4. 提交事务（如需查看数据库数据，取消注释）
            // sqlSession.commit();

            // 5. 删除测试用户
            int deletedRows = userMapper.deleteUserById(user.getId());
            assertEquals( 1, deletedRows);//"应删除1条用户记录",

            // 6. 验证删除结果
            SysUser deletedUser = userMapper.selectById(user.getId());
            assertNull( deletedUser);//"用户应已被删除",
            System.out.println("删除成功，用户ID: " + user.getId());

        } // 自动关闭sqlSession（无需finally块）
    }
}