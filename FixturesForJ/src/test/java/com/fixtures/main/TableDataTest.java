package com.fixtures.main;

import static junit.framework.Assert.assertEquals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.fixtures.test.NonNullableForeignKey;
import com.fixtures.test.NullableForeignKey;
import com.fixtures.test.PrimaryKey;
import com.google.common.collect.ImmutableMap;

public class TableDataTest {

	private void givenDataSetUp(Map<String, Map<String, Object>> data) {
		data.put("JEEP", ImmutableMap.of("name", (Object)new String("jeep"), "id", (Object)new PrimaryKey(1L)));
		data.put("CAR", ImmutableMap.of("name", (Object)new String("car"),"id", (Object)new NullableForeignKey("1"), "engine_id", (Object)new NonNullableForeignKey("1")));
		data.put("LORRY", ImmutableMap.of("name", (Object)new String("lorry"),"id", (Object)new NullableForeignKey("1")));
	}
	
	@Test
	public void getRowDataWithNoForeignKey(){
		Map<String, Map<String, Object>> data = new HashMap<String, Map<String,Object>>();
		givenDataSetUp(data);
		TableData tableData = new TableData(data, "t_automobile");
		List<RowData> listOfRowData = tableData.getListOfRowDataWithNoForeignKey();
		assertEquals(1, listOfRowData.size());
		assertEquals("JEEP", listOfRowData.get(0).getSectionName());
	}
	
	@Test
	public void getRowDataWithNullableForeignKey(){
		Map<String, Map<String, Object>> data = new HashMap<String, Map<String,Object>>();
		givenDataSetUp(data);
		TableData tableData = new TableData(data, "t_automobile");
		List<RowData> listOfRowData = tableData.getListOfRowDataWithNullableForeignKey();
		assertEquals(2, listOfRowData.size());
		assertEquals("LORRY", listOfRowData.get(0).getSectionName());
	}
}
