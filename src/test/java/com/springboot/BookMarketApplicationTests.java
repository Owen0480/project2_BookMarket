package com.springboot;

import com.springboot.domain.Customer;
import com.springboot.repository.mybatis.CustomerMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class BookMarketApplicationTests {

	@Autowired
	CustomerMapper customerMapper;

	@Test
	void contextLoads() {
	}

	@Test
	@Transactional
	@Rollback(false)  // 트랜잭션 롤백 방지, DB에 데이터 남김
	void mybatisInsertTest1() {
		Customer customer = new Customer();
		customer.setName("Alice");
		customer.setPhone("010-1234-5678");

		int rowCount = customerMapper.insertCustomer(customer);
		System.out.println("Inserted row count: " + rowCount);
		System.out.println("Generated ID: " + customer.getId()); // auto_increment된 PK 확인
	}

	@Test
	void mybatisSelectTest1() {
		long searchId = 1L; // 조회할 고객 ID
		Customer customer = customerMapper.findCustomerById(searchId);
		if (customer != null) {
			System.out.println("Customer found: " + customer.getName() + ", Phone: " + customer.getPhone());
		} else {
			System.out.println("Customer with ID " + searchId + " not found.");
		}
	}
}
