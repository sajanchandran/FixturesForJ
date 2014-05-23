package com.fixtures.test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Map;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;

import com.fixtures.main.DataCacheHelper;
import com.fixtures.main.FixtureDataNotFoundException;
import com.fixtures.main.FixturesForJ;
import com.fixtures.main.ParseYaml;

public class ParseYamlTest {
	
	@Mock
	private FixturesForJ mockFixturesForJ;
	@Mock
	private DataCacheHelper mockCachedData;
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	@Before
	public void onceBeforeEachTest(){
		initMocks(this);
	}

	@Test
	public void readYamlFileCorrectly(){
		ParseYaml parser = new ParseYaml("src/test/resources/com/fixtures/data/t_country.yml", null);
		Map<String, Object> hashMap = parser.get("GB");
		assertNotNull(hashMap);
	}
	
	@Test
	public void readFromGBInput(){
		ParseYaml parser = new ParseYaml("src/test/resources/com/fixtures/data/t_country.yml", null);
		Map<String, Object> hashMap = parser.get("GB");
		assertEquals("1", ((PrimaryKey)hashMap.get("key")).getKey());
		assertEquals("Great Britain", hashMap.get("name"));
		assertEquals("GB", hashMap.get("code"));
		assertEquals("London", hashMap.get("capital"));
	}
	
	@Test
	public void returnsNullWhenUnknownParentKeyIsAsked(){
		ParseYaml parser = new ParseYaml("src/test/resources/com/fixtures/data/t_country.yml", null);
		Map<String, Object> hashMap = parser.get("GR");
		assertNull(hashMap);
	}
	
	@Test
	public void returnsNullWhenUnknownChildKeyIsAsked(){
		ParseYaml parser = new ParseYaml("src/test/resources/com/fixtures/data/t_country.yml", null);
		Map<String, Object> hashMap = parser.get("FR");
		assertNull(hashMap.get("population"));
	}
	
	@Test
	public void throwExceptionWhenFileNotFound(){
		expectedException.expect(FixtureDataNotFoundException.class);
		expectedException.expectMessage("Could not read data from path -->src/test/resources/com/fixtures/data/states.yml");
		ParseYaml parser = new ParseYaml("src/test/resources/com/fixtures/data/states.yml", null);
		parser.get("FR");
	}
	
	@Test
	public void loadAllInvokesCache(){
		ParseYaml parser = new ParseYaml("src/test/resources/com/fixtures/data/t_country.yml", mockCachedData);
		parser.loadAll();
		verify(mockCachedData, times(1)).add(anyMap(), eq("t_country"));
	}
}
