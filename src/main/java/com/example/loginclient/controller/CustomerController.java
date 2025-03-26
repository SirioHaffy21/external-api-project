package com.example.loginclient.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.loginclient.model.Customer;
import com.example.loginclient.service.CustomerService;
import com.example.loginclient.service.ExternalLoginService;

@Controller
public class CustomerController {

    @Autowired
    private ExternalLoginService authService;

    @Autowired
    private CustomerService customerService;

    @GetMapping("/customers")
    public String showCustomers(Model model, HttpSession session) {
        // Lấy token
        String token = (String) session.getAttribute("token");

        if (token == null) {
            model.addAttribute("error", "Đăng nhập thất bại!");
            return "error";
        }

        // Lấy danh sách khách hàng
        List<Customer> customers = customerService.fetchCustomers(token);
        model.addAttribute("customers", customers);
        return "customers"; // Hiển thị trang customers.html
    }
}