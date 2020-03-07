package commandPointer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;

public class ClassLoader
{
	private class SelfMethod
	{
		private Method method;
		private String name;
		private Annotation[] annotations;
		private Class<?> paramsType[];
		private Class<?> returnType;
		
		class ParamException extends Exception
		{
			private static final long serialVersionUID = 1L;
			
			String reason;
			public ParamException(String string)
			{
				reason=string;
			}
			@Override
			public String getMessage()
			{
				return reason;
			}
		}
		
		public SelfMethod(String name,Method method)
		{
			this.name=name;
			this.method=method;
			annotations=method.getAnnotations();
			paramsType=method.getParameterTypes();
			returnType=method.getReturnType();
		}
		public <T extends Annotation> T getAnnotation(Class<T> type) 
		{
			for (Annotation annotation : annotations)
			{
				if(annotation.getClass()==type)
				{
					return (T) annotation;
				}
			}
			return null;
		}
		
		public Object startMethod(Object...objects) throws ParamException
		{
			if(objects.length<paramsType.length)
			{
				throw new ParamException("参数数量错误");
			}
			return null;
		}
	}
	
}
