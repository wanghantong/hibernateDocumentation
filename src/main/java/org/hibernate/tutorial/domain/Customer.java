/**

 * 创建时间：Sep 22, 2014 3:16:00 PM

 * 项目名称：hiberzore

 * @author T.Y

 * @version 1.0

 * @since JDK 1.7

 * 文件名称：Customer.java

 * 类说明：

 */
package org.hibernate.tutorial.domain;

import java.util.HashSet;
import java.util.Set;

public class Customer {
	private Long id;
	private String name;
	private String city;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Long getId() {
		return id;
	}

	private void setId(Long id) {
		this.id = id;
	}

}
