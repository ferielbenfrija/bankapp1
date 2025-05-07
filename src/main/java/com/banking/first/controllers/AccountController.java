package com.banking.first.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.banking.first.entities.AppUser;
import com.banking.first.entities.Registerdto;
import com.banking.first.entities.Transaction;
import com.banking.first.repositories.UserRepository;
import com.banking.first.services.TransactionService;


import jakarta.validation.Valid;

@Controller
public class AccountController {
	@Autowired
private UserRepository repo;
	@Autowired
private com.banking.first.services.EmailService emailService;
	@Autowired
private TransactionService transactionService;
public static String uploadDirectory=System.getProperty("user.dir")+"/src/main/resources/uploads";

@GetMapping("/register")
public String register(Model model) {
	Registerdto registerdto = new Registerdto();
	model.addAttribute(registerdto);
	model.addAttribute("success",false);

	return"register";
}
@PostMapping("/register")
public String register(Model model,@Valid @ModelAttribute Registerdto registerdto , BindingResult result, @RequestParam("profileImage")MultipartFile profileImage) {
	if (!registerdto.getPassword().equals(registerdto.getConfirmpassword())) { 
		result.addError( new FieldError ("registerdto", "confirmPassword" ,"Password and Confirm Password do not match")); 
		} 
		AppUser appUser = repo.findByEmail (registerdto.getEmail()); 
		if (appUser != null) { 
			result.addError( new FieldError("registerdto", "email" ,"Email address is already used"));
			}
		
		if (result.hasErrors()) { 
			return"register";}
		//UPLOAD
		StringBuilder fileName = new StringBuilder();

		// Récupération du fichier unique 
		MultipartFile file = profileImage;

		// Dossier d'upload
		String uploadDirectory = "uploads/profileImage";

		// Création du chemin complet du fichier
		
		Path fileNameAndPath = Paths.get(uploadDirectory, file.getOriginalFilename());
		fileName.append(file.getOriginalFilename());

		try {
		    // Créer le dossier s'il n'existe pas
		    if (!Files.exists(Paths.get(uploadDirectory))) {
		        Files.createDirectories(Paths.get(uploadDirectory));
		    }

		    // Écrire le fichier
		    Files.write(fileNameAndPath, file.getBytes());

		} catch (IOException e) {
		    e.printStackTrace();
		}

try {

   var bCryptEncoder = new BCryptPasswordEncoder();

    AppUser newUser = new AppUser();
    newUser.setFirstname(registerdto.getFirstName());
    newUser.setLastname(registerdto.getLastName());
    newUser.setEmail(registerdto.getEmail());
    newUser.setPhone(registerdto.getPhone());
    newUser.setAddress(registerdto.getAddress());
    newUser.setRole("client");
    newUser.setCreatedAt(new Date());
    newUser.setPassworld(bCryptEncoder.encode(registerdto.getPassword()));
    newUser.setProfileImagePath(file.getOriginalFilename()); // ✅ Correct

    repo.save(newUser);

    model.addAttribute("registerDto", new Registerdto());
    model.addAttribute("success", true);

} 
	catch (Exception ex) {
		result.addError(new FieldError("registerdto", "firstName", ex.getMessage()));
	}
		return "register";
		}
@GetMapping("/account/details")
public String showAccountDetails(Model model, @AuthenticationPrincipal org.springframework.security.core.userdetails.User user) {
    String email = user.getUsername(); // Récupère l'email de l'utilisateur connecté
    AppUser appUser = repo.findByEmail(email); // Va chercher les infos du user
    System.out.println("Profile Image Path: " + appUser.getProfileImagePath());  // Débogage

    model.addAttribute("user", appUser); // On envoie les infos à la page HTML
    return "account-details"; // Va afficher le fichier account-details.html
}
@GetMapping("/account/deposit")
public String showDepositPage(Model model) {
    return "deposit";  // Affiche la page de dépôt
}
@PostMapping("/account/deposit")
public String deposit(@RequestParam("amount") double amount,
                      @AuthenticationPrincipal org.springframework.security.core.userdetails.User user,
                      Model model) {
    String email = user.getUsername();
    AppUser appUser = repo.findByEmail(email);

    if (amount > 0) {
        appUser.setBalance(appUser.getBalance() + amount);
        repo.save(appUser);
        
        transactionService.addTransaction(appUser, "DEPOSIT", amount);


        emailService.sendTransactionEmail(
            appUser.getEmail(),
            "Confirmation de dépôt",
            "Bonjour " + appUser.getFirstname() + ",\n\nVous avez déposé " + amount + "€ sur votre compte.\nVotre nouveau solde est de " + appUser.getBalance() + "€."
        );

        model.addAttribute("successMessage", "Dépôt effectué avec succès !");
    } else {
        model.addAttribute("errorMessage", "Le montant du dépôt doit être supérieur à zéro.");
    }

    return "redirect:/account/details";
}


@GetMapping("/account/withdraw")
public String showWithdrawPage(Model model) {
    return "withdraw";  // Affiche la page de retrait
}

@PostMapping("/account/withdraw")
public String withdraw(@RequestParam("amount") double amount,
                       @AuthenticationPrincipal org.springframework.security.core.userdetails.User user,
                       Model model) {
    String email = user.getUsername();
    AppUser appUser = repo.findByEmail(email);

    if (amount > 0 && amount <= appUser.getBalance()) {
        appUser.setBalance(appUser.getBalance() - amount);
        repo.save(appUser);
        
        transactionService.addTransaction(appUser, "WITHDRAWAL", amount);

        emailService.sendTransactionEmail(
            appUser.getEmail(),
            "Confirmation de retrait",
            "Bonjour " + appUser.getFirstname() + ",\n\nVous avez retiré " + amount + "€ de votre compte.\nVotre nouveau solde est de " + appUser.getBalance() + "€."
        );

        model.addAttribute("successMessage", "Retrait effectué avec succès !");
    } else {
        model.addAttribute("errorMessage", "Montant invalide ou solde insuffisant.");
    }

    return "redirect:/account/details";
}


@GetMapping("/account/history")
public String viewHistory(Model model, @AuthenticationPrincipal org.springframework.security.core.userdetails.User user) {
    String email = user.getUsername();
    AppUser appUser = repo.findByEmail(email);

    List<Transaction> transactions = transactionService.getUserTransactions(appUser);
    model.addAttribute("transactions", transactions);
    return "transaction-history"; 
}



}