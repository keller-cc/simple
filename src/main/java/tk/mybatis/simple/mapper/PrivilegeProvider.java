package tk.mybatis.simple.mapper;

import org.apache.ibatis.jdbc.SQL;

public class PrivilegeProvider {
    public String selectById(final Long id){
        return new SQL(){{
            SELECT("id, privilege_name, privilege_url");
            FROM("sys_privilege");
            WHERE("id = #{id}");
        }}.toString();
        /**或者直接返回字符串
         * return "select id,  privilege_name, privilege_url "+
         *      "from sys_privilege where id = #{id}";
         */
    }
}
