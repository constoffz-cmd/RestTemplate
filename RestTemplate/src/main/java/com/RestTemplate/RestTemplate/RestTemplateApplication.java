package com.RestTemplate.RestTemplate;

import com.RestTemplate.RestTemplate.Services.Communication;
import com.RestTemplate.RestTemplate.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

@SpringBootApplication
public class RestTemplateApplication implements CommandLineRunner {

	private final Communication communication;
	private final StringBuilder finalCode = new StringBuilder();
	private Logger logger = Logger.getLogger(RestTemplateApplication.class.getName());

	@Autowired
	public RestTemplateApplication(Communication communication) {
		this.communication = communication;
	}


	public static void main(String[] args) {

		SpringApplication.run(RestTemplateApplication.class, args);

	}


	@Override
	public void run(String... args) throws Exception {
		try {
			logger.info("\n--- Получение списка всех пользователей ---");
			ResponseEntity<List<User>> usersResponse = communication.getAllUsers();
			if (usersResponse.getStatusCode().is2xxSuccessful()) {
			} else {
				logger.warning("Ошибка при получении списка пользователей: " + usersResponse.getStatusCode());
				return;
			}

			logger.info("\n--- Сохранение пользователя (id=3) ---");
			User newUser = new User(3L, "James", "Brown", (byte) 30);
			ResponseEntity<String> createUserResponse = communication.createUser(newUser);

			if (createUserResponse.getStatusCode().is2xxSuccessful()) {
				String codePart1 = createUserResponse.getBody();
				finalCode.append(codePart1);
				logger.info("\n---Пользователь успешно создан. Первая часть кода: " + codePart1);
			} else {
				logger.warning("Ошибка при создании пользователя: " + createUserResponse.getStatusCode());

			}

			logger.info("\n--- Изменение пользователя (id=3) ---");
			User updatedUser = new User(3L, "Thomas", "Shelby", (byte) 25); // age не меняем, передаем null
			ResponseEntity<String> updateUserResponse = communication.updateUser(updatedUser);

			if (updateUserResponse.getStatusCode().is2xxSuccessful()) {
				String codePart2 = updateUserResponse.getBody();
				finalCode.append(codePart2);
				logger.info("\n---Пользователь успешно изменен. Вторая часть кода: " + codePart2);
			} else {
				logger.warning("Ошибка при изменении пользователя: " + updateUserResponse.getStatusCode());

			}

			String codePart3 = communication.deleteUser(3L);
			finalCode.append(codePart3);
			logger.info("\n---ИТОГОВЫЙ КОД: " + finalCode.toString());
			logger.info("\n---Длина кода: " + finalCode.length());


		} catch (Exception e) {
			logger.warning("Произошла непредвиденная ошибка: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
