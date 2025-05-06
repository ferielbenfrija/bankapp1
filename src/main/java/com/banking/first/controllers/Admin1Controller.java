package com.banking.first.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.banking.first.entities.AppUser;
import com.banking.first.repositories.UserRepository;

@Controller
@RequestMapping("/admin")
public class Admin1Controller  {

    @Autowired
    private UserRepository userRepository;


    // Afficher la liste des utilisateurs
    @GetMapping("/users")
    public String listUsers(Model model) {
        List<AppUser> users = userRepository.findAll(); // Liste des utilisateurs
        model.addAttribute("users", users); // Ajouter les utilisateurs au modèle
        return "admin/users"; // Page de gestion des utilisateurs
    }

    // Supprimer un utilisateur
    @PostMapping("/delete")
    public String deleteUser(@RequestParam("id") Long id) {
        userRepository.deleteById(id); // Supprimer l'utilisateur par ID
        return "redirect:/admin/users"; // Redirige vers la page des utilisateurs après suppression
    }

    // Changer le rôle d'un utilisateur
    @PostMapping("/changer-role")
    public String updateRole(@RequestParam Long id, @RequestParam String role) {
        AppUser user = userRepository.findById(id).orElse(null); // Trouver l'utilisateur par ID
        if (user != null) {
            user.setRole(role); // Changer le rôle
            userRepository.save(user); // Sauvegarder les modifications
        }
        return "redirect:/admin/users"; // Redirige vers la page des utilisateurs après la mise à jour
    }
}