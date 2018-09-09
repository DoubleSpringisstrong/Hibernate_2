package com.wzc.hibernate.entities.n21both;

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
		System.out.println("null+" + SessionFactory == null);

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
	public void testCascade(){
		
	}
	
	
	@Test
	public void testDelete() {
		// �ڲ��趨������ϵ������£��� 1 ��һ���� n �Ķ��������ã�����ɾ��
		Customer customer = session.get(Customer.class, 1);
		session.delete(customer);
	}

	
	/**
	 * ���²���
	 */
	@Test
	public void testUpdate() {
		Customer customer = session.get(Customer.class, 1);
		customer.getOrders().iterator().next().setOrderName("GGG");
	}

	@Test
	public void testOne2ManyGet() {
		
		//1���� n ��һ�˵ļ��ϲ����ӳټ���
		Customer customer = session.get(Customer.class, 1);
		
		System.out.println(customer.getCustomerName());
		
		//2�����ﷵ�صļ��������� class org.hibernate.collection.internal.PersistentSet  Hibernate���õļ�������
		//�����;����ӳټ��غʹ�Ŵ������Ĺ���
		System.out.println(customer.getOrders().getClass());
		
		//3�����ܻᷢ�� LazyInitializationException �������쳣
		
		
		//4������Ҫʹ�ü�����Ԫ��ʱ���г�ʼ��
	}

	// ��Order����Customer
	@Test
	public void testMany2OneGet() {

		// 1������ѯ���һ�˵�һ��������Ĭ������£�ֻ�����һ������Ƕ˵Ķ���
		// ��û����֮������һ���Ƕ˵Ķ���
		Order order = session.get(Order.class, 1);
		System.out.println(order.getOrderName());

		// session.close();//������ر�session�ᷢ���������쳣

		// 2������Ҫʹ�õ���Ӧ��customer����ʱ���ŷ��Ͷ�Ӧ��SQL��䣬��Ϊ������
		Customer customer = order.getCustomer();
		System.out.println(customer.getCustomerName());

		// 3���ڲ�ѯCustomer����ʱ���Ӷ��һ�˵�����1��һ��ʱ
		// ���ر�sessionʱ��Ĭ������»ᷢ���������쳣 LazyInitializationException

		// 4���ڻ�ȡOrder����ʱ����Ĭ������£����õ�Customer������һ���������ֻ��Ҫ�õ�ʱ���Ż��ʼ��

	}

	@Test
	public void testMany2OneSave() {

		// ����˫�������ϵ
		Customer customer = new Customer();
		customer.setCustomerName("CC");

		Order order1 = new Order();
		order1.setOrderName("order-3");

		Order order2 = new Order();
		order2.setOrderName("order-4");

		order1.setCustomer(customer);
		order2.setCustomer(customer);

		customer.getOrders().add(order1);
		customer.getOrders().add(order2);

		Order order3 = new Order();
		order3.setOrderName("order-3");

		Order order4 = new Order();
		order4.setOrderName("order-4");

		// order3.setCustomer(customer);
		// order4.setCustomer(customer);

		// һ��Customer���Զ�Ӧ���Order��Customer�а���Order�ļ��ϣ�Order�к���Customer�����ã�Order��Ӧ�����ݱ��а���Customer������ֵ��Order�ļ���ֵ
		// �Ȳ���Customer,�ٲ���Order���� 3�� INSERT ��䣬 2 �� UPDATE���
		// ��Ϊ 1 ��һ�˺� n ��һ�˶�Ҫά��������ϵ��customer�а���order�ļ���
		// ͨ���� 1 ��һ�˵� set �������� inverse=true ��ʹ 1 ��һ�˷���ά��������ϵ
		// �����趨 Customer �� set ���Ե� inverse=true �Ȳ��� 1 ��һ�ˣ��ٲ��� n ��һ�ˣ������� UPDATE
		// ���

		session.save(customer);
		session.save(order1);
		session.save(order2);

		// �Ȳ������Ƕˣ��ٲ���1���Ƕˣ����� 3�� INSERT ��䣬���� 4�� UPDATE ���
		// session.save(order3);
		// session.save(order4);
		// session.save(customer);

	}

}
