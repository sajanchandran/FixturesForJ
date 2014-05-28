package com.fixtures.data.structure;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

/** This class represent a table entity, populated with RowData objects which represents each row of the table.
 * 
 * @author chandrans1
 */
public class TableData {

	private List<RowData> rows;

	public TableData(List<RowData> rows) {
		this.rows = rows;
	}

	public List<String> getSections() {
		return Lists.transform(rows, new Function<RowData, String>() {
			@Override
			@Nullable
			public String apply(@Nullable RowData input) {
				return input.getSectionName();
			}
		});
	}

	public RowData getRowData(String section) {
		for(RowData rowData : rows){
			if(section.equals(rowData.getSectionName())){
				return rowData;
			}
		}
		return null;
	}

	public List<RowData> getListOfRowDataWithNoForeignKey() {
		List<RowData> listOfRowDataWhichHasNoForeignKey = new ArrayList<RowData>();
		for (RowData rowData : rows) {
			if(!rowData.hasNullableForeignKey() && !rowData.hasNonNullableForeignKey()){
				listOfRowDataWhichHasNoForeignKey.add(rowData);
			}
		}
		return listOfRowDataWhichHasNoForeignKey;
	}

	public List<RowData> getListOfRowDataWithNullableForeignKey() {
		List<RowData> listOfRowDataWithNullableForeignKey = new ArrayList<RowData>();
		for (RowData rowData : rows) {
			if(rowData.hasNullableForeignKey()){
				listOfRowDataWithNullableForeignKey.add(rowData);
			}
		}
		return listOfRowDataWithNullableForeignKey;
	}
	
	public void addRowData(RowData rowData){
		rows.add(rowData);
	}

}
