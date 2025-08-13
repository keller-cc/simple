package tk.mybatis.simple.mapper;

import org.apache.ibatis.annotations.*;
import tk.mybatis.simple.model.SysRole;

import java.util.List;

@CacheNamespaceRef(RoleMapper.class)
public interface RoleMapper {

    @Select({"select id, role_name roleName, enabled, create_by createBy, create_time createTime ",
            "from sys_role ",
            "where id = #{id}" })
    SysRole selectById(Long id);

    @Results(id = "roleResultMap", value = {
            @Result(property = "id", column = "id", id = true),
            @Result(property = "roleName", column = "role_name"),
            @Result(property = "enabled", column = "enabled"),
            @Result(property = "createBy", column = "create_by"),
            @Result(property = "createTime", column = "create_time")
    })
    @Select("SELECT id, role_name, enabled, create_by, create_time FROM sys_role WHERE id = #{id}")
    SysRole selectById2(Long id);


    @ResultMap("roleResultMap")
    @Select("select * from sys_role")
    List<SysRole> selectAll();

    @Insert({"insert into sys_role(id, role_name, enabled, create_by, create_time)", "" +
            "values(#{id}, #{roleName}, #{enabled}, #{createBy}, #{createTime, jdbcType=TIMESTAMP})" })
    int insert(SysRole sysRole);

    @Insert({"insert into sys_role(role_name, enabled, create_by, create_time)", "" +
            "values(#{roleName}, #{enabled}, #{createBy}, #{createTime, jdbcType=TIMESTAMP})" })
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert2(SysRole sysRole);

    @Insert({"insert into sys_role(role_name, enabled, create_by, create_time)", "" +
            "values(#{roleName}, #{enabled}, #{createBy}, #{createTime, jdbcType=TIMESTAMP})" })
    @SelectKey(statement = "SELECT LAST_INSERT_ID()",
            keyProperty = "id",
            resultType = Long.class,
            before = false)
    int insert3(SysRole sysRole);

    @Update({"update sys_role ",
            "set role_name = #{roleName}, ",
                "create_by = #{createBy}, ",
                "create_time = #{createTime, jdbcType = TIMESTAMP} ",
            "where id = #{id}"
    })
    int updateById(SysRole sysRole);

    @Delete("delete from sys_role where id = #{id}")
    int deleteById(Long id);

    SysRole selectRoleById(Long id);

    List<SysRole> selectAllRoleAndPrivileges();

    List<SysRole> selectRoleByUserId(Long id);

    /**
     * 根据用户ID获取用户的角色信息
     * @param userId 用户ID
     * @return 角色列表
     */
    List<SysRole> selectRoleByUserIdChoose(Long userId);
}

