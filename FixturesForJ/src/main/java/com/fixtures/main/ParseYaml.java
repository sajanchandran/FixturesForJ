package com.fixtures.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;

import org.springframework.util.StringUtils;
import org.yaml.snakeyaml.Yaml;

public class ParseYaml {

	private String path;
	private DataCacheHelper cachedData;

	public ParseYaml(String path, DataCacheHelper cachedData) {
		this.path = path;
		this.cachedData = cachedData;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> get(String name) {
		Map<String, Map<String, Object>> object = null;
		Yaml yaml = new Yaml();
		try {
			object = (Map<String, Map<String, Object>>) yaml.load(new FileInputStream(new File(path)));
		} catch (FileNotFoundException e) {
			throw new FixtureDataNotFoundException("Could not read data from path -->"+path);
		}
		return object.get(name);
	}

	@SuppressWarnings("unchecked")
	public void loadAll() {
		Yaml yaml = new Yaml();
		Map<String, Map<String, Object>> data = null;
		try {
			data = (Map<String, Map<String, Object>>) yaml.load(new FileInputStream(new File(path)));
			cachedData.add(data, fileName());
		} catch (FileNotFoundException e) {
			throw new FixtureDataNotFoundException("Could not read data from path -->"+path);
		}
	}

	private String fileName() {
		return StringUtils.stripFilenameExtension(new File(path).getName());
	}

}
