package com.springboot.repository.mybatis;

import com.springboot.domain.Customer;

public interface CustomerMapper {
    Customer findCustomerById(long id);
    int insertCustomer(Customer customer);
}
