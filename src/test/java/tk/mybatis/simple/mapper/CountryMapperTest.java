package tk.mybatis.simple.mapper;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;
import tk.mybatis.simple.model.Country;
import tk.mybatis.simple.mapper.BaseMapperTest;
import tk.mybatis.simple.model.CountryExample;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CountryMapperTest extends BaseMapperTest {
    @Test
    void testSelectAll() {
        // 使用 try-with-resources 自动关闭资源
        try (SqlSession sqlSession = getSqlSession()) {
            List<Country> countryList = sqlSession.selectList(
                    "tk.mybatis.simple.mapper.CountryMapper.selectAll"
            );
            printCountryList(countryList);
        }
    }
    @Test
    public void testExample() {
        SqlSession sqlSession = getSqlSession();
        try {
            CountryMapper countryMapper = sqlSession.getMapper(CountryMapper.class);

            // 1. Create Example object
            CountryExample example = new CountryExample();

            // 2. Set ordering (ID descending, countryname ascending)
            example.setOrderByClause("id desc, countryname asc");

            // 3. Set DISTINCT
            example.setDistinct(true);

            // 4. Create primary criteria
            CountryExample.Criteria criteria = example.createCriteria();
            // ID between 1 and 3
            criteria.andIdGreaterThanOrEqualTo(1);
            criteria.andIdLessThan(4);
            // Country code containing 'U' (with wildcards)
            criteria.andCountrycodeLike("%U%");

            // 5. Create OR criteria
            CountryExample.Criteria orCriteria = example.or();
            orCriteria.andCountrynameEqualTo("中国");

            // 6. Execute query
            List<Country> countryList = countryMapper.selectByExample(example);

            // 7. Verify results
            assertNotNull( countryList);//"Country list should not be null",
            assertTrue( countryList.size() > 0);//"Should return at least one country",

            // 8. Print results (optional)
            printCountryList(countryList);

        } finally {
            // Close sqlSession
            sqlSession.close();
        }
    }

    @Test
    public void testUpdateByExampleSelective() {
        try (SqlSession sqlSession = getSqlSession()) {
            // 1. Get mapper interface
            CountryMapper countryMapper = sqlSession.getMapper(CountryMapper.class);

            // 2. Create example with criteria (id > 2)
            CountryExample example = new CountryExample();
            CountryExample.Criteria criteria = example.createCriteria();
            criteria.andIdGreaterThan(2);

            // 3. Create update template
            Country country = new Country();
            country.setCountryname("China"); // Only update countryname field

            // 4. Execute selective update
            countryMapper.updateByExampleSelective(country, example);

            // 5. Verify update
            printCountryList(countryMapper.selectByExample(example));
        }
        // 7. Close sqlSession
    }

    @Test
    public void testDeleteByExample() {
        try (SqlSession sqlSession = getSqlSession()) {
            // 1. Get mapper interface
            CountryMapper countryMapper = sqlSession.getMapper(CountryMapper.class);

            // 2. Create example with criteria (id > 2)
            CountryExample example = new CountryExample();
            CountryExample.Criteria criteria = example.createCriteria();
            criteria.andIdGreaterThan(2);

            countryMapper.deleteByExample(example);

            assertEquals(0, countryMapper.countByExample(example));
        }
        // 7. Close sqlSession
    }

    private void printCountryList(List<Country> countries) {
        for (Country country : countries) {
            System.out.printf(
                    "ID: %d, Name: %s, Code: %s\n",
                    country.getId(),
                    country.getCountryname(),
                    country.getCountrycode()
            );
        }
    }

}