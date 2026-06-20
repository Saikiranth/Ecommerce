/*
 * package com.example.demo;
 * 
 * import java.util.ArrayList; import java.util.List;
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
 * @Controller public class CartController {
 * 
 * private List<CartItem> cartItems = new ArrayList<>();
 * 
 * // HOME
 * 
 * @GetMapping("/customerhome") public String home(Model model) { return
 * "customerhome"; }
 * 
 * 
 * 
 * @Autowired private ProductRepository productRepository;
 * 
 * @PostMapping("/addCart") public String addCart(@RequestParam Long productId,
 * HttpSession session) {
 * 
 * Product p = productRepository.findById(productId).orElse(null); if (p ==
 * null) return "redirect:/customer/home";
 * 
 * CartItem item = new CartItem(p.getName(), p.getImageUrl(), p.getPrice());
 * 
 * List<CartItem> cart = (List<CartItem>) session.getAttribute("cart"); if (cart
 * == null) cart = new ArrayList<>(); cart.add(item);
 * session.setAttribute("cart", cart);
 * 
 * return "cart"; }
 * 
 * // CART PAGE (FIXED)
 * 
 * @GetMapping("/cart") public String cart(Model model, HttpSession session) {
 * 
 * List<CartItem> cart = (List<CartItem>) session.getAttribute("cart"); if (cart
 * == null) cart = new ArrayList<>();
 * 
 * double total = cart.stream() .mapToDouble(CartItem::getPrice) .sum();
 * 
 * model.addAttribute("items", cart); model.addAttribute("total", total);
 * 
 * return "cart"; }
 * 
 * // REMOVE ITEM
 * 
 * @GetMapping("/removeFromCart") public String removeFromCart(@RequestParam
 * String product, HttpSession session) {
 * 
 * List<CartItem> cart = (List<CartItem>) session.getAttribute("cart"); if (cart
 * != null) { cart.removeIf(item -> item.getName().equals(product));
 * session.setAttribute("cart", cart); }
 * 
 * return "redirect:/cart"; } // CHECKOUT
 * 
 * @GetMapping("/checkout") public String checkout(Model model, HttpSession
 * session) {
 * 
 * List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
 * 
 * if (cart == null) cart = new ArrayList<>();
 * 
 * double total = cart.stream() .mapToDouble(CartItem::getPrice) .sum();
 * 
 * model.addAttribute("items", cart); model.addAttribute("total", total);
 * 
 * return "checkout"; } // ORDER SUCCESS STORE private Order lastOrder;
 * 
 * @PostMapping("/placeOrder") public String placeOrder(@RequestParam double
 * total, HttpSession session) {
 * 
 * List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
 * 
 * if (cart == null) cart = new ArrayList<>();
 * 
 * String products = cart.stream() .map(CartItem::getName) .reduce((a, b) -> a +
 * ", " + b) .orElse("Empty");
 * 
 * Order order = new Order(); order.setProducts(products);
 * order.setTotalAmount(total); order.setStatus("SUCCESS");
 * 
 * lastOrder = order;
 * 
 * session.removeAttribute("cart"); // clear only this user
 * 
 * return "redirect:/success"; } // SUCCESS PAGE
 * 
 * @GetMapping("/success") public String success(Model model) {
 * model.addAttribute("order", lastOrder); return "success"; } }
 */


package com.example.demo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

@Controller
public class CartController {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderRepository orderRepository;

    @PostMapping("/addCart")
    public String addCart(@RequestParam Long productId, HttpSession session) {

        // Fetch product from DB instead of hardcoded map
        Product p = productRepository.findById(productId).orElse(null);

        if (p != null) {
            CartItem item = new CartItem(p.getName(), p.getImageUrl(), p.getPrice());

            List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
            if (cart == null) cart = new ArrayList<>();

            cart.add(item);
            session.setAttribute("cart", cart);
        }

        return "redirect:/cart";
    }

    @GetMapping("/cart")
    public String cart(Model model, HttpSession session) {

        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null) cart = new ArrayList<>();

        double total = cart.stream()
                .mapToDouble(CartItem::getPrice)
                .sum();

        model.addAttribute("items", cart);
        model.addAttribute("total", total);

        return "cart";
    }

    @GetMapping("/removeFromCart")
    public String removeFromCart(@RequestParam String product, HttpSession session) {

        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart != null) {
            cart.removeIf(item -> item.getName().equals(product));
            session.setAttribute("cart", cart);
        }

        return "redirect:/cart";
    }

    @GetMapping("/checkout")
    public String checkout(Model model, HttpSession session) {

        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null) cart = new ArrayList<>();

        double total = cart.stream()
                .mapToDouble(CartItem::getPrice)
                .sum();

        model.addAttribute("items", cart);
        model.addAttribute("total", total);

        return "checkout";
    }

    private Order lastOrder;

    @PostMapping("/placeOrder")
    public String placeOrder(@RequestParam double total, HttpSession session) {

        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null) cart = new ArrayList<>();

        String products = cart.stream()
                .map(CartItem::getName)
                .reduce((a, b) -> a + ", " + b)
                .orElse("Empty");

        User user = (User) session.getAttribute("user");  // ← get logged in user

        Order order = new Order();
        order.setProducts(products);
        order.setTotalAmount(total);
        order.setStatus("SUCCESS");
        order.setTrackingStatus("PLACED"); 
        order.setOrderDate(new java.util.Date());
        if (user != null) order.setUserId(user.getId());  // ← save userId

        orderRepository.save(order);
        session.setAttribute("lastOrder", order);  // ← store in session
        session.removeAttribute("cart");

        return "redirect:/success";
    }

    @GetMapping("/success")
    public String success(Model model, HttpSession session) {
        model.addAttribute("order", session.getAttribute("lastOrder"));
        return "success";
    }
}