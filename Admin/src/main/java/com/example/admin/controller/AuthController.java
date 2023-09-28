package com.example.admin.controller;

import com.example.library.dto.AdminDto;
import com.example.library.model.Admin;
import com.example.library.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.security.Principal;

@Controller
public class AuthController {

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    AdminService adminService;

    @GetMapping("/index")
    public String home(Model model, Principal principal, HttpSession session){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken){
            return "redirect:/login";
        }
        String username = principal.getName();
        session.setAttribute("username", username);
        model.addAttribute("title", "index ");
        return "index";
    }
    /**Authentication authentication = SecurityContextHolder.getContext().getAuthentication();: Đoạn này lấy đối tượng Authentication từ SecurityContextHolder. SecurityContextHolder là một lớp trong Spring Security, mà bạn sử dụng để truy cập thông tin về xác thực của người dùng hiện tại trong hệ thống.

     if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {: Đoạn này kiểm tra xem authentication có giá trị là null hoặc có phải là một đối tượng AnonymousAuthenticationToken hay không. AnonymousAuthenticationToken là một loại đối tượng Authentication đặc biệt trong Spring Security, đại diện cho việc người dùng chưa đăng nhập hoặc đăng nhập ẩn danh.

     Nếu điều kiện trong if là đúng (nghĩa là người dùng chưa đăng nhập hoặc đăng nhập ẩn danh), thì đoạn code sau đó được thực thi:

     return "redirect:/login";: Đoạn code này chuyển hướng người dùng đến trang đăng nhập, được định nghĩa bằng URL "/login". Điều này đảm bảo rằng người dùng chỉ có thể truy cập các phần của ứng dụng mà họ có quyền sau khi họ đăng nhập thành công.
     Tóm lại, đoạn code này là một phần quan trọng trong việc kiểm tra xác thực của người dùng trong ứng dụng Spring Security và đảm bảo rằng họ chỉ có thể truy cập các phần của ứng dụng sau khi đã đăng nhập.*/

    @GetMapping("/login")
    public String login(Model model){
        model.addAttribute("title", "Login");
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model){
        model.addAttribute("adminDTO", new AdminDto());
        model.addAttribute("title", "Register");
        return "register";
    }

    @PostMapping("/register")
    public String addNewAdmin(@ModelAttribute("adminDto") AdminDto adminDto
            , Model model, BindingResult bindingResult, HttpSession session){
        try {
            session.removeAttribute("mess");
            if (bindingResult.hasErrors()){
                model.addAttribute("adminDto", adminDto);
                return "redirect:/register";
            }
            String username = adminDto.getUsername();
            Admin admin = adminService.findByUserName(username);
            if (admin != null){
                model.addAttribute("adminDto", adminDto);
                session.setAttribute("mess","Email existed" );
                return "redirect:/register";
            }
            if (adminDto.getPassword().equals(adminDto.getRepeatpassword())){
                adminDto.setPassword(passwordEncoder.encode(adminDto.getPassword()));
                adminService.save(adminDto);
                model.addAttribute("adminDto", adminDto);
                session.setAttribute("mess","Success" );
            }else {
                session.setAttribute("mess","Password is not the same" );
                return "redirect:/register";
            }
        }catch (Exception e){
            session.setAttribute("mess","Error" );
        }
        return "redirect:/register";
    }

    @GetMapping("/forgot-password")
    public String forgotPass(Model model){
        model.addAttribute("title","Forgot Password");
        return "forgot-password";
    }
}
