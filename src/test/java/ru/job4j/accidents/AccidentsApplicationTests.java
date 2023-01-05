package ru.job4j.accidents;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.repository.AccidentHibernate;
import java.util.Collection;
import java.util.Optional;

/**
 * Делаю как бы тестирование, просто для себя проверять
 * результаты работы без запуска всех слоев
 * Я понимаю, что это скорее всего неправильно из-за
 * одного метода поднимать всю систему, делаю это просто в учебных целях
 */
@Disabled
@SpringBootTest
public class AccidentsApplicationTests {

	private  final AccidentHibernate accidentHibernate;

	@Autowired
	AccidentsApplicationTests(AccidentHibernate accidentHibernate) {
		this.accidentHibernate = accidentHibernate;
	}

	@Test
	public void findById() {
		System.out.println(accidentHibernate.findById(1));
	}

	@Test
	public void findAll() {
		Collection<Accident> accidents = accidentHibernate.findAll();
		accidents.forEach(System.out::println);
	}

	@Test
	public void save() {
		Accident accident = accidentHibernate.findById(1).get();
		accident.setName("SAVE");
		accidentHibernate.save(accident);
	}

	@Test
	public void replace() {
		Optional<Accident> optionalAccident = accidentHibernate.findById(1);
		Accident accident = optionalAccident.get();
		accident.setId(2);
		accident.setName("REPLACE");
		System.out.println(accident);
		accidentHibernate.replace(accident);
	}
}