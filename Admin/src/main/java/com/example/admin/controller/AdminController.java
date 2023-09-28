package com.example.admin.controller;

import com.example.library.model.Category;
import com.example.library.service.CateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.List;

@Controller
public class AdminController {

    @Autowired
    CateService cateService;

    @GetMapping("/category")
    public String category(Model model, Principal principal){
        if (principal == null){
            return "redirect:/login";
        }
        List<Category> categoryList = cateService.findAll();
        model.addAttribute("category", categoryList);
        model.addAttribute("categoryNew", new Category());
        model.addAttribute("title", "Category");
        model.addAttribute("size", categoryList.size());
        return "category";
    }

    @PostMapping("/addCategory")
    public String addCategory(@ModelAttribute("categoryNew") Category category, HttpSession session, Model model){
        try{
            cateService.save(category);
            session.setAttribute("success", "Adding Success");
        }catch (Exception e){
            session.setAttribute("falsee", "Fail");
            e.printStackTrace();
        }
        return "redirect:/category";
    }

    @GetMapping("/updateCategory")
    public String updateCate(Category category, RedirectAttributes attributes){
       try{
           cateService.update(category);
           attributes.addFlashAttribute("success", "Update Success");
       }catch (Exception e){
           e.printStackTrace();
           attributes.addFlashAttribute("failed", "Error");
       }
        return "redirect:/category";
    }

    @GetMapping("/findById")
    @ResponseBody
    public Category findById(Long id){
        return cateService.findById(id);
    }

    @GetMapping("/deleteCategory")
    public String delete(Long cateid, RedirectAttributes attributes){
        try {
            cateService.deleteById(cateid);
            attributes.addFlashAttribute("success","Deleted");
        }catch (Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("failed", "Error");
        }
        return "redirect:/category";
    }

    @GetMapping("/enableCategory")
    public String enable(Long cateid, RedirectAttributes attributes){
        try {
            cateService.enableById(cateid);
            attributes.addFlashAttribute("success", "Success enable");
        }catch (Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("failed", "Error");
        }
        return "redirect:/category";

    }
}
