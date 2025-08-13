package tk.mybatis.simple.mapper;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.simple.model.SysUser;
import tk.mybatis.simple.model.SysRole;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public interface UserMapper {
    /**
     * 通过用户ID查询用户信息
     * @param id 用户ID
     * @return 用户对象，如果未找到返回 null
     */
    SysUser selectById(Long id);

    /**
     * 查询全部用户
     * @return 用户列表
     */
    List<SysUser> selectAll();

    /**
     * 根据用户ID查询角色列表
     * @param userId 用户ID
     * @return 角色列表
     */
    List<SysRole> selectRolesByUserId(Long userId);

    /**
     * 新增用户
     * @param sysUser 用户对象
     * @return 受影响的行数
     */
    int insert(SysUser sysUser);
    int insert2(SysUser sysUser);
    int insert3(SysUser sysUser);

    /**
     * 根据主键更新
     * @param sysUser 用户对象
     * @return 受影响的行数
     */
    int updateById(SysUser sysUser);

    /**
     * 根据主键删除用户
     * @param id 用户ID
     * @return 受影响的行数
     */
    int deleteById(Long id);

    /**
     * 根据动态条件查询用户信息
     * @param sysUser 用户对象
     * @return 用户列表
     */
    List<SysUser> selectByUser(SysUser sysUser);

    /**
     * 根据主键更新
     * @param sysUser 用户对象
     * @return 受影响的行数
     */
    int updateByIdSelective(SysUser sysUser);

    /**
     * 根据用户id或用户名查询
     * @param sysUser 用户对象
     * @return 用户对象
     */
    SysUser selectByIdOrUserName(SysUser sysUser);

    /**
     * 根据用户id集合查询
     * @param idList 用户id集合
     * @return 用户列表
     */
    List<SysUser> selectByIdList(List<Long> idList);

    /**
     * 批量插入用户信息
     * @param userList 用户信息列表
     * @return 受影响的行数
     */
    int insertList(List<SysUser> userList);

    /**
     * 通过Map更新列
     * @param map
     * @return
     */
    int updateByMap(Map<String, Object> map);

    /**
     * 根据用户ID获取用户信息和用户的角色信息
     *
     * @param id 用户ID
     * @return 包含角色信息的用户对象
     */
    SysUser selectUserAndRoleById(Long id);
    SysUser selectUserAndRoleById2(Long id);
    SysUser selectUserAndRoleByIdSelect(Long id);

    /**
     * 获取所有的用户以及对应的所有角色
     */
    List<SysUser> selectAllUserAndRoles();

    /**
     * 通过嵌套查询获取指定用户的信息以及用户的角色和权限信息
     * @param id 用户ID
     * @return 用户的所有信息
     */
    SysUser selectAllUserAndRolesSelect(Long id);

    /**
     * 使用存储过程查询用户信息
     * @param user 包含用户ID的实体对象，查询结果将填充到该对象中
     */
    void selectUserById(SysUser user);

    /**
     * 使用存储过程分页查询用户
     * @param params 包含以下键值：
     *   - userName (String): 模糊查询的用户名
     *   - pageNum (int): 当前页码（从1开始）
     *   - pageSize (int): 每页记录数
     *   - total (Long): 输出参数，返回总记录数
     * @return 当前页的用户列表
     */
    List<SysUser> selectUserPage(Map<String, Object> params);

    /**
     * 保存用户信息和关联角色信息
     * @param user     用户实体对象，包含以下字段：
     *                - userName     用户名（必填）
     *                - userPassword 密码（必填）
     *                - userEmail    邮箱（可选）
     *                - userInfo     附加信息（可选）
     *                - headImg      头像二进制数据（可选）
     * @param roleIds  角色ID列表，格式为逗号分隔的字符串（如 "1,2,3"）
     * @return         受影响的行数（通常为1）
     */
    int insertUserAndRoles(
            @Param("user") SysUser user,
            @Param("roleIds") String roleIds
    );

    /**
     * 根据用户ID删除用户及其关联角色信息
     * @param id 用户ID（必填）
     * @return   受影响的行数（用户表+用户角色关联表）
     */
    int deleteUserById(@Param("id") Long id);
}

