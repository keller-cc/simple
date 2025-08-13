package tk.mybatis.simple.mapper;

import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;
import tk.mybatis.simple.model.SysRole;
import tk.mybatis.simple.model.SysUser;

import static org.junit.jupiter.api.Assertions.*;

public class CacheTest extends BaseMapperTest {
    @Test
    public void testLocalCache() {
        // ==== 第一个 SqlSession ====
        SysUser user1 = null;
        try (SqlSession sqlSession1 = getSqlSession()) {
            UserMapper mapper1 = sqlSession1.getMapper(UserMapper.class);

            // 第一次查询（缓存到一级缓存）
             user1 = mapper1.selectById(1L);
            assertNotNull( user1);//"用户1应存在",

            // 修改对象属性（内存中修改，未提交数据库）
            user1.setUserName("New Name");

            // 第二次查询（命中缓存）
            SysUser user2 = mapper1.selectById(1L);
            assertEquals(user1, user2);//"user1和user2应是同一实例",
            assertEquals("New Name", user2.getUserName());//"user2应反映内存修改",
        } // 自动关闭sqlSession1（清空一级缓存）
        System.out.println("开启新的sqlSession");
        // ==== 第二个 SqlSession ====
        try (SqlSession sqlSession2 = getSqlSession()) {
            UserMapper mapper2 = sqlSession2.getMapper(UserMapper.class);

            // 新Session查询（重新从数据库加载）
            SysUser user3 = mapper2.selectById(1L);
            assertNotEquals("New Name", user3.getUserName());//"user3的用户名应为原始值",

            // 执行删除操作（清空当前Session的缓存）
            mapper2.deleteById(2L);

            // 再次查询（重新从数据库加载）
            SysUser user4 = mapper2.selectById(1L);
            assertNotEquals(user3, user4);//"delete操作后应刷新缓存",
        }
    }

    @Test
    public void testL2Cache() {
        // ==== 第一个 SqlSession ====
        SysRole role1=null;
        try (SqlSession sqlSession1 = getSqlSession()) {
            RoleMapper roleMapper1 = sqlSession1.getMapper(RoleMapper.class);

            // 第一次查询（数据库）
            role1 = roleMapper1.selectById(1L);
            assertNotNull(role1);//"角色1应存在",

            // 修改内存对象（未提交数据库）
            role1.setRoleName("New Name");

            // 第二次查询（一级缓存命中）
            SysRole role2 = roleMapper1.selectById(1L);
            assertEquals(role1, role2);//"role1和role2应是同一实例",
            assertEquals( "New Name", role2.getRoleName());//"role2应反映内存修改",
        }
        // 关闭时提交二级缓存

        // ==== 第二个 SqlSession ====
        System.out.println("开启新的 sqlSession");
        try (SqlSession sqlSession2 = getSqlSession()) {
            RoleMapper roleMapper2 = sqlSession2.getMapper(RoleMapper.class);

            // 新Session查询（命中二级缓存）
            SysRole role3 = roleMapper2.selectById(1L);
            assertEquals( "New Name", role3.getRoleName());//"二级缓存应返回修改后的值",
            assertNotEquals( role1, role3);//"role3是新实例（反序列化生成）",

            // 再次查询（仍命中二级缓存）
            SysRole role4 = roleMapper2.selectById(1L);
            assertNotEquals(role3, role4);//"二级缓存每次返回新实例",
        }
    }
}