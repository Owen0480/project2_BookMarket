package com.springboot.repository.mybatis;

import com.springboot.domain.Shipping;

public interface ShippingMapper {
    Shipping findShippingById(long id);
    int insertShipping(Shipping shipping);
}
