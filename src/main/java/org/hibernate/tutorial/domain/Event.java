/**

 * 创建时间：Sep 17, 2014 8:58:35 PM

 * 项目名称：hiberzore

 * @author T.Y

 * @version 1.0

 * @since JDK 1.7

 * 文件名称：Event.java

 * 类说明：

 */
package org.hibernate.tutorial.domain;

import java.util.Date;

public class Event {
	private Long id;

	private String title;
	private Date date;

	public Event() {
	}

	public Long getId() {
		return id;
	}

	private void setId(Long id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
