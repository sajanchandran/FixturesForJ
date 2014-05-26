package com.fixtures.main;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fixtures.test.ForeignKey;
import com.fixtures.test.NullableForeignKey;
import com.fixtures.test.NonNullableForeignKey;
import com.fixtures.test.PrimaryKey;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

public class RowDataTest {

	private RowData rowData;

	@Before
	public void onceBeforeEachTest(){
		Map<String, Object> rowDatas = new HashMap<String, Object>();
		rowDatas.put("id", new PrimaryKey("1"));
		rowDatas.put("name", "Sajan");
		rowDatas.put("foreign_id_in", new NullableForeignKey("IN"));
		rowDatas.put("foreign_id_in1", new NullableForeignKey("IN"));
		rowDatas.put("foreign_id_uk", new NullableForeignKey("UK"));
		rowDatas.put("non_nullable_foreign_id", new NonNullableForeignKey("US"));
		rowData = new RowData(rowDatas, "t_country", null);
	}
	
	@Test
	public void hasNullableForeignKey(){
		assertTrue(rowData.hasNullableForeignKey());
	}
	
	@Test
	public void isRowHasNonNullableForeignKey(){
		assertTrue(rowData.hasNonNullableForeignKey());
	}
	
	@Test
	public void getForeignKey(){
		assertEquals(3, rowData.getNullableForeignKeys().size());
	}
	
	@Test
	public void getPrimaryKey(){
		assertEquals(new PrimaryKey("1"), rowData.getPrimaryKey());
	}
	
	@Test
	public void getPrimaryKeyColumnName(){
		assertEquals("id", rowData.getPrimaryKeyColumnName());
	}
	
	@Test
	public void getTableName(){
		assertEquals("t_country", rowData.getTableName());
	}
	
	@Test
	public void getSpecifiedNullableForeignKey(){
		assertEquals(new NullableForeignKey("UK"), rowData.getNullableForeignKey("UK"));
	}
	
	@Test
	public void changeForeignKeyValue(){
		int foreignKeyReplacedCount = 0;
		rowData.changeNullableForeignKey(new PrimaryKey("2"), new NullableForeignKey("IN"));
		for (ForeignKey foreignKey : rowData.getNullableForeignKeys()) {
			if(foreignKey.toString().equals("2")){
				foreignKeyReplacedCount+=1;
			}
		}
		assertEquals(2, foreignKeyReplacedCount);
	}
	
	@Test
	public void changeForeignKeyValueSetNullWhenNull(){
		rowData.changeNullableForeignKey(null, new NullableForeignKey("IN"));
		assertEquals(2, rowData.getNullableForeignKeys().size());
		assertEquals(new NullableForeignKey("UK"), rowData.getNullableForeignKeys().get(0));
	}
	
	@Test
	public void changeNonNullableForeignKeyValue(){
		rowData.changeNonNullableForeignKey(new PrimaryKey("2"));
		assertEquals(new NonNullableForeignKey("2"), rowData.getNonNullableForeignKey());
	}
	
	@Test
	public void changeNonNullableForeignKeyValueSetNullWhenNull(){
		rowData.changeNonNullableForeignKey(null);
		assertFalse(rowData.hasNonNullableForeignKey());
	}
	
	@Test
	public void getColumnNames(){
		assertEquals(ImmutableSet.of("id","name","foreign_id_in","foreign_id_in1","foreign_id_uk", "non_nullable_foreign_id"), rowData.getColumnNames());
	}
	
	@Test
	public void getColumnValues(){
		assertEquals(new PrimaryKey("1"), rowData.getColumnValue("id"));
	}
	
	@Test
	public void equalsMethodCorrectlyChecksPrimaryKey(){
		Map<String, Object> rowDatas = new HashMap<String, Object>();
		rowDatas.put("id", new PrimaryKey("1"));
		rowDatas.put("name", "Sajan");
		rowDatas.put("foreign_id", new NullableForeignKey("IN"));
		rowData = new RowData(rowDatas, "t_country", null);
		
		Map<String, Object> rowDatas1 = new HashMap<String, Object>();
		rowDatas1.put("id", new PrimaryKey("1"));
		rowDatas1.put("name", "Sajan");
		rowDatas1.put("foreign_id", new NullableForeignKey("IN"));
		RowData rowData1 = new RowData(rowDatas1, "t_country", null);
		
		assertEquals(rowData, rowData1);
	}
	
	@Test
	public void hasSpecifiedNullableForeignKey(){
		assertFalse(rowData.hasNullableForeignKey("foreign_id_us"));
	}
	
	@Test
	public void setForeignKeyToNull(){
		List<String> listOfRemovedFKeys = rowData.setForeignKeyToNull(rowData.getNullableForeignKey("IN"));
		assertNull(rowData.getNullableForeignKey("IN"));
		assertEquals(2, listOfRemovedFKeys.size());
		assertEquals(ImmutableList.of("foreign_id_in1","foreign_id_in"), listOfRemovedFKeys);
	}
}
