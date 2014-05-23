package com.fixtures.test;


import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;

import com.fixtures.main.RowData;

public class PersistData {

	private JdbcTemplate jdbcTemplate;

	public PersistData(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public void persist(RowData rowData) {
		String insertColumnsNames = StringUtils.collectionToCommaDelimitedString(rowData.getColumnNames());
		StringBuilder sql = new StringBuilder();
		List<Object> listOfColumnValues = new ArrayList<Object>();
		for (String columnName : rowData.getColumnNames()) {
			listOfColumnValues.add(rowData.getColumnValue(columnName));
		}
		String insertColumnsValues = StringUtils.collectionToDelimitedString(listOfColumnValues,",","'","'");
		sql.append("insert into "+rowData.getTableName()+" ("+ insertColumnsNames + ") values" + " ("+insertColumnsValues + ")");
		System.out.println(sql);
		jdbcTemplate.update(sql.toString());
	}

	public void clean(RowData primaryKeyRowData) {
		StringBuilder sql = new StringBuilder();
		if(org.apache.commons.lang3.StringUtils.isNumeric(primaryKeyRowData.getPrimaryKey().getKey())){
			sql.append("delete from ").append(primaryKeyRowData.getTableName()).append(" where ").
			append(primaryKeyRowData.getPrimaryKeyColumnName()).append(" in ( ").
			append(primaryKeyRowData.getPrimaryKey()).append(" )");
		}else{
			sql.append("delete from ").append(primaryKeyRowData.getTableName()).append(" where ").
			append(primaryKeyRowData.getPrimaryKeyColumnName()).append(" in ( ").
			append("'"+primaryKeyRowData.getPrimaryKey()+"'").append(" )");
		}
		System.out.println(sql);
		jdbcTemplate.update(sql.toString());
	}

	public void update(String columnName, RowData rowData, RowData parentRowData) {
		StringBuilder sql = new StringBuilder();
		sql.append("update ").append(rowData.getTableName()).append(" set ").
								append(columnName).append("=").append(rowData.getPrimaryKey()).append(" where ").append(parentRowData.getPrimaryKeyColumnName())
								.append("=").append(parentRowData.getPrimaryKey());
		System.out.println(sql);
		jdbcTemplate.update(sql.toString());
	}

}
