/**

 * 创建时间：Sep 19, 2014 5:20:55 PM

 * 项目名称：hiberzore

 * @author T.Y

 * @version 1.0

 * @since JDK 1.7

 * 文件名称：AssociationTest.java

 * 类说明：

 */
package org.hibernate.tutorial.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.tutorial.util.HibernateUtil;
import org.junit.Test;

public class BiDirectionalTest {

	@Test
	public void createAndStorePerson() {
		Long eventId = createAndStoreEvent("My Event", new Date());
		System.out.println(eventId);
	}

	@Test
	public void createAndStoreEvent() {
		Long personId = createAndStorePerson("Foo", "Bar");
		System.err.println(personId);
	}

	@Test
	public void addEventToPersonInAnUnit() {
		Long eventId = createAndStoreEvent("My Event", new Date());
		Long personId = createAndStorePerson("Foo", "Bar");
		System.out.println("Added person " + personId + " to event " + eventId);
		addEventToPersonInAnUnit(personId, eventId);
	}

	@Test
	public void addEmailToPerson() {
		Long personId = createAndStorePerson("Foo", "Bar");
		String emailAddress = "hantogn4510@163.com";
		addEmailToPerson(personId, emailAddress);
	}

	@Test
	public void addPersonToEventUnidirectional() {
		Long eventId = createAndStoreEvent("My Event3", new Date());
		Long personId = createAndStorePerson("Foo3", "Bar3");
		// ----------------------5.1-----------------------------------
		// -----测试表明：配置了inverse="true"的一方，不能自动的进行双向维护，理由：关联表无数据
		addPersonToEventUnidirectional(personId, eventId);
	}

	@Test
	public void addEventToPersonUnidirectional() {
		Long eventId = createAndStoreEvent("My Event3", new Date());
		Long personId = createAndStorePerson("Foo3", "Bar3");
		// ----------------------5.1-----------------------------------
		// -----测试表明：配置了inverse="true"的一方，不能自动的进行双向维护，理由：关联表无数据
		addEventToPersonUnidirectional(personId, eventId);
	}

	@Test
	public void addPersonToEventBIdirectional() {
		Long eventId = createAndStoreEvent("My Event3", new Date());
		Long personId = createAndStorePerson("Foo3", "Bar3");
		addPersonToEventBIdirectional(personId, eventId);
	}

	@Test
	public void addEventToPersonBIdirectional() {
		Long eventId = createAndStoreEvent("My Event3", new Date());
		Long personId = createAndStorePerson("Foo3", "Bar3");
		addEventToPersonBIdirectional(personId, eventId);
	}

	@Test
	public void removePerson() {
		Long personId = selectMaxIdPerson();
		removePerson(personId);
	}

	@Test(expected = RuntimeException.class)
	public void removeEvent() {
		Long eventId = selectMaxIdEvent();
		System.out.println(eventId);
		// 配置inverse="true"，此时次方既不能级联保存，也不允许级联删除，而且在关联表中存在相关引用时，此方是不允许直接被删除的
		// 如果关联表中存在关联，那么将抛出外键约束异常，如果不存在关联，则只删除当前Event
		removeEvent(eventId);
	}

	@Test
	public void removeEventButPerson() {
		Long eventId = createAndStoreEvent("My Event3", new Date());
		Long personId = createAndStorePerson("Foo3", "Bar3");
		addEventToPersonUnidirectional(personId, eventId);
		removeEventButPerson(personId, eventId);
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

	private void addEventToPerson(Long personId, Long eventId) {
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

	private void addEventToPersonInAnUnit(Long personId, Long eventId) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();

		Person aPerson = (Person) session.load(Person.class, personId);
		Event anEvent = (Event) session.load(Event.class, eventId);
		aPerson.getEvents().add(anEvent);

		session.getTransaction().commit();
	}

	private void addEmailToPerson(Long personId, String emailAddress) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();

		Person aPerson = (Person) session.load(Person.class, personId);
		// adding to the emailAddress collection might trigger a lazy load of
		// the collection
		aPerson.getEmailAddresses().add(emailAddress);

		session.getTransaction().commit();
	}

