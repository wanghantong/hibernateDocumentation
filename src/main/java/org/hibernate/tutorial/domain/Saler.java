/**

 * 创建时间：Sep 22, 2014 3:17:02 PM

 * 项目名称：hiberzore

 * @author T.Y

 * @version 1.0

 * @since JDK 1.7

 * 文件名称：Saler.java

 * 类说明：

 */
package org.hibernate.tutorial.domain;

import java.util.HashSet;
import java.util.Set;

public class Saler {
	private Long id;
	private int saleAge;

	public int getSaleAge() {
		return saleAge;
	}

	public void setSaleAge(int saleAge) {
		this.saleAge = saleAge;
	}

	public Long getId() {
		return id;
	}

	private void setId(Long id) {
		this.id = id;
	}

	private Set<Customer> customers = new HashSet<Customer>();

	public Set<Customer> getCustomers() {
		return customers;
	}

	public void setCustomers(Set<Customer> customers) {
		this.customers = customers;
	}

}
