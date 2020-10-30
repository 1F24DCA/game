package game.base.common;

import java.lang.reflect.*;
import java.util.*;
import java.util.regex.*;

public class Generator {
	// 매개변수로 집어넣은 객체의 클래스명과 함께 멤버 변수의 값들을 출력해주는 메서드
	@SuppressWarnings({"rawtypes", "unchecked"})
	public static String generateToString(Object objectToGenerateToString) {
		Class objectClass = objectToGenerateToString.getClass();
		
		String className = objectClass.getSimpleName();
		String memberInfo = "";
		
		boolean isFirstField = true;
		for (Field field : Generator.getDecalredFieldsWithSuperclasses(objectClass)) {
			// 쉼표를 깔끔하게 찍기 위해 사용함
			if (!isFirstField) {
				memberInfo += ",";
			} else {
				isFirstField = false;
			}
			
			String value = null;
			
			// 필드명을 찾음, gameInfo, valueA, isTrigger, privateMethodList 등의 글자 캡쳐
			// 첫번째 글자(1번 캡쳐), 나머지 글자(2번 캡쳐)
			String regex = "^([a-z])([a-z]*(?:[A-Z][a-z]*)*)$";
			Matcher m = Pattern.compile(regex).matcher(field.getName());
			if (m.find()) {
				try {
					// 앞에 get을 붙이고 첫번째 글자(1번 캡쳐)를 대문자, 나머지 문자 그대로 출력
					String getterString = "get" + m.group(1).toUpperCase() + m.group(2);
					Method getter = objectClass.getMethod(getterString, new Class[] {});
					
					// getter 메서드 호출 후 String으로 변환하여 value를 세팅함
					value = getter.invoke(objectToGenerateToString, new Object[] {}).toString();
				} catch (Exception e) {
					// getter 호출 관련 문제가 생겼을 경우 예외를 던짐
					throw new RuntimeException(e);
				}
			}
			
			memberInfo += field.getName();
			memberInfo += "=";
			memberInfo += value;
		}
		
		// 멤버변수가 있다면 대괄호안에 멤버변수 리스트를 집어넣고, 아니면 클래스명만 출력
		if (memberInfo.length() > 0) {
			memberInfo = " ["+memberInfo+"]";
		}
		
		return className+memberInfo;
	}
	
	// 조상 클래스들의 멤버 변수를 모두 가져오기 위해 메서드를 제작함
	@SuppressWarnings("rawtypes")
	private static List<Field> getDecalredFieldsWithSuperclasses(Class objectClass) {
		List<Field> classFields = new ArrayList<Field>();
		
		// 
		Class superClass = objectClass.getSuperclass();
		if (!superClass.equals(Object.class)) {
			classFields.addAll(Generator.getDecalredFieldsWithSuperclasses(superClass));
		}
		
		classFields.addAll(Arrays.asList(objectClass.getDeclaredFields()));
		
		return classFields;
	}
}
