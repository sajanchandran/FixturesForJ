package com.fixtures.main;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.fixtures.test.ForeignKey;
import com.fixtures.test.PersistData;
import com.google.common.collect.Maps;

public class FixturesForJ {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	private PersistData persistData;
	private Deque<RowData> toCleanUp;
	private List<RowData> nonNullableForeignKeyRecords;
	private DataCacheHelper cachedData;
	private String testDataLocation = "src/test/resources/com/fixtures/data";
	private RowData rowData;
	
	public void init() {
		persistData = new PersistData(jdbcTemplate);
		cachedData = new DataCacheHelper();
		toCleanUp = new ArrayDeque<RowData>();
		nonNullableForeignKeyRecords = new ArrayList<RowData>();
		File file = new File(testDataLocation);
		File[] fileList = scanFolderForYmlDataFiles(file);
		for (File dataFiles : fileList) {
			ParseYaml parseYaml = new ParseYaml(dataFiles.getAbsolutePath(), cachedData);
			parseYaml.loadAll();
		}
		
		if(rowData != null){
			cachedData.add(rowData);
		}
		
		Map<String, TableData> mapOfTableNameVsData = cachedData.getMapOfTableNameVsData();
		for (String tableName : mapOfTableNameVsData.keySet()) {
			TableData tableData = mapOfTableNameVsData.get(tableName);
			for(String section : tableData.getSections()){
				saveRowData(tableData.getRowData(section));
			}
		}
	}

	private void saveRowData(RowData rowData){
		List<String> parentForeignKeyToUpdate = null;
		RowData parentRowData = null;
		if(rowData.hasNullableForeignKey() && !isRowDataAlreadyInserted(rowData)){
			for (ForeignKey nullableForeignKey : rowData.getNullableForeignKeys()) {
				parentRowData = cachedData.getRowDataForSection(nullableForeignKey);
				if(parentRowData.hasNullableForeignKey(rowData.getSectionName())){
					parentForeignKeyToUpdate = parentRowData.setForeignKeyToNull(parentRowData.getNullableForeignKey(rowData.getSectionName()));
				}
				saveRowData(parentRowData);
				if(!isRowDataAlreadyInserted(parentRowData)){
					save(parentRowData);
				}
				rowData.changeNullableForeignKey(parentRowData.getPrimaryKey(), nullableForeignKey);
			}
		}
		if(!isRowDataAlreadyInserted(rowData)){
			save(rowData);
		}
		if(parentForeignKeyToUpdate!=null){
			for (String columnName : parentForeignKeyToUpdate) {
				update(columnName,rowData, parentRowData);
			}
		}
	}

	private void update(String columnName, RowData rowData, RowData parentRowData) {
		persistData.update(columnName,rowData, parentRowData);
	}

	private File[] scanFolderForYmlDataFiles(File file) {
		return file.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File arg0, String fileName) {
				return fileName.endsWith("yml");
			}
		});
	}

	private boolean isRowDataAlreadyInserted(RowData rowData) {
		return toCleanUp.contains(rowData);
	}
	
	private void save(RowData dataForSection) {
		persistData.persist(dataForSection);
		toCleanUp.add(dataForSection);
	}

	public void clean() {
		Iterator<RowData> descendingIterator = toCleanUp.descendingIterator();
		while(descendingIterator.hasNext()){
			persistData.clean(descendingIterator.next());
		}
	}

	public FixturesForJ withTestData(String testDataLocation) {
		this.testDataLocation = testDataLocation;
		return this;
	}

	public FixturesForJ addRowData(RowData rowData) {
		this.rowData = rowData;
		return this;
	}

}
