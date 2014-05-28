package com.fixtures.helpers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.fixtures.data.structure.ForeignKey;
import com.fixtures.data.structure.RowData;
import com.fixtures.data.structure.TableData;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class DataCache {

	private Map<String, TableData> mapOfNameVsData = new HashMap<String, TableData>();
	
	public void add(Map<String, Map<String, Object>> data, String tableName) {
		List<RowData> rows = Lists.newArrayList();
		for(Entry<String, Map<String, Object>> entry : data.entrySet()){
			RowData rowData = new RowData(entry.getValue(), tableName, entry.getKey());
			rows.add(rowData);
		}
		TableData tableData = new TableData(rows);
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
