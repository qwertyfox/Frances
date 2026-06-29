package com.qwertyfox.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class ProductDataStructure {
    public static final ProductDataStructure INSTANCE = new ProductDataStructure();

    private final List<String> breadNames = new LinkedList<>();
    private final Map<String, TreeMap<String, Integer>> finalMapOfProducts = new TreeMap<>();


    public static ProductDataStructure getInstance() {
        return INSTANCE;
    }

    private ProductDataStructure(){
    }

    public Map<String, TreeMap<String, Integer>> getFinalMapOfProducts() {
        return Collections.unmodifiableMap(finalMapOfProducts);
    }

    private boolean checkBreadType (String breadName) {

        if(breadNames.contains(breadName)){
            return true;
        }else {
            return false;
        }
    }

    public void addToFinalList (String breadName, String productName, int orderNumber) {
        if(checkBreadType(breadName)){
            TreeMap<String, Integer> existingProduct = finalMapOfProducts.get(breadName);

            if(existingProduct.containsKey(productName)){
                int oldValue = existingProduct.get(productName);
                existingProduct.put(productName, oldValue + orderNumber);
            }else {
                existingProduct.put(productName, orderNumber);
            }

        }else {
            TreeMap<String, Integer> newProduct = new TreeMap<>();
            newProduct.put(productName, orderNumber);
            finalMapOfProducts.put(breadName, newProduct);
            breadNames.add(breadName);
        }
    }

    public void printFinalList() {
        for(String breadType : finalMapOfProducts.keySet()){
            System.out.println(breadType);
            Map<String, Integer> value = finalMapOfProducts.get(breadType);

            for(String key : value.keySet()) {
                System.out.println("\t" + key + " - " + value.get(key));
            }
        }
    }


}
