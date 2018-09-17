package com.wzc.hibernate.one2one.foreign;

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
		
		//��� Manager ����ʱ�����͵� SQL��ѯ�����  manager0_.MGR_ID=department1_.DEPT_ID ��
		//��ȷ��Ӧ���� manager0_.MGR_ID=department1_.MGR_ID
		//������Ҫ�� Manager.hbm.xml �ļ��� one-to-one ������ property-ref="manager" ���ԣ�ʹ���ڲ�ѯʱ
		//�ڻ�� department ����� ʹ���� manager ��������Ӧ�����������ѯ Manager ���ݱ��ж�Ӧ��Ԫ��
		
		Manager manager = department.getManager();
		
		System.out.println(manager.getMgrName());
	}

	@Test
	public void testSave() {

		Department department = new Department();
		department.setDeptName("DEPT-BB");

		Manager manager = new Manager();
		manager.setMgrName("MGR-BB");

		department.setManager(manager);
		manager.setDepartment(department);

		//�������
		//�����ȱ���û������еĶ����������Լ��� UPDATE ���
		session.save(manager);
		session.save(department);
	}

}
