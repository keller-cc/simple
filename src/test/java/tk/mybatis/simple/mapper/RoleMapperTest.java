package tk.mybatis.simple.mapper;

import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;
import tk.mybatis.simple.model.SysPrivilege;
import tk.mybatis.simple.model.SysRole;
import tk.mybatis.simple.type.Enabled;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RoleMapperTest extends BaseMapperTest {
    @Test
    public void testSelectById()  {
        // 使用 try-with-resources 自动管理 SqlSession（推荐）
        try (SqlSession sqlSession = getSqlSession()) {
            RoleMapper roleMapper = sqlSession.getMapper(RoleMapper.class);

            // 1. 查询ID=1的角色
            SysRole role = roleMapper.selectById(1L);

            // 2. 验证结果
            assertNotNull(role, "角色对象不应为null");
            assertEquals("管理员", role.getRoleName(), "角色名应为'管理员'");

            // 打印日志（可选）
            System.out.println("角色信息: " + role);
        }
        // 无需手动关闭 sqlSession（try-with-resources 自动处理）
    }

    @Test
    public void testSelectById2()  {
        // 使用 try-with-resources 自动管理 SqlSession（推荐）
        try (SqlSession sqlSession = getSqlSession()) {
            RoleMapper roleMapper = sqlSession.getMapper(RoleMapper.class);

            // 1. 查询ID=1的角色
            SysRole role = roleMapper.selectById2(1L);

            // 2. 验证结果
            assertNotNull(role, "角色对象不应为null");
            assertEquals("管理员", role.getRoleName(), "角色名应为'管理员'");

            // 打印日志（可选）
            System.out.println("角色信息: " + role);
        }
        // 无需手动关闭 sqlSession（try-with-resources 自动处理）
    }

    @Test
    public void testSelectAll() {
        try (SqlSession sqlSession = getSqlSession()) {
            RoleMapper roleMapper = sqlSession.getMapper(RoleMapper.class);

            // 1. 查询所有角色
            List<SysRole> roleList = roleMapper.selectAll();

            // 2. 验证结果
            assertNotNull(roleList, "角色列表不应为null");
            assertTrue(roleList.size() > 0, "角色数量应大于0");

            // 3. 验证字段映射（驼峰命名）
            SysRole firstRole = roleList.get(0);
            assertNotNull(firstRole.getRoleName(), "角色名映射失败");

            // 打印日志（可选）
            roleList.forEach(role ->
                    System.out.printf("ID: %d, 角色名: %s\n", role.getId(), role.getRoleName())
            );
        }
        // 无需手动关闭 sqlSession（try-with-resources 自动处理）
    }

    @Test
    public void testInsert2() {
        try (SqlSession sqlSession = getSqlSession()) {
            RoleMapper roleMapper = sqlSession.getMapper(RoleMapper.class);

            // 1. 创建测试角色对象
            SysRole role = new SysRole();
            role.setRoleName("测试角色");
            role.setEnabled(Enabled.enabled);
            role.setCreateBy(1L); // 假设创建人ID=1
            role.setCreateTime(new Date()); // 当前时间

            // 2. 执行插入
            int result = roleMapper.insert2(role);
            assertEquals(1, result, "插入操作应返回影响行数1");

            // 3. 验证自增ID回填
            assertNotNull(role.getId(), "插入后ID应自动回填");
            System.out.println("插入成功，生成的角色ID: " + role.getId());
        }
    }

    @Test
    public void testSelectAllRoleAndPrivileges() {
        // 获取SqlSession
        try (SqlSession sqlSession = getSqlSession()) {
            // 获取RoleMapper接口
            RoleMapper roleMapper = sqlSession.getMapper(RoleMapper.class);

            // 执行查询
            List<SysRole> roleList = roleMapper.selectAllRoleAndPrivileges();

            // 验证结果
            assertNotNull( roleList);//"角色列表不应为null",
            assertTrue( roleList.size() > 0);//"角色列表不应为空",

            System.out.println("角色数量: " + roleList.size());

            // 打印角色及其权限
            for (SysRole role : roleList) {
                System.out.println("角色名称: " + role.getRoleName());
                System.out.println("权限数量: " + role.getPrivilegeList().size());

                for (SysPrivilege privilege : role.getPrivilegeList()) {
                    System.out.println("\t权限名称: " + privilege.getPrivilegeName() +
                            ", URL: " + privilege.getPrivilegeUrl());
                }
                System.out.println("----------------------");
            }
        }
    }


    @Test
    public void testSelectRoleByUserIdChoose() {
        // 获取sqlSession
        try (SqlSession sqlSession = getSqlSession()) {
            // 获取RoleMapper接口
            RoleMapper roleMapper = sqlSession.getMapper(RoleMapper.class);

            // 由于数据库数据enable都是1，所以给其中一个角色的enable赋值为0
            SysRole role = roleMapper.selectById(2L);
            System.out.println("修改前角色2状态: " + role.getEnabled());

            role.setEnabled(Enabled.disabled);
            roleMapper.updateById(role);
           // 获取用户1的角色
            List<SysRole> roleList = roleMapper.selectRoleByUserIdChoose(1L);

            for (SysRole r : roleList) {
                System.out.println("角色名: " + r.getRoleName());

                if (r.getId().equals(1L)) {
                    // 第一个角色存在权限信息
                    assertNotNull(r.getPrivilegeList());
                } else if (r.getId().equals(2L)) {
                    // 第二个角色的权限为null
                    ///assertNull(r.getPrivilegeList());
                    continue;
                }

                for (SysPrivilege privilege : r.getPrivilegeList()) {
                    System.out.println("权限名: " + privilege.getPrivilegeName());
                }
            }
        }
        // 不要忘记关闭sqlSession
    }

    @Test
    public void testUpdateById() {
        // 使用try-with-resources自动管理SqlSession（推荐方式）
        try (SqlSession sqlSession = getSqlSession()) {
            RoleMapper roleMapper = sqlSession.getMapper(RoleMapper.class);

            // 1. 查询测试角色（ID=2）
            SysRole role = roleMapper.selectById(2L);
            assertEquals(Enabled.enabled, role.getEnabled());//"初始状态应为启用",

            // 2. 修改状态为禁用
            role.setEnabled(Enabled.disabled);
            roleMapper.updateById(role);
        } // 自动回滚并关闭连接（无需finally块）
    }
}
