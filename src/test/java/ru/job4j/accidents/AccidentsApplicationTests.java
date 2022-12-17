package ru.job4j.accidents;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.job4j.accidents.repository.AccidentHibernate;

@SpringBootTest(classes = AccidentsApplication.class)
public class AccidentsApplicationTests {
	private  final AccidentHibernate accidentHibernate;

	@Autowired
	AccidentsApplicationTests(AccidentHibernate accidentHibernate) {
		this.accidentHibernate = accidentHibernate;
	}

	@Test
	public void findById() {
		accidentHibernate.findById();
	}
}