package com.wzc.hibernate.entities.n21both;

import java.util.HashSet;
import java.util.Set;

public class Customer {
	private Integer customerId;
	private String customerName;

	//˫��n-1���Ѽ��ϳ�ʼ�����Է�ֹ��ָ������
	/**
	 * 1����������������ʱ����ʹ�ýӿ����ͣ���Ϊ Hibernate �ڻ�ȡ��������ʱ�����ص��� Hibernate���õ�
	 * �������ͣ������� JavaSe �е�һ����׼����ʵ��
	 * 2����Ҫ�Ѽ��ϳ�ʼ������ֹ��ָ��ķ���
	 */
	
	private Set<Order> orders = new HashSet<>();

	/**
	 * @return the customerId
	 */
	public Integer getCustomerId() {
		return customerId;
	}

	/**
	 * @return the orders
	 */
	public Set<Order> getOrders() {
		return orders;
	}

	/**
	 * @param orders the orders to set
	 */
	public void setOrders(Set<Order> orders) {
		this.orders = orders;
	}

	/**
	 * @param customerId
	 *            the customerId to set
	 */
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	/**
	 * @return the customerName
	 */
	public String getCustomerName() {
		return customerName;
	}

	/**
	 * @param customerName
	 *            the customerName to set
	 */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}



}
