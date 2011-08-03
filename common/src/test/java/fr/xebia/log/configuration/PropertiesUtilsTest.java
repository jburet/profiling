package fr.xebia.log.configuration;


import org.testng.annotations.Test;

import java.util.List;
import java.util.Properties;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class PropertiesUtilsTest {

    @Test
    public void when_i_pass_null_properties_return_empty_list() {
        List<String> res = PropertiesUtils.convertPropertiesToList("base", null);
        assertNotNull(res);
        assertEquals(res.size(), 0);
    }

    @Test
    public void when_i_pass_empty_properties_return_empty_list() {
        List<String> res = PropertiesUtils.convertPropertiesToList("base", new Properties());
        assertNotNull(res);
        assertEquals(res.size(), 0);
    }

    @Test
    public void when_i_null_base_return_empty_list() {
        Properties props = new Properties();
        props.setProperty("test.1", "test");
        List<String> res = PropertiesUtils.convertPropertiesToList(null, new Properties());
        assertNotNull(res);
        assertEquals(res.size(), 0);
    }

    @Test
    public void when_one_element_match_return_list_with_one_element() {
        Properties props = new Properties();
        props.setProperty("test.0", "test");
        List<String> res = PropertiesUtils.convertPropertiesToList("test", props);
        assertNotNull(res);
        assertEquals(res.size(), 1);
    }

    @Test
    public void when_four_element_match_return_list_with_four_element() {
        Properties props = new Properties();
        props.setProperty("test.0", "test1");
        props.setProperty("test.1", "test2");
        props.setProperty("test.2", "test3");
        props.setProperty("test.3", "test4");
        List<String> res = PropertiesUtils.convertPropertiesToList("test", props);
        assertNotNull(res);
        assertEquals(res.size(), 4);
        assertEquals(res.get(0), "test1");
        assertEquals(res.get(1), "test2");
        assertEquals(res.get(2), "test3");
        assertEquals(res.get(3), "test4");
    }


    @Test
    public void when_invalid_element_match_return_empty_list() {
        Properties props = new Properties();
        props.setProperty("test.zero", "test1");
        props.setProperty("test.one", "test2");
        props.setProperty("test.two", "test3");
        props.setProperty("test.three", "test4");
        List<String> res = PropertiesUtils.convertPropertiesToList("test", props);
        assertNotNull(res);
        assertEquals(res.size(), 0);
    }

    @Test
    public void when_invalid_and_valid_element_match_return_list() {
        Properties props = new Properties();
        props.setProperty("test.zero", "test1");
        props.setProperty("test.1", "test2");
        props.setProperty("test.two", "test3");
        props.setProperty("test.3", "test4");
        List<String> res = PropertiesUtils.convertPropertiesToList("test", props);
        assertNotNull(res);
        assertEquals(res.size(), 2);
        assertEquals(res.get(0), "test2");
        assertEquals(res.get(1), "test4");
    }

}
