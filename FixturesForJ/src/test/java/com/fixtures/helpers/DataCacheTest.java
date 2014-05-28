package com.fixtures.helpers;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

import com.fixtures.data.structure.NullableForeignKey;
import com.fixtures.data.structure.PrimaryKey;
import com.fixtures.data.structure.RowData;
import com.fixtures.data.structure.TableData;
import com.fixtures.helpers.DataCache;
import com.google.common.collect.Maps;

public class DataCacheTest {
	
	private Map<String, Map<String, Object>> statesData;
	private Map<String, Map<String, Object>> countryData;
	private DataCache cacheHelper;


	@SuppressWarnings("unchecked")
	@Before
	public void onceBeforeEachTest() throws FileNotFoundException{
		String statesDataFilePath = "src/test/resources/com/fixtures/data/t_states.yml";
		String countryDataFilePath = "src/test/resources/com/fixtures/data/t_country.yml";
		Yaml yaml = new Yaml();
		statesData = (Map<String, Map<String, Object>>) yaml.load(new FileInputStream(new File(statesDataFilePath)));
		countryData = (Map<String, Map<String, Object>>) yaml.load(new FileInputStream(new File(countryDataFilePath)));
		cacheHelper = new DataCache();
	}
	

	@Test
	public void getRowDataForParticularDataSection(){
		givenCacheHelperHasStatesAndCountryData();
		RowData rowData = whenGetRowDataForSectionIsInvoked();
		assertEquals("KERALA", rowData.getColumnValue("name"));
		assertEquals("KR", rowData.getColumnValue("code"));
		assertEquals(1000, rowData.getColumnValue("population"));
		assertEquals(new NullableForeignKey("IN"), rowData.getColumnValue("country_id"));
	}
	
	@Test
	public void addRowDataToCache(){
		Map<String, Object> dataMap = Maps.newHashMap();
		dataMap.put("id", new PrimaryKey("3"));
		dataMap.put("type", "Honda");
		RowData rowData = new RowData(dataMap, "t_engine", "ENG_3");
		cacheHelper.add(rowData);
		dataMap = Maps.newHashMap();
		dataMap.put("id", new PrimaryKey("4"));
		dataMap.put("type", "BMW");
		rowData = new RowData(dataMap, "t_engine", "ENG_4");
		cacheHelper.add(rowData);
		dataMap = Maps.newHashMap();
		dataMap.put("id", new PrimaryKey("4"));
		dataMap.put("n", "India");
		rowData = new RowData(dataMap, "t_country", "IN");
		cacheHelper.add(rowData);
		Map<String, TableData> mapOfTableNameVsData = cacheHelper.getMapOfTableNameVsData();
		assertEquals(2, mapOfTableNameVsData.size());
		assertTrue(mapOfTableNameVsData.containsKey("t_engine"));
		assertTrue(mapOfTableNameVsData.containsKey("t_country"));
		assertEquals(1, mapOfTableNameVsData.get("t_country").getSections().size());
		assertEquals(2, mapOfTableNameVsData.get("t_engine").getSections().size());
	}

	private RowData whenGetRowDataForSectionIsInvoked() {
		RowData rowData = cacheHelper.getRowDataForSection(new NullableForeignKey("KER"));
		return rowData;
	}

	private void givenCacheHelperHasStatesAndCountryData() {
		cacheHelper.add(countryData, "t_country");
		cacheHelper.add(statesData, "t_states");
	}	
}
