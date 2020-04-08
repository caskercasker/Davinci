package com.sist.temp;

import java.lang.reflect.Method;

class A
{
	public void disp1()
	{
		System.out.println("A:disp1 Call...");
	}
	public void disp2()
	{
		System.out.println("A:disp2 Call...");		
	}
}
public class MainClass {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

			try
			{
			/*
			 * A a=new A(); a.disp1(); a.disp2();
			 */
				A a = new A();
				System.out.println("a=" +a);
				Class clsName =Class.forName("com.sist.temp.A"); // 클래스명 호출
				Object obj=clsName.newInstance(); //객체 주기
				System.out.println("obj=" +obj);			
				
				Method[] methods=clsName.getDeclaredMethods();  //java.lang.reflect
				for(Method m:methods)
				{
					m.invoke(obj, null);
				}
			}catch (Exception ex) {}
	}

}
