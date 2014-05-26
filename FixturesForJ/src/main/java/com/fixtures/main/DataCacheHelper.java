package com.fixtures.main;

import java.util.HashMap;
import java.util.Map;

import com.fixtures.test.ForeignKey;
import com.google.common.collect.Maps;

public class DataCacheHelper {

	private Map<String, TableData> mapOfNameVsData = new HashMap<String, TableData>();
	
	public void add(Map<String, Map<String, Object>> rowData, String tableName) {
		TableData tableData = new TableData(rowData, tableName);
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
		if(mapOfNameVsData.containsKey(rowData.getTableName())){
			TableData tableData = mapOfNameVsData.get(rowData.getTableName());
			tableData.addRowData(rowData);
		}else{
			Map<String, Map<String, Object>> data = Maps.newHashMap();
			data.put(rowData.getSectionName(), rowData.getRawRowData());
			add(data, rowData.getTableName());
		}
	}

}
