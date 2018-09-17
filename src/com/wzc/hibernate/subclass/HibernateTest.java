package com.wzc.hibernate.subclass;

import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import org.hibernate.cfg.Configuration;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TestGenerator;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

public class HibernateTest {

	private SessionFactory SessionFactory;

	private Session session;

	private Transaction transaction;

	@Before
	public void init() {
		System.out.println("init");

		Configuration configuration = new Configuration().configure();

		SessionFactory = configuration.buildSessionFactory();

		session = SessionFactory.openSession();

		transaction = session.beginTransaction();
	}

	@After
	public void destroy() {
		System.out.println("destroy");
		transaction.commit();
		session.close();
		SessionFactory.close();
	}
	
	/**
	 * ʹ��subclass��ȱ�㣺
	 * 1��ʹ���˱������
	 * 2��������е��ֶβ�����ӷǿ�Լ��
	 * 3�����̳н�������ݱ���ֶ�Ҳ��϶�
	 */

	/**
	 * ��ѯ��
	 * 1����ѯ�������ֻ��Ҫ��ѯһ�����ݱ�
	 * 2����ѯ�������Ҳֻ��Ҫ��ѯһ�����ݱ�
	 */
	@Test
	public void testQuery(){
		
		/**
		 * �����ѯʹ���˶�̬�������ݱ���Person��Person������ȫ�����˳�����persons.size()Ϊ2
		 */
		List<Person> persons = session.createQuery("FROM Person").list();
		System.out.println("persons.size:"+persons.size());
		
		/**
		 * ����ֻ��ѯ��student�����������students.size()Ϊ1
		 */
		List<Student> students = session.createQuery("FROM Student").list();
		System.out.println(students.size());
		
	}

	/**
	 * ��������� 1�������������ֻ��Ѽ�¼���뵽һ�����ݱ��� 2�����������hibernate�Զ�ά��
	 */
	@Test
	public void testSave() {

		Person person = new Person();

		person.setAge(24);
		person.setName("WZC");
		session.save(person);

		Student student = new Student();
		student.setAge(23);
		student.setName("WZC");
		student.setSchool("chongyou");

		session.save(student);

	}

}