	private void addEventToPersonUnidirectional(Long personId, Long eventId) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();

		Person aPerson = (Person) session.load(Person.class, personId);
		Event anEvent = (Event) session.load(Event.class, eventId);
		aPerson.getEvents().add(anEvent);

		session.getTransaction().commit();
	}

	private void addPersonToEventBIdirectional(Long personId, Long eventId) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();

		Person aPerson = (Person) session.load(Person.class, personId);
		Event anEvent = (Event) session.load(Event.class, eventId);
		// -----------inverse="true"----其实这行代码无效，不能将数据存入到关联表------------------
		anEvent.getParticipants().add(aPerson);
		// ----------真正起到关联----------------------------------------------------------
		aPerson.getEvents().add(anEvent);

		session.getTransaction().commit();
	}

	private void addPersonToEventUnidirectional(Long personId, Long eventId) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();

		Person aPerson = (Person) session.load(Person.class, personId);
		Event anEvent = (Event) session.load(Event.class, eventId);

		anEvent.getParticipants().add(aPerson);
		// -----测试表明：配置了inverse="true"的一方，不能自动的进行双向维护，理由：关联表无数据
		session.getTransaction().commit();
	}

	private void addEventToPersonBIdirectional(Long personId, Long eventId) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();

		Person aPerson = (Person) session.load(Person.class, personId);
		Event anEvent = (Event) session.load(Event.class, eventId);
		
		aPerson.addToEvent(anEvent);//---源自document，在domain中加了双向维护的方法，其实并不好
//		// ---起到双向保存作用的，
//		aPerson.getEvents().add(anEvent);
//		// ---多了2个查询，以为inverse="true"所以该行代码不能起到双向保存作用
//		anEvent.getParticipants().add(aPerson);
		session.getTransaction().commit();

	}

	private void removePerson(Long personId) {
		// --关联关系数据和对应的Event must be deleted
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		Person aPerson = (Person) session.load(Person.class, personId);
		session.delete(aPerson);
		session.getTransaction().commit();
	}

	private void removePersonFromEvent(Long personId, Long eventId) {
		// 如果关联表中存在关联，那么将抛出外键约束异常，如果不存在关联，则只删除当前Event
		// Session session =
		// HibernateUtil.getSessionFactory().getCurrentSession();
		// session.beginTransaction();
		// Person aPerson = (Person) session.load(Person.class, personId);
		// session.delete(aPerson);
		// session.getTransaction().commit();
	}

	private void removeEvent(Long eventId) {

		// 如果存在关联将会报错，参见removeEventButPerson
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		Event event = (Event) session.load(Event.class, eventId);
		// 配置inverse="true"，此时次方既不能级联保存，也不允许级联删除，而且在关联表中存在相关引用时，此方是不允许直接被删除的
		session.delete(event);
		session.getTransaction().commit();
	}

	private void removeEventButPerson(Long personId, Long eventId) {
		System.out.println("personId :" + personId + " -eventId: " + eventId);
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		Event event = (Event) session.load(Event.class, eventId);
		Person person = (Person) session.load(Person.class, personId);
		// *******************
		// -通过此行来解除关联关系,从关联表中删除关联数据
		person.getEvents().remove(event);
		// *******************
		session.delete(event);
		session.getTransaction().commit();
	}

	private Long selectMaxIdPerson() {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		Long id = (Long) session.createQuery("select max(id) from Person")
				.uniqueResult();
		session.getTransaction().commit();
		return id;
	}

	private Long selectMaxIdEvent() {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		Long id = (Long) session.createQuery("select max(id) from Event")
				.uniqueResult();
		session.getTransaction().commit();
		return id;
	}

}
