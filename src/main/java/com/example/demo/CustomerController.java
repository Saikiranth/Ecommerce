/*
 * package com.example.demo;
 * 
 * import org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.stereotype.Controller; import
 * org.springframework.ui.Model; import
 * org.springframework.web.bind.annotation.GetMapping; import
 * org.springframework.web.bind.annotation.PostMapping; import
 * org.springframework.web.bind.annotation.RequestParam;
 * 
 * import jakarta.servlet.http.HttpSession;
 * 
 * @Controller public class CustomerController {
 * 
 * @Autowired private CartRepository cartRepository;
 * 
 * @Autowired private ProductRepository productRepository;
 * 
 * // ADD TO CART
 * 
 * @PostMapping("/addCart") public String addCart(@RequestParam String product,
 * HttpSession session) {
 * 
 * User user = (User) session.getAttribute("user");
 * 
 * if (user == null) { return "redirect:/login"; }
 * 
 * Product p = productRepository.findByName(product);
 * 
 * if (p == null) { return "redirect:/customer/home"; }
 * 
 * Cart cart = new Cart(); cart.setUserId(user.getId());
 * cart.setProductId(p.getId()); cart.setQuantity(1);
 * 
 * cartRepository.save(cart);
 * 
 * return "redirect:/cart"; }
 * 
 * // VIEW CART
 * 
 * @GetMapping("/cart") public String viewCart(Model model, HttpSession session)
 * {
 * 
 * User user = (User) session.getAttribute("user");
 * 
 * if (user == null) { return "redirect:/login"; }
 * 
 * model.addAttribute("items", cartRepository.findByUserId(user.getId()));
 * 
 * return "cart"; } }
 */

/*
 * package com.example.demo;
 * 
 * import org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.stereotype.Controller; import
 * org.springframework.ui.Model; import
 * org.springframework.web.bind.annotation.GetMapping;
 * 
 * import jakarta.servlet.http.HttpSession;
 * 
 * @Controller public class CustomerController {
 * 
 * @Autowired private ProductRepository productRepository;
 * 
 * @GetMapping("/customer/home") public String customerHome(Model model) {
 * 
 * model.addAttribute("products", productRepository.findAll());
 * 
 * return "customerhome"; } }
 */



package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

@Controller
public class CustomerController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("/customer/home")
    public String customerHome(Model model, HttpSession session,
                               @RequestParam(required = false) String search) {

        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        model.addAttribute("search", search);

        if (search != null && !search.isEmpty()) {
            // Filter products by name containing search text (case-insensitive)
            model.addAttribute("products",
                productRepository.findByNameContainingIgnoreCase(search));
        } else {
            model.addAttribute("products", productRepository.findAll());
        }

        return "customerhome";
    }
    @GetMapping("/orders")
    public String myOrders(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user == null) return "redirect:/login";

        // Fetch only this user's orders
        model.addAttribute("orders", orderRepository.findByUserId(user.getId()));
        return "orders";
    }
}