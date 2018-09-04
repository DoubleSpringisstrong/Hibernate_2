package com.wzc.hibernate.entities.n21;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

import javax.persistence.criteria.CriteriaBuilder.In;

import org.hibernate.Hibernate;
import org.hibernate.LazyInitializationException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.bytecode.enhance.spi.EnhancementContextWrapper;
import org.hibernate.cfg.Configuration;
import org.hibernate.jdbc.Work;
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
	
	@Test
	public void testDelete(){
		//�ڲ��趨������ϵ������£��� 1 ��һ���� n �Ķ��������ã�����ɾ��
		Customer customer = session.get(Customer.class, 1);
		session.delete(customer);
	}
	
	@Test
	public void testUpdate(){
		Order order = session.get(Order.class, 1);
		order.getCustomer().setCustomerName("AAA");
	}

	// ��Order����Customer
	@Test
	public void testMany2OneGet() {
		
		//1������ѯ���һ�˵�һ��������Ĭ������£�ֻ�����һ������Ƕ˵Ķ���
		//��û����֮������һ���Ƕ˵Ķ���
		Order order = session.get(Order.class, 1);
		System.out.println(order.getOrderName());
		
		
//		session.close();//������ر�session�ᷢ���������쳣
		
		//2������Ҫʹ�õ���Ӧ��customer����ʱ���ŷ��Ͷ�Ӧ��SQL��䣬��Ϊ������
		Customer customer = order.getCustomer();
		System.out.println(customer.getCustomerName());
		
		//3���ڲ�ѯCustomer����ʱ���Ӷ��һ�˵�����1��һ��ʱ
		//���ر�sessionʱ��Ĭ������»ᷢ���������쳣 LazyInitializationException
		
		//4���ڻ�ȡOrder����ʱ����Ĭ������£����õ�Customer������һ���������ֻ��Ҫ�õ�ʱ���Ż��ʼ��
		
	}

	@Test
	public void testMany2OneSave() {
		Customer customer = new Customer();
		customer.setCustomerName("AA");

		Order order1 = new Order();
		order1.setOrderName("order-1");

		Order order2 = new Order();
		order2.setOrderName("order-2");

		Order order3 = new Order();
		order3.setOrderName("order-3");

		Order order4 = new Order();
		order4.setOrderName("order-4");

		order1.setCustomer(customer);
		order2.setCustomer(customer);
		order3.setCustomer(customer);
		order4.setCustomer(customer);

		// һ��Customer���Զ�Ӧ���Order��Order�к���Customer�����ã�Order��Ӧ�����ݱ��а���Customer������ֵ
		// �Ȳ���1���Ƕˣ��ٲ������Ƕˣ�ֻ��3�� INSERT ��� �Ƽ�
		// �Ȳ���Customer���ٲ���Order��ֻ�� INSERT���
		// session.save(customer);
		// session.save(order1);
		// session.save(order2);

		// �Ȳ������Ƕˣ��ٲ���1���Ƕˣ�����3�� INSERT ��䣬���� ���� UPDATE ���
		// ��Ϊ�ڲ������Ƕ�ʱ���޷�ȷ��1��һ�˵�����ֵ��ֻ�е�1������ֵȷ����
		session.save(order3);
		session.save(order4);
		session.save(customer);

	}

}
