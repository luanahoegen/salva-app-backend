package com.salva.api.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import com.salva.api.enums.UserType;
import com.salva.api.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FirebaseService {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public FirebaseService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public String saveUser(User user) throws Exception {
        Firestore dbFirestore = FirestoreClient.getFirestore();

        // Normaliza o email para minúsculo
        user.setEmail(user.getEmail().toLowerCase().trim());

        // Verifica se já existe algum administrador cadastrado
        ApiFuture<QuerySnapshot> query = dbFirestore.collection("users")
                .whereEqualTo("type", UserType.ADMINISTRADOR.name())
                .limit(1)
                .get();

        if (query.get().isEmpty()) {
            user.setType(UserType.ADMINISTRADOR);
        } else if (user.getType() == null) {
            user.setType(UserType.CLIENTE);
        }
        
        // Criptografa a senha antes de salvar
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        //Salva o objeto no Cloud Firestore, usa modelo do SDK para salvar
        ApiFuture<WriteResult> collectionsApiFuture = dbFirestore.collection("users").document().set(user);
        return collectionsApiFuture.get().getUpdateTime().toString();
    }

    public Optional<User> authenticate(String email, String password) {
        try {
            Firestore dbFirestore = FirestoreClient.getFirestore();
            
            // Normaliza o email para busca
            String normalizedEmail = email.toLowerCase().trim();

            // Busca o usuário no Firestore pelo campo "email"
            ApiFuture<QuerySnapshot> query = dbFirestore.collection("users")
                    .whereEqualTo("email", normalizedEmail)
                    .limit(1)
                    .get();

            List<QueryDocumentSnapshot> documents = query.get().getDocuments();
            
            if (documents.isEmpty()) {
                return Optional.empty();
            }

            QueryDocumentSnapshot document = documents.get(0);
            User user = document.toObject(User.class);
            user.setId(document.getId());

            // Verifica se a senha confere com o hash salvo
            if (passwordEncoder.matches(password, user.getPassword())) {
                user.setPassword(null); // Remove a senha por segurança
                return Optional.of(user);
            }

        } catch (Exception e) {
            System.err.println("Erro na autenticação: " + e.getMessage());
        }

        return Optional.empty();
    }
}