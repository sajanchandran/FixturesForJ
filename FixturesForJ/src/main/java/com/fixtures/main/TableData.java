package com.fixtures.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TableData {

	private Map<String, Map<String, Object>> data;
	private String tableName;

	public TableData(Map<String, Map<String, Object>> data, String tableName) {
		this.data = data;
		this.tableName = tableName;
	}

	public Set<String> getSections() {
		return data.keySet();
	}

	public RowData getRowData(String section) {
		return new RowData(data.get(section), tableName, section);
	}

	public List<RowData> getListOfRowDataWithNoForeignKey() {
		List<RowData> listOfRowDataWhichHasNoForeignKey = new ArrayList<RowData>();
		for (String	rowDataName : data.keySet()) {
			RowData rowData = getRowData(rowDataName);
			if(!rowData.hasNullableForeignKey() && !rowData.hasNonNullableForeignKey()){
				listOfRowDataWhichHasNoForeignKey.add(rowData);
			}
		}
		return listOfRowDataWhichHasNoForeignKey;
	}

	public List<RowData> getListOfRowDataWithNullableForeignKey() {
		List<RowData> listOfRowDataWithNullableForeignKey = new ArrayList<RowData>();
		for (String	rowDataName : data.keySet()) {
			RowData rowData = getRowData(rowDataName);
			if(rowData.hasNullableForeignKey()){
				listOfRowDataWithNullableForeignKey.add(rowData);
			}
		}
		return listOfRowDataWithNullableForeignKey;
	}
	
	public void addRowData(Map<String, Object> rowData){
		
	}

}
