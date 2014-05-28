package com.fixtures.data.structure;

import static com.google.common.collect.ImmutableMap.of;
import static junit.framework.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.fixtures.data.structure.NonNullableForeignKey;
import com.fixtures.data.structure.NullableForeignKey;
import com.fixtures.data.structure.PrimaryKey;
import com.fixtures.data.structure.RowData;
import com.fixtures.data.structure.TableData;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;


public class TableDataTest {

	private List<RowData> rows = Lists.newArrayList();
	
	@Before
	public void onceBeforeEachTest(){
		givenDataSetUp();
	}
	
	@Test
	public void getRowDataWithNoForeignKey(){
		TableData tableData = new TableData(rows);
		List<RowData> listOfRowData = tableData.getListOfRowDataWithNoForeignKey();
		assertEquals(1, listOfRowData.size());
		assertEquals("JEEP", listOfRowData.get(0).getSectionName());
	}
	
	@Test
	public void getRowDataWithNullableForeignKey(){
		TableData tableData = new TableData(rows);
		List<RowData> listOfRowData = tableData.getListOfRowDataWithNullableForeignKey();
		assertEquals(2, listOfRowData.size());
		assertEquals("LORRY", listOfRowData.get(1).getSectionName());
	}
	
	@Test
	public void addRowDataAddsToTheTableCorrectly(){
		TableData tableData = new TableData(rows);
		ImmutableMap<String, Object> rowDataMap = of("name", (Object)"suv", "id", (Object)new PrimaryKey("3"));
		RowData rowData = new RowData(rowDataMap, "t_automobile", "SUV");
		tableData.addRowData(rowData);
		List<String> sections = tableData.getSections();
		assertEquals(4, sections.size());
	}
	

	private void givenDataSetUp() {
		RowData row1 = new RowData(of("name", (Object)new String("jeep"), "id", (Object)new PrimaryKey("1")), "t_automobile", "JEEP");
		rows.add(row1);
		RowData row2 = new RowData(of("name", (Object)new String("car"),"id", (Object)new NullableForeignKey("1"), "engine_id", (Object)new NonNullableForeignKey("1")), 
				"t_automobile", "CAR");
		rows.add(row2);
		RowData row3 = new RowData(of("name", (Object)new String("lorry"),"id", (Object)new NullableForeignKey("1")), "t_automobile", "LORRY");
		rows.add(row3);
	}	
}
