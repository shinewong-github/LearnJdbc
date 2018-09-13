package com.shineWong.jdbc.Day02_ReflectUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * <p>反射的 Utils 函数集合
 * <p>
 * 提供访问私有变量, 获取泛型类型 Class, 提取集合中元素属性等 Utils 函数
 * @author ShineWong
 *
 */
public class ReflectUtils {

	
/**
* 通过反射, 获得定义 Class 时声明的父类的泛型参数的类型
* 如: public EmployeeDao extends BaseDao<Employee, String>
* 获取到Employee, String
* @param clazz
* @param index
* @return
* 
*/
	@SuppressWarnings("unchecked")
	public static Class getSuperClassGenricType(Class clazz, int index){
		Type genType = clazz.getGenericSuperclass();
		
		if(!(genType instanceof ParameterizedType)){
			return Object.class;
		}
		Type [] params = ((ParameterizedType)genType).getActualTypeArguments();
		if(index >= params.length || index < 0){
			return Object.class;
		}
		if(!(params[index] instanceof Class)){
			return Object.class;
		}
		return (Class) params[index];
	}
	
/**
  * 通过反射, 获得 Class 定义中声明的父类的泛型参数类型的第一参数
  * 如: public EmployeeDao extends BaseDao<Employee, String>
  * 获取到为Employee
  * @param clazz
  * @return
  * @see #getSuperClassGenricType(Class, int)
  */
	@SuppressWarnings("unchecked")
	public static<T> Class<T> getSuperGenericType(Class clazz){
		return getSuperClassGenricType(clazz, 0);
	}
	
/**
  * <p>如果该类没有指定的方法，就会去父类找，直到找到Object
  *    如果Object类没有就抛出异常
  * @param object 对象类
  * @param methodName 寻找的方法
  * @param parameterTypes 方法的参数列表
  * @return
  */
	public static Method getDeclaredMethod(Object object, String methodName, Class<?>[] parameterTypes){
		
		for(Class<?> superClass = object.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()){
			try {
				//superClass.getMethod(methodName, parameterTypes);
				return superClass.getDeclaredMethod(methodName, parameterTypes);
			} catch (NoSuchMethodException e) {
		  }
		}
		return null;
	}
	
/**
 * 
  * <p>使 filed 变为可访问的方法</p>
  *  <pre>忽略 private/protected 修饰符 -->field.setAccessible(true)</pre>
  * @param field
  */
	public static void makeAccessible(Field field){
		if(!Modifier.isPublic(field.getModifiers())){//非公共的，设置为可访问
			field.setAccessible(true);
		}
	}
	
/**
  * <p>如果该类没有指定的Field，就会去父类找，直到找到Object
  *    如果Object类没有就抛出异常
  * 循环向上转型, 获取对象的 DeclaredField
  * @param object
  * @param filedName
  * @return
  */
	public static Field getDeclaredField(Object object, String filedName){
		
		for(Class<?> superClass = object.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()){
			try {
				return superClass.getDeclaredField(filedName);
			} catch (NoSuchFieldException e) {
				
			}
		}
		return null;
	}
	
/**
  * 直接调用对象方法, 而忽略修饰符(private, protected)-->method.setAccessible(true);
  * @param object
  * @param methodName
  * @param parameterTypes
  * @param parameters
  * @return
  * @throws InvocationTargetException 
  * @throws IllegalArgumentException 
  */
	public static Object invokeMethod(Object object, String methodName, Class<?> [] parameterTypes,
			Object [] parameters) throws InvocationTargetException{
		Method method = getDeclaredMethod(object, methodName, parameterTypes);
		if(method == null){
			throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + object + "]");
		}
		method.setAccessible(true);//设置方法可访问
		try {
			return method.invoke(object, parameters);
		} catch(IllegalAccessException e) {
			System.out.println("不可能抛出的异常");
		} 
		return null;
	}
	
/**
  * 直接设置对象属性值, 忽略 private/protected 修饰符, 也不经过 setter -->makeAccessible(field);
  * @param object
  * @param fieldName
  * @param value
  */
	public static void setFieldValue(Object object, String fieldName, Object value){
		Field field = getDeclaredField(object, fieldName);
		if (field == null)
			throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + object + "]");
		makeAccessible(field);
		try {
			field.set(object, value);
		} catch (IllegalAccessException e) {
			System.out.println("不可能抛出的异常");
		}
	}
	
/**
  * 直接读取对象的属性值, 忽略 private/protected 修饰符, 也不经过 getter
  * @param object
  * @param fieldName
  * @return
  */
	public static Object getFieldValue(Object object, String fieldName){
		Field field = getDeclaredField(object, fieldName);
		
		if (field == null)
			throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + object + "]");
		makeAccessible(field);
		Object result = null;
		try {
			result = field.get(object);
		} catch (IllegalAccessException e) {
			System.out.println("不可能抛出的异常");
		}
		return result;
	}
}
//
///**
// * 被测试的Person
// * @author ShineWong
// *
// */
//class Person{
//	String name="HGY";
//	int age=25;
//	
//
//}
//
///**
// * 被测试的List extends Person
// * @author ShineWong
// *
// */
//class Lisi extends Person{
//	String shool="GZDX";
//	int age=10;
//	
//	
//}


