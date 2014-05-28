package com.fixtures.main;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fixtures.data.structure.PrimaryKey;
import com.fixtures.data.structure.RowData;
import com.fixtures.main.FixturesForJ;
import com.google.common.collect.Maps;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={
		"classpath:com/fixtures/spring/database.xml",
		"classpath:com/fixtures/spring/config.xml"
		})
public class FixturesForJTest {

	@Autowired
	private FixturesForJ fixturesForJ;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Test
	public void initTest(){
		fixturesForJ.init();
		String key = jdbcTemplate.queryForObject("select name from t_country where key=1", String.class);
		assertEquals("Great Britain", key);		
		key = jdbcTemplate.queryForObject("select name from t_states where state_id=1", String.class);
		assertEquals("KERALA", key);
		key = jdbcTemplate.queryForObject("select country_id from t_states where state_id=1", String.class);
		assertEquals("2", key);
		Long countryCount = jdbcTemplate.queryForLong("select count(*) from t_country");
		assertEquals(4, countryCount.longValue());
		Long statesCount = jdbcTemplate.queryForLong("select count(*) from t_states");
		assertEquals(3, statesCount.longValue());
		fixturesForJ.clean();
	}
	
	@Test
	public void cleanTest(){
		fixturesForJ.init();
		int count = jdbcTemplate.queryForInt("select count(name) from t_country");
		assertEquals(4, count);
		fixturesForJ.clean();
		Long countryCount = jdbcTemplate.queryForLong("select count(*) from t_country");
		assertEquals(0, countryCount.longValue());
		Long statesCount = jdbcTemplate.queryForLong("select count(*) from t_states");
		assertEquals(0, statesCount.longValue());
	}
	
	@Test
	public void configurableFolderPath(){
		fixturesForJ.withTestData("src/test/java/com/fixtures/data").init();
		int count = jdbcTemplate.queryForInt("select count(name) from t_country");
		assertEquals(0, count);
		fixturesForJ.clean();
	}
	
	@Test
	public void moreThanOneForeignKeyIsInvolved(){
		fixturesForJ.withTestData("src/test/resources/com/fixtures/data/moreThanOneForeignKeyPerTable").init();
		Long engine_id_1 = jdbcTemplate.queryForLong("select engine_id from t_automobile where id=1");
		Long engine_id_2 = jdbcTemplate.queryForLong("select engine_id from t_automobile where id=2");
		Long type_1 = jdbcTemplate.queryForLong("select type from t_automobile where id=1");
		Long type_2 = jdbcTemplate.queryForLong("select type from t_automobile where id=2");
		fixturesForJ.clean();
		assertEquals(1, type_1.longValue());
		assertEquals(2, engine_id_1.longValue());
		assertEquals(2, type_2.longValue());
		assertEquals(1, engine_id_2.longValue());
	}
	
	@Test
	public void foreignKeysInterdependentOnEachOther(){
		fixturesForJ.withTestData("src/test/resources/com/fixtures/data/foreignKeysInterdependentOnEachOther").init();
		Long type_1 = jdbcTemplate.queryForLong("select type from t_automobile where id=1");
		Long engine_1 = jdbcTemplate.queryForLong("select engine_id from t_automobile where id=1");
		Long type_2 = jdbcTemplate.queryForLong("select type from t_automobile where id=2");
		Long engine_2 = jdbcTemplate.queryForLong("select engine_id from t_automobile where id=2");
		fixturesForJ.clean();
		assertEquals(2, type_1.longValue());
		assertEquals(2, engine_1.longValue());
		assertEquals(1, type_2.longValue());
		assertEquals(1, engine_2.longValue());
	}
	
	@Test
	public void addProgrammaticallySomeRows(){
		Map<String, Object> dataMap = Maps.newHashMap();
		dataMap.put("id", new PrimaryKey("3"));
		dataMap.put("type", "Honda");
		RowData rowData = new RowData(dataMap, "t_engine", "ENG_3");
		fixturesForJ.withTestData("src/test/resources/com/fixtures/data/foreignKeysInterdependentOnEachOther").addRowData(rowData).init();
		Long engine_1 = jdbcTemplate.queryForLong("select id from t_engine where id=3");
		assertEquals(3, engine_1.longValue());
		fixturesForJ.clean();
	}
	
	@Test
	public void tableHasStringPrimaryKey(){
		fixturesForJ.withTestData("src/test/resources/com/fixtures/data/tableWithStringPrimaryKey").init();
		String country = jdbcTemplate.queryForObject("select country from t_school where name='ADM'", new RowMapper<String>(){
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString("country");
			}
		});
		assertEquals("India", country);
		Date createdDate = jdbcTemplate.queryForObject("select created_date from t_school where name='ADM'", new RowMapper<Date>(){
			@Override
			public Date mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getDate("created_date");
			}
		});
		System.out.println("created date is -->>"+createdDate);
		assertNotNull(createdDate);
		fixturesForJ.clean();
	}
}
