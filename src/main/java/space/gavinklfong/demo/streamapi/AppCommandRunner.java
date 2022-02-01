package space.gavinklfong.demo.streamapi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import space.gavinklfong.demo.streamapi.models.Customer;
import space.gavinklfong.demo.streamapi.models.Order;
import space.gavinklfong.demo.streamapi.models.Product;
import space.gavinklfong.demo.streamapi.repos.CustomerRepo;
import space.gavinklfong.demo.streamapi.repos.OrderRepo;
import space.gavinklfong.demo.streamapi.repos.ProductRepo;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Component
public class  AppCommandRunner implements CommandLineRunner {

	@Autowired
	private CustomerRepo customerRepos;
	
	@Autowired
	private OrderRepo orderRepos;
	
	@Autowired
	private ProductRepo productRepos;

	@Transactional
	@Override
	public void run(String... args) throws Exception {
		//Here goes code to be executed
//		orderRepos.findAll().stream()
//				.sorted(Comparator.comparing(Order::getOrderDate).reversed())
//				.forEach(o->log.info(o.toString()));
		exercise12();
	}
	//Obtain a list of products belongs to category “Books” with price > 100
	void exercise1(){
		List<Product> productList = productRepos.findAll();

		log.info("Original list");
		productList.stream()
						.forEach(p-> log.info(p.toString()));

		log.info("List after filters");
		productList.stream()
				.filter(p -> p.getCategory().equals("Books"))
				.filter(p-> p.getPrice()>100)
				.forEach(p-> log.info(p.toString()));
	}
	//Obtain a list of order with products belong to category “Baby”
	void exercise2(){
		List<Order> orderList = orderRepos.findAll();

		orderList.stream()
				.filter(o->
						o.getProducts()
								.stream()
								.anyMatch(p-> p.getCategory()
										.equalsIgnoreCase("Baby")))
				.forEach(o-> log.info(o.toString()));
	}
	//Obtain a list of product with category = “Toys” and then apply 10% discount
	void exercise3(){
		List<Product> productList = productRepos.findAll();

		log.info("List before discount");
		productList.stream()
				.filter(p -> p.getCategory().equalsIgnoreCase("Toys"))
				.forEach(p-> log.info(p.toString()));

		log.info("List after discount");
		productList.stream()
				.filter(p -> p.getCategory().equalsIgnoreCase("Toys"))
				.map(p-> p.withPrice(p.getPrice()*0.10))
				.forEach(p-> log.info(p.toString()));
	}
	//Obtain a list of products ordered by customer of tier 2 between 01-Feb-2021 and 01-Apr-2021
	void exercise4(){
		List<Order> orderList = orderRepos.findAll();

		orderList.stream()
				.filter(o->o.getCustomer().getTier()==2)
				.filter(o-> o.getOrderDate().compareTo(LocalDate.of(2021,2,1))>=0)
				.filter(o-> o.getOrderDate().compareTo(LocalDate.of(2021,4,1))<=0)
				.flatMap(o-> o.getProducts().stream())
				.distinct()
				.forEach(p-> log.info(p.toString()));
	}
	//Get the cheapest products of “Books” category
	void exercise5(){
		List<Product> productList = productRepos.findAll();

		log.info("List of products");
		productList.stream()
				.forEach(p-> log.info(p.toString()));

		log.info("Cheapest product on list");
		log.info(productList.stream()
				.filter(p->p.getCategory().equalsIgnoreCase("Books"))
				.min(Comparator.comparing(Product::getPrice)).get().toString());
	}
	//Get the 3 most recent placed order
	void exercise6(){
		List<Order> orderList = orderRepos.findAll();

		orderList.stream()
				.sorted(Comparator.comparing(Order::getOrderDate).reversed())
				.limit(3)
				.forEach(o->log.info(o.toString()));
	}
	//Get a list of orders which were ordered on 15-Mar-2021, log the order records to the console and then return its product list
	void exercise7(){
		List<Order> orderList = orderRepos.findAll();

		orderList.stream()
				.filter(o-> o.getOrderDate().isEqual(LocalDate.of(2021,3,15)))
				.peek(o-> log.info(o.toString()))
				.flatMap(o->o.getProducts().stream())
				.distinct()
				.forEach(p->log.info(p.toString()));
	}
	//Calculate total lump sum of all orders placed in Feb 2021
	void exercise8(){
		List<Order> orderList = orderRepos.findAll();

		Double sum =orderList.stream()
				.filter(o->o.getOrderDate().compareTo(LocalDate.of(2021,2,1))>=0)
				.filter(o-> o.getOrderDate().compareTo(LocalDate.of(2021,3,1))<0)
				.flatMap(o->o.getProducts().stream())
				.mapToDouble(p-> p.getPrice())
				.sum();
		log.info(sum.toString());
	}
	//Calculate order average payment placed on 15-Mar-2021
	void exercise9(){
		List<Order> orderList = orderRepos.findAll();

		Double avg = orderList.stream()
				.filter(o -> o.getOrderDate().isEqual(LocalDate.of(2021, 3, 15)))
				.mapToDouble(o->o.getProducts().stream().mapToDouble(p->p.getPrice()).average().getAsDouble())
				.average().getAsDouble();

		log.info(avg.toString());

	}
	//Obtain a collection of statistic figures (i.e. sum, average, max, min, count) for all products of category “Books”
	void exercise10(){
		DoubleSummaryStatistics statistics = productRepos.findAll()
				.stream()
				.filter(p -> p.getCategory().equalsIgnoreCase("Books"))
				.mapToDouble(p -> p.getPrice())
				.summaryStatistics();

		log.info(String.format("count = %1$d, average = %2$f, max = %3$f, min = %4$f, sum = %5$f",
				statistics.getCount(), statistics.getAverage(), statistics.getMax(), statistics.getMin(), statistics.getSum()));

	}
	//Obtain a data map with order id and order’s product count
	void exercise11(){
		Map<Long, Integer> result = orderRepos.findAll()
				.stream()
				.collect(
						Collectors.toMap(
								order -> order.getId(),
								order -> order.getProducts().size()
						)
				);
		log.info(result.toString());
	}
	//Produce a data map with order records grouped by customer
	void exercise12(){
		Map<Customer, List<Order>> result = orderRepos.findAll()
				.stream()
				.collect(
						Collectors.groupingBy(Order::getCustomer)
				);
		log.info(result.toString());
	}
	//Produce a data map with order record and product total sum
	void exercise13(){
		Map<Order, Double> result = orderRepos.findAll()
				.stream()
				.collect(
						Collectors.toMap(
								Function.identity(),
								order -> order.getProducts().stream()
										.mapToDouble(p -> p.getPrice()).sum()
						)
				);
		log.info(result.toString());
	}
	//Obtain a data map with list of product name by category
	void exercise14(){
		Map<String, List<String>> result = productRepos.findAll()
				.stream()
				.collect(
						Collectors.groupingBy(
								Product::getCategory,
								Collectors.mapping(product -> product.getName(), Collectors.toList()))
				);
		log.info(result.toString());
	}
	//Get the most expensive product by category
	void exercise15(){
		Map<String, Optional<Product>> result = productRepos.findAll()
				.stream()
				.collect(
						Collectors.groupingBy(
								Product::getCategory,
								Collectors.maxBy(Comparator.comparing(Product::getPrice))));
		log.info(result.toString());
	}


}
