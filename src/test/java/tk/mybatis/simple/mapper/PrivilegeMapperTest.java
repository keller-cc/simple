package tk.mybatis.simple.mapper;

import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;
import tk.mybatis.simple.model.SysPrivilege;

import static org.junit.jupiter.api.Assertions.*;

public class PrivilegeMapperTest extends BaseMapperTest{
    @Test
    public void testSelectById() {
        // 使用 try-with-resources 自动管理 SqlSession（推荐）
        try (SqlSession sqlSession = getSqlSession()) {
            // 1. 获取 Mapper 接口
            PrivilegeMapper privilegeMapper = sqlSession.getMapper(PrivilegeMapper.class);

            // 2. 调用方法查询权限（id=1）
            SysPrivilege privilege = privilegeMapper.selectById(1L);

            // 3. 验证结果
            assertNotNull(privilege, "权限对象不应为null");
            assertEquals("用户管理", privilege.getPrivilegeName(), "权限名称不匹配");

            // 打印日志（可选）
            System.out.println("权限ID: " + privilege.getId());
            System.out.println("权限名称: " + privilege.getPrivilegeName());
        }
        // 无需手动关闭 sqlSession（try-with-resources 自动处理）
    }
}
