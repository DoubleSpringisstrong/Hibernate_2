package com.wzc.hibernate.entity;

import java.sql.Blob;
import java.util.Date;

//javabean��
//����ÿ�����ԣ�����Ҫдget��set��������������쳣

public class News {
	/*
	 * ��߿���Ч��:
	 * A:���������Զ��ṩ���췽��
	 * 	a:�޲ι��췽�� 
	 * 		�ڴ��������Ҽ�--source--Generate Constructors from Superclass
	 * 		atl+shift+s+c
	 *	b:���ι��췽�� 
	 *		�ڴ��������Ҽ�--source--Generate Constructors using fields.. -- finish
	 *		atl+shift+s+o
	 * B:�ɶԵ�getXxx()��setXxx()
	 * 		�ڴ��������Ҽ�--source--Generate Getters and Setters...
	 * 		atl+shift+s+r
	 */
	
	
	
	private Integer id;
	private String title;
	private String author;
	
	private Date date;
	
	//����Property�е�formula����
	//����������title:author
	
	private String desc;
	
	//���ı�
	private String context;
	
	//�����ƴ��ļ�
	private Blob image;
	

	public News() {
		super();
		// TODO Auto-generated constructor stub
	}
	

	public News(String title, String author, Date date) {
		super();
		this.title = title;
		this.author = author;
		this.date = date;
	}



	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	
	/**
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}


	/**
	 * @param desc the desc to set
	 */
	public void setDesc(String Desc) {
		desc = Desc;
	}

	

	/**
	 * @return the context
	 */
	public String getContext() {
		return context;
	}


	/**
	 * @param context the context to set
	 */
	public void setContext(String context) {
		this.context = context;
	}


	/**
	 * @return the image
	 */
	public Blob getImage() {
		return image;
	}


	/**
	 * @param image the image to set
	 */
	public void setImage(Blob image) {
		this.image = image;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "News [id=" + id + ", title=" + title + ", author=" + author + ", date=" + date + "]";
	}
	
	

}
