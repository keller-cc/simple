package tk.mybatis.simple.mapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.BeforeAll;

import java.io.IOException;
import java.io.Reader;

/**
 * 基础测试类（所有Mapper测试继承此类）
 */
public class BaseMapperTest {
    private static SqlSessionFactory sqlSessionFactory;

    @BeforeAll
    static void init() {
        try (Reader reader = Resources.getResourceAsReader("mybatis-config.xml")) {
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        } catch (IOException e) {
            throw new RuntimeException("初始化SqlSessionFactory失败", e);
        }
    }

    /**
     * 获取SqlSession（需手动关闭）
     */
    protected SqlSession getSqlSession() {
        return sqlSessionFactory.openSession();
    }
}