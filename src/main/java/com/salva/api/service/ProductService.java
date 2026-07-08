package com.salva.api.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import com.salva.api.model.Product;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class ProductService {

    public String save(Product product) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();

        ApiFuture<WriteResult> future =
                db.collection("products")
                  .document()
                  .set(product);

        return future.get().getUpdateTime().toString();
    }

    public Product findById(String id) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();

        DocumentSnapshot doc =
                db.collection("products")
                  .document(id)
                  .get()
                  .get();

        return doc.exists() ? doc.toObject(Product.class) : null;
    }

    public List<Product> findAll() throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();

        QuerySnapshot snapshot =
                db.collection("products")
                  .get()
                  .get();

        return snapshot.toObjects(Product.class);
    }
}