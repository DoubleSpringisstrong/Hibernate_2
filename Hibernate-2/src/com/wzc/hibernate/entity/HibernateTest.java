package com.wzc.hibernate.entity;

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

import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TestGenerator;
import com.sun.org.apache.xml.internal.resolver.helpers.PublicId;

import sun.management.VMOptionCompositeData;

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
	 * �������
	 * Worker����һ��Pay������
	 * �����ļ�������Component
	 */
	
	@Test
	public void testComponent(){
		Worker worker = new Worker();
		
		Pay pay = new Pay();
		
		pay.setMonthlyPay(1000);
		pay.setYearPay(20000);
		pay.setVocationWithPay(5);
		
		worker.setName("ABCD");
		worker.setPay(pay);
		
		session.save(worker);
	}
	
	@Test
	public void testBlob() throws IOException, SQLException{
		
		//��Blob����浽���ݿ���
//		News news = new News();
//		news.setAuthor("cc");
//		news.setContext("Context");
//		news.setDate(new Date());
//		news.setTitle("CC");
//		news.setDesc("Desc");
//		
//		InputStream iStream = new FileInputStream("DSCF1814.jpg");
//		
//		Blob image = Hibernate.getLobCreator(session).createBlob(iStream, iStream.available());
//		news.setImage(image);
//		
//		session.save(news);
		
		//��Blob��������ݿ���ȡ����
		
		News news = session.get(News.class, 1);
		Blob image = news.getImage();
		
		InputStream iStream = image.getBinaryStream();
		System.out.println(iStream.available());
	}
	
	/**
	 * ����Property �е�formula����
	 * ����һ�� SQL ���ʽ, Hibernate ����������������������Ե�ֵ. 
	 *��������: �����ǳ־û�����������Զ�ֱ�Ӻͱ���ֶ�ƥ��, �־û������Щ���Ե�ֵ����������ʱͨ��������ܵó���, �������Գ�Ϊ��������
	 */
	@Test
	public void testPropertyFormula(){
		News news = session.load(News.class, 1);
		news.setAuthor("Oracle");

		
		System.out.println(news.getDesc());
	}
	
	/**
	 * ����ID��ʶ���������ݱ���ID������OID��Ӧ��ϵ����
	 */
	@Test
	public void testIdIdentifier(){
		News news = new News("AA","bb",new java.sql.Date(new Date().getTime()));
		
		session.save(news);
	}
	
	/**
	 * ���Զ�̬����
	 * ��hbm.xml������dynamic-update: 
	 * ������Ϊ true, ��ʾ������һ������ʱ, �ᶯ̬���� update ���, 
	 * update ����н���������ȡֵ��Ҫ���µ��ֶ�. Ĭ��ֵΪ false
	 */
	@Test
	public void testDynamicUpdate(){
		News news = session.get(News.class, 1);
		news.setAuthor("Oracle");
	}
	
	/**
	 * ����JDBCԭ���Ĵ洢����
	 * ��������ӡ��Connection��ʾ������C3P0�����ʾ�������ļ�hibernate.cfg.xml��c3p0����Դ�Ѿ������
	 */
	@Test
	public void testDoWork() {
		// TODO Auto-generated method stub
		session.doWork(new Work() {
			
			@Override
			public void execute(Connection connection) throws SQLException {
				// TODO Auto-generated method stub
				
				System.out.println(connection);
				
				//���������ͨ��Connection����JDBCԭ���Ĵ洢����
				
			}
		});

	}

	/**
	 * evict����session�����а�ָ���ĳ־û������Ƴ�
	 */

	@Test
	public void testEvict() {
		News news = session.get(News.class, 1);
		News news2 = session.get(News.class, 2);

		news.setTitle("AA");
		news2.setTitle("BB");

		session.evict(news);// ���ύ����֮ǰ���־û�����ӻ������Ƴ����Ͳ���ִ�и��²���

	}

	/**
	 * delete��ִ��ɾ������ ��OID�����ݱ��е�һ����¼��Ӧ���ͻ�׼��ִ��ɾ������ ��OID�����ݱ���û����Ӧ�ļ�¼�����׳��쳣
	 * 
	 * delete����ִ����flush��Ž���ɾ���������ǵ��÷���������ɾ������ɾ����Ķ������ٽ���saveOrUpdate������
	 * ��ΪOID��Ϊnull ����ͨ������ hibernate �����ļ�
	 * hibernate.use_identifier_rollbackΪtrue��ʹɾ������� ����OID��Ϊnull
	 */

	@Test
	public void testDelete() {

		// �����һ��������󣬿��Խ���ɾ��
		// News news = new News();
		// news.setId(4);

		// ����һ���־û����󣬿��Խ���ɾ��
		News news = session.get(News.class, 6);

		session.delete(news);

		System.out.println(news);
	}

	/**
	 * Session �� saveOrUpdate() ����ͬʱ������ save() �� update() �����Ĺ���
	 * ��������������update���������ʱ������save �ж�����Ϊ��ʱ����ı�׼ 1��Java ����� OID Ϊ null 2��ӳ���ļ���Ϊ
	 * <id> ������ unsaved-value ����, ���� Java ����� OID ȡֵ����� unsaved-value ����ֵƥ��
	 * 
	 * ע�⣺ 1����OID��Ϊnull�������ݱ��л�û�к����Ӧ�ļ�¼�������쳣 2���˽⣺OID��ֵ����ID�� unsaved-value
	 * ����ֵ�Ķ���Ҳ����Ϊ��һ���������
	 * 
	 */

	@Test
	public void testSaveOrUpdate() {
		News news = new News("FFF", "ff", new Date());
		// news.setId(100);//�����ݱ���û�д�ID��������쳣

		
		session.saveOrUpdate(news);
		
	}

	/**
	 * update: 1��������һ���־û����󣬲���Ҫ��ʽ�ص���update��������Ϊ�ڵ���Transaction��commit����ʱ
	 * ������ִ��session��flush����
	 * 
	 * 2������һ�����������Ҫ��ʽ�ص���session��update���������԰�һ����������Ϊ�־û�����
	 * 
	 * 
	 * ��Ҫע��ĵ㣺 1������Ҫ���µ������������ݱ��еļ�¼�Ƿ�һ�£����ᷢ�� UPDATE ��䣬ע����������󣬶����ǳ־û�����
	 * �־û�������δ�޸Ķ��������ʱ����update�������ᷢ�� UPDATE ��� �������update��������äĿ�ط��� UPDATE ��䣿��
	 * .hbm.xml��class �ڵ����� select-before-update=true��Ĭ��Ϊfalse������ͨ������Ҫ���ø�����
	 * ��ô�ڵ���update��������֮ǰ�ᷢ��һ�� SELECT ����ٲ�ѯһ�Σ������ͬ�򲻻ᷢ�� UPDATE ���
	 * 
	 * 2��������ݱ���û�ж�Ӧ�ļ�¼,���������� update����������׳��쳣
	 * 
	 * 3���� update() ��������һ���������ʱ, ����� Session �Ļ������Ѿ�������ͬ OID �ĳ־û�����,
	 * ���׳��쳣����Ϊ��session�����У�����������OID��ͬ�Ķ���
	 */
	@Test
	public void testUpdate() {
		News news = session.get(News.class, 4);// �мǣ��ڶ��������������ݱ��ж�Ӧ��id�����û����Ӧ��id�����ȡ��������

		/**
		 * ����������������д��룬���ύtransaction�͹ر�session���ִ򿪣������Ҫ����session.update����
		 * ��ʱnews�Ѿ����һ��������� �������ݱ��л�������Ӧ�ļ�¼�����Ѿ�����session�������� ���Ըı���������Ҳ���ᱻ��⵽
		 */

		transaction.commit();
		session.close();

		// news.setId(100); //���������ط���ID���ˣ���ô���ݿ���û���������ļ�¼������ִ�������update�����׳��쳣

		session = SessionFactory.openSession();
		transaction = session.beginTransaction();
		//

		news.setAuthor("Oracle");
		session.update(news);// ��ʱ����Ҫ����session.update��������ʹ�޸������ݱ�����Ч

	}

	/**
	 * get VS load: 1��ִ��get���������������ض��� ִ��load����������ʹ�øö����򲻻�����ִ�в�ѯ���������Ƿ���һ���������
	 * get������������load���ӳټ���
	 * 
	 * 2�������ݱ���û�ж�Ӧ�ļ�¼����Session Ҳû�б��ر� get����null load
	 * ����ʹ�������������ԣ�û���⣻����Ҫ��ʼ���ˣ��׳��쳣
	 * 
	 * 3��load���ܻ��׳� LazyInitializationException �쳣��ԭ���ǣ���ִ��loadʱ�ȷ��ص���һ��
	 * ��������ڴ�������ʼ��֮ǰ��session�ر��ˣ����Ի��׳�����쳣
	 */

	@Test
	public void testLoad() {

		News news = session.load(News.class, 1);// �ڶ�������Ϊ�����ݱ��е�id
		// session.close(); //���ִ����仰���ͻ��׳��������쳣
		System.out.println(news);

	}

	/**
	 * �����ݿ��м��س־û�����
	 */

	@Test
	public void testGet() {
		News news = session.get(News.class, 4);
		System.out.println(news);
	}

	/**
	 * persistҲ��ִ�б������ ��save������: �ڵ���persist֮ǰ���������Ѿ���ID�ˣ��򲻻�ִ��INSERT ���ǻ��׳��쳣
	 */
	@Test
	public void testPersist() {
		News news = new News();
		news.setAuthor("dd");
		news.setTitle("DD");
		news.setDate(new Date());
		// news.setId(100); ���������ִ�л��׳��쳣

		session.persist(news);

	}

	/**
	 * 1��save(): 1)��ʹһ����ʱ�����Ϊ�־û����� 2)��Ϊ�������ID 3)����flush����ʱ���ᷢ��һ��INSERT���
	 * 4)����save()����֮ǰ����ID����Ч�� 5)���־û������ID�ǲ��ܱ��޸ĵ�
	 * 
	 */
	@Test
	public void testSave() {
		News news = new News();
		news.setAuthor("aa");
		news.setTitle("AA");
		news.setId(100);// ��������ID����Ч�ģ�����������ݿ��д洢���������κ�����
		news.setDate(new Date());

		/**
		 * ���δ�ӡ��һ�� ��һ�Σ�News [id=null, title=AA, author=aa, date=Mon Aug 27
		 * 21:06:41 CST 2018] �ڶ��Σ�News [id=10, title=AA, author=aa, date=Mon Aug
		 * 27 21:06:41 CST 2018]
		 * 
		 */
		System.out.println(news);

		session.save(news);

		System.out.println(news);

	}

	/**
	 * clear()������session����
	 */
	@Test
	public void testClear() {
		News news = session.get(News.class, 1);

		/**
		 * ����clear()�󣬾ͻ��ٷ�һ��SQL�Ĳ�ѯ��䣬ԭ��Session�еĻ�������Ѿ�û��
		 */
		session.clear();

		News news2 = session.get(News.class, 1);
	}

	/**
	 * refresh():��ǿ�Ʒ��� SELECT ��䣬��ʹSession�����еĶ���״̬�����ݿ������ݱ��״̬����һ��
	 * ����Ǹı����ݿ��е�ֵ������Ӱ�쵽�˴������ֵ
	 * Ϊ��ʹ�������δ�ӡ�Ľ����ͬ����debug���öϵ��ڵ�һ�δ�ӡ��ı����ݱ��ֵ������refresh֮��
	 * �ڶ��δ�ӡ�ͻ����Session�����еĶ����ֵ�����������ݱ��е����ݸı�֮�󣬸���Session��������е�ֵ��
	 * ��Ҫ��hibernate.cfg.xml�����ø��뼶��Ϊ2
	 */
	@Test
	public void testRefresh() {
		News news = session.get(News.class, 1);
		System.out.println(news);

		session.refresh(news);

		System.out.println(news);

	}

	/**
	 * ����session.flush����
	 * flush:ʹ���ݱ��еļ�¼��Session�����еĶ����״̬����һ�£�Ϊ�˱���һ�£����ܻᷢ����Ӧ��sql���
	 * �ȸı�˴������ֵ���̶�Ӱ�쵽���ݿ��е�ֵ 1����Transaction��commit�����У��ȵ���session.flush���������ύ����
	 * 2��flush()���ܻᷢ��sql��䣬�������ύ����
	 * 3��ע�⣺��δ�ύ�������ʾ����session.flush����֮ǰ��Ҳ�п��ܽ���flush����
	 * 
	 * 1����ִ��HQL��QBC��ѯ
	 * Ҫ���ѯ�������µ�״̬�����Ƚ���flush�������Եõ����ݱ�����²�������Ӧ�����testSessionFlush() 2)
	 * ������¼��id���ɵײ����ݿ�ʹ�������������ɵģ����ڵ���save()ʱ������������insert��� ��Ϊsave�����󣬱��뱣֤�����ID�Ǵ��ڵ�
	 * 
	 * 
	 */
	@Test
	public void testSessionFlush2() {
		News news = new News("Java", "SUN", new Date());
		session.save(news);
	}

	@Test
	public void testSessionFlush() {
		News news = session.get(News.class, 1);
		/**
		 * �ڴ˴�����Ϊ������session�������ں�transaction�С����Ի���transaction.commit����ִ��֮ǰ
		 * ����session.flush���������˴�����Ϣ�����ݿ������ݱ����Ϣ����ͬ����ִ��sql������£� Hibernate: update
		 * NEWS set TITLE=?, AUTHOR=?, DATE=? where ID=?
		 */
		news.setAuthor("SUN");

		/**
		 * �ڴ˴� ��Ҫ���ѯ�Ľ�������µģ�����session�����еĶ���״̬�Ѿ���ΪsetAuthor("Oracle")�ı�,��Ȼ
		 * ���ݱ��л�û�б䣬��������news2�Ĵ�ӡ�Ѿ�����
		 */
		News news2 = (News) session.createCriteria(News.class).uniqueResult();
		System.out.println(news2);

	}

	/**
	 * ����Session����
	 * 
	 */
	@Test
	public void testSessionCache() {

		/**
		 * ����������session�����ݿ��л�ȡ���ݣ�ֻ�е�һ��ִ����sql�Ĳ�ѯ��䣬�ڶ���ֱ�Ӵ�ӡ Hibernate: select
		 * news0_.ID as ID1_0_0_, news0_.TITLE as TITLE2_0_0_, news0_.AUTHOR as
		 * AUTHOR3_0_0_, news0_.DATE as DATE4_0_0_ from NEWS news0_ where
		 * news0_.ID=?
		 * 
		 * Session�ӿڵ�ʵ������һЩ���ϣ���Щ���Ϲ�����Session���棬�����˴����ݿ��� ��õ���Ϣ Session����Ϊһ������
		 */
		News news = session.get(News.class, 1);
		System.out.println(news);

		News news2 = session.get(News.class, 1);
		System.out.println(news2);

		System.out.println("Test");

	}

}
