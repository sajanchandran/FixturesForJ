package com.fixtures.helpers;

import static junit.framework.Assert.assertEquals;

import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fixtures.data.structure.RowData;
import com.fixtures.helpers.ParseYaml;
import com.fixtures.helpers.PersistRecord;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={
		"classpath:com/fixtures/spring/database.xml",
		"classpath:com/fixtures/spring/config.xml"
		})
public class PersistRecordTest {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	private PersistRecord persistData;

	@Before
	public void onceBeforeEachTest(){
		persistData = new PersistRecord(jdbcTemplate);
	}
	
	@After
	public void onceAfterEachTest(){
		jdbcTemplate.update("delete from t_continent where continent_id=1");
		jdbcTemplate.update("delete from t_automobile where id=1");
		jdbcTemplate.update("delete from t_engine where id=1");
		
	}
	
	@Test
	public void persistDataCorrectly(){
		ParseYaml parser = new ParseYaml("src/test/resources/com/fixtures/data/t_continent.yml", null);
		Map<String, Object> data = parser.get("ASIA");
		RowData rowData = new RowData(data, "t_continent", null);
		persistData.persist(rowData);
		String key = jdbcTemplate.queryForObject("select name from t_continent where continent_id=1", String.class);
		assertEquals("ASIA", key);
	}
	
	@Test(expected=EmptyResultDataAccessException.class)
	public void deleteDataCorrectly(){
		ParseYaml parser = new ParseYaml("src/test/resources/com/fixtures/data/t_continent.yml", null);
		Map<String, Object> data = parser.get("ASIA");
		RowData rowData = new RowData(data, "t_continent", null);
		persistData.persist(rowData);
		persistData.clean(rowData);
		jdbcTemplate.queryForObject("select name from t_continent where continent_id=1", String.class);
	}
	
	@Test(expected=EmptyResultDataAccessException.class)
	public void deleteRecordCorrectlyWhenPrimaryKeyIsAString(){
		ParseYaml parser = new ParseYaml("src/test/resources/com/fixtures/data/tableWithStringPrimaryKey/t_school.yml", null);
		Map<String, Object> school1Data = parser.get("SCHOOL_1");
		RowData rowData = new RowData(school1Data, "t_school", "SCHOOL_1");
		persistData.persist(rowData);
		persistData.clean(rowData);
		jdbcTemplate.queryForObject("select country from t_school where name='ADM'", String.class);
	}
	
	@Test
	public void updateDataCorrectly(){
		ParseYaml engineParser = new ParseYaml("src/test/resources/com/fixtures/data/foreignKeysInterdependentOnEachOther/t_engine.yml", null);
		Map<String, Object> engineData = engineParser.get("ENG_1");
		RowData rowData = new RowData(engineData, "t_engine", "ENG_1");
		persistData.persist(rowData);
		engineData = engineParser.get("ENG_2");
		RowData rowData1 = new RowData(engineData, "t_engine", "ENG_2");
		persistData.persist(rowData1);
		persistData.update("type", rowData, rowData1);
		String engineType = jdbcTemplate.queryForObject("select type from t_engine where id=2", String.class);
		assertEquals("1", engineType);
		persistData.clean(rowData);
		persistData.clean(rowData1);
	}
}
