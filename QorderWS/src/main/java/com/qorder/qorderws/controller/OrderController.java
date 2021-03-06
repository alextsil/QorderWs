package com.qorder.qorderws.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.qorder.qorderws.dto.order.OrderDTO;
import com.qorder.qorderws.dto.order.OrderViewDTO;
import com.qorder.qorderws.exception.ResourceNotFoundException;
import com.qorder.qorderws.model.order.EOrderStatus;
import com.qorder.qorderws.service.IOrderService;

@RestController
@RequestMapping(value = "/orders")
public class OrderController {

	private static final Logger LOGGER = LoggerFactory.getLogger(OrderController.class);

	@Autowired
	private IOrderService orderService;

	@RequestMapping(value = "/business", params = "id", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<OrderViewDTO> createOrder(@RequestParam Long id, @RequestBody OrderDTO orderDTO) throws ResourceNotFoundException {
		LOGGER.info("Request for order submit");
		OrderViewDTO orderView = orderService.submitOrder(id, orderDTO);
		
		return new ResponseEntity<>(orderView,HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/business", params = "id", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<OrderViewDTO[]> getOrdersByBusinessId(@RequestParam Long id) throws ResourceNotFoundException {
		OrderViewDTO[] businessOrders = orderService.fetchOrdersByBusinessID(id);
		LOGGER.info("Request for business orders.\nFetchedList size is " + businessOrders.length);
		
		return new ResponseEntity<>(businessOrders, HttpStatus.OK);
	}
	
	@RequestMapping(value ="/business/{businessId}/order", params = "status", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<OrderViewDTO[]> getOrdersByStatus(@PathVariable Long businessId, @RequestParam String status) throws ResourceNotFoundException, IllegalArgumentException {
		EOrderStatus orderStatus = EOrderStatus.valueOf(status);
		OrderViewDTO[] businessOrders = orderService.fetchOrdersByStatus(businessId, orderStatus);
		LOGGER.info("Request for business orders with status query.\nFetchedList size is " + businessOrders.length);
		
		return new ResponseEntity<>(businessOrders, HttpStatus.OK);
	}
	
	@RequestMapping(value ="/order", params="id", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<OrderViewDTO> getOrdersById(@RequestParam Long id) throws ResourceNotFoundException {
		OrderViewDTO orderView = orderService.fetchOrderById(id);
		LOGGER.info("Request for order with id " + id);
		
		return new ResponseEntity<>(orderView, HttpStatus.OK);
	}
	
	@RequestMapping(value ="/order/{orderId}/order", params = "status", method = RequestMethod.POST)
	ResponseEntity<Void> changeOrderStatus(@PathVariable Long orderId, @RequestParam String status) throws ResourceNotFoundException, IllegalArgumentException {
		EOrderStatus orderStatus = EOrderStatus.valueOf(status);
		orderService.changeOrderStatus(orderId, orderStatus);
		LOGGER.info("Request to change status if order with id " + orderId);
		
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	ResponseEntity<String> sendBadRequestException(IllegalArgumentException ex) {
		LOGGER.warn("Exception was thrown, with cause " + ex.getCause() + "\nMessage: " + ex.getLocalizedMessage(), ex );
		return new ResponseEntity<>(ex.getLocalizedMessage(),HttpStatus.BAD_REQUEST);
	}
}
