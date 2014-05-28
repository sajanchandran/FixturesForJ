package com.fixtures.data.structure;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.collect.Lists;

/**This class represent a row in a TableData object, construct this object to add rows programatically 
 * on top of yaml files.
 * 
 * @author chandrans1
 *
 */
public class RowData {

	private Map<String, Object> rowData;
	private String tableName;
	private String sectionName;

	/** Constructor for creating RowData for TableData.
	 * @param rowData, map of column name and column value
	 * @param tableName
	 * @param sectionName, name of the section from the yaml file
	 */
	public RowData(Map<String, Object> rowData, String tableName, String sectionName) {
		this.rowData = rowData;
		this.tableName = tableName;
		this.sectionName = sectionName;
	}
	
	public String getSectionName(){
		return this.sectionName;
	}

	public boolean hasNullableForeignKey() {
		for (Object object : rowData.values()) {
			if(object instanceof NullableForeignKey){
				return true;
			}
		}
		return false;
	}

	public List<ForeignKey> getNullableForeignKeys() {
		List<ForeignKey> listOfForeignKeys = Lists.newArrayList();
		for (Object object : rowData.values()) {
			if(object instanceof NullableForeignKey){
				listOfForeignKeys.add((ForeignKey) object);
			}
		}
		return listOfForeignKeys;
	}
	
	public ForeignKey getNonNullableForeignKey() {
		for (Object object : rowData.values()) {
			if(object instanceof NonNullableForeignKey){
				return (NonNullableForeignKey) object;
			}
		}
		return null;
	}

	public String getTableName() {
		return tableName;
	}

	public Set<String> getColumnNames() {
		return rowData.keySet();
	}

	public Object getColumnValue(String columnName) {
		return rowData.get(columnName);
	}

	public PrimaryKey getPrimaryKey() {
		for (Object object : rowData.values()) {
			if(object instanceof PrimaryKey){
				return (PrimaryKey) object;
			}
		}
		return null;
	}

	public String getPrimaryKeyColumnName() {
		for (String columnName : rowData.keySet()) {
			if(rowData.get(columnName) instanceof PrimaryKey){
				return columnName;
			}
		}
		return null;
	}

	public void changeNullableForeignKey(PrimaryKey primaryKeyOfOtherTable, ForeignKey foreignKey) {
		for (String columnName : rowData.keySet()) {
			if(rowData.get(columnName).equals(foreignKey)){
				if(primaryKeyOfOtherTable == null){
					rowData.remove(columnName);
					return;
				}
				rowData.put(columnName, new NullableForeignKey(primaryKeyOfOtherTable.toString()));
			}
		}
	}

	public boolean hasNonNullableForeignKey() {
		for (Object object : rowData.values()) {
			if(object instanceof NonNullableForeignKey){
				return true;
			}
		}
		return false;
			
	}

	public void changeNonNullableForeignKey(PrimaryKey primaryKey) {
		for (String columnName : rowData.keySet()) {
			if(rowData.get(columnName) instanceof NonNullableForeignKey){
				if(primaryKey == null){
					rowData.remove(columnName);
					return;
				}
				rowData.put(columnName, new NonNullableForeignKey(primaryKey.toString()));
			}
		}
	}

	public boolean hasNullableForeignKey(String name) {
		for (ForeignKey foreignKey : this.getNullableForeignKeys()) {
			if(foreignKey.toString().equals(name)){
				return true;
			}
		}
		return false;
	}
	
	public ForeignKey getNullableForeignKey(String foreignKeyValue) {
		for (ForeignKey foreignKey : this.getNullableForeignKeys()) {
			if(foreignKey.toString().equals(foreignKeyValue)){
				return foreignKey;
			}
		}
		return null;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof RowData){
			RowData anotherOne = (RowData)obj;
			return anotherOne.tableName.equals(this.tableName) && anotherOne.getPrimaryKey().equals(this.getPrimaryKey());
		}
		return false;
	}
	
	@Override
	public String toString() {
		return tableName + " " + sectionName + " " + rowData; 
	}

	public List<String> setForeignKeyToNull(ForeignKey nullableForeignKey) {
		List<String> listOfRemovedFKeys = Lists.newArrayList();
		Iterator<Entry<String, Object>> iterator = rowData.entrySet().iterator();
		while(iterator.hasNext()){
			Entry<String, Object> entry = iterator.next();
			if (entry.getValue().equals(nullableForeignKey)) {
				listOfRemovedFKeys.add(entry.getKey());
				iterator.remove();
			}
			
		}
		return listOfRemovedFKeys;
	}
	
	public Map<String, Object> getRawRowData(){
		return rowData;
	}
}
