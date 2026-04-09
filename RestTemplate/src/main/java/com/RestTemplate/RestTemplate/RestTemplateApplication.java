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

@SpringBootApplication
public class RestTemplateApplication implements CommandLineRunner {

	private final Communication communication;
	private final StringBuilder finalCode = new StringBuilder();

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
			//String sessia;
			// 1. Получить список всех пользователей
			System.out.println("--- Получение списка всех пользователей ---");
			ResponseEntity<List<User>> usersResponse = communication.getAllUsers();
			if (usersResponse.getStatusCode().is2xxSuccessful()) {
				//System.out.println("Список пользователей получен. Количество: " + Objects.requireNonNull(usersResponse.getBody()).size());
				//sessia = usersResponse.getHeaders().get("set-cookie"));
				//System.out.println("Cookies: " + usersResponse.getHeaders().get("set-cookie"));
			} else {
				System.err.println("Ошибка при получении списка пользователей: " + usersResponse.getStatusCode());
				return;
			}

			//System.out.println("\n--- Сохранение пользователя (id=3) ---");
			User newUser = new User(3L, "James", "Brown", (byte) 30); // Age на ваш выбор
			ResponseEntity<String> createUserResponse = communication.createUser(newUser);

			if (createUserResponse.getStatusCode().is2xxSuccessful()) {
				String codePart1 = createUserResponse.getBody();
				finalCode.append(codePart1);
				System.out.println("Пользователь успешно создан. Первая часть кода: " + codePart1);
			} else {
				System.err.println("Ошибка при создании пользователя: " + createUserResponse.getStatusCode());

			}


			System.out.println("\n--- Изменение пользователя (id=3) ---");

			User updatedUser = new User(3L, "Thomas", "Shelby", (byte) 25); // age не меняем, передаем null
			ResponseEntity<String> updateUserResponse = communication.updateUser(updatedUser);

			if (updateUserResponse.getStatusCode().is2xxSuccessful()) {
				String codePart2 = updateUserResponse.getBody();
				finalCode.append(codePart2);
				System.out.println("Пользователь успешно изменен. Вторая часть кода: " + codePart2);
			} else {
				System.err.println("Ошибка при изменении пользователя: " + updateUserResponse.getStatusCode());

			}


			String codePart3 = communication.deleteUser(3L);
			finalCode.append(codePart3);
			//System.out.println("Пользователь успешно удален. Третья часть кода: " + codePart3);


			System.out.println("\n---------------------------------------");
			System.out.println("ИТОГОВЫЙ КОД: " + finalCode.toString());
			System.out.println("Длина кода: " + finalCode.length()); // Должно быть 18


		} catch (Exception e) {
			System.err.println("Произошла непредвиденная ошибка: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
