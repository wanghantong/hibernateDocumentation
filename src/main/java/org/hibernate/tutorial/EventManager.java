/**

 * 创建时间：Sep 17, 2014 9:47:02 PM

 * 项目名称：hiberzore

 * @author T.Y

 * @version 1.0

 * @since JDK 1.7

 * 文件名称：EventManager.java

 * 类说明：

 */
package org.hibernate.tutorial;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.tutorial.domain.Event;
import org.hibernate.tutorial.domain.Person;
import org.hibernate.tutorial.util.HibernateUtil;

public class EventManager {
	public static void main(String[] args) {
		EventManager mgr = new EventManager();
		// -------------------1-------------------------
		// mgr.createAndStoreEvent("My Event", new Date());
		// -------------------2-------------------------
		// List<Event> events = mgr.listEvents();
		// for (int i = 0; i < events.size(); i++) {
		// Event theEvent = (Event) events.get(i);
		// System.out.println("Event: " + theEvent.getTitle() + " Time: "
		// + theEvent.getDate());
		// }
		// ---------------------3---------------------------
		Long eventId = mgr.createAndStoreEvent("My Event", new Date());
		System.out.println(eventId);
		Long personId = mgr.createAndStorePerson("Foo", "Bar");
		//----------------------3.1-----------------------------
		// mgr.addPersonToEvent(personId, eventId);
		// System.out.println("Added person " + personId + " to event " + eventId);
		//----------------------3.2--------------------------------
		mgr.addPersonToEventInAnUnit(personId, eventId);
		System.out.println("Added person " + personId + " to event " + eventId);
		//----------------------4-----------------------------------
		
		HibernateUtil.getSessionFactory().close();
		
		
		
	}

	private Long createAndStorePerson(String Firstname, String Lastname) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();

		Person person = new Person();
		person.setFirstname(Firstname);
		person.setLastname(Firstname);
		Serializable id = session.save(person);
		session.getTransaction().commit();
		return (Long) id;
	}

	private Long createAndStoreEvent(String title, Date theDate) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();

		Event theEvent = new Event();
		theEvent.setTitle(title);
		theEvent.setDate(theDate);
		Serializable id = session.save(theEvent);
		session.getTransaction().commit();
		return (Long) id;
	}

	private List<Event> listEvents() {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		List<Event> result = session.createQuery("from Event").list();
		session.getTransaction().commit();
		return result;
	}

	private void addPersonToEvent(Long personId, Long eventId) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();

		Person aPerson = (Person) session
				.createQuery(
						"select p from Person p left join fetch p.events where p.id = :pid")
				.setParameter("pid", personId).uniqueResult(); // Eager fetch
																// the
																// collection so
																// we can use it
																// detached
		Event anEvent = (Event) session.load(Event.class, eventId);

		session.getTransaction().commit();

		// End of first unit of work

		aPerson.getEvents().add(anEvent); // aPerson (and its collection) is
											// detached

		// Begin second unit of work

		Session session2 = HibernateUtil.getSessionFactory()
				.getCurrentSession();
		session2.beginTransaction();
		session2.update(aPerson); // Reattachment of aPerson

		session2.getTransaction().commit();
	}
	
	private void addPersonToEventInAnUnit(Long personId, Long eventId) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        Person aPerson = (Person) session.load(Person.class, personId);
        Event anEvent = (Event) session.load(Event.class, eventId);
        aPerson.getEvents().add(anEvent);

        session.getTransaction().commit();
    }
}
