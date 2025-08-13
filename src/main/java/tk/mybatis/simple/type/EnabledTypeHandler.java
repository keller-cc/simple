package tk.mybatis.simple.type;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

/**
 * Enabled 枚举类型处理器
 * 功能：实现 Enabled 枚举与数据库整数字段的双向转换
 */
public class EnabledTypeHandler implements TypeHandler<Enabled> {
    private final Map<Integer, Enabled> enabledMap = new HashMap<>();

    public EnabledTypeHandler() {
        // 初始化枚举映射
        for (Enabled enabled : Enabled.values()) {
            enabledMap.put(enabled.getValue(), enabled);
        }
    }

    @Override
    public void setParameter(
            PreparedStatement ps,
            int i,
            Enabled parameter,
            JdbcType jdbcType
    ) throws SQLException {
        // 将枚举值写入数据库（存储对应的整数值）
        ps.setInt(i, parameter.getValue());
    }

    @Override
    public Enabled getResult(ResultSet rs, String columnName) throws SQLException {
        // 从数据库读取整数值并转换为枚举
        int value = rs.getInt(columnName);
        return enabledMap.get(value);
    }

    @Override
    public Enabled getResult(ResultSet rs, int columnIndex) throws SQLException {
        // 按列索引获取枚举
        int value = rs.getInt(columnIndex);
        return enabledMap.get(value);
    }

    @Override
    public Enabled getResult(CallableStatement cs, int columnIndex) throws SQLException {
        // 处理存储过程返回的枚举
        int value = cs.getInt(columnIndex);
        return enabledMap.get(value);
    }
}