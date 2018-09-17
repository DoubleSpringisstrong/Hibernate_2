package com.wzc.hibernate.joined.subclass;

import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import org.hibernate.cfg.Configuration;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;



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
	 * ʹ��joined-subclass���ŵ㣺
	 * 1������Ҫʹ���˱������
	 * 2��������е��ֶ�����ӷǿ�Լ��
	 * 3��û��������ֶ�
	 */

	/**
	 * ��ѯ��
	 * 1����ѯ���������һ���������Ӳ�ѯ
	 * 2����ѯ���������һ���ڲ�ѯ
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
	 * ���������
	 *  1�����������������Ҫ���뵽�������ݱ���
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
