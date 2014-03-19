package com.fixtures.main;

import static com.google.common.collect.ImmutableMap.of;

import java.util.HashMap;
import java.util.Map;

import com.fixtures.test.ForeignKey;

public class DataCacheHelper {

	private Map<String, TableData> mapOfNameVsData = new HashMap<String, TableData>();
	
	public void add(Map<String, Map<String, Object>> data, String tableName) {
		TableData tableData = new TableData(data, tableName);
		if(mapOfNameVsData.containsKey(tableName)){
			tableData.addRowData(data.get(tableName));
		}
		mapOfNameVsData.put(tableName, tableData);
	}

	public RowData getRowDataForSection(ForeignKey foreignKey) {
		for (String tableName : mapOfNameVsData.keySet()) {
			TableData tableData = mapOfNameVsData.get(tableName);
			for (String section : tableData.getSections()) {
				if(foreignKey.toString().equals(section)){
					return tableData.getRowData(section);
				}
			}
		}
		return null;
	}
	
	public Map<String, TableData> getMapOfTableNameVsData(){
		return mapOfNameVsData;
	}

	public void add(RowData rowData) {
		add(of(rowData.getSectionName(), rowData.getRawRowData()), rowData.getTableName());
	}

}
