package com.wzc.hibernate.one2one.primary;

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
	
	@Test
	public void testGet2(){
		
		//�ڲ�ѯû�������ʵ�����ʱ��ʹ�õ����������Ӳ�ѯ��left outer join����
		//���������Ǹ�ʵ�����һ����������г�ʼ��
		
		Manager manager = session.get(Manager.class, 1);
		System.out.println(manager.getMgrName());
		System.out.println(manager.getDepartment().getDeptName());
	}
	
	@Test
	public void testGet(){
		
		//1��������� Manager ����ʱʹ�õ���������
		//2�����ܻᷢ���������쳣
		Department department = session.get(Department.class, 1);
		
		System.out.println(department.getDeptName());
		
	
		Manager manager = department.getManager();
		
		System.out.println(manager.getMgrName());
	}

	@Test
	public void testSave() {

		Department department = new Department();
		department.setDeptName("DEPT-AA");

		Manager manager = new Manager();
		manager.setMgrName("MGR-AA");

		department.setManager(manager);
		manager.setDepartment(department);

		//�������
		//��������֮ǰ�����ӳ��Ĳ�ͬ�������Ȳ�����һ�������Ȳ��� manager �ٲ��� department ������
		//����� UPDATE
		session.save(department);
		session.save(manager);
	}

}
