package com.fixtures.main;

import static com.google.common.collect.ImmutableMap.of;
import static junit.framework.Assert.assertEquals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.fixtures.test.NonNullableForeignKey;
import com.fixtures.test.NullableForeignKey;
import com.fixtures.test.PrimaryKey;
import com.google.common.collect.ImmutableMap;


public class TableDataTest {

	private Map<String, Map<String, Object>> data;
	
	@Before
	public void onceBeforeEachTest(){
		data = new HashMap<String, Map<String,Object>>();
		givenDataSetUp(data);
	}
	
	@Test
	public void getRowDataWithNoForeignKey(){
		TableData tableData = new TableData(data, "t_automobile");
		List<RowData> listOfRowData = tableData.getListOfRowDataWithNoForeignKey();
		assertEquals(1, listOfRowData.size());
		assertEquals("JEEP", listOfRowData.get(0).getSectionName());
	}
	
	@Test
	public void getRowDataWithNullableForeignKey(){
		TableData tableData = new TableData(data, "t_automobile");
		List<RowData> listOfRowData = tableData.getListOfRowDataWithNullableForeignKey();
		assertEquals(2, listOfRowData.size());
		assertEquals("LORRY", listOfRowData.get(0).getSectionName());
	}
	
	@Test
	public void addRowDataAddsToTheTableCorrectly(){
		TableData tableData = new TableData(data, "t_automobile");
		ImmutableMap<String, Object> rowDataMap = of("name", (Object)"suv", "id", (Object)new PrimaryKey("3"));
		RowData rowData = new RowData(rowDataMap, "t_automobile", "SUV");
		tableData.addRowData(rowData);
		Set<String> sections = tableData.getSections();
		assertEquals(4, sections.size());
	}
	

	private void givenDataSetUp(Map<String, Map<String, Object>> data) {
		data.put("JEEP", of("name", (Object)new String("jeep"), "id", (Object)new PrimaryKey("1")));
		data.put("CAR", of("name", (Object)new String("car"),"id", (Object)new NullableForeignKey("1"), "engine_id", (Object)new NonNullableForeignKey("1")));
		data.put("LORRY", of("name", (Object)new String("lorry"),"id", (Object)new NullableForeignKey("1")));
	}	
}
