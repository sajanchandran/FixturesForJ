package com.fixtures.main;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

import com.fixtures.test.NullableForeignKey;
import com.fixtures.test.PrimaryKey;
import com.google.common.collect.Maps;

public class DataCacheHelperTest {
	
	private Map<String, Map<String, Object>> statesData;
	private Map<String, Map<String, Object>> countryData;


	@SuppressWarnings("unchecked")
	@Before
	public void onceBeforeEachTest() throws FileNotFoundException{
		String statesDataFilePath = "src/test/resources/com/fixtures/data/t_states.yml";
		String countryDataFilePath = "src/test/resources/com/fixtures/data/t_country.yml";
		Yaml yaml = new Yaml();
		statesData = (Map<String, Map<String, Object>>) yaml.load(new FileInputStream(new File(statesDataFilePath)));
		countryData = (Map<String, Map<String, Object>>) yaml.load(new FileInputStream(new File(countryDataFilePath)));
	}
	

	@Test
	public void getRowDataForParticularDataSection(){
		DataCacheHelper cacheHelper = new DataCacheHelper();
		cacheHelper.add(countryData, "t_country");
		cacheHelper.add(statesData, "t_states");
		RowData rowData = cacheHelper.getRowDataForSection(new NullableForeignKey("KER"));
		assertEquals("KERALA", rowData.getColumnValue("name"));
		assertEquals("KR", rowData.getColumnValue("code"));
		assertEquals(1000, rowData.getColumnValue("population"));
		assertEquals(new NullableForeignKey("IN"), rowData.getColumnValue("country_id"));
	}
	
	@Test
	public void addRowDataToCache(){
		DataCacheHelper cacheHelper = new DataCacheHelper();
		Map<String, Object> dataMap = Maps.newHashMap();
		dataMap.put("id", new PrimaryKey(3L));
		dataMap.put("type", "Honda");
		RowData rowData = new RowData(dataMap, "t_engine", "ENG_3");
		cacheHelper.add(rowData);
		dataMap = Maps.newHashMap();
		dataMap.put("id", new PrimaryKey(4L));
		dataMap.put("type", "Maruti");
		rowData = new RowData(dataMap, "t_engine", "ENG_4");
		cacheHelper.add(rowData);
		Map<String, TableData> mapOfTableNameVsData = cacheHelper.getMapOfTableNameVsData();
		assertEquals(2, mapOfTableNameVsData.size());
		assertTrue(mapOfTableNameVsData.containsKey("t_engine"));
	}
}
