/**

 * 创建时间：Sep 19, 2014 3:04:02 PM

 * 项目名称：hiberzore

 * @author T.Y

 * @version 1.0

 * @since JDK 1.7

 * 文件名称：EventManagerServlet.java

 * 类说明：

 */
package org.hibernate.tutorial.web;

import java.io.IOException;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.tutorial.util.HibernateUtil;

public class EventManagerServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy");

		try {
			// Begin unit of work
			HibernateUtil.getSessionFactory().getCurrentSession()
					.beginTransaction();

			

			// End unit of work
			HibernateUtil.getSessionFactory().getCurrentSession()
					.getTransaction().commit();
		} catch (Exception ex) {
			HibernateUtil.getSessionFactory().getCurrentSession()
					.getTransaction().rollback();
			if (ServletException.class.isInstance(ex)) {
				throw (ServletException) ex;
			} else {
				throw new ServletException(ex);
			}
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);

	}

}
