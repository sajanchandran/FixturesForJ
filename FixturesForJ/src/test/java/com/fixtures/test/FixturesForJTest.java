package com.fixtures.test;

import static junit.framework.Assert.assertEquals;

import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fixtures.main.FixturesForJ;
import com.fixtures.main.RowData;
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
		fixturesForJ.withTestData("src/test/java/com/fixtures/test").init();
		int count = jdbcTemplate.queryForInt("select count(name) from t_country");
		assertEquals(0, count);
	}
	
	@Test
	public void moreThanOneForeignKeyIsInvolved(){
		fixturesForJ.withTestData("src/test/resources/com/fixture/moreThanOneForeignKeyPerTable").init();
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
		fixturesForJ.withTestData("src/test/resources/com/fixture/foreignKeysInterdependentOnEachOther").init();
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
		dataMap.put("id", new PrimaryKey(3L));
		dataMap.put("type", "Honda");
		RowData rowData = new RowData(dataMap, "t_engine", "ENG_3");
		fixturesForJ.withTestData("src/test/resources/com/fixture/foreignKeysInterdependentOnEachOther").addRowData(rowData).init();
		Long engine_1 = jdbcTemplate.queryForLong("select engine_id from t_automobile where id=3");
		assertEquals(3, engine_1.longValue());
		fixturesForJ.clean();
	}
}
