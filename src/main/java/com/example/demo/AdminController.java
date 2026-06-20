package com.example.demo;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdminController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private UserRepository userRepository;

	/*
	 * @GetMapping("/admin/home") public String adminHome(Model model) {
	 * 
	 * model.addAttribute("products", productRepository.findAll());
	 * model.addAttribute("orders", orderRepository.findAll());
	 * model.addAttribute("users", userRepository.findAll()); return "adminhome"; }
	 */
    
    @GetMapping("/admin/home")
    public String adminHome(Model model) {
        model.addAttribute("products", productRepository.findAll());
        model.addAttribute("orders", orderRepository.findAll());
        model.addAttribute("users", userRepository.findAll());

        // Sales Report data
        List<Order> allOrders = orderRepository.findAll();

        double totalRevenue = allOrders.stream()
                .mapToDouble(Order::getTotalAmount)
                .sum();

        long totalOrders = allOrders.size();

        long successOrders = allOrders.stream()
                .filter(o -> "SUCCESS".equals(o.getStatus()))
                .count();

        model.addAttribute("totalRevenue", totalRevenue);
        model.addAttribute("totalOrders", totalOrders);
        model.addAttribute("successOrders", successOrders);
        model.addAttribute("salesOrders", allOrders);  // for the table

        return "adminhome";
    }
    @PostMapping("/admin/products/add")
    public String addProduct(@ModelAttribute Product product) {
        productRepository.save(product);
        return "redirect:/admin/home";
    }

    @GetMapping("/admin/orders")
    public String adminOrders(Model model) {
        model.addAttribute("orders", orderRepository.findAll());
        return "admin-orders";
    }
    @PostMapping("/admin/updateTracking")
    public String updateTracking(@RequestParam Long orderId,
                                  @RequestParam String trackingStatus) {

        Order order = orderRepository.findById(orderId).orElse(null);
        if (order != null) {
            order.setTrackingStatus(trackingStatus);
            orderRepository.save(order);
        }
        return "redirect:/admin/home#order";
    }
}