package com.shineWong.jdbc.Day03_MataData;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;
import org.junit.Test;

public class BeanUtilsTest {

/**
 * BeanUtils.setProperty
 * BeanUtils.getProperty
 * @throws IllegalAccessException
 * @throws InvocationTargetException
 * @throws NoSuchMethodException
 */
	@Test
	public void testGetProperty() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		Object object = new GraStudent();
		System.out.println(object); 
		
		BeanUtils.setProperty(object, "type", "7");
		System.out.println(object); 
		
//		Object val = BeanUtils.getProperty(object, "identityCard");
//		System.out.println(val);
	}
	
	@Test
	public void testSetProperty() throws IllegalAccessException, InvocationTargetException {
		
		Object object = new Student();
		System.out.println(object); 
		
		BeanUtils.setProperty(object, "examCard", "1234");
		System.out.println(object); 
		
	}

}
