/**

 * 创建时间：Sep 22, 2014 3:26:04 PM

 * 项目名称：hiberzore

 * @author T.Y

 * @version 1.0

 * @since JDK 1.7

 * 文件名称：UnidirectionalTest.java

 * 类说明：

 */
package org.hibernate.tutorial.domain;

import java.io.Serializable;

import org.hibernate.Session;
import org.hibernate.tutorial.util.HibernateUtil;
import org.junit.Test;

public class UnidirectionalTest {

	@Test
	public void createAndStoreSaler() {
		Long salerId = createAndStoreSaler(10);
		System.out.println(salerId);
	}

	private Long createAndStoreSaler(int age) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		Saler saler = new Saler();
		saler.setSaleAge(age);
		Serializable id = session.save(saler);
		session.getTransaction().commit();
		return (Long) id;
	}

	@Test
	public void createAndStoreCustomer() {
		Long customerId = createAndStoreCustomer("北京", "张三");
		System.err.println(customerId);
	}

	private Long createAndStoreCustomer(String city, String name) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();

		Customer customer = new Customer();
		customer.setCity(city);
		customer.setName(name);

		Serializable id = session.save(customer);
		session.getTransaction().commit();
		return (Long) id;
	}

	@Test
	public void removeCustomer() {
		Long customerId = selectMaxIdCustomer();
		removeCustomer(customerId);
	}

	private void removeCustomer(Long customerId) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		Customer customer = (Customer) session.load(Customer.class, customerId);
		session.delete(customer);
		session.getTransaction().commit();
	}

	@Test
	public void removeSaler() {
		Long customerId = selectMaxIdCustomer();
		removeSaler(customerId);
	}

	private void removeSaler(Long salerId) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		Saler saler = (Saler) session.load(Saler.class, salerId);
		// 配置inverse="true"，此时次方既不能级联保存，也不允许级联删除，而且在关联表中存在相关引用时，此方是不允许直接被删除的
		session.delete(saler);
		session.getTransaction().commit();
	}

	@Test
	public void addCustomerToSalerUnidirectional() {
		Long salerId = createAndStoreSaler(10);
		Long customerId = createAndStoreCustomer("北京", "张三");
		System.out.println("Added Customer " + salerId + " to Saler "
				+ customerId);
		addCustomerToSalerUnidirectional(salerId, customerId);
	}

	private void addCustomerToSalerUnidirectional(Long customerId, Long salerId) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();

		Customer customer = (Customer) session.load(Customer.class, customerId);
		Saler saler = (Saler) session.load(Saler.class, salerId);
		saler.getCustomers().add(customer);
		session.getTransaction().commit();
	}

	@Test
	public void removeCustomerButSaler() {
		Long salerId = createAndStoreSaler(10);
		Long customerId = createAndStoreCustomer("北京", "张三");
		System.out.println("Added Customer " + salerId + " to Saler "
				+ customerId);
		addCustomerToSalerUnidirectional(salerId, customerId);
		removeCustomerButSaler(salerId, customerId);
	}

	private void removeCustomerButSaler(Long customerId, Long salerId) {
		System.out.println("customerId :" + customerId + " -salerId: "
				+ salerId);
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		Customer customer = (Customer) session.load(Customer.class, customerId);
		Saler saler = (Saler) session.load(Saler.class, salerId);
		// *******************
		// -通过此行来解除关联关系,从关联表中删除关联数据
		saler.getCustomers().remove(customer);
		// *******************
		session.delete(customer);
		session.getTransaction().commit();
	}

	private Long selectMaxIdCustomer() {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		Long id = (Long) session.createQuery("select max(id) from Customer")
				.uniqueResult();
		session.getTransaction().commit();
		return id;
	}

	private Long selectMaxIdSaler() {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		Long id = (Long) session.createQuery("select max(id) from Saler")
				.uniqueResult();
		session.getTransaction().commit();
		return id;
	}
}
