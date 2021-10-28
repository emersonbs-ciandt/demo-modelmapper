package com.example.demo;

import com.example.demo.dto.OrderDTO;
import com.example.demo.model.Address;
import com.example.demo.model.Customer;
import com.example.demo.model.ListOrder;
import com.example.demo.model.Name;
import com.example.demo.model.Order;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class DemoApplicationTests {

	private ObjectMapper jsonMapper = new ObjectMapper();

	private ModelMapper modelMapper  = new ModelMapper();

	@SneakyThrows
	@Test
	void testFlatteningModelMapper() {
		Order order = getOrder();

		System.out.println(jsonMapper.writeValueAsString(order));

		OrderDTO orderDTO = modelMapper.map(order, OrderDTO.class);

		System.out.println(jsonMapper.writeValueAsString(orderDTO));

		assertThat(orderDTO.getCustomerFirstName()).isEqualTo(order.getCustomer().getName().getFirstName());
		assertThat(orderDTO.getBillingStreet()).isEqualTo(order.getBilling().getStreet());
		assertThat(orderDTO.getBillingCity()).isEqualTo(order.getBilling().getCity());
	}

	@SneakyThrows
	@Test
	void testProjectionModelMapper() {
		OrderDTO orderDTO = getOrderDTO();

		System.out.println(jsonMapper.writeValueAsString(orderDTO));

		Order order = modelMapper.map(orderDTO, Order.class);

		System.out.println(jsonMapper.writeValueAsString(order));

		assertThat(orderDTO.getCustomerFirstName()).isEqualTo(order.getCustomer().getName().getFirstName());
		assertThat(orderDTO.getBillingStreet()).isEqualTo(order.getBilling().getStreet());
		assertThat(orderDTO.getBillingCity()).isEqualTo(order.getBilling().getCity());
	}


	@SneakyThrows
	@Test
	void testSkippingPropertiesModelMapper() {

		Order order = getOrder();

		System.out.println(jsonMapper.writeValueAsString(order));

		modelMapper.createTypeMap(Order.class, OrderDTO.class)
				.addMappings(mapper -> mapper.skip(OrderDTO::setCustomerFirstName));

		OrderDTO orderDTO = modelMapper.map(order, OrderDTO.class);

		System.out.println(jsonMapper.writeValueAsString(orderDTO));

		assertThat(orderDTO.getCustomerFirstName()).isNotEqualTo(order.getCustomer().getName().getFirstName());
	}

	@SneakyThrows
	@Test
	void testAddMappingModelMapper() {
		OrderDTO orderDTO = getOrderDTO();

		System.out.println(jsonMapper.writeValueAsString(orderDTO));

		modelMapper.createTypeMap(OrderDTO.class, Order.class)
				.addMapping(OrderDTO::getCustomerFirstName, Order::setOptionalName);

		Order order = modelMapper.map(orderDTO, Order.class);

		System.out.println(jsonMapper.writeValueAsString(order));

		assertThat(orderDTO.getCustomerFirstName()).isEqualTo(order.getCustomer().getName().getFirstName());
		assertThat(orderDTO.getBillingStreet()).isEqualTo(order.getBilling().getStreet());
		assertThat(orderDTO.getBillingCity()).isEqualTo(order.getBilling().getCity());
		assertThat(orderDTO.getCustomerFirstName()).isEqualTo(order.getOptionalName());
	}

	@SneakyThrows
	@Test
	void testToListModelMapper() {

		Converter<Order, ListOrder> converter = context -> {
			List<Order> orderList = new ArrayList<>();
			Order o = context.getSource();
			o.setOptionalName("Change value on converter");
			orderList.add(o);

			ListOrder listOrder = new ListOrder();
			listOrder.setOrderList(orderList);
			return listOrder;
		};

		modelMapper.createTypeMap(Order.class, ListOrder.class).setConverter(converter);

		Order order = getOrder();

		ListOrder orderList = modelMapper.map(order, ListOrder.class);

		System.out.println(jsonMapper.writeValueAsString(orderList));

		assertThat(orderList.getOrderList().get(0).getCustomer().getName().getFirstName()).isEqualTo(order.getCustomer().getName().getFirstName());
	}

	private Order getOrder() {
		Order order = new Order();

		Name name = new Name();
		name.setFirstName("First Name 1");
		name.setLastName("Last Name 1");

		Customer customer = new Customer();
		customer.setName(name);

		Address address = new Address();
		address.setCity("City 1");
		address.setStreet("Street 1");

		order.setCustomer(customer);
		order.setBilling(address);
		return order;
	}


	private OrderDTO getOrderDTO() {
		OrderDTO orderDTO = new OrderDTO();
		orderDTO.setCustomerFirstName("First Name 1");
		orderDTO.setCustomerLastName("Last Name 1");
		orderDTO.setBillingStreet("Street 1");
		orderDTO.setBillingCity("City 1");
		return orderDTO;
	}

}
