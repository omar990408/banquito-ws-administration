package ec.edu.espe.arquitectura.banquito.administration;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class AdministrationApplicationTests {

	Calculator calculator = new Calculator();
	@Test
	void contextLoads() {
	}

	@Test
	public void testAdd() {
		//given
		int a = 10;
		int b = 20;
		//when
		int result = calculator.add(a, b);
		//then
		assertThat(result).isEqualTo(30);
	}

	class Calculator {
		public int add(int a, int b) {
			return a + b;
		}
	}

}
