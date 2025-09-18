package com.manufacturas.order.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.manufacturas.order.model.Product;

@FeignClient("product-service")
public interface ProductFeignClient {

    @RequestMapping(method = RequestMethod.GET, value = "/api/v1/products/{productId}", consumes = "application/json")
    Product getProduct(@PathVariable("productId") Long productId);

    @RequestMapping(method = RequestMethod.PUT, value = "/api/v1/products/{productId}/stock", consumes = "application/json")
    void updateStock(@PathVariable("productId") Long productId, @RequestBody Integer stock);
}
